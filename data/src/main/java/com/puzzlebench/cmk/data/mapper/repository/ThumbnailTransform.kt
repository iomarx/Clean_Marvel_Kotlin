package com.puzzlebench.cmk.data.mapper.repository

import com.puzzlebench.cmk.data.model.ThumbnailRealm
import com.puzzlebench.cmk.domain.model.Thumbnail

class ThumbnailTransform : BaseMapperRepository<Thumbnail, ThumbnailRealm?> {

    override fun transform(input: Thumbnail): ThumbnailRealm =
            ThumbnailRealm(input.path, input.extension)

    override fun transform(input: ThumbnailRealm?): Thumbnail =
            Thumbnail(input?.path ?: "", input?.extension ?: "")
}