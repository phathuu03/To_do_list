package com.example.myapplication.bottomsheet

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.BottomSheetAttachmentBinding
import com.example.myapplication.model.AttachmentNote
import com.example.myapplication.utils.enums.MediaType
import com.example.myapplication.viewmodel.AttachmentNoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

private var isPermissionImage = false
private var isPermissionVideo = false

class ChooseAttachmentBottomSheetFragment(private val viewModel: AttachmentNoteViewModel)  : BottomSheetDialogFragment() {

    private val binding by lazy {
        BottomSheetAttachmentBinding.inflate(layoutInflater)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutChooseImage.setOnClickListener {
            checkAndRequestPermission()
            if(isPermissionImage){
                openImagePicker()
            }
        }
        binding.layoutChooseVideo.setOnClickListener {
            openVideoPicker()
        }


    }

    private fun checkAndRequestPermission() {
            val permissionListener = object :PermissionListener{
                override fun onPermissionGranted() {
                    isPermissionImage = true
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    isPermissionImage = false
                }


            }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.CAMERA)
            .check();
    }


    private fun openImagePicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun openVideoPicker(){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
    }



    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val typeMedia = MediaType.IMAGE
            viewModel.addAttachment(AttachmentNote(uri=uri , type = typeMedia))

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }




}