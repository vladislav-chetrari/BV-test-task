package vlad.chetrari.bvtesttask.app.main.routes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vlad.chetrari.bvtesttask.R

class MainRoutesListAdapter(
    private val onRouteSelected: (MainRoute) -> Unit
) : ListAdapter<MainRoute, MainRoutesListAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_main_route, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), onRouteSelected)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView
            get() = itemView.findViewById(R.id.title)
        private val subtitle: TextView
            get() = itemView.findViewById(R.id.subtitle)

        fun bind(route: MainRoute, onRouteSelected: (MainRoute) -> Unit) {
            title.setText(route.titleResId)
            subtitle.setText(route.subtitleResId)
            itemView.setBackgroundColor(itemView.resources.getColor(route.backgroundColorResId, null))
            itemView.setOnClickListener { onRouteSelected(route) }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<MainRoute>() {
        override fun areItemsTheSame(oldItem: MainRoute, newItem: MainRoute) = oldItem.ordinal == newItem.ordinal
        override fun areContentsTheSame(oldItem: MainRoute, newItem: MainRoute) = oldItem == newItem
    }
}