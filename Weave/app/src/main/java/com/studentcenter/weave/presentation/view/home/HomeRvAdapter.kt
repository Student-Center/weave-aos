package com.studentcenter.weave.presentation.view.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.studentcenter.weave.databinding.ItemTeamProfileBinding
import com.studentcenter.weave.domain.entity.team.GetTeamListItemEntity
import com.studentcenter.weave.presentation.view.MainActivity

class HomeRvAdapter : RecyclerView.Adapter<HomeRvAdapter.TeamProfileViewHolder>() {
    var dataList = mutableListOf<GetTeamListItemEntity>()

    inner class TeamProfileViewHolder(private val binding: ItemTeamProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: GetTeamListItemEntity) {
            binding.tvTeamType.text = "${data.memberCount}:${data.memberCount}"
            binding.tvTeamTitle.text = data.teamIntroduce
            binding.tvTeamLocation.text = data.location

            itemView.setOnClickListener {
                (itemView.context as MainActivity).replaceFragmentWithStack(DetailFragment("018dbcf7-c738-79b5-92fa-d508ea7ee7c4"))
            }

            val visibilityArray = arrayOf(binding.item1, binding.item2, binding.item3, binding.item4)

            // 팀원 수에 따라 보이거나 가리기
            for (i in visibilityArray.indices) {
                visibilityArray[i].visibility = if (i < data.memberCount) View.VISIBLE else View.GONE
            }

            // 팀원 정보 설정
            for (i in 0 until data.memberCount) {
                val member = data.memberInfos[i]
                val imageView = when (i) {
                    0 -> binding.ivItemProfile1
                    1 -> binding.ivItemProfile2
                    2 -> binding.ivItemProfile3
                    3 -> binding.ivItemProfile4
                    else -> null
                }
//                imageView?.let {
//                    loadImage(it, member.url)
//                }
                val univTextView = when (i) {
                    0 -> binding.tvItemUniv1
                    1 -> binding.tvItemUniv2
                    2 -> binding.tvItemUniv3
                    3 -> binding.tvItemUniv4
                    else -> null
                }
                univTextView?.text = member.universityName
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
                .transform(CenterCrop(), RoundedCorners(48))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.i("TEST", "false")
                        imageView.setBackgroundResource(R.drawable.shape_profile_radius_10)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.i("TEST", "true")
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

    fun changeList(newItem: List<GetTeamListItemEntity>){
        val homeDiffUtil = HomeDiffUtil(this.dataList, newItem)
        val diffResult = DiffUtil.calculateDiff(homeDiffUtil)

        this.dataList.apply {
            clear()
            addAll(newItem)
            diffResult.dispatchUpdatesTo(this@HomeRvAdapter)
        }
    }
}
