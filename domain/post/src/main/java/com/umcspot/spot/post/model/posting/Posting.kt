package com.umcspot.spot.post.model.posting

import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType

data class Posting (
    val title : String,
    val content : String,
    val postType: PostType,
    val imageFile : ImageRef
)