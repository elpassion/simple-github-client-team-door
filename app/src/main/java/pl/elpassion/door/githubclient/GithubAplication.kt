package pl.elpassion.door.githubclient

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class GithubAplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics());
    }
}