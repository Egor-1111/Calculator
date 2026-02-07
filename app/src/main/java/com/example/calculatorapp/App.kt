package com.example.calculatorapp

import android.app.Activity
import android.app.Application
import android.os.Bundle

class App : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        var isLocked = true
        var startedActivities = 0
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityStarted(activity: Activity) {
        startedActivities++

        if (startedActivities == 1 && isLocked && activity !is AuthActivity) {
            activity.startActivity(
                AuthActivity.newIntent(activity)
            )
        }
    }

    override fun onActivityStopped(activity: Activity) {
        startedActivities--

        if (startedActivities == 0) {
            isLocked = true
        }
    }

    // Остальное не используем
    override fun onActivityCreated(a: Activity, b: Bundle?) {}
    override fun onActivityResumed(a: Activity) {}
    override fun onActivityPaused(a: Activity) {}
    override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
    override fun onActivityDestroyed(a: Activity) {}
}
