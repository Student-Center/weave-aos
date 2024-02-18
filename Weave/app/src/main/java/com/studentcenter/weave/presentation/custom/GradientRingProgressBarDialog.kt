package com.studentcenter.weave.presentation.custom

import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import com.studentcenter.weave.R

class GradientRingProgressBarDialog(context: Context) : Dialog(context) {

    private val mHandler = Handler(Looper.getMainLooper())
    private lateinit var progressBar: GradientRingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)

        progressBar = findViewById(R.id.progress)

        startRotation()
        setCancelable(false)
    }

    override fun onStart() {
        super.onStart()

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun startRotation() {
        mHandler.postDelayed(mRunnable, 70)
    }

    private val mRunnable = object : Runnable {
        override fun run() {
            progressBar.rotation = progressBar.rotation + 20f
            mHandler.postDelayed(this, 70)
        }
    }

    override fun show() {
        if(!this.isShowing) super.show()
    }
}
