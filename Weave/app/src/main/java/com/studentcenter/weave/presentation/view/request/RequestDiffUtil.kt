package com.studentcenter.weave.presentation.view.request

import androidx.recyclerview.widget.DiffUtil
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity

class RequestDiffUtil(
    private val oldList: List<MeetingListItemEntity>,
    private val newList: List<MeetingListItemEntity>
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