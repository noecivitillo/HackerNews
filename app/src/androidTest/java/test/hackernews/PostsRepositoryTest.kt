package test.hackernews

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import test.hackernews.data.Listing
import test.hackernews.data.PostsRepository
import test.hackernews.data.db.HackerNewsDb
import test.hackernews.model.*

@RunWith(AndroidJUnit4::class)
class PostsRepositoryTest {
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()
    private lateinit var context: Context
    private lateinit var db: HackerNewsDb
    private lateinit var repository: PostsRepository

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
        db = HackerNewsDb.create(context, true)
        repository = PostsRepository(null, db)

        //insert dummy data
        db.posts().insert(
            listOf(
                Hit(1, HighlightResult(Author(""), StoryTitle(""), StoryUrl("")), null, "", null, null, null)
            )
        )
    }

    @Test
    fun verifyListLoaded() {
        val listing = repository.getPosts()
        val list = getList(listing)
        assertThat(list.size, `is`(1))
    }


    /**
     * extract the list from the Listing object
     */
    private fun getList(listing: Listing<Hit>): List<Hit> {
        val observer = LoggingObserver<List<Hit>>()
        listing.list.observeForever(observer)
        assertThat(observer.value, `is`(notNullValue()))
        return observer.value!!
    }

    /**
     * simple observer that logs the latest value it receives
     */
    private class LoggingObserver<T> : Observer<T> {
        var value: T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }

}