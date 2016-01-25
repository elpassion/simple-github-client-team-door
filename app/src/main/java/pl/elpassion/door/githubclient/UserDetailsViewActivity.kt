package pl.elpassion.door.githubclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import pl.elpassion.door.githubclient.GithubSearchViewActivity.Companion.retrofit
import pl.elpassion.door.githubclient.adapter.UserRepositoriesAdapter
import rx.Subscription

class UserDetailsViewActivity : AppCompatActivity() {

    companion object {
        private val endPointError = "Problem z pobraniem danych z Githuba"
        private val userKey = "userKey"

        fun start(context: Context, user: User) {
            val intent = Intent(context, UserDetailsViewActivity::class.java)
            intent.putExtra(userKey, user)
            context.startActivity(intent)
        }
    }

    val repositoriesRecycleView: RecyclerView by lazy { findViewById(R.id.user_details_repositories_list) as RecyclerView }
    val userAvatar: ImageView by lazy { findViewById(R.id.user_details_avatar) as ImageView }
    val userName: TextView by lazy { findViewById(R.id.user_details_name) as TextView }
    val userRepositoriesService by lazy { retrofit.create(UserRepositoriesService::class.java) }
    val user by lazy { intent.getParcelableExtra<User>(userKey) }
    var subscription : Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_user_details)
        setUpUserDetails(user)
        setUpUserRepositories(user)
    }

    private fun setUpUserRepositories(user: User) {
        repositoriesRecycleView.layoutManager = LinearLayoutManager(this)
        subscription?.unsubscribe()
        subscription = userRepositoriesService.getUserRepositories(user.name)
                .applySchedulers()
                .subscribe (onUserRepositoriesDownloadSuccess, onUserRepositoriesDownloadFailure)
    }

    private fun setUpUserDetails(user: User) {
        userName.text = user.name
        Glide.with(this)
                .load(user.avatarUrl)
                .into(userAvatar)
    }

    private val onUserRepositoriesDownloadSuccess = { it: List<Repository> ->
        repositoriesRecycleView.adapter = UserRepositoriesAdapter(it)
    }

    private val onUserRepositoriesDownloadFailure = { x: Throwable ->
        Snackbar.make(repositoriesRecycleView, endPointError, LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

}
