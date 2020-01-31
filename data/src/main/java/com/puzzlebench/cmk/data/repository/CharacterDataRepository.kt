package com.puzzlebench.cmk.data.repository

import com.puzzlebench.cmk.data.mapper.repository.CharacterMapperRepository
import com.puzzlebench.cmk.data.mapper.repository.NullableCharacterMapper
import com.puzzlebench.cmk.data.repository.source.CharacterDataSource
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.repository.CharacterRepository


class CharacterDataRepository constructor(
        private val dataSource: CharacterDataSource,
        private val mapper: CharacterMapperRepository,
        private val nullableMapper: NullableCharacterMapper
) : CharacterRepository {

    override fun getAll(sortOrder: String): List<Character> {
        return dataSource.getAllCharacters(sortOrder).map { mapper.transform(it) }
    }

    override fun save(c: List<Character>) {
        dataSource.saveCharacters(c.map { mapper.transform(it) })
    }

    override fun findById(id: Int): Character? {
        return nullableMapper.transform(dataSource.findCharacterById(id))
    }

    override fun delete(id: Int): Int = dataSource.deleteCharacter(id)
}
