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
package com.matisse.ui

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.devchie.videomaker.R
import com.matisse.internal.entity.Album
import com.matisse.internal.entity.Item
import com.matisse.internal.entity.SelectionSpec
import com.matisse.internal.model.AlbumCollection
import com.matisse.internal.model.SelectedItemCollection
import com.matisse.internal.ui.AlbumPreviewActivity
import com.matisse.internal.ui.BasePreviewActivity
import com.matisse.internal.ui.MediaSelectionFragment
import com.matisse.internal.ui.SelectedPreviewActivity
import com.matisse.internal.ui.adapter.AlbumMediaAdapter
import com.matisse.internal.ui.adapter.AlbumsAdapter
import com.matisse.internal.ui.widget.AlbumsSpinner
import com.matisse.internal.ui.widget.CheckRadioView
import com.matisse.internal.ui.widget.IncapableDialog
import com.matisse.internal.utils.MediaStoreCompat
import com.matisse.internal.utils.PathUtils
import com.matisse.internal.utils.PhotoMetadataUtils
import com.matisse.internal.utils.SingleMediaScanner
import java.util.*

/**
 * Main Activity to display albums and media content (images/videos) in each album
 * and also support media selecting operations.
 */
class MatisseActivity : AppCompatActivity(), AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener, MediaSelectionFragment.SelectionProvider, View.OnClickListener, AlbumMediaAdapter.CheckStateListener, AlbumMediaAdapter.OnMediaClickListener, AlbumMediaAdapter.OnPhotoCapture  {
    private val mAlbumCollection = AlbumCollection()
    private var mMediaStoreCompat: MediaStoreCompat? = null
    private val mSelectedCollection = SelectedItemCollection(this)
    private var mSpec: SelectionSpec? = null
    private var mAlbumsSpinner: AlbumsSpinner? = null
    private var mAlbumsAdapter: AlbumsAdapter? = null
    private var mButtonPreview: TextView? = null
    private var mButtonApply: TextView? = null
    private var mContainer: View? = null
    private var mEmptyView: View? = null
    private var mOriginalLayout: LinearLayout? = null
    private var mOriginal: CheckRadioView? = null
    private var mOriginalEnable = false
    private var recyclerPhoto: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // programmatically set theme before super.onCreate()
        mSpec = SelectionSpec.getInstance()
        setTheme(mSpec?.themeId!!)
        super.onCreate(savedInstanceState)
        if (!mSpec!!.hasInited) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        setContentView(R.layout.activity_matisse)
        if (mSpec!!.needOrientationRestriction()) {
            requestedOrientation = mSpec!!.orientation
        }
        if (mSpec!!.capture) {
            mMediaStoreCompat = MediaStoreCompat(this)
            if (mSpec!!.captureStrategy == null) throw RuntimeException("Don't forget to set CaptureStrategy.")
            mMediaStoreCompat!!.setCaptureStrategy(mSpec!!.captureStrategy)
        }
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        val navigationIcon = toolbar.navigationIcon
        val ta = theme.obtainStyledAttributes(intArrayOf(R.attr.album_element_color))
        val color = ta.getColor(0, 0)
        ta.recycle()
        navigationIcon!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        mButtonPreview = findViewById<View>(R.id.button_preview) as TextView
        mButtonApply = findViewById<View>(R.id.button_apply) as TextView
        mButtonPreview!!.setOnClickListener(this)
        mButtonApply!!.setOnClickListener(this)
        mContainer = findViewById(R.id.container)
        mEmptyView = findViewById(R.id.empty_view)
        mOriginalLayout = findViewById(R.id.originalLayout)
        mOriginal = findViewById(R.id.original)
        mOriginalLayout?.setOnClickListener(this)
        mSelectedCollection.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mOriginalEnable = savedInstanceState.getBoolean(CHECK_STATE)
        }
        updateBottomToolbar()
        mAlbumsAdapter = AlbumsAdapter(this, null, false)
        mAlbumsSpinner = AlbumsSpinner(this)
        mAlbumsSpinner!!.setOnItemSelectedListener(this)
        mAlbumsSpinner!!.setSelectedTextView(findViewById<View>(R.id.selected_album) as TextView)
        mAlbumsSpinner!!.setPopupAnchorView(findViewById(R.id.toolbar))
        mAlbumsSpinner!!.setAdapter(mAlbumsAdapter)
        mAlbumCollection.onCreate(this, this)
        mAlbumCollection.onRestoreInstanceState(savedInstanceState)
        mAlbumCollection.loadAlbums()

       // addRecyclerView()
    }
  /*  private var dragMgr: RecyclerViewDragDropManager? = null
    private var listPhoto = ArrayList<Photo>()
    private var positionSelected = 0
    private var photoselected: Photo? = null
    private var pathSelected: String? = null
    private var photoAdapter: PhotoAdapter? = null

    private fun addRecyclerView() {
        recyclerPhoto = findViewById(R.id.recyclerPhoto)
        val recyclerViewDragDropManager = RecyclerViewDragDropManager()
        dragMgr = recyclerViewDragDropManager
        recyclerViewDragDropManager.setInitiateOnMove(false)
        dragMgr!!.setInitiateOnLongPress(true)
        recyclerPhoto?.setLayoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        val photoAdapter2 = PhotoAdapter(listPhoto, this, object : ItemClickListener {
            override fun onItemClick(view: View, i: Int) {
                positionSelected = i
                val selectedPhotoActivity = this@MatisseActivity
                selectedPhotoActivity.photoselected = selectedPhotoActivity.listPhoto[i]
                val selectedPhotoActivity2 = this@MatisseActivity
                selectedPhotoActivity2.pathSelected = selectedPhotoActivity2.photoselected!!.paths
                val requestOptions = RequestOptions()
                requestOptions.centerCrop().placeholder(R.drawable.__picker_ic_photo_black_48dp).error(R.drawable.__picker_ic_broken_image_black_48dp)
            }

            override fun onItemDeleteClick(view: View, i: Int) {
                listPhoto.removeAt(i)
                photoAdapter!!.notifyDataSetChanged()
            }
        })
        photoAdapter = photoAdapter2
        photoAdapter2.setHasStableIds(true)
        recyclerPhoto?.setAdapter(dragMgr!!.createWrappedAdapter(photoAdapter!!))
        dragMgr!!.attachRecyclerView(recyclerPhoto!!)
    }
*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mSelectedCollection.onSaveInstanceState(outState)
        mAlbumCollection.onSaveInstanceState(outState)
        outState.putBoolean("checkState", mOriginalEnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAlbumCollection.onDestroy()
        mSpec!!.onCheckedListener = null
        mSpec!!.onSelectedListener = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CODE_PREVIEW) {
            val resultBundle = data!!.getBundleExtra(BasePreviewActivity.EXTRA_RESULT_BUNDLE)
            val selected: ArrayList<Item> = resultBundle.getParcelableArrayList(SelectedItemCollection.STATE_SELECTION)!!
            mOriginalEnable = data.getBooleanExtra(BasePreviewActivity.EXTRA_RESULT_ORIGINAL_ENABLE, false)
            val collectionType = resultBundle.getInt(SelectedItemCollection.STATE_COLLECTION_TYPE,
                    SelectedItemCollection.COLLECTION_UNDEFINED)
            if (data.getBooleanExtra(BasePreviewActivity.EXTRA_RESULT_APPLY, false)) {
                val result = Intent()
                val selectedUris = ArrayList<Uri>()
                val selectedPaths = ArrayList<String>()
                if (selected != null) {
                    for (item in selected) {
                        selectedUris.add(item.contentUri)
                        selectedPaths.add(PathUtils.getPath(this, item.contentUri))
                    }
                }
                result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selectedUris)
                result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPaths)
                result.putExtra(EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable)
                setResult(Activity.RESULT_OK, result)
                finish()
            } else {
                mSelectedCollection.overwrite(selected, collectionType)
                val mediaSelectionFragment = supportFragmentManager.findFragmentByTag(
                        MediaSelectionFragment::class.java.simpleName)
                if (mediaSelectionFragment is MediaSelectionFragment) {
                    mediaSelectionFragment.refreshMediaGrid()
                }
                updateBottomToolbar()
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE) {
            // Just pass the data back to previous calling Activity.
            val contentUri = mMediaStoreCompat!!.currentPhotoUri
            val path = mMediaStoreCompat!!.currentPhotoPath
            val selected = ArrayList<Uri>()
            selected.add(contentUri)
            val selectedPath = ArrayList<String>()
            selectedPath.add(path)
            val result = Intent()
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selected)
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPath)
            setResult(Activity.RESULT_OK, result)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) this@MatisseActivity.revokeUriPermission(contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            SingleMediaScanner(this.applicationContext, path) { Log.i("SingleMediaScanner", "scan finish!") }
            finish()
        }
    }

    private fun updateBottomToolbar() {
        val selectedCount = mSelectedCollection.count()
        if (selectedCount == 0) {
            mButtonPreview!!.isEnabled = false
            mButtonApply!!.isEnabled = false
            mButtonApply!!.text = getString(R.string.button_apply_default)
        } else if (selectedCount == 1 && mSpec!!.singleSelectionModeEnabled()) {
            mButtonPreview!!.isEnabled = true
            mButtonApply!!.setText(R.string.button_apply_default)
            mButtonApply!!.isEnabled = true
        } else {
            mButtonPreview!!.isEnabled = true
            mButtonApply!!.isEnabled = true
            mButtonApply!!.text = getString(R.string.button_apply, selectedCount)
        }
        if (mSpec!!.originalable) {
            mOriginalLayout!!.visibility = View.VISIBLE
            updateOriginalState()
        } else {
            mOriginalLayout!!.visibility = View.INVISIBLE
        }
    }

    private fun updateOriginalState() {
        mOriginal!!.setChecked(mOriginalEnable)
        if (countOverMaxSize() > 0) {
            if (mOriginalEnable) {
                val incapableDialog = IncapableDialog.newInstance("",
                        getString(R.string.error_over_original_size, mSpec!!.originalMaxSize))
                incapableDialog.show(supportFragmentManager,
                        IncapableDialog::class.java.name)
                mOriginal!!.setChecked(false)
                mOriginalEnable = false
            }
        }
    }

    private fun countOverMaxSize(): Int {
        var count = 0
        val selectedCount = mSelectedCollection.count()
        for (i in 0 until selectedCount) {
            val item = mSelectedCollection.asList()[i]
            if (item.isImage) {
                val size = PhotoMetadataUtils.getSizeInMB(item.size)
                if (size > mSpec!!.originalMaxSize) {
                    count++
                }
            }
        }
        return count
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_preview) {
            val intent = Intent(this, SelectedPreviewActivity::class.java)
            intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection.dataWithBundle)
            intent.putExtra(BasePreviewActivity.EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable)
            startActivityForResult(intent, REQUEST_CODE_PREVIEW)
        } else if (v.id == R.id.button_apply) {
            val result = Intent()
            val selectedUris = mSelectedCollection.asListOfUri() as ArrayList<Uri>
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selectedUris)
            val selectedPaths = mSelectedCollection.asListOfString() as ArrayList<String>
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPaths)
            result.putExtra(EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable)
            setResult(Activity.RESULT_OK, result)
            finish()
        } else if (v.id == R.id.originalLayout) {
            val count = countOverMaxSize()
            if (count > 0) {
                val incapableDialog = IncapableDialog.newInstance("",
                        getString(R.string.error_over_original_count, count, mSpec!!.originalMaxSize))
                incapableDialog.show(supportFragmentManager,
                        IncapableDialog::class.java.name)
                return
            }
            mOriginalEnable = !mOriginalEnable
            mOriginal!!.setChecked(mOriginalEnable)
            if (mSpec!!.onCheckedListener != null) {
                mSpec!!.onCheckedListener.onCheck(mOriginalEnable)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        mAlbumCollection.setStateCurrentSelection(position)
        mAlbumsAdapter!!.cursor.moveToPosition(position)
        val album = Album.valueOf(mAlbumsAdapter!!.cursor)
        if (album.isAll && SelectionSpec.getInstance().capture) {
            album.addCaptureCount()
        }
        onAlbumSelected(album)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onAlbumLoad(cursor: Cursor) {
        mAlbumsAdapter!!.swapCursor(cursor)
        // select default album.
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            cursor.moveToPosition(mAlbumCollection.currentSelection)
            mAlbumsSpinner!!.setSelection(this@MatisseActivity,
                    mAlbumCollection.currentSelection)
            val album = Album.valueOf(cursor)
            if (album.isAll && SelectionSpec.getInstance().capture) {
                album.addCaptureCount()
            }
            onAlbumSelected(album)
        }
    }

    override fun onAlbumReset() {
        mAlbumsAdapter!!.swapCursor(null)
    }

    private fun onAlbumSelected(album: Album) {
        if (album.isAll && album.isEmpty) {
            mContainer!!.visibility = View.GONE
            mEmptyView!!.visibility = View.VISIBLE
        } else {
            mContainer!!.visibility = View.VISIBLE
            mEmptyView!!.visibility = View.GONE
            val fragment: Fragment = MediaSelectionFragment.newInstance(album)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, MediaSelectionFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    override fun onUpdate() {
        // notify bottom toolbar that check state changed.
        updateBottomToolbar()
        if (mSpec!!.onSelectedListener != null) {
            mSpec!!.onSelectedListener.onSelected(
                    mSelectedCollection.asListOfUri(), mSelectedCollection.asListOfString())
        }
    }

    override fun onMediaClick(album: Album, item: Item, adapterPosition: Int) {
        val intent = Intent(this, AlbumPreviewActivity::class.java)
        intent.putExtra(AlbumPreviewActivity.EXTRA_ALBUM, album)
        intent.putExtra(AlbumPreviewActivity.EXTRA_ITEM, item)
        intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection.dataWithBundle)
        intent.putExtra(BasePreviewActivity.EXTRA_RESULT_ORIGINAL_ENABLE, mOriginalEnable)
        startActivityForResult(intent, REQUEST_CODE_PREVIEW)
    }

    override fun provideSelectedItemCollection(): SelectedItemCollection {
        return mSelectedCollection
    }

    override fun capture() {
        if (mMediaStoreCompat != null) {
            mMediaStoreCompat!!.dispatchCaptureIntent(this, REQUEST_CODE_CAPTURE)
        }
    }

    companion object {
        const val EXTRA_RESULT_SELECTION = "extra_result_selection"
        const val EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path"
        const val EXTRA_RESULT_ORIGINAL_ENABLE = "extra_result_original_enable"
        private const val REQUEST_CODE_PREVIEW = 23
        private const val REQUEST_CODE_CAPTURE = 24
        const val CHECK_STATE = "checkState"
    }
}