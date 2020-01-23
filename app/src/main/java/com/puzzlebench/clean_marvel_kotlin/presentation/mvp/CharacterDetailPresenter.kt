package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import com.puzzlebench.cmk.domain.usecase.GetSingleCharacterUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDetailPresenter(
        private val view: CharacterDetailContract.View,
        private val getSingleCharacterUseCase: GetSingleCharacterUseCase,
        private val subscriptions: CompositeDisposable
) : CharacterDetailContract.Presenter {

    override fun getCharacterDetail(characterId: Int) {
        subscriptions.add(getSingleCharacterUseCase.invoke(characterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.displayLoader()
                }
                .doAfterTerminate {
                    view.hideLoader()
                }
                .subscribe({
                    view.displayCharacterDetail(it)
                }, {
                    view.displayError(it)
                })
        )
    }
}