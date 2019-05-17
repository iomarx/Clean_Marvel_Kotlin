package com.puzzlebench.cmk.data.repository.source

import com.puzzlebench.cmk.data.model.CharacterRealm

interface ICharacterDataSource {
    fun getAllCharacters(): List<CharacterRealm>
    fun saveCharacters(c: List<CharacterRealm>)
}