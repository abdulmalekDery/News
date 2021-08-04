package com.beinmedia.test.features.newsTab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beinmedia.test.R
import com.beinmedia.test.databinding.ItemNewsBinding
import com.beinmedia.test.databinding.ItemNewsStaggerItemBinding
import com.beinmedia.test.dependencyinjection.module.GlideApp
import com.beinmedia.test.models.network.pojo.home.News
import com.beinmedia.test.utils.RecyclerItemListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter

class NewsStaggerAdapter(dataSet: List<News> = emptyList(),
                         val listener: (item : News, index : Int,bitmapFromView: Bitmap?)->Unit):
    DragDropSwipeAdapter<News, NewsStaggerAdapter.NewsViewHolder>(dataSet) {

    lateinit var context: Context

    inner class NewsViewHolder(val binding: ItemNewsStaggerItemBinding) :
        DragDropSwipeAdapter.ViewHolder(binding.root), View.OnClickListener {
        val wholeView: CardView = itemView.findViewById(R.id.news_item_view_group)
        fun bind(item: News) {
            binding.news = item
            binding.executePendingBindings()
        }

        init {
            binding.notDragView.setOnClickListener(this)
            binding.shareIcon.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (view?.id== R.id.share_icon) {
                listener(dataSet[bindingAdapterPosition],bindingAdapterPosition,getBitmapFromView(binding.newsImage))
            }

            else
                listener(dataSet[bindingAdapterPosition],bindingAdapterPosition,null)
        }
    }

    fun getBitmapFromView(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    override fun getItemCount() = dataSet.size

    override fun getViewHolder(parent: View): NewsViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemNewsStaggerItemBinding.inflate(layoutInflater, parent as ViewGroup, false)
        return NewsViewHolder(binding)
    }

    override fun getViewToTouchToStartDraggingItem(
        item: News,
        viewHolder: NewsViewHolder,
        position: Int
    ): View? {
        return viewHolder.wholeView
    }

    override fun onBindViewHolder(item: News, holder: NewsViewHolder, position: Int) {
        val item = dataSet[position]

        GlideApp.with(holder.binding.newsImage.context).load(item.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.newsImage)
//            .getSize { width, height ->
//                val aspectRatio = width.toFloat() / height.toFloat()
//                holder.binding.newsImage.setAspectRatio(aspectRatio)
//                if (dynamicView) {
//                    val lp = ConstraintLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    holder.binding.newsImage.layoutParams = lp
//                } else {
//                    val  imageHeight = context.resources.getDimension(R.dimen.image_height)
//                    val lp = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight.toInt())
//                    holder.binding.newsImage.layoutParams = lp
//                }
//            }
        GlideApp.with(holder.binding.shareIcon.context).load(
            R.drawable.ic_baseline_share_24
        ).into(holder.binding.shareIcon)
        holder.bind(item)
    }

}