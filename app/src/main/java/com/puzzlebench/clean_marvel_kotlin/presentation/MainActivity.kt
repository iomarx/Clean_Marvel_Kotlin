package com.puzzlebench.clean_marvel_kotlin.presentation

import android.os.Bundle
import com.puzzlebench.clean_marvel_kotlin.R
import com.puzzlebench.clean_marvel_kotlin.presentation.base.BaseRxActivity
import com.puzzlebench.clean_marvel_kotlin.presentation.mvp.CharacterPresenter
import com.puzzlebench.clean_marvel_kotlin.presentation.mvp.CharacterView
import com.puzzlebench.cmk.data.mapper.repository.CharacterMapperRepository
import com.puzzlebench.cmk.data.mapper.repository.NullableCharacterMapper
import com.puzzlebench.cmk.data.mapper.repository.ThumbnailTransform
import com.puzzlebench.cmk.data.repository.CharacterDataRepository
import com.puzzlebench.cmk.data.repository.source.CharacterDataSourceImpl
import com.puzzlebench.cmk.data.service.CharacterServicesImpl
import com.puzzlebench.cmk.domain.usecase.GetCharacterServiceUseCase
import com.puzzlebench.cmk.domain.usecase.SaveCharacterRepositoryUseCase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseRxActivity() {

    private val characterService = CharacterServicesImpl()
    private val characterRepository = CharacterDataRepository(
            this,
            CharacterDataSourceImpl(),
            CharacterMapperRepository(),
            NullableCharacterMapper(ThumbnailTransform())
    )

    private val getCharacterServiceUseCase = GetCharacterServiceUseCase(characterService)
    private val saveCharacterRepositoryUseCase = SaveCharacterRepositoryUseCase(characterRepository)

    private val presenter = CharacterPresenter(CharacterView(this),
            getCharacterServiceUseCase,
            saveCharacterRepositoryUseCase,
            subscriptions
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.init()
        fab_refresh.setOnClickListener {
            presenter.requestCharacters()
        }
    }
}
