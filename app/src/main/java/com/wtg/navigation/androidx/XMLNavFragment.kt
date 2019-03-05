package com.wtg.navigation.androidx

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wtg.navigation.R
import kotlinx.android.synthetic.main.fragment_example.*
import java.util.Random

class XMLNavFragment : Fragment() {

    private var color = R.color.random_background_0 + Random().nextInt(8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        color = savedInstanceState?.getInt("color", color) ?: color
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("color", color)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false).apply {
            setBackgroundResource(color)
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
        findNavController().navigate(R.id.navPushIn)
    }

    private fun onRaiseNextFragment() {
        disableButtons()
        findNavController().navigate(R.id.navPushUp)
    }

    private fun popBackStack() {
        disableButtons()
        fragmentManager?.popBackStack()
    }

}