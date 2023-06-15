package dev.aquiladvx.speerandroidassessment.ui.user_connections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.HolderConnectionBinding

class UserConnectionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<GithubUserProfile>()
    private var userClickListener: ((String) -> Unit)? = null

    fun updateConnections(messages: List<GithubUserProfile>) {
        val lastIndex = items.lastIndex
        items = messages
        notifyItemRangeChanged(lastIndex, items.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = HolderConnectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserConnectionViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserConnectionViewHolder).bind(items[position], userClickListener)
    }

    fun setOnUserClickListener(listener: (user: String) -> Unit) {
        userClickListener = listener
    }
}