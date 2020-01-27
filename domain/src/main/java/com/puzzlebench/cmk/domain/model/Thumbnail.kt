package com.puzzlebench.cmk.domain.model

class Thumbnail(
        var path: String,
        var extension: String
) {
    val url: String
        get() {
            return "$path.$extension"
        }
}
