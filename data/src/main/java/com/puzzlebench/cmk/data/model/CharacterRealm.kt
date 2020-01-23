package com.puzzlebench.cmk.data.model

import io.realm.RealmObject


open class CharacterRealm(
        var id: Int = 0,
        var name: String? = null,
        var description: String? = null,
        var thumbnail: ThumbnailRealm? = null
) : RealmObject()