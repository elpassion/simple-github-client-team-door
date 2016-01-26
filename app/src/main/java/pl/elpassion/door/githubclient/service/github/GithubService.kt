package pl.elpassion.door.githubclient.service.github

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory

object GithubService {

    private val baseUrl = "https://api.github.com"
    private val gson: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    val githubRepositoriesService by lazy { retrofit.create(GithubRepositoriesService::class.java) }
    val githubUsersService by lazy { retrofit.create(GithubUsersService::class.java) }
    val userRepositoriesService by lazy { retrofit.create(UserRepositoriesService::class.java) }

}