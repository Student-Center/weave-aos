package com.studentcenter.weave.presentation.view.chat

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.density
import com.studentcenter.weave.databinding.ItemMatchDetailProfileBinding
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingMemberEntity
import com.studentcenter.weave.domain.enums.AnimalType
import com.studentcenter.weave.domain.enums.MbtiType
import com.studentcenter.weave.domain.extension.emoji

class MatchDetailRvAdapter: RecyclerView.Adapter<MatchDetailRvAdapter.ProfileViewHolder>() {
    var dataList = listOf<PreparedMeetingMemberEntity>()

    inner class ProfileViewHolder(private val binding: ItemMatchDetailProfileBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: PreparedMeetingMemberEntity){
            binding.tvProfileMbti.text = MbtiType.values().find { it.name == data.mbti.uppercase() }?.description ?: ""
            binding.tvProfileAnimal.text = AnimalType.values().find { it.name == data.animalType }?.description
            binding.tvProfileHeight.text = "${emoji(0x1F4CF)} ${itemView.context.getString(R.string.cm, "수정필요")}"
            binding.tvProfileUniv.text = data.universityName
            binding.tvProfileMajor.text = "수정 필요"
            binding.tvProfileAge.text = "${data.birthYear.toString().takeLast(2)}년생"
            binding.tvKakaoId.text = itemView.context.getString(R.string.match_kakao_id, data.kakaoId)

                binding.ivProfileCertified.setImageDrawable(
                    if(true){ // 수정 필요
                        AppCompatResources.getDrawable(itemView.context, R.drawable.ic_certified)
                    } else {
                        AppCompatResources.getDrawable(itemView.context, R.drawable.ic_non_certified)
                    }
                )

            Glide.with(binding.ivProfile)
                .load("https://i.namu.wiki/i/FsQI6BbZf8RruggqMbw6Yq7rqh0vdKMEzITKNcI2ZqYDxpR2t5Gjx9I5qr9e7Ok4qGOj37w-cJ0e7rObxOEiKTh2GTWErpg7ZhXvEi9YgWTomPifoVlkg9IuXhlTcB_hbIteiCOfjnlYZ4d0CkHVOQ.webp")
                .optionalCenterCrop()
                .transform(RoundedCorners((24 * density!!).toInt()))
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
                        binding.ivProfile.background = null

                        return false
                    }
                })
                .into(binding.ivProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemMatchDetailProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

}