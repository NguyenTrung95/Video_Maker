/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.matisse.internal.ui

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.devchie.videomaker.R
import com.devchie.videomaker.adaper.PhotoAdapter
import com.devchie.videomaker.listener.ItemClickListener
import com.devchie.videomaker.model.Photo
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.matisse.internal.entity.Album
import com.matisse.internal.entity.Item
import com.matisse.internal.entity.SelectionSpec
import com.matisse.internal.model.AlbumMediaCollection
import com.matisse.internal.model.AlbumMediaCollection.AlbumMediaCallbacks
import com.matisse.internal.model.SelectedItemCollection
import com.matisse.internal.ui.adapter.AlbumMediaAdapter
import com.matisse.internal.ui.adapter.AlbumMediaAdapter.CheckStateListener
import com.matisse.internal.ui.adapter.AlbumMediaAdapter.OnMediaClickListener
import com.matisse.internal.ui.widget.MediaGridInset
import com.matisse.internal.utils.UIUtils
import java.util.*

class MediaSelectionFragment : Fragment(), AlbumMediaCallbacks, CheckStateListener, OnMediaClickListener {
    private val mAlbumMediaCollection = AlbumMediaCollection()
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: AlbumMediaAdapter? = null
    private var mSelectionProvider: SelectionProvider? = null
    private var mCheckStateListener: CheckStateListener? = null
    private var mOnMediaClickListener: OnMediaClickListener? = null

    private var mRecyclerViewSelected: RecyclerView? = null
    private var imgPhoto:ImageView?=null
    private var dragMgr: RecyclerViewDragDropManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mSelectionProvider = if (context is SelectionProvider) {
            context
        } else {
            throw IllegalStateException("Context must implement SelectionProvider.")
        }
        if (context is CheckStateListener) {
            mCheckStateListener = context
        }
        if (context is OnMediaClickListener) {
            mOnMediaClickListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_media_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById<View>(R.id.recyclerview) as RecyclerView
        mRecyclerViewSelected = view.findViewById<View>(R.id.recyclerPhoto) as RecyclerView
        imgPhoto = view.findViewById<View>(R.id.imageViewPhoto) as ImageView


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val album: Album? = arguments!!.getParcelable(EXTRA_ALBUM)
        mAdapter = AlbumMediaAdapter(context,
                mSelectionProvider!!.provideSelectedItemCollection(), mRecyclerView)
        mAdapter!!.registerCheckStateListener(this)
        mAdapter!!.registerOnMediaClickListener(this)
        mRecyclerView!!.setHasFixedSize(true)
        val spanCount: Int
        val selectionSpec = SelectionSpec.getInstance()
        spanCount = if (selectionSpec.gridExpectedSize > 0) {
            UIUtils.spanCount(context, selectionSpec.gridExpectedSize)
        } else {
            selectionSpec.spanCount
        }
        mRecyclerView!!.layoutManager = GridLayoutManager(context, spanCount)
        val spacing = resources.getDimensionPixelSize(R.dimen.media_grid_spacing)
        mRecyclerView!!.addItemDecoration(MediaGridInset(spanCount, spacing, false))
        mRecyclerView!!.adapter = mAdapter
        mAlbumMediaCollection.onCreate(activity!!, this)
        mAlbumMediaCollection.load(album, selectionSpec.capture)

       addRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAlbumMediaCollection.onDestroy()
    }

    fun refreshMediaGrid() {
        mAdapter!!.notifyDataSetChanged()
    }

    fun refreshSelection() {
        mAdapter!!.refreshSelection()
    }

    override fun onAlbumMediaLoad(cursor: Cursor) {
        mAdapter!!.swapCursor(cursor)
    }

    override fun onAlbumMediaReset() {
        mAdapter!!.swapCursor(null)
    }

    override fun onUpdate() {
        // notify outer Activity that check state changed
        if (mCheckStateListener != null) {
            mCheckStateListener!!.onUpdate()
        }
    }

    override fun onMediaClick(album: Album, item: Item, adapterPosition: Int) {
        if (mOnMediaClickListener != null) {
            mOnMediaClickListener!!.onMediaClick(arguments!!.getParcelable<Parcelable>(EXTRA_ALBUM) as Album?,
                    item, adapterPosition)

            listPhoto.add(Photo(System.currentTimeMillis(),album.coverUri.toString()))
            photoAdapter?.notifyDataSetChanged()
        }
    }

    private var photoselected: Photo? = null
    private var listPhoto = ArrayList<Photo>()
    private var positionSelected = 0
    private var pathSelected: String? = null
    private var photoAdapter: PhotoAdapter? = null

    private fun addRecyclerView() {

        val recyclerViewDragDropManager = RecyclerViewDragDropManager()
        dragMgr = recyclerViewDragDropManager
        recyclerViewDragDropManager.setInitiateOnMove(false)
        dragMgr!!.setInitiateOnLongPress(true)
        mRecyclerViewSelected?.setLayoutManager(LinearLayoutManager(context, RecyclerView.HORIZONTAL, false))
        photoAdapter = PhotoAdapter(listPhoto, context, object : ItemClickListener {
            override fun onItemClick(view: View, i: Int) {
                positionSelected = i
                photoselected =  listPhoto[i]
                pathSelected =  photoselected!!.paths
                val requestOptions = RequestOptions()
                requestOptions.centerCrop().placeholder(R.drawable.__picker_ic_photo_black_48dp).error(R.drawable.__picker_ic_broken_image_black_48dp)
                Glide.with(context!!).load(photoselected!!.paths).apply((requestOptions as BaseRequestOptions<*>)).thumbnail(0.1f).into(imgPhoto!!)
            }

            override fun onItemDeleteClick(view: View, i: Int) {
                listPhoto.removeAt(i)
                photoAdapter!!.notifyDataSetChanged()
            }
        })
        photoAdapter?.setHasStableIds(true)
        mRecyclerViewSelected?.setAdapter(dragMgr!!.createWrappedAdapter(photoAdapter!!))
        dragMgr!!.attachRecyclerView(mRecyclerViewSelected!!)
    }

    interface SelectionProvider {
        fun provideSelectedItemCollection(): SelectedItemCollection?
    }

    companion object {
        const val EXTRA_ALBUM = "extra_album"

        //
        fun newInstance(album: Album?): MediaSelectionFragment {
            val fragment = MediaSelectionFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_ALBUM, album)
            fragment.arguments = args
            return fragment
        }
    }
}