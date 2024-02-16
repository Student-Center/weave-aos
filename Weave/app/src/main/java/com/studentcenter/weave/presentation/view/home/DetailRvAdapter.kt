package com.studentcenter.weave.presentation.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.ItemTeamDetailProfileBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.enums.AnimalType
import com.studentcenter.weave.domain.enums.MbtiType
import com.studentcenter.weave.domain.extension.emoji

class DetailRvAdapter: RecyclerView.Adapter<DetailRvAdapter.ProfileViewHolder>() {
    var dataList = listOf<TeamDetailMemberEntity>()

    inner class ProfileViewHolder(private val binding: ItemTeamDetailProfileBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: TeamDetailMemberEntity){
            binding.tvProfileMbti.text = MbtiType.values().find { it.name == data.mbti.uppercase() }?.description ?: ""
            binding.tvProfileAnimal.text = AnimalType.values().find { it.name == data.animalType }?.description
            binding.tvProfileHeight.text = "${emoji(0x1F4CF)} ${itemView.context.getString(R.string.cm, data.height.toString())}"
            binding.tvProfileUniv.text = data.universityName
            binding.tvProfileMajor.text = data.majorName
            binding.tvProfileAge.text = "${data.birthYear.toString().takeLast(2)}년생"

//            Glide.with(binding.ivProfile)
//                .load(data.url)
//                .transform(CenterCrop(), RoundedCorners(40))
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        binding.ivProfile.setBackgroundResource(R.drawable.shape_profile_radius_10)
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
//                        binding.ivProfile.background = resource
//                        return true
//                    }
//                })
//                .into(binding.ivProfile)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemTeamDetailProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

}