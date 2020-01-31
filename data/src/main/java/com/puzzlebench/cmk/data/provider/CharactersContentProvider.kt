package com.puzzlebench.cmk.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.puzzlebench.cmk.data.mapper.repository.CharacterMapperRepository
import com.puzzlebench.cmk.data.mapper.repository.NullableCharacterMapper
import com.puzzlebench.cmk.data.mapper.repository.ThumbnailTransform
import com.puzzlebench.cmk.data.repository.CharacterContentProviderRepository
import com.puzzlebench.cmk.data.repository.source.CharacterDataSourceImpl
import com.puzzlebench.cmk.domain.model.Character
import com.puzzlebench.cmk.domain.model.Thumbnail
import com.puzzlebench.cmk.domain.repository.CharacterRepository

class CharactersContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, TABLE, ALL_CHARACTERS)
        addURI(AUTHORITY, "$TABLE/#", SINGLE_CHARACTER)
    }

    private val columns by lazy {
        arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_THUMBNAIL_PATH,
                COLUMN_THUMBNAIL_EXTENSION
        )
    }

    private val repository: CharacterRepository by lazy {
        CharacterContentProviderRepository(
                CharacterMapperRepository(),
                CharacterDataSourceImpl(),
                NullableCharacterMapper(ThumbnailTransform())
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != ALL_CHARACTERS) return null

        var resultUri: Uri? = null

        values?.let {
            val character = Character(
                    it.getAsInteger(COLUMN_ID),
                    it.getAsString(COLUMN_NAME),
                    it.getAsString(COLUMN_DESCRIPTION),
                    Thumbnail(
                            it.getAsString(COLUMN_THUMBNAIL_PATH),
                            it.getAsString(COLUMN_THUMBNAIL_EXTENSION)
                    )
            )

            repository.save(listOf(character))
            resultUri = ContentUris.withAppendedId(CONTENT_URI, character.id.toLong())
            context?.contentResolver?.notifyChange(uri, null)
        }
        Log.i("ContentProvider", "Saved value with uri = $resultUri")

        return resultUri
    }

    override fun query(
            uri: Uri,
            projection: Array<String>?,
            selection: String?,
            selectionArgs: Array<String>?,
            sortOrder: String?
    ): Cursor? {
        val matrixCursor = MatrixCursor(columns)

        if (uriMatcher.match(uri) == SINGLE_CHARACTER) {
            val id = uri.lastPathSegment?.toInt() ?: 0
            val result = repository.findById(id)
            result?.let {
                matrixCursor.addRow(characterToArray(it))
            }
        } else {
            val characters = repository.getAll(sortOrder ?: "")
            characters.forEach {
                matrixCursor.addRow(characterToArray(it))
            }
        }

        matrixCursor.setNotificationUri(context?.contentResolver, uri)

        return matrixCursor
    }

    private fun characterToArray(it: Character) = arrayOf(
            it.id, it.name, it.description, it.thumbnail.path, it.thumbnail.extension
    )

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<String>?
    ): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (uriMatcher.match(uri) != SINGLE_CHARACTER) return 0

        val id = uri.lastPathSegment?.toInt() ?: 0
        val deleteResult = repository.delete(id)
        if (deleteResult > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return deleteResult
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            ALL_CHARACTERS -> "vnd.android.cursor.dir/$AUTHORITY"
            SINGLE_CHARACTER -> "vnd.android.cursor.item/$AUTHORITY"
            else -> null
        }
    }

    companion object {
        private const val AUTHORITY = "com.puzzlebench.clean_marvel_kotlin"
        private const val TABLE = "Characters"
        private const val URI = "content://$AUTHORITY/$TABLE"
        val CONTENT_URI = Uri.parse(URI)
        private const val ALL_CHARACTERS = 1
        private const val SINGLE_CHARACTER = 2

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_THUMBNAIL_PATH = "path"
        const val COLUMN_THUMBNAIL_EXTENSION = "extension"
    }
}