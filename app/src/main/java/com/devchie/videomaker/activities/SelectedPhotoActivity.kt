package com.devchie.videomaker.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.devchie.photoeditor.activity.EditPhotoActivity
import com.devchie.videomaker.R
import com.devchie.videomaker.adaper.PhotoAdapter
import com.devchie.videomaker.ads.AdmobAds
import com.devchie.videomaker.ads.FacebookAds
import com.devchie.videomaker.listener.ItemClickListener
import com.devchie.videomaker.model.Photo
import com.devchie.videomaker.view.RoundRectView
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.matisse.Matisse
import com.matisse.MimeType
import com.matisse.engine.impl.GlideEngine

import java.io.File
import java.util.*

class SelectedPhotoActivity : BaseSplitActivity() {
    private var btnAddMore: FloatingActionButton? = null
    private var btnAddPhoto: LinearLayout? = null
    private var btnEditPhoto: FloatingActionButton? = null
    private var btnMovie: ImageView? = null
    private var countAd = 1
    private var dragMgr: RecyclerViewDragDropManager? = null
    private val drawerLayout: DrawerLayout? = null
    var imgPhoto: ImageView? = null
    var listPhoto = ArrayList<Photo>()
    private val llFacebook: LinearLayout? = null
    private val llGmail: LinearLayout? = null
    private var llHolderRecyclerView: RoundRectView? = null
    private val llPolicy: LinearLayout? = null
    private val llRate: LinearLayout? = null
    private val llshare: LinearLayout? = null
    private val mAdview: AdView? = null
    private val maxImgCount = 30
    private val navigationView: NavigationView? = null
    var pathSelected: String? = null
    private var photo1: Photo? = null
    var photoAdapter: PhotoAdapter? = null
    private var photos: ArrayList<String>? = null
    var photoselected: Photo? = null
    var positionSelected = 0
    private var progressDialog: ProgressDialog? = null
    private var recyclerPhoto: RecyclerView? = null
    private var selectedPhotos: ArrayList<String>? = ArrayList()
    private val sendPhotos = ArrayList<String>()
    private var uriSelected: Uri? = null
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_selected_photo)
        Thread.setDefaultUncaughtExceptionHandler { thread, th -> Log.e("EXCEPTION_IN_THREAD", thread.name + " : " + th.message) }
        addControls()
        addRecyclerView()
        addPhoto()
        FacebookAds.loadBanner(this)
        AdmobAds.loadBanner(this)
    }

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
                val selectedPhotoActivity = this@SelectedPhotoActivity
                selectedPhotoActivity.photoselected = selectedPhotoActivity.listPhoto[i]
                val selectedPhotoActivity2 = this@SelectedPhotoActivity
                selectedPhotoActivity2.pathSelected = selectedPhotoActivity2.photoselected!!.paths
                val requestOptions = RequestOptions()
                requestOptions.centerCrop().placeholder(R.drawable.__picker_ic_photo_black_48dp).error(R.drawable.__picker_ic_broken_image_black_48dp)
                Glide.with(this@SelectedPhotoActivity).load(photoselected!!.paths).apply((requestOptions as BaseRequestOptions<*>)).thumbnail(0.1f).into(imgPhoto!!)
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

    private fun addControls() {
        llHolderRecyclerView = findViewById<View>(R.id.llRecyclerView) as RoundRectView
        btnEditPhoto = findViewById<View>(R.id.btnEditPhoto) as FloatingActionButton
        btnMovie = findViewById<ImageView>(R.id.movie_add_float) as ImageView
        btnAddMore = findViewById<View>(R.id.photo_add_float) as FloatingActionButton
        btnAddPhoto = findViewById<View>(R.id.movie_add) as LinearLayout
        imgPhoto = findViewById<View>(R.id.imageViewPhoto) as ImageView

        findViewById<FloatingActionButton>(R.id.btnEditPhoto).setOnClickListener {
            gotoPhotoEditor()
        }


        findViewById<FloatingActionButton>(R.id.photo_add_float).setOnClickListener {
            addPhoto()

        }


        findViewById<LinearLayout>(R.id.movie_add).setOnClickListener {
            addPhoto()
        }


        findViewById<ImageView>(R.id.movie_add_float).setOnClickListener {
            val progressDialog2 = ProgressDialog(this)
            progressDialog = progressDialog2
            progressDialog2.setMessage(getString(R.string.com_facebook_loading))
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
            countAd++
            photoAdapter!!.notifyDataSetChanged()
            sendPhotos.clear()
            var i = 0
            while (i < listPhoto.size) {
                sendPhotos.add(listPhoto[i].paths)
                i++
            }
            createMovie()
        }

    }

    @SuppressLint("RestrictedApi")
    public override fun onActivityResult(i: Int, i2: Int, intent: Intent?) {
        super.onActivityResult(i, i2, intent)
        if (i2 == -1 && intent != null && i == 100) {
            selectedPhotos!!.clear()
            val arrayList: ArrayList<*> = Matisse.obtainPathResult(intent) as ArrayList<*>
            selectedPhotos = (arrayList) as ArrayList<String>?
            if (arrayList != null && !arrayList.isEmpty()) {
                if (intent != null) {
                    btnAddPhoto!!.visibility = View.GONE
                    llHolderRecyclerView!!.visibility = View.VISIBLE
                    btnAddMore!!.visibility = View.VISIBLE
                    btnEditPhoto!!.visibility = View.VISIBLE
                }
                for (i3 in selectedPhotos!!.indices) {
                    listPhoto.add(Photo(i3.toLong(), selectedPhotos!![i3]))
                }
                photo1 = listPhoto[0]
                uriSelected = Uri.fromFile(File(photo1!!.paths))
                val requestOptions = RequestOptions()
                requestOptions.centerCrop().placeholder(R.drawable.__picker_ic_photo_black_48dp).error(R.drawable.__picker_ic_broken_image_black_48dp)
                Glide.with((this as FragmentActivity)).load(uriSelected).apply((requestOptions as BaseRequestOptions<*>)).thumbnail(0.1f).into(imgPhoto!!)
                photoAdapter!!.notifyDataSetChanged()
            }
        }
        if (i == 113 && i2 == 115) {
            photos = intent!!.getStringArrayListExtra("AFTER")
            selectedPhotos!!.clear()
            sendPhotos.clear()
            listPhoto.clear()
            val arrayList2 = photos
            if (arrayList2 != null) {
                selectedPhotos!!.addAll(arrayList2)
                for (i4 in selectedPhotos!!.indices) {
                    listPhoto.add(Photo(i4.toLong(), selectedPhotos!![i4]))
                }
            }
            photo1 = listPhoto[0]
            uriSelected = Uri.fromFile(File(photo1!!.paths))
            val requestOptions2 = RequestOptions()
            requestOptions2.centerCrop().placeholder(R.drawable.__picker_ic_photo_black_48dp).error(R.drawable.__picker_ic_broken_image_black_48dp)
            Glide.with((this as FragmentActivity)).load(uriSelected).apply((requestOptions2 as BaseRequestOptions<*>)).thumbnail(0.1f).into(imgPhoto!!)
        }
    }


    private fun createMovie() {
        photoAdapter!!.notifyDataSetChanged()
        if (sendPhotos.size < 3) {
            val progressDialog2 = progressDialog
            if (progressDialog2 != null && progressDialog2.isShowing) {
                progressDialog!!.dismiss()
            }
            Toast.makeText(this, getString(R.string.more_than_3_photos), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, MovieActivity::class.java)
        val bundle = Bundle()
        bundle.putStringArrayList("PHOTO", sendPhotos)
        intent.putExtras(bundle)
        startActivity(intent)
        if (!FacebookAds.showFullAds(null)) {
            AdmobAds.showFullAds(null)
        }
    }
    private var selectedUriList: List<Uri>? = null

    private fun addPhoto() {

        Matisse.from(this as Activity).choose(MimeType.ofImage(), false).showSingleMediaType(true).theme(R.style.Matisse_Dracula).countable(true).maxSelectable(30).restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f).imageEngine(GlideEngine()).showPreview(false).forResult(100)
    }

    public override fun onPostResume() {
        super.onPostResume()
    }

    public override fun onDestroy() {
        super.onDestroy()
        Runtime.getRuntime().gc()
    }

    public override fun onStop() {
        val progressDialog2 = progressDialog
        if (progressDialog2 != null && progressDialog2.isShowing) {
            progressDialog!!.dismiss()
        }
        super.onStop()
    }

    private fun gotoPhotoEditor() {
        val intent = Intent(this@SelectedPhotoActivity, EditPhotoActivity::class.java)
        val arrayList: ArrayList<String> = ArrayList<String>()
        for (i in listPhoto.indices) {
            arrayList.add(listPhoto[i].paths)
        }
        intent.putStringArrayListExtra("PHOTO", arrayList as ArrayList<String>?)
        intent.putExtra("POSITION", positionSelected)
        if (positionSelected < arrayList.size) {
            startActivityForResult(intent, 113)
        }
    }

    companion object {
        const val REQUEST_CODE_SELECT = 100
    }
}