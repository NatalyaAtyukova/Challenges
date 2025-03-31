package com.challenges.dailychallenges.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Comment(
    @DocumentId
    val id: String = "",
    
    @PropertyName("challenge_id")
    val challengeId: String = "",
    
    @PropertyName("user_id")
    val userId: String = "",
    
    @PropertyName("user_name")
    val userName: String = "",
    
    @PropertyName("user_photo_url")
    val userPhotoUrl: String? = null,
    
    val text: String = "",
    
    @PropertyName("created_at")
    val createdAt: Timestamp = Timestamp.now(),
    
    @PropertyName("likes_count")
    val likesCount: Int = 0,
    
    @PropertyName("is_reported")
    val isReported: Boolean = false,
    
    @PropertyName("report_reason")
    val reportReason: String? = null
) 