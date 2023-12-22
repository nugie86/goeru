package com.tegas.mygoeruapps.ui.teacher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.data.getImageUri
import com.tegas.mygoeruapps.data.reduceFileImage
import com.tegas.mygoeruapps.data.uriToFile
import com.tegas.mygoeruapps.databinding.ActivityTeacherBinding
import com.tegas.mygoeruapps.ui.splash.WelcomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding
    private val viewModel by viewModels<TeacherViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSession()

        if (!allPermissionGranted()) {
            requestPermission.launch(REQUIRED_PERMISSION)
        }

        val launchCameraIntent = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isSuccess ->
            if (isSuccess) {
                showImage()
            }
        }

        postImage()
        postDescription()

        binding.cameraButton.setOnClickListener {
            currentImageUri = getImageUri(this)
            launchCameraIntent.launch(currentImageUri)
        }

        binding.galleryButton.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.descriptionButton.setOnClickListener {
            var token: String
            val desc = binding.edtDescription.text.toString()

            if (desc.isNotEmpty()) {
                showProgressIndicator(true)

                viewModel.getSession().observe(this) { user ->
                    token = user.token
                    viewModel.postDescription(token, desc)
                }
            } else {
                showToast("Fill the description")
            }
        }
        binding.imageBtn.setOnClickListener {
            var token: String
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()

                val imageName = imageFile.name
                val imageSize = imageFile.length() / (1024 * 1024) // in MB

                Log.d("ImageInfo", "Selected Image: Name - $imageName, Size - $imageSize MB")

                showProgressIndicator(true)

                val requestImageFile = imageFile.asRequestBody("image/*".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile
                )
                viewModel.getSession().observe(this) { user ->
                    token = user.token
                    viewModel.postImage(token, multipartBody)
                }
            } ?: showToast(getString(R.string.empty_image))
        }

        binding.btnLogout.setOnClickListener {
            setLogout()
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun postDescription() {
        viewModel.descriptionResponse.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showProgressIndicator(true)
                    disableInterface()
                }

                is Result.Error -> {
                    showProgressIndicator(false)
                    enableInterface()
                    Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                    Log.d("Desc", "${it.error}")
                }

                is Result.Success -> {
                    showProgressIndicator(false)
                    enableInterface()
                    Toast.makeText(this, "Your description has been updated", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun postImage() {
        viewModel.postResponse.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showProgressIndicator(true)
                    disableInterface()
                }

                is Result.Error -> {
                    showProgressIndicator(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    enableInterface()
                }

                is Result.Success -> {
                    showProgressIndicator(false)
                    enableInterface()
                    Toast.makeText(this, "Your photo has been updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun disableInterface() {
        binding.cameraButton.isEnabled = false
        binding.galleryButton.isEnabled = false
        binding.imageBtn.isEnabled = false
        binding.edtDescription.isEnabled = false
        binding.descriptionButton.isEnabled = false
    }

    private fun enableInterface() {
        binding.cameraButton.isEnabled = true
        binding.galleryButton.isEnabled = true
        binding.imageBtn.isEnabled = true
        binding.edtDescription.isEnabled = true
        binding.descriptionButton.isEnabled = true
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        when (isGranted) {
            true -> {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            }

            false -> {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this, REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "show Image:$it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showProgressIndicator(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private fun getSession() {
        showLoading(true)
        viewModel.getSession().observe(this) { user ->
            val token = user.token
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

            val userId = user.id

            binding.btnProfile.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("id", userId)
                startActivity(intent)
            }
        }
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            when (isLoading) {
                true -> View.VISIBLE
                else -> View.GONE
            }
    }

    private fun setLogout() {
        viewModel.logout()
        startActivity(Intent(this, WelcomeActivity::class.java))
    }

}