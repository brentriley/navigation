package com.brent.navigation.tab

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.brent.navigation.tab.TabNavigation.Listener

/**
 * Utility to provide simple and effective Tab Navigation behaviour using [Fragment]s
 * This will track the state of current Navigation stacks and view states, and is connected to the
 * lifecycle of its [FragmentActivity].
 *
 * Consistent instance state (including rotation support) is built in, but each
 *
 * App performance is optimised by ensuring only one Page is visible and attached at any given time.
 *
 * [Listener]s can be used to refine and control the behaviour of the controller
 */
interface TabNavigation {

    var currentPage: Int

    fun setContainer(@IdRes containerId: Int): TabNavigation

    fun addPage(pageId: Int, fragment: Fragment): TabNavigation

    fun addListener(listener: TabNavigation.Listener): TabNavigation

    fun removeListener(listener: TabNavigation.Listener): TabNavigation

    fun useCommitNowForBaseFragments(): TabNavigation

    fun enableRobustLogging(): TabNavigation

    fun resetCurrentPage()
    fun clearStateForPage(pageId: Int)
    fun clearAllStates()

    val currentFragmentManager: FragmentManager?
    val currentFragment: Fragment?

    interface Listener

    interface OnPageChangedListener : Listener {
        fun onNavigationPageChanged(currentPage: Int)
    }

    interface OnPageReselectedListener : Listener {
        fun onCurrentPageReselected(currentPage: Int)
    }

    interface NavigationInterceptor : Listener {
        fun onAttemptToNavigateToPage(nextPage: Int): Int?
    }

    class AnimSet(
        var enterRes: Int,
        var exitRes: Int
    ) {
        fun setCustomAnimations(@AnimatorRes @AnimRes enterRes: Int, @AnimatorRes @AnimRes exitRes: Int) {
            this.enterRes = enterRes
            this.exitRes = exitRes
        }
    }

    interface AnimationInterceptor : Listener {
        fun onOverrideTabAnimation(animations: AnimSet)
    }
}