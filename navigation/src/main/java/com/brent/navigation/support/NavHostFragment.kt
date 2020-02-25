package com.brent.navigation.support

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.brent.navigation.NavLog

/**
 * Simple extension of NavHostFragment which allows for better navigation during state loss.
 * While it is recommended that you manage that state of your app correctly (eg aborting navigation while
 * backgrounded), there are often cases where this cannot be done in real world implementations.
 *
 * If the FragmentManager has had its state saved, this will obviously run into issues.
 * But in those cases your app should be able to recover based on your SavedState restoration code.
 *
 * With Android X if a fragment is stopped and a fragment transaction is attempted without commitAllowingStateLoss
 * your app will crash. However commitAllowingState loss has its own issues;
 *  - If your fragment was just stopped and is collected / saved, you will be on the wrong screen
 *  - If you want to manipulate the back stack, you cannot use clearBackStack
 *
 */

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
            NavLog.d("Attempting to restore pendingDirections")

            val pending = state.getBundle(KEY_SAVED_NAV_STATE) ?: Bundle()
            pendingDirections = PendingDirections.fromBundle(pending)
            if(pendingDirections != null) {
                NavLog.d("pendingDirections restored")
            } else {
                NavLog.e("No pending directions found")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        pendingDirections?.let { pending ->
            NavLog.d("Restored with pending directions, committing now")
            pendingDirections = null
            navController.navigate(pending, pending.navOptions)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        pendingDirections?.let { pending ->
            NavLog.d("Saving pendingDirections in onSaveInstanceState")
            outState.putBundle(KEY_SAVED_NAV_STATE, Bundle().also { state ->
                pending.toBundle(state)
            })
        }
    }
}