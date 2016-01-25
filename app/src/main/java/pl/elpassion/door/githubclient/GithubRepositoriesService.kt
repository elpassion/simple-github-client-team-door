package pl.elpassion.door.githubclient

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface GithubRepositoriesService {


    @GET("search/repositories")
    fun searchForReposByName(@Query("q") name :String) : Observable<RepositoriesSearchResponse>

}