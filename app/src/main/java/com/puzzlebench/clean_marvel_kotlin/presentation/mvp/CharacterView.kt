package com.puzzlebench.clean_marvel_kotlin.presentation.mvp

import android.content.ContentUris
import android.database.Cursor
import android.os.Bundle
import android.util.Log
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
import com.puzzlebench.clean_marvel_kotlin.presentation.provider.CharactersContentProvider
import com.puzzlebench.clean_marvel_kotlin.presentation.provider.CharactersContentProvider.Companion.COLUMN_DESCRIPTION
import com.puzzlebench.clean_marvel_kotlin.presentation.provider.CharactersContentProvider.Companion.COLUMN_ID
import com.puzzlebench.clean_marvel_kotlin.presentation.provider.CharactersContentProvider.Companion.COLUMN_NAME
import com.puzzlebench.clean_marvel_kotlin.presentation.provider.CharactersContentProvider.Companion.COLUMN_THUMBNAIL_EXTENSION
import com.puzzlebench.clean_marvel_kotlin.presentation.provider.CharactersContentProvider.Companion.COLUMN_THUMBNAIL_PATH
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.model.Thumbnail
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class CharacterView(activity: MainActivity) : LoaderManager.LoaderCallbacks<Cursor> {

    private val activityRef = WeakReference(activity)

    private var adapter = CharacterAdapter { character ->
        // displayCharacterDetail(character)
        val params = Bundle().apply {
            putInt(SELECTED_CHARACTER_ARG, character.id)
        }
        LoaderManager.getInstance(activity).restartLoader(LOADER_SINGLE_ID, params, this)
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
            // initializeLoader(it)
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

    fun initializeLoader() {
        runSafely {
            LoaderManager.getInstance(it).initLoader(LOADER_ALL_ID, null, this)
        }
    }

    fun restartLoader() {
        runSafely {
            LoaderManager.getInstance(it).restartLoader(LOADER_ALL_ID, null, this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val characterId = args?.getInt(SELECTED_CHARACTER_ARG) ?: 0
        val uri = if (characterId == 0) {
            CharactersContentProvider.CONTENT_URI
        } else {
            ContentUris.withAppendedId(CharactersContentProvider.CONTENT_URI, characterId.toLong())
        }

        return activityRef.get()?.let {
            CursorLoader(it, uri, null, null, null, COLUMN_NAME)
        } ?: run {
            throw Exception("Invalid activity")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null || data.count == 0) {
            if (loader.id == LOADER_ALL_ID) {
                showCharacters(emptyList())
                showToastNoItemToShow()
            }
            return
        }

        val characters = extractData(data)

        if (loader.id == LOADER_ALL_ID) {
            showCharacters(characters)
        } else {
            characters.firstOrNull()?.let { safeCharacter ->
                displayCharacterDetail(safeCharacter)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) = Unit

    private fun extractData(cursor: Cursor): List<Character> {
        val characters = mutableListOf<Character>()

        if (cursor.moveToFirst()) {
            do {
                with(cursor) {
                    val id = getInt(getColumnIndex(COLUMN_ID))
                    val name = getString(getColumnIndex(COLUMN_NAME))
                    val description = getString(getColumnIndex(COLUMN_DESCRIPTION))
                    val thumbnailPath = getString(getColumnIndex(COLUMN_THUMBNAIL_PATH))
                    val thumbnailExtension = getString(getColumnIndex(COLUMN_THUMBNAIL_EXTENSION))

                    Log.i("Cursro", "Character = $id, $name, $description, $thumbnailPath, $thumbnailExtension")
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
        private const val SELECTED_CHARACTER_ARG = "SELECTED_CHARACTER_ARG"
        private const val LOADER_ALL_ID = 0
        private const val LOADER_SINGLE_ID = 1
    }
}
