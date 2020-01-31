package com.puzzlebench.cmk.data.repository.source

import com.puzzlebench.cmk.data.model.CharacterRealm

interface CharacterDataSource {
    fun getAllCharacters(sortOrder: String = ""): List<CharacterRealm>
    fun saveCharacters(c: List<CharacterRealm>)
    fun findCharacterById(id: Int): CharacterRealm?
    fun deleteCharacter(id: Int): Int
}