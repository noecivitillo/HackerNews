package test.hackernews.data

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import test.hackernews.data.api.HackerNewsApi
import test.hackernews.data.db.HackerNewsDb
import test.hackernews.model.Data
import test.hackernews.model.Hit
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class PostsRepository @Inject constructor(private val api: HackerNewsApi?, private val db: HackerNewsDb) {
    @MainThread
    fun getPosts(): Listing<Hit> {
        val refreshTrigger = MutableLiveData<Unit>()

        val listPosts = db.posts().allPosts()

        val networkState = getPostsFromApi()

        val refreshState = refreshTrigger.switchMap{
           getPostsFromApi()
        }

        return Listing(
            list = listPosts,
            loadingState = networkState,
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    fun delete(post: Hit) = ioThread {
        post.isDeleted = true
        db.posts().updatePost(post)

    }

    private fun insertAllPostsInDb(list: List<Hit>) = ioThread {
            db.posts().insert(list)
    }

    /**
     * A call to obtain the posts
     * All the new data is inserted in database
     */
    private fun getPostsFromApi(): LiveData<LoadingState> {
        val networkState = MutableLiveData<LoadingState>()
        networkState.value = LoadingState.LOADING
        api?.getPosts()?.enqueue(
            object : Callback<Data> {
                override fun onFailure(call: Call<Data>, t: Throwable) {
                    //Only handle the failure of the server, this could be improved, e.g checking the internet connection
                    // and throwing the corresponding error
                    networkState.value = LoadingState.error(t.message)
                }

                override fun onResponse(
                    call: Call<Data>,
                    response: Response<Data>
                ) {

                    ioThread {
                        //Check if data exists and insert
                        if(db.posts().isDbEmpty() == 0){
                            insertAllPostsInDb(response.body()!!.hits)
                        }else{
                            //Get all dismissed posts
                            val listFromDb = db.posts().allDeleted()
                            //Convert the response in a mutable list, to remove the posts dismissed
                            val newList = response.body()!!.hits.toMutableList()

                            response.body()!!.hits.forEach {hit->
                                val id = hit.story_id
                                val postToRemove: Hit? = listFromDb.find { id == it.story_id }
                                newList.remove(postToRemove)
                            }
                            //Update the db
                            db.posts().updateAll(newList.toList())
                        }
                    }

                    networkState.postValue(LoadingState.LOADED)
                }
            }
        )
        return networkState
    }
}
