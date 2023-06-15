package dev.aquiladvx.speerandroidassessment.ui.user_connections

import androidx.recyclerview.widget.RecyclerView
import dev.aquiladvx.speerandroidassessment.common.loadImage
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.HolderConnectionBinding

class UserConnectionViewHolder(private val binding: HolderConnectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(user: GithubUserProfile, clickListener: ((String) -> Unit)?) {
        with(binding) {
            itemView.context.loadImage(user.avatarUrl, ivUserAvatar)
            tvUserUsername.text = user.login
            clConnection.setOnClickListener { clickListener?.invoke(user.login) }
        }

    }
}