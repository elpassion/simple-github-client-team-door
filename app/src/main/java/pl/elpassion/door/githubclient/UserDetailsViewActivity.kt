package pl.elpassion.door.githubclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_user_details)
        Log.e("Uzytkownik", intent.getParcelableExtra<User>(userKey).name)
    }

}