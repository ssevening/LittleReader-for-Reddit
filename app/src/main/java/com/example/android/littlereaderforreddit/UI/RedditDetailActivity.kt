package com.example.android.littlereaderforreddit.UI

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v4.view.ViewCompat

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.android.littlereaderforreddit.Data.Comments
import com.example.android.littlereaderforreddit.Data.FeedDetail
import com.example.android.littlereaderforreddit.Network.RetrofitClient

import com.example.android.littlereaderforreddit.R
import com.example.android.littlereaderforreddit.Util.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reddit_detail.*
import kotlinx.android.synthetic.main.feed_detail.*

class RedditDetailActivity : AppCompatActivity(), LoaderCallbacks<List<Comments>> {
    lateinit var detail: FeedDetail
    lateinit var commentsAdapter: CommentsAdapter
    private val COMMENTS_LOADER = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reddit_detail)

        detail = intent.getParcelableExtra(Constant.EXTRA_FEED_DETAIL)
        subreddit_name.text = detail.subredditName
        created_time.text = detail.created.toString()
        score.text = detail.score.toString()
        num_comments.text = detail.num_comments.toString()
        feed_title.text = detail.title
        val image = detail.largeImage
        if (hasThumbnailImage(image)) {
              Picasso.with(this)
                      .load(image)
                      .into(imageView)
        } else {
            imageView.visibility = View.GONE
        }

        commentsAdapter = CommentsAdapter()
        comments_recycler.adapter = commentsAdapter
        comments_recycler.layoutManager = LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(comments_recycler, false)
        supportLoaderManager.initLoader(COMMENTS_LOADER, null, this)

    }

    override fun onLoaderReset(loader: Loader<List<Comments>>?) {

    }

    override fun onLoadFinished(loader: Loader<List<Comments>>?, commentList: List<Comments>?) {
        if (commentList?.size?:0 > 1) {
            commentsAdapter.setComments(commentList!![1])
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Comments>> {
        return object: AsyncTaskLoader<List<Comments>>(this) {
            override fun onStartLoading() {
                forceLoad()
            }
            override fun loadInBackground(): List<Comments>? {
                try {
                    val response = RetrofitClient.instance.getComments(detail.id).execute()
                    if (response.isSuccessful) {
                        return response.body()
                    }
                    return null
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }
            }
        }
    }

    override fun onDestroy() {
        imageView?.setImageDrawable(null)
        super.onDestroy()
    }
    private fun hasThumbnailImage(url: String?): Boolean = url != null && url.contains("https")
}
