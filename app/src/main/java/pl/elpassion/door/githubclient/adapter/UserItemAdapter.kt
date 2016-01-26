package pl.elpassion.door.githubclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.greenrobot.event.EventBus
import pl.elpassion.door.githubclient.R
import pl.elpassion.door.githubclient.event.UserClickedEvent
import pl.elpassion.door.githubclient.service.github.response.User


class UserItemAdapter(private val user: User) : ItemAdapter {

    override val itemViewType = R.id.user_list_item

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.github_search_user_item, parent, false)
        return UserItemHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val userHolder = holder as UserItemHolder
        Glide.with(userHolder.avatar.context)
                .load(user.avatarUrl)
                .into(userHolder.avatar)
        userHolder.name.text = user.name
        val view = userHolder.itemView
        view.setOnClickListener(onUserClicked)
    }

    private val onUserClicked = { view: View ->
        val userClickedEvent = UserClickedEvent(user)
        EventBus.getDefault().post(userClickedEvent)
    }

    private inner class UserItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById(R.id.user_name) as TextView
        val avatar = itemView.findViewById(R.id.user_avatar) as ImageView
    }
}