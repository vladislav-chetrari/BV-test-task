package vlad.chetrari.bvtesttask.app.main.stream.search.v2

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.data.model.ui.Twit
import java.text.SimpleDateFormat
import java.util.*


class TwitListAdapter : ListAdapter<Twit, TwitListAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_twit, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val creationTimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        private val twitText: TextView
            get() = itemView.findViewById(R.id.twitText)
        private val twitTime: TextView
            get() = itemView.findViewById(R.id.twitTime)

        fun bind(twit: Twit) {
            twitText.text = twit.text
            twitText.movementMethod = LinkMovementMethod.getInstance()
            twitTime.text = creationTimeFormatter.format(Date(twit.creationTimestampEpochMillis))
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Twit>() {
        override fun areItemsTheSame(oldItem: Twit, newItem: Twit) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Twit, newItem: Twit) = oldItem == newItem
    }
}