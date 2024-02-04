package com.weave.weave.presentation.view.team

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.weave.weave.domain.entity.home.TeamTestEntity

class DiffUtilCallback(
    private val oldList: List<TeamTestEntity>,
    private val newList: List<TeamTestEntity>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        Log.e("Diff", "Items: ${oldList[oldItemPosition].title == newList[newItemPosition].title}")
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        Log.e("Diff", "Contents: ${oldList[oldItemPosition] == newList[newItemPosition]}")
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}