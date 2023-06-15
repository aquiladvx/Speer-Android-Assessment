package dev.aquiladvx.speerandroidassessment.ui.user_connections

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.HolderConnectionBinding

class UserConnectionViewHolder(private val binding: HolderConnectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(user: GithubUserProfile, clickListener: ((String) -> Unit)?) {
        with(binding) {
            Glide
                .with(itemView.context)
                .load(user.avatarUrl)
                .into(ivUserAvatar)

            tvUserUsername.text = user.login
            clConnection.setOnClickListener { clickListener?.invoke(user.login) }
        }

    }
}