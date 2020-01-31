package com.puzzlebench.cmk.data.repository

import android.content.ContentValues
import android.content.Context
import com.puzzlebench.cmk.data.mapper.repository.CharacterMapperRepository
import com.puzzlebench.cmk.data.mapper.repository.NullableCharacterMapper
import com.puzzlebench.cmk.data.provider.CharactersContentProvider
import com.puzzlebench.cmk.data.repository.source.CharacterDataSource
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.repository.CharacterRepository


class CharacterDataRepository constructor(
        private val context: Context,
        private val dataSource: CharacterDataSource,
        private val mapper: CharacterMapperRepository,
        private val nullableMapper: NullableCharacterMapper
) : CharacterRepository {

    override fun getAll(sortOrder: String): List<Character> {
        return dataSource.getAllCharacters(sortOrder).map { mapper.transform(it) }
    }

    override fun save(characters: List<Character>) {
        characters.forEach {
            val values = ContentValues().apply {
                put(CharactersContentProvider.COLUMN_ID, it.id)
                put(CharactersContentProvider.COLUMN_NAME, it.name)
                put(CharactersContentProvider.COLUMN_DESCRIPTION, it.name)
                put(CharactersContentProvider.COLUMN_THUMBNAIL_PATH, it.thumbnail.path)
                put(CharactersContentProvider.COLUMN_THUMBNAIL_EXTENSION, it.thumbnail.extension)
            }

            val contentResolver = context.contentResolver
            contentResolver.insert(CharactersContentProvider.CONTENT_URI, values)
        }
    }

    override fun findById(id: Int): Character? {
        return nullableMapper.transform(dataSource.findCharacterById(id))
    }

    override fun delete(id: Int): Int = dataSource.deleteCharacter(id)
}
