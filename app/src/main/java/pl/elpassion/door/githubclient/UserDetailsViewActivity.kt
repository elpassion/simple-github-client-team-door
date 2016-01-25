package pl.elpassion.door.githubclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import pl.elpassion.door.githubclient.adapter.UserRepositoriesAdapter

class UserDetailsViewActivity : AppCompatActivity(){

    companion object {
        private val userKey = "userKey"

        fun start(context : Context, user : User) {
            val intent = Intent(context, UserDetailsViewActivity::class.java)
            intent.putExtra(userKey, user)
            context.startActivity(intent)
        }
    }

    val repositoriesRecycleView : RecyclerView by lazy { findViewById(R.id.user_details_repositories_list) as RecyclerView }
    val userAvatar : ImageView by lazy { findViewById(R.id.user_details_avatar) as ImageView}
    val userName : TextView by lazy { findViewById(R.id.user_details_name) as TextView}

    val userRepositoriesService by lazy { GithubSearchViewActivity.retrofit.create(UserRepositoriesService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_user_details)
        val user = intent.getParcelableExtra<User>(userKey)
        userName.text = user.name
        Glide.with(this)
                .load(user.avatarUrl)
                .into(userAvatar)

        repositoriesRecycleView.layoutManager = LinearLayoutManager(this)
        userRepositoriesService.getUserRepositories(user.name)
        .applySchedulers()
        .subscribe ({ repositoriesRecycleView.adapter =  UserRepositoriesAdapter(it)},
                {})
        Log.e("Uzytkownik", user.name)
    }

}