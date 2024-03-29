package com.studentcenter.weave.presentation.view.team

import androidx.recyclerview.widget.DiffUtil
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity

class TeamDiffUtil(
    private val oldList: List<GetMyTeamItemEntity>,
    private val newList: List<GetMyTeamItemEntity>
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