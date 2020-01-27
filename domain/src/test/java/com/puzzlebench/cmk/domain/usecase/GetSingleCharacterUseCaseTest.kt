package com.puzzlebench.cmk.domain.usecase

import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.model.Thumbnail
import com.puzzlebench.cmk.domain.service.CharacterServices
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class GetSingleCharacterUseCaseTest {

    @Mock
    private lateinit var characterServicesMock: CharacterServices

    private lateinit var singleCharacterUseCase: GetSingleCharacterUseCase

    @Before
    fun setup() {
        singleCharacterUseCase = GetSingleCharacterUseCase(characterServicesMock)
    }

    @Test
    fun invokeSuccess() {
        val character = Character(
                1,
                "Batman",
                "The Dark Knight",
                Thumbnail("image", ".png")
        )
        givenCharacterDetailObservableResult(Single.just(listOf(character)))

        singleCharacterUseCase.invoke(anyInt()).test()
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it == character
                }
        verifyInteractionsWithServicesMock()
    }

    private fun givenCharacterDetailObservableResult(expectedResult: Single<Any>) {
        `when`(characterServicesMock.getCharacterDetail(anyInt())).thenAnswer {
            expectedResult
        }
    }

    private fun verifyInteractionsWithServicesMock() {
        verify(characterServicesMock).getCharacterDetail(anyInt())
        verifyNoMoreInteractions(characterServicesMock)
    }

    @Test
    fun invokeWithEmptyList() {
        givenCharacterDetailObservableResult(Single.just(emptyList<Character>()))

        singleCharacterUseCase.invoke(anyInt()).test()
                .assertNotComplete()
                .assertError {
                    it.message == "No character with the given Id was found"
                }
        verifyInteractionsWithServicesMock()
    }

    @Test
    fun invokeFailureWithErrorFromService() {
        val serviceException = Exception("Service exception")
        givenCharacterDetailObservableResult(Single.error(serviceException))

        singleCharacterUseCase.invoke(anyInt()).test()
                .assertNotComplete()
                .assertError {
                    it == serviceException
                }
        verifyInteractionsWithServicesMock()
    }
}