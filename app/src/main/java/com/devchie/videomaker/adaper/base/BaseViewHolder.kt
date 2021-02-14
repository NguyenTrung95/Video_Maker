package com.devchie.videomaker.adaper.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder<T> : RecyclerView.ViewHolder {

    constructor(itemView: View) : super(itemView)
    constructor(itemView: View, list: MutableList<T>) : super(itemView)
    constructor(itemView: View, list: MutableList<T>, listener: OnItemClickListener<T>?=null) : super(itemView) {
        itemView.setOnClickListener { list[adapterPosition]?.let { listener?.onClick(it, adapterPosition) } }
    }
    abstract fun bindData(t: T, position: Int, viewType: Int)

}