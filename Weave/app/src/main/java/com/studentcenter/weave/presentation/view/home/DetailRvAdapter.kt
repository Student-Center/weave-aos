package com.studentcenter.weave.presentation.view.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.ItemTeamDetailProfileBinding
import com.studentcenter.weave.domain.entity.home.Member

class DetailRvAdapter: RecyclerView.Adapter<DetailRvAdapter.ProfileViewHolder>() {
    var dataList = listOf<Member>()

    inner class ProfileViewHolder(private val binding: ItemTeamDetailProfileBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Member){
            binding.tvProfileMbti.text = data.mbti
            binding.tvProfileAnimal.text = data.animal
            binding.tvProfileHeight.text = data.height
            binding.tvProfileUniv.text = data.univ
            binding.tvProfileMajor.text = data.major
            binding.tvProfileAge.text = data.age

            Glide.with(binding.ivProfile)
                .load(data.url)
                .transform(CenterCrop(), RoundedCorners(40))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.ivProfile.setBackgroundResource(R.drawable.shape_profile_radius_10)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.ivProfile.background = resource
                        return true
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