package com.puzzlebench.clean_marvel_kotlin.presentation.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.puzzlebench.cmk.data.model.CharacterRealm
import io.realm.Realm

class CharactersContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "CharacterRealm", ALL_CHARACTERS)
        addURI(AUTHORITY, "CharacterRealm/#", SINGLE_CHARACTER)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val columns by lazy {
        arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION)
    }

    override fun query(
            uri: Uri,
            projection: Array<String>?,
            selection: String?,
            selectionArgs: Array<String>?,
            sortOrder: String?
    ): Cursor? {
        val matrixCursor = MatrixCursor(columns)

        Realm.getDefaultInstance().use { realm ->
            val baseQuery = realm.where(CharacterRealm::class.java)

            if (uriMatcher.match(uri) == SINGLE_CHARACTER) {
                val id = uri.lastPathSegment?.toInt() ?: 0
                val result = baseQuery.equalTo(COLUMN_ID, id).findFirst()
                result?.let {
                    matrixCursor.addRow(characterToArray(it))
                }
            } else {
                val realmResults = baseQuery.findAll()

                realmResults.forEach {
                    matrixCursor.addRow(characterToArray(it))
                }
            }
        }

        return matrixCursor
    }

    private fun characterToArray(it: CharacterRealm) = arrayOf(it._id, it.name, it.description)

    override fun onCreate(): Boolean {
        context?.let {
            Realm.init(it)
        }

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            ALL_CHARACTERS -> "vnd.android.cursor.dir/com.puzzlebench.clean_marvel_kotlin"
            SINGLE_CHARACTER -> "vnd.android.cursor.item(com.puzzlebench.clean_marvel_kotlin"
            else -> null
        }
    }

    companion object {
        private const val AUTHORITY = "com.puzzlebench.clean_marvel_kotlin"
        private const val ALL_CHARACTERS = 1
        private const val SINGLE_CHARACTER = 2

        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
    }
}