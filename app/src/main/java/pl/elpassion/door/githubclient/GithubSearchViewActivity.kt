package pl.elpassion.door.githubclient

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pl.elpassion.door.githubclient.adapter.GithubSearchResultListAdapter
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import rx.Observable
import rx.Observable.zip
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

class GithubSearchViewActivity : AppCompatActivity() {

    companion object {
        private val endPointError = "Problem z pobraniem danych z Githuba"
        val baseUrl = "https://api.github.com"
        val gson: Gson = GsonBuilder()
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .create()
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    val githubRecycleView: RecyclerView by lazy { findViewById(R.id.search_results_list) as RecyclerView }
    val githubRepositoriesService by lazy { retrofit.create(GithubRepositoriesService::class.java) }
    val githubUsersService by lazy { retrofit.create(GithubUsersService::class.java) }
    val searchName: EditText by lazy { findViewById(R.id.search_name) as EditText }
    val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    var subscription : Subscription? = null
    val checkBoxUsersOnly : CheckBox by lazy { findViewById(R.id.check_users_only) as CheckBox }
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

    private fun setRecycleViewAdapter(githubSearchItems: List<GithubSearchItem>) {
        githubRecycleView.adapter = GithubSearchResultListAdapter(githubSearchItems)
    }


    inner class SearchNameChangedListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val searchName = searchName.text.toString()
            val usersObservable = githubUsersService.searchForUsersByName(searchName)
            val repositoriesObservable = githubRepositoriesService.searchForReposByName(searchName)
            subscription?.unsubscribe()
            subscription = zip(usersObservable, repositoriesObservable, joinTwoCallsResponses)
                    .applySchedulers()
                    .subscribe (onGithubItemsSearchSuccess, onGithubItemsSearchFailure)
        }

        private val joinTwoCallsResponses =  { userResponse : UserSearchResponse, repositoriesResponse: RepositoriesSearchResponse ->
                (userResponse.items + repositoriesResponse.items ).sortedBy { it.name }
        }

        private val onGithubItemsSearchFailure = { e: Throwable ->
            Snackbar.make(githubRecycleView, endPointError, Snackbar.LENGTH_LONG).show()
        }

        private val onGithubItemsSearchSuccess = { it: List<GithubSearchItem> ->
            searchResults.clear()
            searchResults.addAll(it)
            if (checkBoxUsersOnly.isChecked)
                setRecycleViewAdapter(searchResults.filter { it is User })
            else
                setRecycleViewAdapter(searchResults)
        }


        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


    inner class CheckUsersOnlyListener : CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (isChecked)
                setRecycleViewAdapter(searchResults.filter { it is User })
            else
                setRecycleViewAdapter(searchResults)

        }

    }

}
