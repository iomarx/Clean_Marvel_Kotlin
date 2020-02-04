package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.puzzlebench.clean_marvel_kotlin.R
import com.puzzlebench.clean_marvel_kotlin.presentation.MainActivity
import com.puzzlebench.clean_marvel_kotlin.presentation.adapter.CharacterAdapter
import com.puzzlebench.clean_marvel_kotlin.presentation.dialog.CharacterDetailDialog
import com.puzzlebench.clean_marvel_kotlin.presentation.extension.showToast
import com.puzzlebench.cmk.data.provider.CharactersContentProvider
import com.puzzlebench.cmk.data.provider.CharactersContentProvider.Companion.COLUMN_DESCRIPTION
import com.puzzlebench.cmk.data.provider.CharactersContentProvider.Companion.COLUMN_ID
import com.puzzlebench.cmk.data.provider.CharactersContentProvider.Companion.COLUMN_NAME
import com.puzzlebench.cmk.data.provider.CharactersContentProvider.Companion.COLUMN_THUMBNAIL_EXTENSION
import com.puzzlebench.cmk.data.provider.CharactersContentProvider.Companion.COLUMN_THUMBNAIL_PATH
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.model.Thumbnail
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class CharacterView(activity: MainActivity) : LoaderManager.LoaderCallbacks<Cursor> {

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
            LoaderManager.getInstance(it).initLoader(LOADER_ALL_ID, null, this)
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

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return activityRef.get()?.let {
            val uri = CharactersContentProvider.CONTENT_URI
            CursorLoader(it, uri, null, null, null, COLUMN_NAME)
        } ?: run {
            throw Exception("Invalid activity")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val characters = extractData(data)
        showCharacters(characters)

        if (characters.isEmpty()) {
            showToastNoItemToShow()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) = Unit

    private fun extractData(cursor: Cursor?): List<Character> {
        val characters = mutableListOf<Character>()

        if (cursor?.moveToFirst() == true) {
            do {
                with(cursor) {
                    val id = getInt(getColumnIndex(COLUMN_ID))
                    val name = getString(getColumnIndex(COLUMN_NAME))
                    val description = getString(getColumnIndex(COLUMN_DESCRIPTION))
                    val thumbnailPath = getString(getColumnIndex(COLUMN_THUMBNAIL_PATH))
                    val thumbnailExtension = getString(getColumnIndex(COLUMN_THUMBNAIL_EXTENSION))

                    characters.add(Character(
                            id,
                            name,
                            description,
                            Thumbnail(thumbnailPath, thumbnailExtension))
                    )
                }
            } while (cursor.moveToNext())
        }

        return characters
    }

    companion object {
        private const val SPAN_COUNT = 1
        private const val LOADER_ALL_ID = 0
    }
}
