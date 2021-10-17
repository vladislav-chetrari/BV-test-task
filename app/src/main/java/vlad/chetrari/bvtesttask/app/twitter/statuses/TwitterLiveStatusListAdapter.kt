package vlad.chetrari.bvtesttask.app.twitter.statuses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import vlad.chetrari.bvtesttask.R

class TwitterLiveStatusListAdapter : ListAdapter<TwitterLiveStatus, TwitterLiveStatusViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TwitterLiveStatusViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_twitter_status, parent, false)
    )

    override fun onBindViewHolder(holder: TwitterLiveStatusViewHolder, position: Int) = holder.bind(getItem(position))

    private class ItemCallback : DiffUtil.ItemCallback<TwitterLiveStatus>() {
        override fun areItemsTheSame(
            oldItem: TwitterLiveStatus,
            newItem: TwitterLiveStatus
        ) = oldItem.status.id == newItem.status.id

        override fun areContentsTheSame(
            oldItem: TwitterLiveStatus,
            newItem: TwitterLiveStatus
        ) = oldItem.status == newItem.status
    }
}