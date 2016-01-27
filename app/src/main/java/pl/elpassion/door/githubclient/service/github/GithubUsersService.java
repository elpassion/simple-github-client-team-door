package pl.elpassion.door.githubclient.service.github;

import pl.elpassion.door.githubclient.service.github.response.UserSearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface GithubUsersService {

    @GET("search/users")
    Observable<UserSearchResponse> searchForUsersByName(@Query("q") String name);

}