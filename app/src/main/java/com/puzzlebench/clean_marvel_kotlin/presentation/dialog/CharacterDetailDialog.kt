package com.puzzlebench.clean_marvel_kotlin.presentation.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.puzzlebench.clean_marvel_kotlin.R

class CharacterDetailDialog : DialogFragment() {

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

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.dialog_character_detail, container, false)
    }

    override fun getTheme() = R.style.FullscreenDialogTheme

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val characterId = arguments.getInt(ARGUMENT_CHARACTER_ID)
    }
}