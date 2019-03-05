package com.wtg.navigation.graph

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wtg.navigation.R
import kotlinx.android.synthetic.main.fragment_state.*
import java.util.Random

class StateCheckFragment: Fragment() {

    private val alphabet = "abcdefghijklmnopqrstuvwxyz"

    private val random = Random()

    private var stateNumber = 1
    private var stateLetter = alphabet[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.apply {
            stateNumber = getInt("number", stateNumber)
            stateLetter = getChar("letter", stateLetter)
            Log.e("TAG", "Restoring instance state to $stateNumber $stateLetter")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.e("TAG", "Saving instance state as $stateNumber $stateLetter")

        outState.putInt("number", stateNumber)
        outState.putChar("letter", stateLetter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindState()

        stateChange.setOnClickListener {
            onChangeState()
        }
    }

    private fun onChangeState() {
        stateNumber = random.nextInt(100)
        stateLetter = alphabet.random()
        bindState()
    }

    private fun bindState() {
        stateValue.text = getString(R.string.state_current, stateNumber, stateLetter)
    }
}