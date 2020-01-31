package com.puzzlebench.cmk.data.mapper.repository

import com.puzzlebench.cmk.data.model.CharacterRealm
import com.puzzlebench.cmk.domain.model.Character

class NullableCharacterMapper(
        private val thumbnailTransform: ThumbnailTransform
) : BaseMapperRepository<Character?, CharacterRealm?> {

    override fun transform(input: Character?): CharacterRealm? {
        if (input == null) return null

        return CharacterRealm(
                input.id,
                input.name,
                input.description,
                thumbnailTransform.transform(input.thumbnail))
    }

    override fun transform(input: CharacterRealm?): Character? {
        if (input == null) return null

        return Character(
                input._id,
                input.name ?: "",
                input.description ?: "",
                thumbnailTransform.transform(input.thumbnail)
        )
    }
}