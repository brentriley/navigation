package com.brent.navigation.support

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

internal const val KEY_SAVED_NAV_STATE = "navigation::pending"
internal const val KEY_NAV_ACTION_ID = "navigation::actionId"
internal const val KEY_NAV_ARGUMENTS = "navigation::arguments"

internal const val KEY_NAV_OPTIONS = "navigation::options"

internal const val KEY_NAV_ENTER_RES = "navigation::options::enter_res"
internal const val KEY_NAV_EXIT_RES = "navigation::options::exit_res"
internal const val KEY_NAV_POP_ENTER_RES = "navigation::options::pop_enter_res"
internal const val KEY_NAV_POP_EXIT_RES = "navigation::options::pop_exit_res"
internal const val KEY_NAV_POP_TO = "navigation::options::pop_to"
internal const val KEY_NAV_POP_INCLUSIVE = "navigation::options::pop_inclusive"
internal const val KEY_NAV_SINGLE_TOP = "navigation::options::single_top"

class PendingDirections @JvmOverloads constructor(
    @IdRes private val actionId: Int,
    private val args: Bundle = Bundle(),
    val navOptions: NavOptions? = null
) : NavDirections {

    constructor(
        directions: NavDirections,
        navOptions: NavOptions?
    ) : this(directions.actionId, directions.arguments, navOptions)

    override fun getActionId() = actionId
    override fun getArguments() = args

    fun toBundle(bundle: Bundle? = null): Bundle {
        return (bundle ?: Bundle()).apply {
            putInt(KEY_NAV_ACTION_ID, actionId)
            putBundle(KEY_NAV_ARGUMENTS, arguments)

            navOptions?.let { options ->
                putBundle(KEY_NAV_OPTIONS, options.toBundle())
            }
        }
    }

    private fun NavOptions.toBundle(bundle: Bundle? = null): Bundle {
        return (bundle ?: Bundle()).apply {
            putInt(KEY_NAV_ENTER_RES, enterAnim)
            putInt(KEY_NAV_EXIT_RES, exitAnim)
            putInt(KEY_NAV_POP_ENTER_RES, popEnterAnim)
            putInt(KEY_NAV_POP_EXIT_RES, popExitAnim)
            putInt(KEY_NAV_POP_TO, popUpTo)
            putBoolean(KEY_NAV_POP_INCLUSIVE, isPopUpToInclusive)
            putBoolean(KEY_NAV_SINGLE_TOP, shouldLaunchSingleTop())
        }
    }

    companion object {

        private fun NavOptions.Builder.setBundle(bundle: Bundle?) = bundle?.let { options ->
            setEnterAnim(options.getInt(KEY_NAV_ENTER_RES, -1))
            setExitAnim(options.getInt(KEY_NAV_EXIT_RES, -1))
            setPopEnterAnim(options.getInt(KEY_NAV_POP_ENTER_RES, -1))
            setPopExitAnim(options.getInt(KEY_NAV_POP_EXIT_RES, -1))
            setLaunchSingleTop(options.getBoolean(KEY_NAV_SINGLE_TOP, false))
            setPopUpTo(
                options.getInt(KEY_NAV_POP_TO, -1),
                options.getBoolean(KEY_NAV_POP_INCLUSIVE, false)
            )
        } ?: this


        fun fromBundle(bundle: Bundle?) = bundle
            ?.takeIf { it.getInt(KEY_NAV_ACTION_ID, -1) > 0 }
            ?.let { directionBundle ->
                PendingDirections(
                    directionBundle.getInt(KEY_NAV_ACTION_ID),
                    directionBundle.getBundle(KEY_NAV_ARGUMENTS) ?: Bundle(),
                    if (directionBundle.containsKey(KEY_NAV_OPTIONS)) {
                        NavOptions.Builder()
                            .setBundle(directionBundle.getBundle(KEY_NAV_OPTIONS))
                            .build()
                    } else {
                        null
                    }
                )
            }
    }
}