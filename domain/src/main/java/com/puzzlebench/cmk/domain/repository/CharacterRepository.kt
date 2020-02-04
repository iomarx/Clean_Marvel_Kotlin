package com.puzzlebench.cmk.domain.repository

import com.puzzlebench.cmk.domain.model.Character

interface CharacterRepository {
    fun getAll(sortOrder: String = ""): List<Character>

    fun save(characters: List<Character>)

    fun delete(id: Int): Int
}