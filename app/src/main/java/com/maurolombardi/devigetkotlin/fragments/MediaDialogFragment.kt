package com.maurolombardi.devigetkotlin.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.maurolombardi.devigetkotlin.R
import kotlinx.android.synthetic.main.media_dialog_layout.*

open class MediaFragmentDialog(private val url: String?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = requireActivity().layoutInflater

        builder.setView(inflater.inflate(R.layout.media_dialog_layout, null))

        return builder.create()
    }

    private fun showMedia() {
        if (url == null) {
            dismiss()
        }
        dialog_close!!.setOnClickListener { dismiss() }
        dialog_webview!!.settings.javaScriptEnabled = true
        dialog_webview.loadUrl(url)
    }

    override fun onStart() {
        super.onStart()
        showMedia()
    }
}
