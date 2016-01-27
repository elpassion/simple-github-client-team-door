package pl.elpassion.door.githubclient

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.test.ActivityInstrumentationTestCase2
import pl.elpassion.door.githubclient.activity.GithubSearchViewActivity
import pl.elpassion.door.githubclient.service.github.GithubService
import pl.elpassion.door.githubclient.service.github.GithubUsersService
import pl.elpassion.door.githubclient.service.github.response.UserSearchResponse
import rx.Observable


class ApplicationTest : ActivityInstrumentationTestCase2<GithubSearchViewActivity>(GithubSearchViewActivity::class.java) {
    override fun setUp() {
        super.setUp()
        GithubService.githubUsersService = object : GithubUsersService {
            override fun searchForUsersByName(name: String): Observable<UserSearchResponse> {
                return Observable.error(Throwable())
            }
        }
        activity
    }

    fun testIfSnackBarShowsWhenNoInternetConnection() {
        onView(withId(R.id.search_name)).perform(typeText("tom"))
        onView(withText("Problem z pobraniem danych z Githuba")).check(matches(isDisplayed()))
    }
}