package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import com.puzzlebench.cmk.domain.model.Character

interface CharacterDetailContract {
    interface View {
        fun displayLoader()
        fun hideLoader()
        fun displayCharacterDetail(character: Character)
        fun displayError(error: Throwable)
    }

    interface Presenter {
        fun getCharacterDetail(characterId: Int)
    }
}