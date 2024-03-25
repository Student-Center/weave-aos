package com.studentcenter.weave.presentation.view.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.density
import com.studentcenter.weave.databinding.ItemTeamDetailProfileBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.enums.MbtiType
import com.studentcenter.weave.domain.extension.emoji

class DetailRvAdapter: RecyclerView.Adapter<DetailRvAdapter.ProfileViewHolder>() {
    var dataList = listOf<TeamDetailMemberEntity>()

    inner class ProfileViewHolder(private val binding: ItemTeamDetailProfileBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: TeamDetailMemberEntity){

            // 아직 미팅 프로필이 저장되지 않았을 경우 Frame Gone 처리
            if(data.animalType.isNullOrEmpty()) binding.tvProfileAnimal.visibility = View.GONE
            if(data.height == 0) binding.tvProfileHeight.visibility = View.GONE

            binding.tvProfileMbti.text = MbtiType.values().find { it.name == data.mbti.uppercase() }?.description ?: ""
            binding.tvProfileAnimal.text = data.animalType
            binding.tvProfileHeight.text = "${emoji(0x1F4CF)} ${itemView.context.getString(R.string.cm, data.height.toString())}"
            binding.tvProfileUniv.text = data.universityName
            binding.tvProfileMajor.text = data.majorName
            binding.tvProfileAge.text = "${data.birthYear.toString().takeLast(2)}년생"

            binding.ivProfileCertified.setImageDrawable(
                if(data.isUnivVerified){
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_certified)
                } else {
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_non_certified)
                }
            )

            Glide.with(binding.ivProfile)
                .load("${BuildConfig.MBTI_DETAIL}${data.mbti.uppercase()}.png")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.ivProfile.setBackgroundColor(Color.TRANSPARENT)

                        val params = binding.ivProfile.layoutParams as ConstraintLayout.LayoutParams
                        params.width = (140 * density!!).toInt()
                        params.height = (120 * density!!).toInt()
                        binding.ivProfile.layoutParams = params
                        return false
                    }
                })
                .into(binding.ivProfile)
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