package com.umcspot.spot.study.model

enum class ViewerStatus {
    NOT_APPLIED,  
    APPLIED,      
    APPROVED,     
    OWNER,        
    UNKNOWN       
}

data class StudyDetailModel(
    val id: Long,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val categories: List<String>,
    val totalMembers: Int,
    val currentMembers: Int,
    val likeCount: Int,
    val hitCount: Int,
    val viewerStatus: ViewerStatus 
)