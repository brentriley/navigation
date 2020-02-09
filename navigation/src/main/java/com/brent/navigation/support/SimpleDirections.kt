package com.brent.navigation.support

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections

class SimpleDirections @JvmOverloads constructor(
    @IdRes private val actionId: Int,
    private val args: Bundle = Bundle()
) : NavDirections {
    override fun getActionId() = actionId
    override fun getArguments() = args
}