package pl.elpassion.door.githubclient

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


interface GithubService {

    @GET("search/users")
    fun searchForUsersByName(@Query("q") name :String) :Observable<UserSearchResponse>

    @GET("search/repositories")
    fun searchForReposByName(@Query("q") name :String) :Observable<RepositoriesSearchResponse>
}