package test.hackernews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import test.hackernews.model.Hit


@Dao
interface HackerNewsDao{
    @Query("SELECT COUNT(*) FROM Hit")
    fun isDbEmpty(): Int

    @Query("SELECT * FROM Hit WHERE isDeleted = 0 ORDER BY created_at DESC")
    fun allPosts(): LiveData<List<Hit>>

    @Query("SELECT * FROM Hit WHERE isDeleted = 1")
    fun allDeleted(): List<Hit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Hit>)

    @Update
    fun updatePost(post: Hit)

    @Update
    fun updateAll(posts: List<Hit>)

}