package com.puzzlebench.clean_marvel_kotlin.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.puzzlebench.clean_marvel_kotlin.R
import com.puzzlebench.clean_marvel_kotlin.presentation.base.BaseRxDialog
import com.puzzlebench.clean_marvel_kotlin.presentation.mvp.CharacterDetailContract
import com.puzzlebench.clean_marvel_kotlin.presentation.mvp.CharacterDetailPresenter
import com.puzzlebench.clean_marvel_kotlin.presentation.mvp.CharacterDetailView
import com.puzzlebench.cmk.data.service.CharacterServicesImpl
import com.puzzlebench.cmk.domain.service.CharacterServices
import com.puzzlebench.cmk.domain.usecase.GetSingleCharacterUseCase

class CharacterDetailDialog : BaseRxDialog() {

    private val characterService: CharacterServices by lazy {
        CharacterServicesImpl()
    }

    private val presenter: CharacterDetailContract.Presenter by lazy {
        CharacterDetailPresenter(
                CharacterDetailView(this),
                GetSingleCharacterUseCase(characterService),
                subscriptions
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_character_detail, container, false)
    }

    override fun getTheme() = R.style.FullscreenDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterId = arguments?.getInt(ARGUMENT_CHARACTER_ID) ?: 0
        presenter.getCharacterDetail(characterId)
    }

    companion object {
        const val TAG = "CharacterDetailDialog"
        private const val ARGUMENT_CHARACTER_ID = "ARGUMENT_CHARACTER_ID"

        fun newInstance(characterId: Int): CharacterDetailDialog {
            val bundle = Bundle().apply {
                putInt(ARGUMENT_CHARACTER_ID, characterId)
            }

            return CharacterDetailDialog().apply {
                arguments = bundle
            }
        }
    }
}