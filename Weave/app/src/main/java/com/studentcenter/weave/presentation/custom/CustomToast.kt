package com.studentcenter.weave.presentation.custom

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.ToastCustomBinding

object CustomToast {
    fun createToast(context: Context, message: String): Toast {
        val inflater = LayoutInflater.from(context)
        val binding: ToastCustomBinding = DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

        binding.tvMsg.text = message

        return Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
}