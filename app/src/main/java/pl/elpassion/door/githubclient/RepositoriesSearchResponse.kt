package pl.elpassion.door.githubclient

class RepositoriesSearchResponse(val items: List<Repository>)
class Repository(override val name: String) : GithubSearchItem
