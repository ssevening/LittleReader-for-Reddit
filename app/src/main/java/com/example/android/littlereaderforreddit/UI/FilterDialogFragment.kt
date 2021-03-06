package com.example.android.littlereaderforreddit.UI

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.android.littlereaderforreddit.Manager.SubredditsManager
import com.example.android.littlereaderforreddit.R


class FilterDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(): FilterDialogFragment {
            val frag = FilterDialogFragment()
            return frag
        }
    }

    interface FilterDialogListener {
        fun onCickFilterDone()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.dialog_filter, null)
        builder.setView(view)

        val recyclerView = view.findViewById(R.id.filter_recycler) as RecyclerView
        val listener = targetFragment as FilterDialogListener

        val subreddits = SubredditsManager.getAllSubredditsList()
        var subredditsPrefs = SubredditsManager.getExcludedSubredditsList()
        recyclerView.adapter = FilterAdapter(subreddits, subredditsPrefs)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        builder.setPositiveButton("Done", { dialog, which ->
            SubredditsManager.setExcludedSubredditsList(subredditsPrefs)
            listener.onCickFilterDone()
        })

        return builder.create()
    }

}