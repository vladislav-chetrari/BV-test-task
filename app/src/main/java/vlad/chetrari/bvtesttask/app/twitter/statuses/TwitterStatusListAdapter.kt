package vlad.chetrari.bvtesttask.app.twitter.statuses

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus

class TwitterStatusListAdapter : ListAdapter<TwitterStatus, TwitterStatusListAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_twitter_status, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val res: Resources
            get() = itemView.resources
        private val userImage: ShapeableImageView
            get() = itemView.findViewById(R.id.userImage)
        private val userScreenName: TextView
            get() = itemView.findViewById(R.id.userScreenName)
        private val userDescription: TextView
            get() = itemView.findViewById(R.id.userDescription)
        private val twitText: TextView
            get() = itemView.findViewById(R.id.twitText)

        fun bind(twitterStatus: TwitterStatus) {
            twitterStatus.userProfileImageUrl?.let { url -> userImage.load(url) }
            userScreenName.text = res.getString(R.string.user_screen_name_format, twitterStatus.userScreenName)
            twitterStatus.userDescription.let { description ->
                userDescription.text = description
                userDescription.isVisible = description.isNotBlank()
            }
            twitText.text = twitterStatus.text
        }

        private fun ImageView.load(url: String) = Picasso.get().load(url).into(this)
    }

    private class ItemCallback : DiffUtil.ItemCallback<TwitterStatus>() {
        override fun areItemsTheSame(oldItem: TwitterStatus, newItem: TwitterStatus) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TwitterStatus, newItem: TwitterStatus) = oldItem == newItem
    }
}