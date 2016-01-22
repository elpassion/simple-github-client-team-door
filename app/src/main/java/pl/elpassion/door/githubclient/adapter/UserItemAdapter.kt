package pl.elpassion.door.githubclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import pl.elpassion.door.githubclient.R
import pl.elpassion.door.githubclient.User


class UserItemAdapter(private val user: User) : ItemAdapter {

    override val itemViewType = R.id.user_list_item

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.github_search_result_list, parent, false)
        return UserItemHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val nameRateItemHolder = holder as UserItemHolder
        Glide.with(nameRateItemHolder.avatar.context)
                .load(user.avatarUrl)
                .into(nameRateItemHolder.avatar)
        nameRateItemHolder.name.text = user.name
    }

    private inner class UserItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById(R.id.user_name) as TextView
        val avatar = itemView.findViewById(R.id.user_avatar) as ImageView
    }
}