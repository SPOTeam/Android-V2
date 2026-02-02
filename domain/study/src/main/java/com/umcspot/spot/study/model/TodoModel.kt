package com.umcspot.spot.study.model

data class TodoModel(
    val id: Long,               
    val memberId: String,       
    val content: String,        
    val isCompleted: Boolean    
)