package com.studentcenter.weave.presentation.view.team

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.studentcenter.weave.databinding.ItemMyTeamBinding
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity
import com.studentcenter.weave.presentation.view.MainActivity

class TeamRvAdapter : RecyclerView.Adapter<TeamRvAdapter.TeamProfileViewHolder>() {
    var dataList = mutableListOf<GetMyTeamItemEntity>()

    inner class TeamProfileViewHolder(private val binding: ItemMyTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GetMyTeamItemEntity) {
            binding.tvTeamType.text = when(data.memberCount){
                2 -> "2:2"
                3 -> "3:3"
                4 -> "4:4"
                else -> "0:0"
            }
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

//                imageView?.let { loadImage(it, member.url) }

                // 대학명 임시 로직
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

//        private fun loadImage(imageView: ImageView, url: String) {
//            Glide.with(imageView)
//                .load(url)
//                .transform(CenterCrop(), RoundedCorners(48))
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.i("TEST", "false")
//                        imageView.setBackgroundResource(R.drawable.shape_profile_radius_10)
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        model: Any,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.i("TEST", "true")
//                        imageView.foreground = resource
//                        imageView.setBackgroundColor(itemView.context.getColor(R.color.transparent))
//                        return true
//                    }
//                })
//                .into(imageView)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamProfileViewHolder {
        val binding =
            ItemMyTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: TeamProfileViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun changeList(newItem: List<GetMyTeamItemEntity>){
        val diffUtilCallback = DiffUtilCallback(this.dataList, newItem)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.dataList.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@TeamRvAdapter)
        }
    }

    interface OnItemClickListener{
        fun onClick(teamIntroduce: String, id: String, isLeader: Boolean)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}
