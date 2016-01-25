package pl.elpassion.door.githubclient

import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


interface GithubUserReposService {

    @GET("users/{user}/repos")
    fun searchUserReposByUserName(@Path("user") userName :String) : Observable<RepositoriesSearchResponse>
}