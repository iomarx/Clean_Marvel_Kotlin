package com.puzzlebench.cmk.domain.usecase

import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.service.CharacterServices
import io.reactivex.Single

class GetSingleCharacterUseCase(private val characterService: CharacterServices) {

    operator fun invoke(characterId: Int): Single<Character> =
            characterService.getCharacterDetail(characterId).map {
                if (it.isEmpty()) throw Exception(NOT_FOUND_ITEM)

                it[0]
            }

    companion object {
        private const val NOT_FOUND_ITEM = "No character with the given Id was found"
    }
}