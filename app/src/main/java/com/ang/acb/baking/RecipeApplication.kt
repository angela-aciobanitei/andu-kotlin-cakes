package com.ang.acb.baking

import android.app.Activity
import android.app.Application
import android.app.Service
import com.ang.acb.baking.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * When using Dagger for injecting Activities or Services, you need to create a
 * custom Application that implements HasAndroidInjector and HasServiceInjector,
 * respectively. See: https://dagger.dev/android.html.
 */
class RecipeApplication: Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun serviceInjector() = dispatchingServiceInjector

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)
    }
}