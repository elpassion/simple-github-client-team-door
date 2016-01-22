package pl.elpassion.door.githubclient

class UserSearchResponse(val items: List<User>)
class User(val login: String, val avatarUrl: String, val reposUrl: String)
