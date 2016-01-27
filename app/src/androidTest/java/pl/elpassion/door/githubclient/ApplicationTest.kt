package pl.elpassion.door.githubclient

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.test.ActivityInstrumentationTestCase2
import pl.elpassion.door.githubclient.activity.GithubSearchViewActivity
import pl.elpassion.door.githubclient.service.github.GithubRepositoriesService
import pl.elpassion.door.githubclient.service.github.GithubService
import pl.elpassion.door.githubclient.service.github.GithubUsersService
import pl.elpassion.door.githubclient.service.github.response.RepositoriesSearchResponse
import pl.elpassion.door.githubclient.service.github.response.Repository
import pl.elpassion.door.githubclient.service.github.response.User
import pl.elpassion.door.githubclient.service.github.response.UserSearchResponse
import rx.Observable


class ApplicationTest : ActivityInstrumentationTestCase2<GithubSearchViewActivity>(GithubSearchViewActivity::class.java) {

    override fun setUp() {
        super.setUp()
        activity
    }

    fun testIfSnackBarShowsWhenNoGithubConnection() {
        setUserServiceToFail()
        setRepositoriesServiceToFail()
        val errorMessage = "Problem z pobraniem danych z Githuba"
        typeTextToSearch("t")
        checkIfSnackbarIsVisibleWithError(errorMessage)
    }

    fun testAfterTypedSomeTextInSearchFieldTextIsAppeared() {
        typeTextToSearch("tom")
        onView(withId(R.id.search_name)).check(matches(withText("tom")))
    }

    fun testIfCheckBoxIsCheckedAfterClickOnIt() {
        clickOnCheckBox()
        onView(withId(R.id.check_users_only)).check(matches(isChecked()))
    }

    fun testIfTextIsTypedSomeResultsAreFound() {
        setUserServiceToReturnExampleUser()
        setRepositoriesServiceToReturnExampleRepository()
        typeTextToSearch("tom")
        checkIfSearchResultListIsNotEmpty()
    }

    private fun checkIfSnackbarIsVisibleWithError(errorMessage: String) {
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withText(errorMessage)))
    }

    private fun checkIfSearchResultListIsNotEmpty() {
        onView(withId(R.id.search_results_list)).check(matches(hasDescendant(isDisplayed())))
    }

    private fun typeTextToSearch(text: String) {
        onView(withId(R.id.search_name)).perform(typeText(text))
    }

    private fun clickOnCheckBox() {
        onView(withId(R.id.check_users_only)).perform(click())
    }

    private fun setRepositoriesServiceToReturnExampleRepository() {
        val repository = Repository("TestRepoName")
        val listOfRepositories = listOf(repository)
        setRepositoriesServiceToReturnRepositories(listOfRepositories)
    }

    private fun setUserServiceToReturnExampleUser() {
        val user = User("TestName", "TestAvatar", "TestUrl")
        val listOfUsers = listOf(user)
        setUserServiceToReturnUsers(listOfUsers)
    }

    private fun setUserServiceToReturnUsers(listOfUsers: List<User>) {
        val userSearchResponse = UserSearchResponse(listOfUsers)
        val observable = Observable.just(userSearchResponse)
        setUserServiceToReturn(observable)
    }

    private fun setRepositoriesServiceToReturnRepositories(listOfRepositories: List<Repository>) {
        val repositoriesSearchResponse = RepositoriesSearchResponse(listOfRepositories)
        val observable = Observable.just(repositoriesSearchResponse)
        setRepositoriesServiceToReturn(observable)
    }

    private fun setRepositoriesServiceToFail() {
        val observable = Observable.error<RepositoriesSearchResponse>(Throwable())
        setRepositoriesServiceToReturn(observable)
    }

    private fun setUserServiceToFail() {
        val observable = Observable.error<UserSearchResponse>(Throwable())
        setUserServiceToReturn(observable)
    }

    private fun setUserServiceToReturn(observable: Observable<UserSearchResponse>) {
        GithubService.githubUsersService = GithubUsersService { name: String ->
            observable
        }
    }

    private fun setRepositoriesServiceToReturn(observable: Observable<RepositoriesSearchResponse>) {
        GithubService.githubRepositoriesService = GithubRepositoriesService { name: String ->
            observable
        }
    }

}
