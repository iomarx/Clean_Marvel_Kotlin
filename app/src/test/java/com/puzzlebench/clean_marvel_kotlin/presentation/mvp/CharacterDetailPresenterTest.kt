package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.usecase.GetSingleCharacterUseCase
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterDetailPresenterTest {

    private lateinit var characterDetailPresenter: CharacterDetailContract.Presenter

    @Mock
    private lateinit var viewMock: CharacterDetailContract.View

    @Mock
    private lateinit var useCaseMock: GetSingleCharacterUseCase

    @Mock
    private lateinit var subscriptionsMock: CompositeDisposable

    @Mock
    private lateinit var characterMock: Character

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }
        characterDetailPresenter = CharacterDetailPresenter(
                viewMock, useCaseMock, subscriptionsMock
        )
    }

    @Test
    fun getCharacterDetailSuccess() {
        givenUseCaseResult(Single.just(characterMock))

        characterDetailPresenter.getCharacterDetail(anyInt())

        verifyLoaderInteractions()
        verify(viewMock).displayCharacterDetail(characterMock)
        verifyNoMoreInteractions(viewMock)
    }

    private fun verifyLoaderInteractions() {
        verify(viewMock).displayLoader()
        verify(viewMock).hideLoader()
    }

    private fun givenUseCaseResult(result: Single<Any>) {
        `when`(useCaseMock.invoke(anyInt())).thenAnswer { result }
    }

    @Test
    fun getCharacterDetailError() {
        val expectedException = Exception("Service exception")
        givenUseCaseResult(Single.error(expectedException))

        characterDetailPresenter.getCharacterDetail(anyInt())

        verifyLoaderInteractions()
        verify(viewMock).displayError(expectedException)
        verifyNoMoreInteractions(viewMock)
    }
}