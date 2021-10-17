package vlad.chetrari.bvtesttask.app.twitter.statuses

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus

class TwitterLiveStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
    private val lifespanProgressBar: ProgressBar
        get() = itemView.findViewById(R.id.lifespanProgressBar)

    fun bind(liveStatus: TwitterLiveStatus) {
        val status = liveStatus.status
        bindUserData(status)
        twitText.text = status.text
        bindLifespanData(liveStatus)
    }

    private fun bindUserData(status: TwitterStatus) {
        status.userProfileImageUrl?.let { url -> userImage.load(url) }
        userScreenName.text = res.getString(R.string.user_screen_name_format, status.userScreenName)
        status.userDescription.let { description ->
            userDescription.text = description
            userDescription.isVisible = description.isNotBlank()
        }
    }

    private fun bindLifespanData(liveStatus: TwitterLiveStatus) {
        lifespanProgressBar.max = liveStatus.timeToLiveSeconds
        lifespanProgressBar.progress = liveStatus.timeToLiveSeconds
        liveStatus.onCountdownTick = {
            lifespanProgressBar.progress = liveStatus.lifespanCountdownSeconds
        }
    }

    private fun ImageView.load(url: String) = Picasso.get().load(url).into(this)
}