package com.brent.navigation.support

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment

class NavHostFragment : NavHostFragment() {

    companion object {

        @JvmOverloads
        @JvmStatic
        fun newInstance(@NavigationRes graphResId: Int, args: Bundle? = null): com.brent.navigation.support.NavHostFragment {
            //We don't know how or when androidx will change their argument setup
            //While this is a bit odd, it allows us to adapt as users import different
            //versions of the androidx libraries
            val baseHost = create(graphResId, args)
            return com.brent.navigation.support.NavHostFragment().apply {
                arguments = baseHost.arguments
            }
        }
    }

    var pendingDirections: PendingDirections? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { state ->
            val pending = state.getBundle(KEY_SAVED_NAV_STATE) ?: Bundle()
            pendingDirections = PendingDirections.fromBundle(pending)
        }
    }

    override fun onResume() {
        super.onResume()

        pendingDirections?.let { pending ->
            pendingDirections = null
            navController.navigate(pending, pending.navOptions)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        pendingDirections?.let { pending ->
            outState.putBundle(KEY_SAVED_NAV_STATE, Bundle().also { state ->
                pending.toBundle(state)
            })
        }
    }
}