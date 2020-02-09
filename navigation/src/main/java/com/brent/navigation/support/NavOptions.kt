package com.brent.navigation.support

import androidx.navigation.NavOptions
import com.brent.navigation.R

fun NavOptions.Builder.setPushAnimations(willClearBackStack: Boolean = false) = apply {
    setEnterAnim(R.anim.fragment_from_right)
    setExitAnim(R.animator.fragment_pop_out)

    if(willClearBackStack) {
        setPopEnterAnim(R.animator.fragment_pop_in)
        setPopExitAnim(R.animator.fragment_pop_out)
    } else {
        setPopEnterAnim(R.animator.fragment_pop_in)
        setPopExitAnim(R.anim.fragment_to_right)
    }
}

fun NavOptions.Builder.setSlideAnimations(willClearBackStack: Boolean = false) = apply {
    setEnterAnim(R.anim.fragment_from_right)
    setExitAnim(R.anim.fragment_to_left)

    if(willClearBackStack) {
        setPopEnterAnim(R.anim.fragment_from_left)
        setPopExitAnim(R.anim.fragment_to_left)
    } else {
        setPopEnterAnim(R.anim.fragment_from_left)
        setPopExitAnim(R.anim.fragment_to_right)
    }
}

fun pushIn(willClearBackStack: Boolean = false) = NavOptions.Builder()
    .setPushAnimations(willClearBackStack)
    .build()

fun slideIn(willClearBackStack: Boolean = false) = NavOptions.Builder()
    .setSlideAnimations(willClearBackStack)
    .build()

object NavOptions {

    @JvmOverloads
    @JvmStatic
    fun pushIn(willClearBackStack: Boolean = false) = NavOptions.Builder()
        .setPushAnimations(willClearBackStack)
        .build()

    @JvmOverloads
    @JvmStatic
    fun slideIn(willClearBackStack: Boolean = false) = NavOptions.Builder()
        .setSlideAnimations(willClearBackStack)
        .build()

}