package pl.elpassion.door.githubclient.adapter

import pl.elpassion.door.githubclient.GithubSearchItem
import pl.elpassion.door.githubclient.Repository

class UserRepositoriesAdapter (val userRepositories: List<GithubSearchItem> ) : BaseAdapter(){

    init{
        userRepositories.forEach {
           adapters.add(RepositoryItemAdapter(it as Repository))
        }
    }
}