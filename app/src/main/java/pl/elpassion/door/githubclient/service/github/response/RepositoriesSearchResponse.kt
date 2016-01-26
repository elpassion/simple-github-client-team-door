package pl.elpassion.door.githubclient.service.github.response

class RepositoriesSearchResponse(val items: List<Repository>)
class Repository(override val name: String) : GithubSearchItem
