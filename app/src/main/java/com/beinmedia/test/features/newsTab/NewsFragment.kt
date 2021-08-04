package com.beinmedia.test.features.newsTab

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.beinmedia.test.R
import com.beinmedia.test.base.BaseFragment
import com.beinmedia.test.databinding.FragmentNewsBinding
import com.beinmedia.test.models.network.control.Resource
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.beinmedia.test.dependencyinjection.module.GlideApp
import com.beinmedia.test.models.local.models.NewsDB
import com.beinmedia.test.models.network.pojo.home.News
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewsFragment :BaseFragment() {

    private val viewModel: NewsViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsStaggerAdapter: NewsStaggerAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private var isGridAdapter = true
    private lateinit var currentNewsPhotoPath: String
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                getNewsImage()
            }
        }
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                showFillFeedDataBottomSheetDialog()
//            }
        }
    private lateinit var newsImageView: ImageView
    val timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater,container,false)
        binding.newsViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        initViewModel()
        viewModel.getNews("kuwait")
        create10MinutesTask()
    }
    private var firstTime=true
    private fun create10MinutesTask() {
        val scheduledTask: TimerTask = object : TimerTask() {
            override fun run() {
                if(!firstTime) {
                    firstTime = false
                    requireActivity().runOnUiThread {
                        if (newsAdapter.dataSet.size > 10) {
                            newsAdapter.dataSet = newsAdapter.dataSet.take(10) as ArrayList<News>
                            requireActivity().runOnUiThread {
                                newsAdapter.notifyDataSetChanged()
                            }
                        }
                        if (newsStaggerAdapter.dataSet.size > 10) {
                            newsStaggerAdapter.dataSet =
                                newsStaggerAdapter.dataSet.take(10) as ArrayList<News>

                            newsStaggerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
        timer.schedule(scheduledTask, 0L, 600000L)
    }

    override fun onDetach() {
        super.onDetach()
        if(timer!=null){
            timer.cancel()
        }
    }

    private fun setUpViews() {
        newsStaggerAdapter = NewsStaggerAdapter(){ news,index,image ->
            if(image != null){
                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                val uri = saveImage(image)
                share.putExtra(Intent.EXTRA_STREAM, uri)
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(share, "Select"))
            } else {
                showYesNoDialog(news,index)
            }
        }
        newsAdapter = NewsAdapter(){ news,index,image ->
            if(image != null){
                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                val uri = saveImage(image)
                share.putExtra(Intent.EXTRA_STREAM, uri)
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(share, "Select"))
            } else {
                showYesNoDialog(news,index)
            }
        }
        gridLayoutManager = GridLayoutManager(activity,2)
        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.newsList.layoutManager = gridLayoutManager
        binding.newsList.adapter = newsAdapter
        binding.newsList.orientation = DragDropSwipeRecyclerView.ListOrientation.GRID_LIST_WITH_HORIZONTAL_SWIPING
        binding.newsList.numOfRowsPerColumnInGridList = 2
        binding.newsList.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
        binding.newsList.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.LEFT)

        binding.fab.setOnClickListener {
            toggleAdapter()
        }
        binding.cameraIcon.setOnClickListener {
            openAddNewsDialog()
        }
        binding.addImageTextView.setOnClickListener {
            openAddNewsDialog()
        }
    }

    private fun openAddNewsDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_news)
        val titleEditText = dialog.findViewById(R.id.title_edit_text) as EditText
        val descEditText = dialog.findViewById(R.id.description_edit_text) as EditText
        newsImageView = dialog.findViewById(R.id.add_image_image_view) as ImageView
        newsImageView.setOnClickListener {
            getNewsImage()
        }
        titleEditText.setText("Dummy title")
        descEditText.setText("Dummy Description")
        val saveButton = dialog.findViewById(R.id.save_button) as Button
        val cancelButton = dialog.findViewById(R.id.cancel_add_news_button) as Button
        saveButton.setOnClickListener {
            if(titleEditText.text?.isNotEmpty() == true &&
                descEditText.text?.isNotEmpty() == true && currentNewsPhotoPath.isNotEmpty()
            ) {
                val uuid = UUID.randomUUID()
                val url = uuid.toString()
                val newsDb = NewsDB(
                    title = titleEditText.text.toString(),
                    description = descEditText.text.toString(),
                    content = "dummy content",
                    deleteDate = "",
                    image = currentNewsPhotoPath,
                    isDeleted = false,
                    publishedAt = "",
                    url = url
                )
                val news = News(
                    title = titleEditText.text.toString(),
                    description = descEditText.text.toString(),
                    content = "dummy content",
                    image = currentNewsPhotoPath,

                    publishedAt = "",
                    url = url
                )
                newsAdapter.addItem(news)
                newsStaggerAdapter.addItem(news)
                viewModel.insertNewsItem(newsDb)
                createLocalNotification(titleEditText?.text.toString(), descEditText?.text.toString())
                dialog.dismiss()
            } else {
                Toast.makeText(activity,getString(R.string.required_error),Toast.LENGTH_SHORT).show()
            }
        }
        cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun getNewsImage() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        } else {
            // request a permission
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            try {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.beinmedia.test.utils.ExtendFileProvider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(), getString((R.string.application_not_found)), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentNewsPhotoPath = absolutePath
            GlideApp.with(requireActivity())
                .load(currentNewsPhotoPath)
                .into(newsImageView)
        }
    }



    private fun saveImage(image: Bitmap): Uri? {
        val imagesFolder = File(requireActivity().cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "news_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(
                requireContext(),
                "com.beinmedia.test.utils.ExtendFileProvider",
                file
            )
        } catch (e: IOException) {
            Log.d(
                "NewsFragment",
                "IOException while trying to write file for sharing: " + e.message
            )
        }
        return uri
    }

    private fun initViewModel() {
        showLoading()
        viewModel.newsMutableLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    binding.searchIcon.visibility = View.VISIBLE
                    newsAdapter.dataSet = it.data?.articles!!.filter { news -> news.content!="ar" }
                    newsStaggerAdapter.dataSet = it.data.articles.filter { news -> news.content!="ar" }
                    newsAdapter.notifyItemRangeInserted(0,newsAdapter.dataSet.size)
                    newsStaggerAdapter.notifyItemRangeInserted(0,newsStaggerAdapter.dataSet.size)
                    dismissLoading()
                    if (newsAdapter.dataSet.isEmpty()){
                        binding.noDataView.visibility = VISIBLE
                        binding.newsList.visibility = INVISIBLE
                        binding.fab.visibility = INVISIBLE
                    }
                }
                else -> {
                    Toast.makeText(activity,getString(R.string.network_error),Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    binding.searchIcon.visibility = View.VISIBLE
                }
            }
        })
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // load feed depending on query value
                binding.searchProgressBar.visibility = View.VISIBLE
                binding.searchIcon.visibility = View.GONE
                viewModel.getNews(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun dismissLoading() {
        binding.newsList.visibility = VISIBLE
        binding.fab.visibility = VISIBLE
        binding.newsProgress.visibility = INVISIBLE
    }

    private fun showLoading() {
        binding.newsList.visibility = INVISIBLE
        binding.fab.visibility = INVISIBLE
        binding.newsProgress.visibility = VISIBLE
    }

    private fun toggleAdapter(){
        isGridAdapter = !isGridAdapter
        if(isGridAdapter){
            binding.newsList.layoutManager = gridLayoutManager
            binding.newsList.adapter = newsAdapter
            newsAdapter.notifyDataSetChanged()
            binding.fab.setImageResource(R.drawable.ic_baseline_dynamic_feed_24)
        } else {
            binding.newsList.layoutManager = staggeredLayoutManager
            binding.newsList.adapter = newsStaggerAdapter
            newsStaggerAdapter.notifyDataSetChanged()
            binding.fab.setImageResource(R.drawable.ic_baseline_normal_feed_24)
        }
    }

    private fun showYesNoDialog(news: News,index: Int){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.dialog_yes_no)
        val yesButton = bottomSheetDialog.findViewById<TextView>(R.id.delete_btn)
        val noButton = bottomSheetDialog.findViewById<TextView>(R.id.cancel_btn)
        bottomSheetDialog.show()
        yesButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
            viewModel.moveItemToTrash(news.url)
            newsAdapter.removeItem(index)
            newsStaggerAdapter.removeItem(index)
            if (newsAdapter.dataSet.isEmpty())
                binding.noDataView.visibility = View.VISIBLE
        }
        noButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    private fun createLocalNotification(title: String, desc: String) {
        val builder = NotificationCompat.Builder(requireContext(), getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(Random().nextInt(1000), builder.build())
        }
    }
}