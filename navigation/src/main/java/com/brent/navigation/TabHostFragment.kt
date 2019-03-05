package com.brent.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * Simple Fragment that acts as a designated host for a Page's Navigation stack.
 * Any number of Fragments can be added to the content ViewGroup as normal.
 * You will however have a hard time accessing an instance of TabHostFragment directly,
 * instead you should use the [getFragmentManager] method within the instance of [Fragment] passed in
 * [TabNavigation.addPage], which will refer to this Fragment's [getChildFragmentManager]
 */
internal class TabHostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = context ?: return null

        return FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            id = R.id.navigation_content
        }
    }
}