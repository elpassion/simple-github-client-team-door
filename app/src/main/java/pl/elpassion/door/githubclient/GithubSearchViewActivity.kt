package pl.elpassion.door.githubclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.widget.EditText
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pl.elpassion.door.githubclient.adapter.GithubSearchResultListAdapter
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

class GithubSearchViewActivity : AppCompatActivity() {

    companion object {
        val baseUrl = "https://api.github.com"
        val gson : Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    val githubUsersService by lazy { retrofit.create(GithubUsersService::class.java) }
    val githubRepositoriesService by lazy { retrofit.create(GithubRepositoriesService::class.java) }
    val searchName : EditText by lazy { findViewById(R.id.search_name) as EditText}
    val toolbar : Toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    val githubRecycleView : RecyclerView by lazy { findViewById(R.id.search_results_list) as RecyclerView}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_search_view)
        setSupportActionBar(toolbar)
        githubRecycleView.layoutManager = LinearLayoutManager(this)
        searchName.addTextChangedListener(SearchNameChangedListener())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_github_search_view, menu)
        return true
    }

    inner class SearchNameChangedListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val searchName = searchName.text.toString()
            githubUsersService.searchForUsersByName(searchName)
                    .zipWith(githubRepositoriesService.searchForReposByName(searchName), { userResponse, repositoriesResponse ->
                        joinTwoCallsResponses(userResponse, repositoriesResponse)})
                    .applySchedulers()
                    .subscribe ({ setRecycleVieAdapter(it) }, {})
        }

        private fun joinTwoCallsResponses(userResponse: UserSearchResponse, repositoriesResponse: RepositoriesSearchResponse) : List<GithubSearchItem> {
            return ( userResponse.items + repositoriesResponse.items ).sortedBy { it.name }
        }

        private fun setRecycleVieAdapter(githubSearchItems: List<GithubSearchItem>) {
            githubRecycleView.adapter = GithubSearchResultListAdapter(githubSearchItems)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}
