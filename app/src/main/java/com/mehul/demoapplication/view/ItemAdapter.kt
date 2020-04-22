package com.mehul.demoapplication.view

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mehul.demoapplication.BR
import com.mehul.demoapplication.R
import com.mehul.demoapplication.databinding.LayoutItemBinding
import com.mehul.demoapplication.model.Item
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import kotlinx.android.synthetic.main.layout_item.view.*
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList


class ItemAdapter(
    private val ItemList: ArrayList<Item>,
    private val context: Context?
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var arraylist: ArrayList<Item> = ArrayList()


    var searchText = ""
    private val animateFirstListener: ImageLoadingListener =
        AnimateFirstDisplayListener()
    var loader: ImageLoader? = null
    private var options: DisplayImageOptions? = null

    init {
        arraylist.addAll(ItemList)
        loader = ImageLoader.getInstance()
        initLoader(context)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: LayoutItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item, parent, false
            )

        return ViewHolder(binding)
    }
    private fun initLoader(context: Context?) {

        loader?.init(ImageLoaderConfiguration.createDefault(context))
        options = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.loading)
            .showImageForEmptyUri(R.drawable.ic_loading)
            .showImageOnFail(R.drawable.ic_loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build()
    }
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = ItemList[position]
        holder.bind(item)
        loader?.displayImage(
            item.imageHref,
            holder.itemView.ivImg,
            options,
            animateFirstListener
        )


    }

    override fun getItemCount(): Int {
        return ItemList.size
    }

    inner class ViewHolder(var itemRowBinding: LayoutItemBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {
        fun bind(obj: Any?) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
        }

    }

    // Filter Class
    @Throws(ParseException::class)
    fun filter(searchText: String) {
        var searchText = searchText
        searchText = searchText.toLowerCase(Locale.getDefault())
        this.searchText = searchText
        ItemList.clear()
        if (searchText.isEmpty()) {
            ItemList.addAll(arraylist)
        } else {
            for (item in arraylist) {
                if (item.title?.toLowerCase(Locale.getDefault())?.contains(searchText)!!) {
                    ItemList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    private class AnimateFirstDisplayListener : SimpleImageLoadingListener() {
        override fun onLoadingComplete(
            imageUri: String,
            view: View,
            loadedImage: Bitmap
        ) {
            if (loadedImage != null) {
                val imageView = view as ImageView
                val firstDisplay =
                    !displayedImages.contains(imageUri)
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500)
                    displayedImages.add(imageUri)
                }
            }
        }

        companion object {
            val displayedImages =
                Collections.synchronizedList(LinkedList<String>())
        }
    }

}