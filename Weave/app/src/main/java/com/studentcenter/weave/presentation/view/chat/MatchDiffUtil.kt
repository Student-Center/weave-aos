package com.studentcenter.weave.presentation.view.chat

import androidx.recyclerview.widget.DiffUtil
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingItemEntity

class MatchDiffUtil(
    private val oldList: List<PreparedMeetingItemEntity>,
    private val newList: List<PreparedMeetingItemEntity>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}