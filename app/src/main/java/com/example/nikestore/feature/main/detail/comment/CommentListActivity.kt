package com.example.nikestore.feature.main.detail.comment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.feature.main.detail.CommentAdapter
import com.example.nikestore.data.Comment
import kotlinx.android.synthetic.main.activity_comment_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {

    private val viewModel: CommentListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_ID
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }

        viewModel.commentsLiveData.observe(this) {
            val adapter = CommentAdapter(true)
            commentRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapter.comments = it as ArrayList<Comment>
            commentRv.adapter = adapter
        }

        commentListToolbar.onBackButtonClickListener = View.OnClickListener {
            finish()
        }
    }
}