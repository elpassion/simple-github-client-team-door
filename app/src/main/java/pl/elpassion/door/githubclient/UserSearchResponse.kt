package pl.elpassion.door.githubclient

import com.google.gson.annotations.SerializedName

class UserSearchResponse(val items: List<User>)
class User(@SerializedName(value="login") override val name: String, val avatarUrl: String, val reposUrl: String) : GithubSearchItem
