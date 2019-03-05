package com.wtg.navigation.kotlin

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brent.navigation.pushFragment
import com.wtg.navigation.R
import kotlinx.android.synthetic.main.fragment_example.*
import java.util.Random

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
        examplePop.isEnabled = false
    }

    private fun onShowNextFragment() {
        disableButtons()
        pushFragment(ExampleFragment.newInstance())
    }

    private fun onRaiseNextFragment() {
        disableButtons()
        pushFragment(ExampleFragment.newInstance(), R.anim.fragment_up, R.anim.fragment_down)
    }

    private fun popBackStack() {
        disableButtons()
        fragmentManager?.popBackStack()
    }

}