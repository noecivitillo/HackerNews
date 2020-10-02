package test.hackernews.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import test.hackernews.databinding.ItemPostBinding
import test.hackernews.model.Hit


class PostsAdapter(
    private val callback: (Hit) -> Unit
) : ListAdapter<Hit, PostsAdapter.ViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindPosts(getItem(position), callback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    fun getPostByPosition(position: Int) : Hit?{
        return getItem(position)
    }

    class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPosts(post: Hit, callback: (Hit) -> Unit) =
            with(itemView) {
                binding.post = post
                binding.executePendingBindings()

                itemView.setOnClickListener {
                    callback.invoke(post)
                }

            }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Hit>() {
            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean =
                oldItem.story_title == newItem.story_title
        }
    }

}