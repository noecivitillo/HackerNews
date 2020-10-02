package test.hackernews.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import test.hackernews.data.PostsRepository
import test.hackernews.model.Hit


class PostsViewModel @ViewModelInject constructor(val repo: PostsRepository) : ViewModel() {
    private val result = repo.getPosts()

    val posts = result.list
    val networkState = result.loadingState
    val refreshState = result.refreshState

    fun refresh() {
        result.refresh.invoke()
    }

    fun deletePost(post: Hit) {
        repo.delete(post)
    }

}