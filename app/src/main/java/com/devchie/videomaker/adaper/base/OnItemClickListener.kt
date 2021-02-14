package com.devchie.videomaker.adaper.base

interface OnItemClickListener<T> {
    fun onClick(item:T, position: Int)
}