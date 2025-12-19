package com.umcspot.spot.feature.board.post.posting

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.model.posting.Posting
import com.umcspot.spot.post.model.posting.PostingResult
import com.umcspot.spot.post.repository.PostRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _body = MutableStateFlow("")
    val body: StateFlow<String> = _body.asStateFlow()

    private val _postType = MutableStateFlow(PostType.FREE_TALK)
    val postType: StateFlow<PostType> = _postType.asStateFlow()

    private val _image = MutableStateFlow<ImageRef>(ImageRef.None)
    val image: StateFlow<ImageRef> = _image.asStateFlow()

    private val _editingPostId = MutableStateFlow<Long?>(null)
    val editingPostId: StateFlow<Long?> = _editingPostId.asStateFlow()

    private val _submitState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val submitState: StateFlow<UiState<Unit>> = _submitState.asStateFlow()

    fun clear() {
        _editingPostId.value = null
        _title.value = ""
        _body.value = ""
        _postType.value = PostType.PASS_EXPERIENCE
        _image.value = ImageRef.None
    }

    fun load(postId: Long) {
        _editingPostId.value = postId   // ✅ 추가
        viewModelScope.launch {
            runCatching {
                postRepository.getPostDetail(postId).getOrThrow()
            }.onSuccess { res ->
                Log.d("PostingViewModel", "load prevPosting: $res")
                _title.value = res.title
                _body.value = res.content
                _postType.value = res.postType
                _image.value = res.imageUrl
            }.onFailure { e ->
                Log.e("PostingViewModel", "load error", e)
            }
        }
    }

    fun onTitleChange(text: String) { _title.value = text }
    fun onBodyChange(text: String) { _body.value = text }
    fun onSelectPostType(type: PostType) { _postType.value = type }
    fun setImage(image: ImageRef) { _image.value = image }
    fun clearImage() { _image.value = ImageRef.None }


    fun submit() {
        val title = _title.value.trim()
        val body = _body.value.trim()
        val type = _postType.value
        val image = _image.value
        val targetId = _editingPostId.value

        viewModelScope.launch {
            runCatching {
                if (targetId == null) {
                    // 새 글 생성
                    postRepository.postPost(
                        Posting(
                            title = title,
                            content = body,
                            postType = type,
                            imageFile = image
                        )
                    ).getOrThrow()
                } else {
                    // 수정
                    postRepository.editPost(
                        postId = targetId,
                        Posting(
                            title = title,
                            content = body,
                            postType = type,
                            imageFile = image
                        )
                    ).getOrThrow()
                }
            }.onSuccess { res ->
                Log.d("PostingViewModel", "finish create/edit Post: $res")
                _submitState.value = UiState.Success(Unit)
            }.onFailure { e ->
                Log.e("PostingViewModel", "create/edit error", e)
                _submitState.value = UiState.Failure(e.message ?: "error")
            }
        }
    }

    fun consumeSubmitResult() {
        _submitState.value = UiState.Empty
    }
}


