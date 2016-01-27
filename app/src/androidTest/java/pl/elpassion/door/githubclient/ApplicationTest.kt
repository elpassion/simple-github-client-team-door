package pl.elpassion.door.githubclient

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.test.ActivityInstrumentationTestCase2
import pl.elpassion.door.githubclient.activity.GithubSearchViewActivity
import pl.elpassion.door.githubclient.service.github.GithubService
import pl.elpassion.door.githubclient.service.github.GithubUsersService
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


    fun testIfSnackBarShowsWhenNoGithubConnection() {

        GithubService.githubUsersService = object : GithubUsersService {
            override fun searchForUsersByName(name: String): Observable<UserSearchResponse> {
                return Observable.error(Throwable())
            }
        }

        typeTextToSearch("tom")
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()))
    }

}