package pl.elpassion.door.githubclient.adapter

import pl.elpassion.door.githubclient.GithubSearchItem
import pl.elpassion.door.githubclient.Repository
import pl.elpassion.door.githubclient.User


class GithubSearchResultListAdapter(val places : List<GithubSearchItem> ) : BaseAdapter(){

    init{
        places.sortedBy { it.name }.forEach {
            if (it is User)
                adapters.add(UserItemAdapter(it))
            else if (it is Repository)
                adapters.add(RepositoryItemAdapter(it))
        }
    }
}