package com.wtg.navigation.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.brent.navigation.clearBackStack
import com.brent.navigation.pushFragment
import com.brent.navigation.slideFragment
import com.wtg.navigation.R
import kotlinx.android.synthetic.main.fragment_example.*
import java.util.*

class ExampleFragment : Fragment() {

    companion object {

        fun newInstance(@ColorRes color: Int = R.color.random_background_0 + Random().nextInt(8)) = ExampleFragment().apply {
            arguments = Bundle().apply {
                putInt("color", color)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false).apply {
            arguments?.let {
                setBackgroundResource(it.getInt("color", R.color.random_background_0))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backStackCount = fragmentManager?.backStackEntryCount ?: 0

        exampleDepth.text = getString(R.string.backstack_count, backStackCount)

        examplePresent.setOnClickListener {
            onShowNextFragment()
        }

        examplePresentUp.setOnClickListener {
            onRaiseNextFragment()
        }

        examplePresentClearStack.setOnClickListener {
            onShowNextFragmentAndClear()
        }

        exampleSlide.setOnClickListener {
            onSlideNextFragment()
        }

        exampleSlideClearStack.setOnClickListener {
            onSlideNextFragmentAndClear()
        }

        if(backStackCount == 0) {
            examplePop.visibility = View.GONE
        } else {
            examplePop.setOnClickListener {
                popBackStack()
            }
        }
    }

    private fun disableButtons() {
        examplePresent.isEnabled = false
        examplePresentUp.isEnabled = false
        examplePresentClearStack.isEnabled = false
        exampleSlide.isEnabled = false
        exampleSlideClearStack.isEnabled = false
        examplePop.isEnabled = false
    }

    private fun onShowNextFragment() {
        disableButtons()
        pushFragment(newInstance())
    }

    private fun onRaiseNextFragment() {
        disableButtons()
        pushFragment(newInstance(), R.anim.fragment_up, R.anim.fragment_down)
    }

    private fun onSlideNextFragment() {
        disableButtons()
        slideFragment(newInstance())
    }

    private fun onSlideNextFragmentAndClear() {
        disableButtons()
        clearBackStack()
        slideFragment(newInstance())
    }

    private fun onShowNextFragmentAndClear() {
        disableButtons()
        clearBackStack()
        pushFragment(newInstance())
    }

    private fun popBackStack() {
        disableButtons()
        fragmentManager?.popBackStack()
    }

}