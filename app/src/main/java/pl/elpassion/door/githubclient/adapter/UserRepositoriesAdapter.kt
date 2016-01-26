package pl.elpassion.door.githubclient.adapter

import pl.elpassion.door.githubclient.service.github.response.GithubSearchItem
import pl.elpassion.door.githubclient.service.github.response.Repository

class UserRepositoriesAdapter(val userRepositories: List<GithubSearchItem>) : BaseAdapter() {

    init {
        userRepositories.forEach {
            adapters.add(RepositoryItemAdapter(it as Repository))
        }
    }
}