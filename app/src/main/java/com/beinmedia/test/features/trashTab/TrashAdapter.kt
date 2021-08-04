package com.beinmedia.test.features.trashTab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.beinmedia.test.R
import com.beinmedia.test.databinding.ItemDeletedNewsBinding
import com.beinmedia.test.databinding.ItemNewsBinding
import com.beinmedia.test.dependencyinjection.module.GlideApp
import com.beinmedia.test.models.local.models.NewsDB
import com.beinmedia.test.models.network.pojo.home.News
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter

class TrashAdapter(dataSet: List<NewsDB> = emptyList(), ):
    DragDropSwipeAdapter<NewsDB, TrashAdapter.NewsViewHolder>(dataSet) {

    lateinit var context: Context

    inner class NewsViewHolder(val binding: ItemDeletedNewsBinding) :
        DragDropSwipeAdapter.ViewHolder(binding.root), View.OnClickListener {
        val wholeView: CardView = itemView.findViewById(R.id.deleted_news_whole_view)
        fun bind(item: NewsDB) {
            binding.deletedNews = item
            binding.executePendingBindings()
        }

        init {
//            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
        }
    }

    override fun getItemCount() = dataSet.size

    override fun getViewHolder(parent: View): NewsViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemDeletedNewsBinding.inflate(layoutInflater, parent as ViewGroup, false)
        return NewsViewHolder(binding)
    }

    override fun getViewToTouchToStartDraggingItem(
        item: NewsDB,
        viewHolder: NewsViewHolder,
        position: Int
    ): View? {
        return viewHolder.wholeView
    }

    override fun onBindViewHolder(item: NewsDB, holder: NewsViewHolder, position: Int) {
        val item = dataSet[position]

        GlideApp.with(holder.binding.deletedNewsImage.context).load(item.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
            .into(holder.binding.deletedNewsImage)
        GlideApp.with(holder.binding.deletedNewsMenuIcon.context).load(
            R.drawable.ic_baseline_square_dot_menu_24
        ).into(holder.binding.deletedNewsMenuIcon)
        holder.bind(item)
    }

}