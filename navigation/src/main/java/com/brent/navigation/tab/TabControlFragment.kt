package com.brent.navigation.tab

import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.brent.navigation.NavLog
import com.brent.navigation.R

private const val KEY_NAV_STATE = "tabNav::navState"
private const val KEY_USE_COMMIT_NOW = "tabNav::useCommitNow"
private const val KEY_CURRENT_PAGE = "tabNav::currentPage"
private const val KEY_PAGE_STATE = "tabNav::savedState"
private const val KEY_PAGE_ID = "tabNav::pageId"
private const val KEY_SAVED_STATE = "tabNav::state"

internal class TabControlFragment : Fragment(),
    TabNavigation {

    private val transitionAnimations =
        TabNavigation.AnimSet(0, 0)

    private var useCommitNow = false

    private var fragmentHostId = 0
    private val changeListeners = arrayListOf<TabNavigation.Listener>()

    private val savedStates = SparseArray<SavedState?>()
    private val fragments = SparseArray<Fragment>()

    override val currentFragmentManager: FragmentManager?
        get() = fragmentManager?.findFragmentById(fragmentHostId)?.childFragmentManager

    override val currentFragment: Fragment?
        get() = currentFragmentManager?.fragments?.firstOrNull()

    private var currentHostPage: Int = -1

    override fun useCommitNowForBaseFragments(): TabNavigation {
        NavLog.d("Navigation changes will now use commitNow() instead of commit()")
        useCommitNow = true
        return this
    }

    override fun enableRobustLogging(): TabNavigation {
        if (!NavLog.debugLogEnabled) {
            NavLog.debugLogEnabled = true
            NavLog.d("Logging Enabled!")
        }
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getBundle(KEY_NAV_STATE)?.apply {
            NavLog.d("Saved navigation state found")

            currentHostPage = getInt(KEY_CURRENT_PAGE, currentHostPage)
            NavLog.d("Restored current Page to $currentHostPage")
            useCommitNow = getBoolean(KEY_USE_COMMIT_NOW, useCommitNow)

            getBundle(KEY_PAGE_STATE)?.let {
                restoreFragmentStatesFromBundle(it)
            }
        }
    }

    private fun restoreFragmentStatesFromBundle(bundle: Bundle) {
        NavLog.d("Found ${bundle.keySet().size} SavedStates")
        bundle.keySet().forEach { key ->
            bundle.getBundle(key)?.apply {
                val pageId = getInt(KEY_PAGE_ID, -1)
                NavLog.d("Extracting SavedState for Page $pageId")
                savedStates.put(pageId, getParcelable(KEY_SAVED_STATE))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBundle(KEY_NAV_STATE, Bundle().apply {
            NavLog.d("Saving navigation state")

            NavLog.d("useCommitNow $useCommitNow, currentPage $currentHostPage")
            putBoolean(KEY_USE_COMMIT_NOW, useCommitNow)
            putInt(KEY_CURRENT_PAGE, currentHostPage)

            putBundle(KEY_PAGE_STATE, getFragmentStatesAsBundle())
        })
    }

    private fun getFragmentStatesAsBundle() = Bundle().apply {
        for (index in 0 until savedStates.size()) {
            val pageId = savedStates.keyAt(index)
            NavLog.d("Saving state for Page $pageId")

            putBundle("state$pageId", Bundle().apply {
                putInt(KEY_PAGE_ID, pageId)
                putParcelable(KEY_SAVED_STATE, savedStates.valueAt(index))
            })
        }
    }

    override var currentPage: Int
        get() = currentHostPage
        set(value) = attemptToPresentHostForPage(value)

    override fun addListener(listener: TabNavigation.Listener): TabNavigation {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener)
        }

        return this
    }

    override fun removeListener(listener: TabNavigation.Listener): TabNavigation {
        changeListeners.remove(listener)

        return this
    }

    override fun setContainer(@IdRes containerId: Int): TabNavigation {
        NavLog.d("Setting navigation host container set to ViewGroup with id $containerId")

        fragmentHostId = containerId
        val fragmentManager = fragmentManager ?: return this

        if (fragments[currentHostPage] != null && fragmentManager.findFragmentById(fragmentHostId) == null) {
            NavLog.d("No navigation content is displayed, showing initial page ($currentHostPage)")

            val targetPage = currentHostPage
            currentHostPage = -1
            attemptToPresentHostForPage(targetPage)
        }

        return this
    }

    override fun addPage(pageId: Int, fragment: Fragment): TabNavigation {
        NavLog.d("Registering navigation Page with id $pageId and fragment ${fragment::class.java.simpleName}")

        fragments.put(pageId, fragment)
        val fragmentManager = fragmentManager ?: return this

        if (fragmentHostId == 0 && fragments[currentHostPage] == null) {
            NavLog.d("Navigation Page $pageId is the first valid Page, setting as initial Page")

            currentHostPage = pageId

        } else if (fragmentHostId != 0 && fragmentManager.findFragmentById(fragmentHostId) == null) {
            NavLog.d("Navigation Page $pageId is the first valid Page, setting as initial Page")

            attemptToPresentHostForPage(pageId)
        }

        return this
    }

    private fun attemptToPresentHostForPage(attemptedNextPage: Int) {
        val fragmentManager = fragmentManager ?: return
        if (fragmentHostId == 0) return NavLog.e("Attempting to present navigation Page $attemptedNextPage when not attached")

        NavLog.d("Attempting to show navigation Page $attemptedNextPage")
        NavLog.d("Checking with Listeners for intercepts...")

        var nextPage = attemptedNextPage
        changeListeners.filter { it is TabNavigation.NavigationInterceptor }.forEach {
            nextPage = (it as? TabNavigation.NavigationInterceptor)?.onAttemptToNavigateToPage(nextPage)
                    ?: return NavLog.d("A navigation Listener does not want us to present Page $nextPage")
        }

        if (nextPage != attemptedNextPage) {
            NavLog.d("A navigation listener has updated the request to Page $nextPage")
        }

        if (fragments.get(nextPage) == null) {
            NavLog.e("Page $nextPage does not exist in the Page registry")
            throw IndexOutOfBoundsException("Attempting to present unregistered navigation Page $nextPage. Use addPage()")
        }

        if (currentHostPage == nextPage) {
            NavLog.d("Attempted to navigate to $nextPage which is already the current Page, notifying Listeners instead")
            return changeListeners.forEach {
                (it as? TabNavigation.OnPageReselectedListener)?.onCurrentPageReselected(currentHostPage)
            }
        }

        NavLog.d("Looking for current navigation host")
        fragmentManager.findFragmentById(fragmentHostId)?.let { currentFragment ->
            NavLog.d("Navigation host found for page $currentHostPage, saving state for later restoration")
            savedStates.put(currentHostPage, fragmentManager.saveFragmentInstanceState(currentFragment))

        } ?: NavLog.d("No existing navigation host found")

        NavLog.d("Attempting to restore state for Page $nextPage")
        val hostFragment = TabHostFragment()
        hostFragment.setInitialSavedState(savedStates[nextPage])

        transitionAnimations.setCustomAnimations(
            R.anim.fragment_fade_in,
            R.anim.fragment_fade_out
        )

        changeListeners.forEach {
            (it as? TabNavigation.AnimationInterceptor)?.onOverrideTabAnimation(transitionAnimations)
        }

        NavLog.d("Attaching host fragment to Activity in ViewGroup with id $fragmentHostId")
        fragmentManager.beginTransaction()
                .setCustomAnimations(transitionAnimations.enterRes, transitionAnimations.exitRes)
                .replace(fragmentHostId, hostFragment, "nav_$nextPage")
                .setPrimaryNavigationFragment(hostFragment)
                .commitNow()

        NavLog.d("Checking for empty navigation host")
        if (hostFragment.childFragmentManager.fragments.isEmpty()) {
            val fragment = fragments[nextPage]
            NavLog.d("Host fragment for Page $nextPage, doesn't have an attached Fragment, displaying ${fragment::class.java.simpleName}")

            //Add our first fragment. By using commitNow on the nav host, we should be instantly attached.
            hostFragment.childFragmentManager.beginTransaction()
                    .add(R.id.navigation_content, fragment, "base_fragment")
                    .setPrimaryNavigationFragment(fragment).apply {
                        if (useCommitNow) {
                            NavLog.d("Using commitNow() instead of commit()")
                            commitNow()
                        } else {
                            commit()
                        }
                    }
        }

        NavLog.d("Navigation page change successful, notifying Listeners")

        currentHostPage = nextPage
        changeListeners.forEach {
            (it as? TabNavigation.OnPageChangedListener)?.onNavigationPageChanged(nextPage)
        }
    }

    override fun clearAllStates() {
        NavLog.d("Clearing all Saved states")
        savedStates.clear()
    }

    override fun clearStateForPage(pageId: Int) {
        NavLog.d("Clearing Saved state for Page $pageId")
        savedStates.remove(pageId)
    }

    override fun resetCurrentPage() {
        val fragmentManager = fragmentManager
                ?: return NavLog.e("Attempted to reset Page $currentHostPage while not attached")

        NavLog.d("Attempting to reset Page $currentHostPage to base Fragment")

        val currentFragment = fragmentManager.findFragmentById(fragmentHostId)
                ?: return NavLog.d("No existing navigation host found")

        NavLog.d("Navigation host found for page $currentHostPage, clearing back stack")

        if (currentFragment.childFragmentManager.backStackEntryCount == 0) {
            return NavLog.d("No items in back stack to remove")
        }

        val firstBackStackName = currentFragment.childFragmentManager.getBackStackEntryAt(0).name
        NavLog.d("Popping back stack to $firstBackStackName")

        if (useCommitNow) {
            NavLog.d("Using popBackStackImmediate()")
            currentFragment.childFragmentManager.popBackStackImmediate(firstBackStackName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            currentFragment.childFragmentManager.popBackStack(firstBackStackName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}