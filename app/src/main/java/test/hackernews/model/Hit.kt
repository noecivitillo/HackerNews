package test.hackernews.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(
    value = ["story_id", "story_title"],
    unique = true
)]
)
data class Hit(
    @PrimaryKey
    val story_id: Int,
    val _highlightResult: HighlightResult,
    val author: String?,
    val created_at: String,
    val story_title: String?=null,
    val story_url: String?= null,
    val title : String?
){
    var isDeleted : Boolean = false
}