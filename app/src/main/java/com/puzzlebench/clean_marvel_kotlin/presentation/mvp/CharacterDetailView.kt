package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import android.view.View
import android.widget.Toast
import com.puzzlebench.clean_marvel_kotlin.presentation.dialog.CharacterDetailDialog
import com.puzzlebench.clean_marvel_kotlin.presentation.extension.getImageByUrl
import com.puzzlebench.cmk.domain.model.Character
import kotlinx.android.synthetic.main.dialog_character_detail.*
import java.lang.ref.WeakReference

class CharacterDetailView(dialog: CharacterDetailDialog) : CharacterDetailContract.View {

    private val dialogRef = WeakReference(dialog)

    override fun displayLoader() {
        setProgressBarVisibility(View.VISIBLE)
    }

    private fun setProgressBarVisibility(visibility: Int) {
        dialogRef.get()?.progress_bar?.visibility = visibility
    }

    override fun hideLoader() {
        setProgressBarVisibility(View.GONE)
    }

    override fun displayCharacterDetail(character: Character) {
        dialogRef.get()?.let { dialog ->
            with(character) {
                dialog.button_delete.visibility = View.VISIBLE
                dialog.image_character_picture.getImageByUrl(thumbnail.url)
                dialog.text_name.text = name
                dialog.text_description.text = description
            }
        }
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(dialogRef.get()?.context, error.message, Toast.LENGTH_SHORT).show()
    }
}