package com.puzzlebench.cmk.data.service

import com.puzzlebench.cmk.data.mapper.service.CharacterMapperService
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.service.CharacterServices
import io.reactivex.Single


class CharacterServicesImpl(
        private val api: MarvelRequestGenerator = MarvelRequestGenerator(),
        private val mapper: CharacterMapperService = CharacterMapperService()
) : CharacterServices {

    override fun getCharacters(): Single<List<Character>> {
        return api.makeMarvelService().getCharacter().map { response ->
            response.data?.result?.map { characterResponse ->
                mapper.transform(characterResponse)
            }
        }
    }

    override fun getCharacterDetail(characterId: Int): Single<List<Character>> {
        return api.makeMarvelService().getCharacterDetail(characterId).map { response ->
            response.data?.result?.let {
                mapper.transform(it)
            }
        }
    }
}