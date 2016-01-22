package pl.elpassion.dmalantowicz.rest_client_example.adapter

import pl.elpassion.door.githubclient.Repository
import pl.elpassion.door.githubclient.User


class GithubSearchResultListAdapter(val places : List<Any> ) : BaseAdapter(){

    init{
        places.forEach {
            if (it is User)
                adapters.add(UserItemAdapter( it ))
            else if (it is Repository)
                throw RuntimeException()
        }
    }
}