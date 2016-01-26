package pl.elpassion.door.githubclient.adapter

import pl.elpassion.door.githubclient.service.github.response.GithubSearchItem
import pl.elpassion.door.githubclient.service.github.response.Repository
import pl.elpassion.door.githubclient.service.github.response.User


class GithubSearchResultListAdapter(val searchResults: List<GithubSearchItem>) : BaseAdapter() {

    init {
        searchResults.sortedBy { it.name }.forEach {
            if (it is User)
                adapters.add(UserItemAdapter(it))
            else if (it is Repository)
                adapters.add(RepositoryItemAdapter(it))
        }
    }
}