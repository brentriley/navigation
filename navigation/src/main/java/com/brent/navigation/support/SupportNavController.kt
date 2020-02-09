package com.brent.navigation.support

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

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
            it.pendingDirections = PendingDirections(directions, navOptions)
        } else {
            it.navController.navigate(directions, navOptions)
        }
    } ?: navHostFragment.navController.navigate(directions, navOptions)
}

