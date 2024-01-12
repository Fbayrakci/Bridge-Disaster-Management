package com.example.bridge_authorized

import SessionManager
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bridge_authorized.databinding.ActivityAutAddAnnoucmentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class aut_add_annoucments : AppCompatActivity() {
    private lateinit var binding: ActivityAutAddAnnoucmentsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var sessionManager: SessionManager

    var selectedPicture: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutAddAnnoucmentsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Initialize Firebase and other components
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        sessionManager = SessionManager.getInstance(applicationContext)

        registerLauncher()

        // Set up listeners and handlers for UI elements
        binding.addAnnouncementButton.setOnClickListener { uploadAnnouncement() }





    }

    private fun uploadAnnouncement() {
        if (selectedPicture != null) {
            val uuid = UUID.randomUUID()
            val imageName = "$uuid.jpg"
            val reference = storage.reference
            val imageReference = reference.child("annoucmentimages").child(imageName)

            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("annoucmentimages").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    if (sessionManager.isLoggedIn) {
                        val userId = sessionManager.loggedInUserId
                        firestore.collection("authorized").document(userId!!).get().addOnSuccessListener { document ->
                            if (document != null) {
                                val autEmail = document.getString("autemail") ?: ""

                                val postMap = hashMapOf<String, Any>()
                                postMap["downloadUrl"] = downloadUrl
                                postMap["autEmail"] = autEmail
                                postMap["comment"] = binding.announcementText.text.toString()
                                postMap["date"] = Timestamp.now()

                                firestore.collection("annoucments").add(postMap).addOnSuccessListener {
                                    Toast.makeText(this@aut_add_annoucments, "Success Upload", Toast.LENGTH_LONG).show()
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(this@aut_add_annoucments, it.localizedMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
//    fun selectImgClicked(view: View){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
//                    // request permission
//                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                }.show()
//            } else {
//                // request permission
//                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//            }
//        } else {
//            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            activityResultLauncher.launch(intentToGallery)
//        }
//    }

    fun selectImgClicked(view: View){
        if(ContextCompat.checkSelfPermission(this@aut_add_annoucments,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@aut_add_annoucments,Manifest.permission.READ_MEDIA_IMAGES)){
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //activity for result
            activityResultLauncher.launch(intentToGallery)
        }

    }


    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let { url ->
                        binding.selectedImage.setImageURI(url)
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(this@aut_add_annoucments,"Permission needed!",Toast.LENGTH_LONG).show()
            }

        }
    }
}
