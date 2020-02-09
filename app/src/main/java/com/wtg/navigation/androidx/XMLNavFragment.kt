package com.wtg.navigation.androidx

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.brent.navigation.support.navigate
import com.brent.navigation.support.pushIn
import com.wtg.navigation.R
import kotlinx.android.synthetic.main.fragment_example.*
import java.util.*

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
        navigate(R.id.navPushIn)
    }

    private fun onRaiseNextFragment() {
        disableButtons()
        navigate(R.id.navPushUp)
    }

    private fun onShowNextFragmentAndClear() {
        disableButtons()
        navigate(R.id.navPushInClearStack)
    }

    private fun onSlideNextFragment() {
        disableButtons()
        Handler().postDelayed({
            navigate(R.id.navSlideIn, pushIn())
        }, 3000)
    }

    private fun onSlideNextFragmentAndClear() {
        disableButtons()
        navigate(R.id.navSlideInClearStack)
    }


    private fun popBackStack() {
        disableButtons()
        activity?.onBackPressed()
    }

}