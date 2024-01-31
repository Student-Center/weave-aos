package com.weave.weave.presentation.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.weave.weave.databinding.ItemTeamProfileBinding
import com.weave.weave.domain.entity.home.TeamTestEntity

class HomeRvAdapter: RecyclerView.Adapter<HomeRvAdapter.TeamProfileViewHolder>() {
    var dataList = mutableListOf<TeamTestEntity>()

    inner class TeamProfileViewHolder(private val binding: ItemTeamProfileBinding): RecyclerView.ViewHolder(binding.root) {

        // 코드 수정 필요함 -> 중복코드
        fun bind(data: TeamTestEntity){
            binding.tvTeamType.text = data.type
            binding.tvTeamTitle.text = data.title
            binding.tvTeamLocation.text = data.location

            when(data.type){
                "2:2" -> {
                    binding.item3.visibility = View.GONE
                    binding.item4.visibility = View.GONE

                    // 1
                    Glide.with(binding.ivItemProfile1)
                        .load(data.members[0].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile1)

                    binding.tvItemUniv1.text = data.members[0].univ
                    binding.tvItemMbti1.text = data.members[0].mbti

                    // 2
                    Glide.with(binding.ivItemProfile2)
                        .load(data.members[1].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile2)

                    binding.tvItemUniv2.text = data.members[1].univ
                    binding.tvItemMbti2.text = data.members[1].mbti
                }
                "3:3" -> {
                    binding.item4.visibility = View.GONE

                    // 1
                    Glide.with(binding.ivItemProfile1)
                        .load(data.members[0].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile1)

                    binding.tvItemUniv1.text = data.members[0].univ
                    binding.tvItemMbti1.text = data.members[0].mbti

                    // 2
                    Glide.with(binding.ivItemProfile2)
                        .load(data.members[1].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile2)

                    binding.tvItemUniv2.text = data.members[1].univ
                    binding.tvItemMbti2.text = data.members[1].mbti

                    // 3
                    Glide.with(binding.ivItemProfile3)
                        .load(data.members[2].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile3)

                    binding.tvItemUniv3.text = data.members[2].univ
                    binding.tvItemMbti3.text = data.members[2].mbti
                }
                "4:4" -> {
                    // 1
                    Glide.with(binding.ivItemProfile1)
                        .load(data.members[0].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile1)

                    binding.tvItemUniv1.text = data.members[0].univ
                    binding.tvItemMbti1.text = data.members[0].mbti

                    // 2
                    Glide.with(binding.ivItemProfile2)
                        .load(data.members[1].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile2)

                    binding.tvItemUniv2.text = data.members[1].univ
                    binding.tvItemMbti2.text = data.members[1].mbti

                    // 3
                    Glide.with(binding.ivItemProfile3)
                        .load(data.members[2].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile3)

                    binding.tvItemUniv3.text = data.members[2].univ
                    binding.tvItemMbti3.text = data.members[2].mbti

                    // 4
                    Glide.with(binding.ivItemProfile4)
                        .load(data.members[3].url)
                        .transform(CenterCrop(), RoundedCorners(48))
                        .into(binding.ivItemProfile4)

                    binding.tvItemUniv4.text = data.members[3].univ
                    binding.tvItemMbti4.text = data.members[3].mbti
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamProfileViewHolder {
        val binding = ItemTeamProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TeamProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: TeamProfileViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

}