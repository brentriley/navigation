package com.brent.navigation.support

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.brent.navigation.NavLog

/**
 * Find a {@link NavController} given a local {@link Fragment}.
 *
 * <p>This method will locate the {@link NavController} associated with this Fragment,
 * looking first for a {@link NavHostFragment} along the given Fragment's parent chain.
 * If a {@link NavController} is not found, this method will look for one along this
 * Fragment's {@link Fragment#getView() view hierarchy} as specified by
 * {@link Navigation#findNavController(View)}.</p>
 *
 * @param fragment the locally scoped Fragment for navigation
 * @return the locally scoped {@link NavController} for navigating from this {@link Fragment}
 * @throws IllegalStateException if the given Fragment does not correspond with a
 * {@link NavHost} or is not within a NavHost.
 */

fun Fragment.findNavHostFragment(): NavHostFragment {
    var findFragment: Fragment = this

    while (true) {
        if (findFragment is NavHostFragment) {
            return findFragment
        }

        val primaryNavFragment = findFragment.parentFragmentManager
            .primaryNavigationFragment

        (primaryNavFragment as? NavHostFragment)?.let {
            return it
        }

        findFragment = findFragment.parentFragment ?: break
    }

    throw IllegalStateException("Fragment $this does not have a NavHostFragment set")
}

/**
 * Wrappers for NavController.navigate methods, but extracts the Support NavHostFragment, rather
 * than relying on the androidx version.
 */

fun Fragment.navigate(@IdRes id: Int, navOptions: NavOptions? = null) {
    navigate(ActionOnlyNavDirections(id), navOptions)
}

fun Fragment.navigate(@IdRes id: Int, arguments: Bundle?, navOptions: NavOptions? = null) {
    navigate(SimpleDirections(id, arguments ?: Bundle()), navOptions)
}

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions? = null) {
    val navHostFragment = findNavHostFragment()

    (navHostFragment as? com.brent.navigation.support.NavHostFragment)?.let {
        if (it.isStateSaved) {
            NavLog.d("Attempting to call navigate when state is saved. Adding pendingDirection")
            it.pendingDirections = PendingDirections(directions, navOptions)
        } else {
            it.navController.navigate(directions, navOptions)
        }
    } ?: navHostFragment.navController.navigate(directions, navOptions).let {
        NavLog.d("No custom nav controller set, is this intentional?")
    }
}

/**
 * Simple Util / Alias setup that provides Java static methods for clarity and ease of use for projects
 * not written in Kotlin.
 */

object NavControllerCompat {

    @JvmStatic
    fun enableRobustDebugging() {
        NavLog.debugLogEnabled = true
    }

    @JvmStatic
    fun findNavHostFragment(fragment: Fragment) = fragment.findNavHostFragment()

    @JvmStatic
    @JvmOverloads
    fun navigate(fragment: Fragment, @IdRes id: Int, navOptions: NavOptions? = null) =
        fragment.navigate(id, navOptions)

    @JvmStatic
    @JvmOverloads
    fun navigate(
        fragment: Fragment, @IdRes id: Int,
        arguments: Bundle?,
        navOptions: NavOptions? = null
    ) = fragment.navigate(id, arguments, navOptions)

    @JvmStatic
    @JvmOverloads
    fun navigate(fragment: Fragment, directions: NavDirections, navOptions: NavOptions? = null) =
        fragment.navigate(directions, navOptions)
}

