package pl.elpassion.door.githubclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import java.util.*

open class BaseAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected  val adapters = ArrayList<ItemAdapter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        for (adapter in adapters) {
            if (adapter.itemViewType == viewType) {
                return adapter.onCreateViewHolder(parent)
            }
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapters[position].onBindViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return adapters.size
    }

    override fun getItemViewType(position: Int): Int {
        return adapters[position].itemViewType
    }
}
