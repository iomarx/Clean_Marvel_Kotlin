package com.puzzlebench.cmk.domain.usecase

import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.service.CharacterServices
import io.reactivex.Single

class GetSingleCharacterUseCase(private val characterService: CharacterServices) {

    operator fun invoke(characterId: Int): Single<Character?> =
            characterService.getCharacterDetail(characterId)
}