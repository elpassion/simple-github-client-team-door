package pl.elpassion.door.githubclient.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.Menu
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import pl.elpassion.door.githubclient.R
import pl.elpassion.door.githubclient.adapter.GithubSearchResultListAdapter
import pl.elpassion.door.githubclient.common.applySchedulers
import pl.elpassion.door.githubclient.listeners.CustomTextWatcher
import pl.elpassion.door.githubclient.service.github.GithubService.githubRepositoriesService
import pl.elpassion.door.githubclient.service.github.GithubService.githubUsersService
import pl.elpassion.door.githubclient.service.github.response.GithubSearchItem
import pl.elpassion.door.githubclient.service.github.response.RepositoriesSearchResponse
import pl.elpassion.door.githubclient.service.github.response.User
import pl.elpassion.door.githubclient.service.github.response.UserSearchResponse
import rx.Observable.zip
import rx.Subscription
import java.util.*


class GithubSearchViewActivity : AppCompatActivity() {

    companion object {
        private val endPointError = "Problem z pobraniem danych z Githuba"
    }

    val githubRecycleView: RecyclerView by lazy { findViewById(R.id.search_results_list) as RecyclerView }
    val checkBoxUsersOnly : CheckBox by lazy { findViewById(R.id.check_users_only) as CheckBox }
    val searchName: EditText by lazy { findViewById(R.id.search_name) as EditText }
    val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    var subscription : Subscription? = null
    val searchResults: MutableList<GithubSearchItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_search_view)
        setSupportActionBar(toolbar)
        githubRecycleView.layoutManager = LinearLayoutManager(this)
        searchName.addTextChangedListener(SearchNameChangedListener())
        checkBoxUsersOnly.setOnCheckedChangeListener(CheckUsersOnlyListener())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_github_search_view, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    private fun setRecycleViewAdapter() {
       val elements = if (checkBoxUsersOnly.isChecked)  searchResults.filter { it is User } else searchResults
        githubRecycleView.adapter = GithubSearchResultListAdapter(elements)
    }

    inner class CheckUsersOnlyListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) = setRecycleViewAdapter()
    }

    inner class SearchNameChangedListener : CustomTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            val searchName = searchName.text.toString()
            val usersObservable = githubUsersService.searchForUsersByName(searchName)
            val repositoriesObservable = githubRepositoriesService.searchForReposByName(searchName)
            subscription?.unsubscribe()
            subscription = zip(usersObservable, repositoriesObservable, joinTwoCallsResponses)
                    .applySchedulers()
                    .subscribe (onGithubItemsSearchSuccess, onGithubItemsSearchFailure)
        }

        private val joinTwoCallsResponses = { userResponse: UserSearchResponse, repositoriesResponse: RepositoriesSearchResponse ->
            (userResponse.items + repositoriesResponse.items ).sortedBy { it.name }
        }

        private val onGithubItemsSearchFailure = { e: Throwable ->
            Snackbar.make(githubRecycleView, endPointError, Snackbar.LENGTH_LONG).show()
        }

        private val onGithubItemsSearchSuccess = { it: List<GithubSearchItem> ->
            searchResults.clear()
            searchResults.addAll(it)
            setRecycleViewAdapter()
        }
    }


}
