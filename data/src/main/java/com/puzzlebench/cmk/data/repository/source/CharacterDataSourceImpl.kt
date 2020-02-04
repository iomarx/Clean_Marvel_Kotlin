package com.puzzlebench.cmk.data.repository.source

import com.puzzlebench.cmk.data.model.CharacterRealm
import io.realm.Realm


class CharacterDataSourceImpl : DataSource<CharacterRealm>(CharacterRealm::class.java), CharacterDataSource {

    override fun saveCharacters(c: List<CharacterRealm>) {
        this.save(c)
    }

    override fun getAllCharacters(sortOrder: String): List<CharacterRealm> {
        var allCharacters: List<CharacterRealm> = listOf()
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(CharacterRealm::class.java).findAll().sort(sortOrder)
            result.let {
                allCharacters = realm.copyFromRealm(it)
            }
        }
        return allCharacters
    }

    override fun findCharacterById(id: Int): CharacterRealm? = findById(id)

    override fun deleteCharacter(id: Int): Int = delete(id)
}