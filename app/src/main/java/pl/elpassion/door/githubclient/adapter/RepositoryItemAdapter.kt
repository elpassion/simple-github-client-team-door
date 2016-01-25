package pl.elpassion.door.githubclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.elpassion.door.githubclient.R
import pl.elpassion.door.githubclient.Repository

class RepositoryItemAdapter(private val repository: Repository) : ItemAdapter {

    override val itemViewType = R.id.repository_list_item

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.github_search_repository_item, parent, false)
        return UserItemHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val nameRateItemHolder = holder as UserItemHolder
        nameRateItemHolder.name.text = repository.name
    }

    private inner class UserItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById(R.id.repository_name) as TextView
    }
}