package com.studentcenter.weave.presentation.view.chat

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication
import com.studentcenter.weave.databinding.ItemTeamProfileBinding
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingItemEntity
import com.studentcenter.weave.presentation.view.MainActivity

class MatchRvAdapter : RecyclerView.Adapter<MatchRvAdapter.TeamProfileViewHolder>() {
    var dataList = mutableListOf<PreparedMeetingItemEntity>()

    inner class TeamProfileViewHolder(private val binding: ItemTeamProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: PreparedMeetingItemEntity) {
            val teamData = data.otherTeam

            binding.tvTeamType.text = "${teamData.memberCount}:${teamData.memberCount}"
            binding.tvTeamTitle.text = teamData.teamIntroduce
            binding.tvTeamLocation.text = teamData.location

            itemView.setOnClickListener {
                (itemView.context as MainActivity).replaceFragmentWithStack(MatchDetailFragment(data))
            }

            val visibilityArray = arrayOf(binding.item1, binding.item2, binding.item3, binding.item4)

            for (i in visibilityArray.indices) {
                visibilityArray[i].visibility = if (i < teamData.memberCount) View.VISIBLE else View.GONE
            }

            // 팀원 정보 설정
            for (i in 0 until teamData.memberCount) {
                val member = teamData.memberInfos[i]
                val imageView = when (i) {
                    0 -> binding.ivItemProfile1
                    1 -> binding.ivItemProfile2
                    2 -> binding.ivItemProfile3
                    3 -> binding.ivItemProfile4
                    else -> null
                }
                imageView?.let {
                    loadImage(it, member.avatar.toString()) // 실제 프로필 사진 url
                }
                val univTextView = when (i) {
                    0 -> binding.tvItemUniv1
                    1 -> binding.tvItemUniv2
                    2 -> binding.tvItemUniv3
                    3 -> binding.tvItemUniv4
                    else -> null
                }
                univTextView?.text = "${member.universityName}•${member.birthYear.toString().takeLast(2)}"
                val mbtiTextView = when (i) {
                    0 -> binding.tvItemMbti1
                    1 -> binding.tvItemMbti2
                    2 -> binding.tvItemMbti3
                    3 -> binding.tvItemMbti4
                    else -> null
                }
                mbtiTextView?.text = member.mbti
            }
        }

        private fun loadImage(imageView: ImageView, url: String) {
            Glide.with(imageView)
                .load(url)
                .transform(CenterCrop(), RoundedCorners((12* GlobalApplication.density!!).toInt()))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageView.foreground = AppCompatResources.getDrawable(itemView.context, R.drawable.image_defalut_profile)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageView.foreground = resource
                        imageView.setBackgroundColor(itemView.context.getColor(R.color.transparent))
                        return true
                    }
                })
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamProfileViewHolder {
        val binding =
            ItemTeamProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: TeamProfileViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun changeList(newItem: List<PreparedMeetingItemEntity>){
        val homeDiffUtil = MatchDiffUtil(this.dataList, newItem)
        val diffResult = DiffUtil.calculateDiff(homeDiffUtil)

        this.dataList.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@MatchRvAdapter)
        }
    }
}
