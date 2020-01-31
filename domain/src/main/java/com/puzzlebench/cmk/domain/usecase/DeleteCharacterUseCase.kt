package com.puzzlebench.cmk.domain.usecase

import com.puzzlebench.cmk.domain.repository.CharacterRepository

class DeleteCharacterUseCase(private val characterDataRepository: CharacterRepository) {

    operator fun invoke(characterId: Int) = characterDataRepository.delete(characterId)
}