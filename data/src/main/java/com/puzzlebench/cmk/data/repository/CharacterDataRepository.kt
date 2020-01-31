package com.puzzlebench.cmk.data.repository

import com.puzzlebench.cmk.data.mapper.repository.CharacterMapperRepository
import com.puzzlebench.cmk.data.repository.source.CharacterDataSource
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.repository.CharacterRepository


class CharacterDataRepository constructor(
        private val dataSource: CharacterDataSource,
        private val mapper: CharacterMapperRepository
) : CharacterRepository {

    override fun getAll(sortOrder: String): List<Character> {
        return dataSource.getAllCharacters(sortOrder).map { mapper.transform(it) }
    }

    override fun save(characters: List<Character>) {
        dataSource.saveCharacters(characters.map { mapper.transform(it) })
    }

    override fun delete(id: Int): Int = dataSource.deleteCharacter(id)
}
