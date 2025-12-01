package com.umcspot.spot.post

import androidx.lifecycle.ViewModel
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
//    private val postRepository:  // 실제 구현 주입
) : ViewModel() {

    data class PostingData(
        val title : String = "",
        val content : String = "",
        val postType : PostType = PostType.FREE_TALK,
        val time : String = "2025.11.24",
        val onLikeChecked : Boolean = false,
        val image : ImageRef = ImageRef.None
    )

    data class UserData(
        val userName : String = "추연우",
        val image : ImageRef = ImageRef.LocalName("R.drawable.spot_logo")
    )

    val _postingData = MutableStateFlow(PostingData())
    val postingdata = _postingData.asStateFlow()

    val _userData = MutableStateFlow(UserData())
    val userData = _userData.asStateFlow()


//    fun load(postId : PostResult) {
//
//    }

}