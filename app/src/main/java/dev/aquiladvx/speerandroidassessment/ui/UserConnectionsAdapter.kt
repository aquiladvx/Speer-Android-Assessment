package dev.aquiladvx.speerandroidassessment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.databinding.HolderConnectionBinding
import timber.log.Timber

class UserConnectionsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = listOf<GithubUserProfile>()
    private var userClickListener: ((GithubUserProfile) -> Unit)? = null

    fun updateConnections(messages: List<GithubUserProfile>) {
        val lastIndex = items.lastIndex
        items = messages
        Timber.tag("notify").d("$lastIndex to ${items.lastIndex}")
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

    fun setOnClickListener(listener: (user: GithubUserProfile) -> Unit) {
        userClickListener = listener
    }
}