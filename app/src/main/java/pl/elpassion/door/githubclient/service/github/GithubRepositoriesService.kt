package pl.elpassion.door.githubclient.service.github

import pl.elpassion.door.githubclient.service.github.response.RepositoriesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface GithubRepositoriesService {


    @GET("search/repositories")
    fun searchForReposByName(@Query("q") name: String): Observable<RepositoriesSearchResponse>

}