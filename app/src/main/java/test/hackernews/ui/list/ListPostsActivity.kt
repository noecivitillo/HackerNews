package test.hackernews.ui.list

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import test.hackernews.R
import test.hackernews.data.LoadingState
import test.hackernews.databinding.ActivityListBinding
import test.hackernews.ui.detail.DetailPostActivity


@AndroidEntryPoint
class ListPostsActivity : AppCompatActivity() {
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initAdapter()
        initSwipeToRefresh()
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)


        adapter = PostsAdapter {
            val intent = Intent(
                this,
                DetailPostActivity::class.java
            ).apply {
                putExtra(
                    URL,
                    it.story_url
                )
            }
            startActivity(intent)
        }

        binding.recycler.adapter = adapter

        viewModel.posts.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {})

        val simpleCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val post = adapter.getPostByPosition(viewHolder.adapterPosition)
                    post?.let { viewModel.deletePost(it) }
                    adapter.notifyDataSetChanged()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                    /**
                     * We need to set the background and draw the text, using the view item as reference
                     */
                    val itemView = viewHolder.itemView
                    val background = ColorDrawable(
                        ContextCompat.getColor(
                            this@ListPostsActivity,
                            R.color.color_red
                        )
                    )
                    /**
                     * Set the bounds and draw the Canvas
                     * The important thing is the left side of the screen, need to be drawn when the swipe is performed
                     */
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)

                    /**
                     * Use a Paint object to apply style to the text
                     */
                    val paint = Paint().apply {
                        color = Color.WHITE
                        textSize = 25f
                        textAlign = Paint.Align.CENTER
                    }

                    /**
                     * Draw the text
                     */
                    c.drawText(
                        resources.getString(R.string.delete_text),
                        (itemView.right - 60).toFloat(),
                        (itemView.top + itemView.height / 2).toFloat(), paint
                    )
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)


    }

    private fun initSwipeToRefresh() {
        viewModel.refreshState.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = it == LoadingState.LOADING
        })
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    companion object {
        const val URL = "url"
    }
}