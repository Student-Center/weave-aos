package com.weave.presentation.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.weave.presentation.R

class CustomAutoCompleteViewAdapter(context: Context, private val resource: Int, data: List<String>): ArrayAdapter<String>(context, resource, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(resource, parent, false)

        val textView: TextView = view.findViewById(R.id.tv_item)
        textView.text = getItem(position)

        return view
    }
}
