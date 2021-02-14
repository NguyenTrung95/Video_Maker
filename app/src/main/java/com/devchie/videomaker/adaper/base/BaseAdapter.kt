package com.devchie.videomaker.adaper.base

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>> {

    constructor()
    constructor(listener: OnItemClickListener<T>) {
        mOnItemClickListener = listener
    }

    protected var mListItems: MutableList<T> = mutableListOf()
    protected var mOnItemClickListener: OnItemClickListener<T>? = null

    protected abstract fun getLayoutId(viewType: Int): Int
    protected abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<T>

    val listItems: MutableList<T>
        get() = mListItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return createViewHolder(view, viewType)
    }

    fun setListItems(list: MutableList<T>) {
        mListItems.clear()
        mListItems.addAll(list)
        Log.d("Tag",  list.size.toString() )
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mListItems.size
    }

    fun clear() {
        mListItems.clear()
        notifyDataSetChanged()
    }

    fun addAndNotify(element: T) {
        mListItems.add(element)
        notifyItemInserted(itemCount - 1)
    }

    fun add(element: T) {
        mListItems.add(element)
    }

    fun removeAndNotify(index: Int): T {
        val t = mListItems.removeAt(index)
        notifyItemRemoved(index)
        return t
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindData(mListItems[position], position, getItemViewType(position))
    }

}