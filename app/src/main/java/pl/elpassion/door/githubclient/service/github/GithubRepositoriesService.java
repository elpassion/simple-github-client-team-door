package pl.elpassion.door.githubclient.service.github;

import pl.elpassion.door.githubclient.service.github.response.RepositoriesSearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GithubRepositoriesService {

    @GET("search/repositories")
    Observable<RepositoriesSearchResponse> searchForReposByName(@Query("q") String name);

}