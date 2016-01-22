package pl.elpassion.door.githubclient

class UserSearchResponse(val items: List<User>)
class User(override val name: String, val avatarUrl: String, val reposUrl: String) : GithubSearchItem
