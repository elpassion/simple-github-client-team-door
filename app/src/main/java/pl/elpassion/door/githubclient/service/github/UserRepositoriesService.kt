package pl.elpassion.door.githubclient.service.github

import pl.elpassion.door.githubclient.service.github.response.Repository
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


interface UserRepositoriesService {

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") userName: String): Observable<List<Repository>>
}