package test.hackernews.ui.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import test.hackernews.model.Hit
import test.hackernews.util.TimeUtils

@BindingAdapter("title")
fun TextView.setTitle(post : Hit){
    text = when (post.title) {
        null -> post.story_title
        else -> post.title
    }
}


@BindingAdapter("author")
fun TextView.setAuthorAndDate(post : Hit){
    val formattedTime = TimeUtils.getTimeAgo(post.created_at)
    text = when(post.author){
       null -> "${post._highlightResult.author.value} - $formattedTime"
       else -> "${post.author} - $formattedTime"
    }
}
