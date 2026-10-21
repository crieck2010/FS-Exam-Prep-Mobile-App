package com.example

import android.app.Application
import com.example.data.QuestionBank

class FsPrepApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        QuestionBank.initialize(this)
    }

    companion object {
        var instance: FsPrepApplication? = null
            private set
    }
}
