package pl.elpassion.door.githubclient

import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


interface UserRepositoriesService {

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") userName: String): Observable<List<Repository>>
}