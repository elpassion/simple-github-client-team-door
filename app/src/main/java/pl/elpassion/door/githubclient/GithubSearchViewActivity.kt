package pl.elpassion.door.githubclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.Button

class GithubSearchViewActivity : AppCompatActivity() {

    val crashButton by lazy { findViewById(R.id.crash_button) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_search_view)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        crashButton.setOnClickListener {
            throw RuntimeException("Forced Crash !!!")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_github_search_view, menu)
        return true
    }

}
