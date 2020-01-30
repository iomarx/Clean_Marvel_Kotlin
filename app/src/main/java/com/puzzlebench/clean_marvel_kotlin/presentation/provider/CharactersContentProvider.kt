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
        addURI(AUTHORITY, TABLE, ALL_CHARACTERS)
        addURI(AUTHORITY, "$TABLE/#", SINGLE_CHARACTER)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun query(
            uri: Uri,
            projection: Array<String>?,
            selection: String?,
            selectionArgs: Array<String>?,
            sortOrder: String?
    ): Cursor? {
        val matrixCursor = MatrixCursor(columns)

        runOnRealm { realm ->
            val baseQuery = realm.where(CharacterRealm::class.java)

            if (uriMatcher.match(uri) == SINGLE_CHARACTER) {
                val id = uri.lastPathSegment?.toInt() ?: 0
                val result = baseQuery.equalTo(COLUMN_ID, id).findFirst()
                result?.let {
                    matrixCursor.addRow(characterToArray(it))
                }
            } else {
                val realmResults = baseQuery.findAll().sort(sortOrder)

                realmResults.forEach {
                    matrixCursor.addRow(characterToArray(it))
                }
            }
            matrixCursor.setNotificationUri(context?.contentResolver, uri)
        }

        return matrixCursor
    }

    private fun runOnRealm(action: (Realm) -> Unit) {
        Realm.getDefaultInstance().use {
            action.invoke(it)
        }
    }

    private fun characterToArray(it: CharacterRealm) = arrayOf(
            it._id, it.name, it.description, it.thumbnail?.path, it.thumbnail?.extension
    )

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
        if (uriMatcher.match(uri) != SINGLE_CHARACTER) return 0

        var deleteCount = 0

        runOnRealm { realm ->
            val id = uri.lastPathSegment?.toInt() ?: 0
            val result = realm.where(CharacterRealm::class.java).equalTo(COLUMN_ID, id).findFirst()
            realm.executeTransaction {
                result?.deleteFromRealm()
                deleteCount++
            }
        }

        if (deleteCount > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return deleteCount
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