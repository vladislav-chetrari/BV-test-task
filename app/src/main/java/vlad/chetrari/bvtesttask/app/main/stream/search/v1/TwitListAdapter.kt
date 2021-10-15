package vlad.chetrari.bvtesttask.app.main.stream.search.v1

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
import vlad.chetrari.bvtesttask.data.model.ui.SearchStreamTwit

class TwitListAdapter : ListAdapter<SearchStreamTwit, TwitListAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_twit_v1, parent, false)
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

        fun bind(twit: SearchStreamTwit) {
            twit.userProfileImageUrl?.let { url -> userImage.load(url) }
            userScreenName.text = res.getString(R.string.twit_screen_name_format, twit.userScreenName)
            twit.userDescription.let { description ->
                userDescription.text = description
                userDescription.isVisible = description.isNotBlank()
            }
            twitText.text = twit.text
        }

        private fun ImageView.load(url: String) = Picasso.get().load(url).into(this)
    }

    private class ItemCallback : DiffUtil.ItemCallback<SearchStreamTwit>() {
        override fun areItemsTheSame(oldItem: SearchStreamTwit, newItem: SearchStreamTwit) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SearchStreamTwit, newItem: SearchStreamTwit) = oldItem == newItem
    }
}