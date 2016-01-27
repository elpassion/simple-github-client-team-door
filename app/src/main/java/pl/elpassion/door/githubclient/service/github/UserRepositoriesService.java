package pl.elpassion.door.githubclient.service.github;

import java.util.List;

import pl.elpassion.door.githubclient.service.github.response.Repository;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface UserRepositoriesService {

    @GET("users/{user}/repos")
    Observable<List<Repository>> getUserRepositories(@Path("user") String userName);

}