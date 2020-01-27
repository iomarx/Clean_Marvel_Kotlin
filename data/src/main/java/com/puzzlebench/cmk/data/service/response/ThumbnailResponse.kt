package com.puzzlebench.cmk.data.service.response


class ThumbnailResponse(
        var path: String,
        var extension: String
) {
    val securePath: String
        get() {
            return path.replace("http", "https")
        }
}