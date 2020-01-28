package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.puzzlebench.clean_marvel_kotlin.R
import com.puzzlebench.clean_marvel_kotlin.presentation.MainActivity
import com.puzzlebench.clean_marvel_kotlin.presentation.adapter.CharacterAdapter
import com.puzzlebench.clean_marvel_kotlin.presentation.dialog.CharacterDetailDialog
import com.puzzlebench.clean_marvel_kotlin.presentation.extension.showToast
import com.puzzlebench.cmk.domain.model.Character
import kotlinx.android.synthetic.main.activity_main.recycleView
import kotlinx.android.synthetic.main.activity_main.progressBar
import java.lang.ref.WeakReference

class CharacterView(activity: MainActivity) {
    private val activityRef = WeakReference(activity)

    private var adapter = CharacterAdapter { character ->
        displayCharacterDetail(character)
    }

    private fun displayCharacterDetail(character: Character) {
        val detailDialog = CharacterDetailDialog.newInstance(character.id)
        val fragmentManager = activityRef.get()?.supportFragmentManager

        detailDialog.show(fragmentManager, CharacterDetailDialog.TAG)
    }

    fun init() {
        runSafely {
            it.recycleView.layoutManager = GridLayoutManager(it, SPAN_COUNT)
            it.recycleView.adapter = adapter
            hideLoading()
        }
    }

    private fun runSafely(action: (MainActivity) -> Unit) {
        activityRef.get()?.let(action)
    }

    fun showToastNoItemToShow() {
        runSafely {
            val message = it.baseContext.resources.getString(R.string.message_no_items_to_show)
            it.applicationContext.showToast(message)
        }
    }

    fun showToastNetworkError(error: String) {
        runSafely {
            it.applicationContext.showToast(error)
        }
    }

    fun hideLoading() {
        runSafely {
            it.progressBar.visibility = View.GONE
        }
    }

    fun showCharacters(characters: List<Character>) {
        adapter.data = characters
    }

    fun showLoading() {
        runSafely {
            it.progressBar.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val SPAN_COUNT = 1
    }
}
