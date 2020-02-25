package com.brent.navigation

import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.brent.navigation.Navigation.pushFragment
import com.brent.navigation.tab.TabControlFragment
import com.brent.navigation.tab.TabNavigation

/**
 * Extension for [Fragment] classes that provide an easy and reliable means of pushing to the Nav Stack.
 * The provided Fragment will replace the calling Fragment which will be added to the back stack.
 * Utilises the bundled animation resources to avoid hierarchy issues when popping the stack.
 *
 * @param fragment: The Fragment to be presented
 */
fun Fragment.pushFragment(fragment: Fragment) {
    pushFragment(fragment, R.anim.fragment_from_right, R.anim.fragment_to_right)
}

/**
 * Extension for [Fragment] classes that provide an easy and reliable means of pushing to the Nav Stack.
 * The provided Fragment will replace the calling Fragment which will be added to the back stack.
 * Utilises the bundled animation resources to avoid hierarchy issues when popping the stack.
 *
 * Similar to [pushFragment] except allows you to declare incoming Fragment's animations.
 *
 * @param fragment: The Fragment to be presented
 * @param enterRes: Animation or Animator XML resource file used to define [fragment]'s entry
 * behaviour
 * @param popExitRes: Animation or Animator XML resource file used to define [fragment]'s exit
 * behaviour when [FragmentManager.popBackStack] is called
 */
@Suppress("DEPRECATION")
fun Fragment.pushFragment(fragment: Fragment, @AnimRes @AnimatorRes enterRes: Int, @AnimRes @AnimatorRes popExitRes: Int) {
    val fragmentManager = fragmentManager ?: return

    fragmentManager.beginTransaction()
        .setCustomAnimations(enterRes, R.animator.fragment_pop_out, R.animator.fragment_pop_in, popExitRes)
        .replace(id, fragment)
        .addToBackStack("frag_${fragmentManager.backStackEntryCount}")
        .commit()
}

@Suppress("DEPRECATION")
fun Fragment.slideFragment(fragment: Fragment) {
    val fragmentManager = fragmentManager ?: return

    fragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.fragment_from_right, R.anim.fragment_to_left, R.anim.fragment_from_left, R.anim.fragment_to_right)
        .replace(id, fragment)
        .addToBackStack("frag_${fragmentManager.backStackEntryCount}")
        .commit()
}

@Suppress("DEPRECATION")
fun Fragment.clearBackStack() {
    fragmentManager?.let { stack ->
        clearBackStack(stack)
    }
}

fun FragmentActivity.clearBackStack() = clearBackStack(supportFragmentManager)

private fun clearBackStack(stack: FragmentManager) {
    if(stack.backStackEntryCount > 0) {
        stack.popBackStack(
            stack.getBackStackEntryAt(0).name,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}

/**
 * Extension for [Fragment] classes which will attempt to extract or create the [TabNavigation] control
 * for the owner [FragmentActivity].
 *
 * @return If the Fragment is attached to an Activity it will return the existing instance
 * of TabNavigation. If none exists this method will ensure one is created and inject the new controller.
 * If the Fragment is not attached this will return null.
 */
val Fragment.tabNavigation: TabNavigation?
    get() = activity?.tabNavigation

/**
 * Extension for [FragmentActivity] classes which will attempt to extract or create a [TabNavigation] control.
 *
 * @return If a TabNavigation has already been attached to the Activity it will be returned. If none exists
 * this method will ensure one is created and inject the new controller.
 */
val FragmentActivity.tabNavigation: TabNavigation
    get() = supportFragmentManager.fragments.firstOrNull { fragment ->
        fragment is TabControlFragment
    } as? TabControlFragment
        ?: TabControlFragment().also { controller ->

        Log.i("Navigation", "Building Navigation Controller")
        supportFragmentManager.beginTransaction().apply {
            add(controller, "navigationController")
        }.commitNow()
    }

val Fragment.navController get() = findNavController(this)

val FragmentActivity.navController: NavController get() {
    for(fragment in supportFragmentManager.fragments) {
        try {
            return findNavController(fragment)
        } catch (e: IllegalStateException) {
            //Skip
        }
    }

    throw IllegalStateException("FragmentActivity $this does not have a NavController")
}

/**
 * Simple Util / Alias setup that provides Java static methods for clarity and ease of use for projects
 * not written in Kotlin.
 */

object Navigation {

    /**
     * Java alias for [tabNavigation].
     *
     * @return If the Fragment is attached to an Activity it will return the existing instance
     * of TabNavigation. If none exists this method will ensure one is created and inject the new controller.
     * If the Fragment is not attached this will return null.
     */
    @JvmStatic
    fun getTabNavigation(fragment: Fragment) = fragment.activity?.tabNavigation

    /**
     * Java alias for [tabNavigation].
     *
     * @return If a TabNavigation has already been attached to the Activity it will be returned. If none exists
     * this method will ensure one is created and inject the new controller.
     */
    @JvmStatic
    fun getTabNavigation(activity: FragmentActivity) = activity.tabNavigation

    /**
     * Java alias for [pushFragment].
     * Provides an easy and reliable means of pushing to the Nav Stack.
     * The provided Fragment will replace the calling Fragment which will be added to the back stack.
     * Utilises the bundled animation resources to avoid hierarchy issues when popping the stack.
     *
     * @param fragment: The Fragment to be presented
     */
    @JvmStatic
    fun pushFragment(currentFragment: Fragment, nextFragment: Fragment) = currentFragment.pushFragment(nextFragment)

    /**
     * Java alias for [pushFragment].
     * Provides an easy and reliable means of pushing to the Nav Stack.
     * The provided Fragment will replace the calling Fragment which will be added to the back stack.
     * Utilises the bundled animation resources to avoid hierarchy issues when popping the stack.
     *
     * Similar to [pushFragment] except allows you to declare incoming Fragment's animations.
     *
     * @param fragment: The Fragment to be presented
     * @param enterRes: Animation or Animator XML resource file used to define [fragment]'s entry
     * behaviour
     * @param popExitRes: Animation or Animator XML resource file used to define [fragment]'s exit
     * behaviour when [FragmentManager.popBackStack] is called
     */
    @JvmStatic
    fun pushFragment(currentFragment: Fragment, nextFragment: Fragment, @AnimRes @AnimatorRes enterRes: Int, @AnimRes @AnimatorRes popExitRes: Int) = currentFragment.pushFragment(nextFragment, enterRes, popExitRes)

    @JvmStatic
    fun slideFragment(currentFragment: Fragment, nextFragment: Fragment) = currentFragment.slideFragment(nextFragment)

    @JvmStatic
    fun clearBackStack(currentFragment: Fragment) = currentFragment.clearBackStack()

    @JvmStatic
    fun clearBackStack(activity: FragmentActivity) = activity.clearBackStack()
}