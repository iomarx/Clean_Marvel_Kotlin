package com.puzzlebench.clean_marvel_kotlin.presentation.base

import android.support.v4.app.DialogFragment
import io.reactivex.disposables.CompositeDisposable

open class BaseRxDialog : DialogFragment() {

    protected var subscriptions = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }
}