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

    private fun typeTextToSearch(text : String) {
        onView(withId(R.id.search_name)).perform(typeText(text))
    }

    private fun clickOnCheckBox() {
        onView(withId(R.id.check_users_only)).perform(click())
    }


    fun testIfSnackBarShowsWhenNoGithubConnection() {

        GithubService.githubUsersService = object : GithubUsersService {
            override fun searchForUsersByName(name: String): Observable<UserSearchResponse> {
                return Observable.error(Throwable())
            }
        }

        GithubService.githubRepositoriesService = object : GithubRepositoriesService {
            override fun searchForReposByName(name: String): Observable<RepositoriesSearchResponse> {
                return Observable.error(Throwable())
            }
        }

        typeTextToSearch("t")
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withText("Problem z pobraniem danych z Githuba")))
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

        GithubService.githubUsersService = object : GithubUsersService {
            override fun searchForUsersByName(name: String): Observable<UserSearchResponse> {
                return Observable.just(UserSearchResponse(listOf(User("TestName", "TestAvatar", "TestUrl"))))
            }
        }

        GithubService.githubRepositoriesService = object : GithubRepositoriesService {
            override fun searchForReposByName(name: String): Observable<RepositoriesSearchResponse> {
                return Observable.just(RepositoriesSearchResponse(listOf(Repository("TestRepoName"))))
            }
        }

        typeTextToSearch("tom")
        onView(withId(R.id.search_results_list)).check(matches(hasDescendant(isDisplayed())))
    }



}