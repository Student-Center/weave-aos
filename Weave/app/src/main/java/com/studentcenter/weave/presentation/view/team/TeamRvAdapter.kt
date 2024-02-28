package com.studentcenter.weave.presentation.view.team

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.databinding.ItemMyTeamBinding
import com.studentcenter.weave.databinding.ItemTeamFooterBinding
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity
import com.studentcenter.weave.presentation.view.MainActivity

class TeamRvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_VIEW_TYPE_PROFILE = 0
    private val ITEM_VIEW_TYPE_FOOTER = 1

    var dataList = mutableListOf<GetMyTeamItemEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_PROFILE -> {
                val binding = ItemMyTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TeamProfileViewHolder(binding)
            }
            ITEM_VIEW_TYPE_FOOTER -> {
                val binding = ItemTeamFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FooterViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TeamProfileViewHolder -> holder.bind(dataList[position])
            is FooterViewHolder -> {}
        }
    }

    override fun getItemCount(): Int {
        return if (dataList.isNotEmpty()) { dataList.size + 1 } else { 0 }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) {
            ITEM_VIEW_TYPE_FOOTER
        } else {
            ITEM_VIEW_TYPE_PROFILE
        }
    }

    inner class FooterViewHolder(private val binding: ItemTeamFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        // Footer ViewHolder의 내용 처리
    }

    inner class TeamProfileViewHolder(private val binding: ItemMyTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: GetMyTeamItemEntity) {
            binding.tvTeamType.text = "${data.memberCount}:${data.memberCount}"
            binding.tvTeamTitle.text = data.teamIntroduce
            binding.tvTeamLocation.text = data.location

            binding.ibMenu.setOnClickListener {
                var isLeader = false

                for(i in 0 until data.memberInfos.size){
                    if(data.memberInfos[i].isMe && data.memberInfos[i].role == "LEADER"){
                        isLeader = true
                    }
                }

                itemClickListener.onClick(data.teamIntroduce, data.id, isLeader)
            }

            itemView.setOnClickListener {
                (itemView.context as MainActivity).replaceFragmentWithStack(TeamDetailFragment(data.id))
            }

            binding.btnRequest2.setOnClickListener { requestBtnClickListener.onClick(data.teamIntroduce, data.id, false) }
            binding.btnRequest3.setOnClickListener { requestBtnClickListener.onClick(data.teamIntroduce, data.id, false) }
            binding.btnRequest4.setOnClickListener { requestBtnClickListener.onClick(data.teamIntroduce, data.id, false) }

            val visibilityArray = arrayOf(binding.item1, binding.item2, binding.item3, binding.item4)
            val visibilityMemberArray = arrayOf(binding.llItem2Text, binding.llItem3Text, binding.llItem4Text)
            val visibilityButtonArray = arrayOf(binding.btnRequest2, binding.btnRequest3, binding.btnRequest4)

            // 팀원 수에 따라 보이거나 가리기
            for (i in visibilityArray.indices) {
                visibilityArray[i].visibility = if (i < data.memberCount) View.VISIBLE else View.GONE
            }

            // 팀원 정보 설정
            for (i in 0 until data.memberInfos.size) {
                val member = data.memberInfos[i]
                val imageView = when (i) {
                    0 -> binding.ivItemProfile1
                    1 -> binding.ivItemProfile2
                    2 -> binding.ivItemProfile3
                    3 -> binding.ivItemProfile4
                    else -> null
                }
                val myView = when (i) {
                    0 -> binding.viewMy1
                    1 -> binding.viewMy2
                    2 -> binding.viewMy3
                    3 -> binding.viewMy4
                    else -> null
                }
                if(member.isMe){
                    myView?.visibility = View.VISIBLE
                }

                val univTextView = when (i) {
                    0 -> binding.tvItemUniv1
                    1 -> binding.tvItemUniv2
                    2 -> binding.tvItemUniv3
                    3 -> binding.tvItemUniv4
                    else -> null
                }
                val mbtiTextView = when (i) {
                    0 -> binding.tvItemMbti1
                    1 -> binding.tvItemMbti2
                    2 -> binding.tvItemMbti3
                    3 -> binding.tvItemMbti4
                    else -> null
                }

                for (j in 0 until data.memberInfos.size-1) {
                    visibilityMemberArray[j].visibility = View.VISIBLE
                    visibilityButtonArray[j].visibility = View.GONE
                }

                 imageView?.let { loadImage(it, "${BuildConfig.MBTI_LIST}${member.mbti.uppercase()}.png") }

                val univName = if(member.universityName.length >= 6){
                    "${member.universityName.substring(0, 5)}.."
                } else if (member.universityName.length == 5){
                    member.universityName.substring(0, 3)
                } else {
                    member.universityName
                }

                univTextView?.text = "${univName}•${member.birthYear.toString().takeLast(2)}"
                mbtiTextView?.text = member.mbti
            }
        }

        private fun loadImage(imageView: ImageView, url: String) {
            Glide.with(imageView)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(48))
                .into(imageView)
        }
    }

    fun changeList(newItem: List<GetMyTeamItemEntity>){
        val teamDiffUtil = TeamDiffUtil(this.dataList, newItem)
        val diffResult = DiffUtil.calculateDiff(teamDiffUtil)

        this.dataList.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@TeamRvAdapter)
        }
    }

    interface OnItemClickListener{
        fun onClick(teamIntroduce: String, id: String, isLeader: Boolean)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    private lateinit var requestBtnClickListener: OnItemClickListener

    fun setRequestBtnClickListener(onItemClickListener: OnItemClickListener){
        this.requestBtnClickListener = onItemClickListener
    }

}
