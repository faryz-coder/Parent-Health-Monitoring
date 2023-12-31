package com.example.boilerplate.main.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.boilerplate.databinding.FragmentProfileBinding
import com.example.boilerplate.main.MainViewModel
import com.example.boilerplate.manager.FirestoreManager
import com.example.boilerplate.model.UserInfo
import com.example.boilerplate.utils.UtilsInterface
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(), View.OnClickListener, UtilsInterface {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var mainViewModel: MainViewModel
    private val binding get() = _binding!!
    private var updateImg: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.profileImage.setImageURI(uri)
                    updateImg = uri
                }
            }

        binding.btnSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnSelectImage.isEnabled = binding.inputFullName.isEnabled

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayInfo()

        binding.btnUpdateProfile.setOnClickListener(this)
        binding.profileLayout.setOnClickListener(this)
    }

    private fun displayInfo() {
        val info = mainViewModel.userInfo.value

        binding.inputFullName.editText?.setText(info?.fullName)
        binding.inputEmail.editText?.setText(info?.email)
        binding.inputPhoneNumber.editText?.setText(info?.phoneNumber)
        binding.inputAddress.editText?.setText(info?.address)
        binding.inputAbout.editText?.setText(info?.about)
        binding.inputHeight.editText?.setText(info?.height)
        binding.inputWeight.editText?.setText(info?.weight)

        info?.userImage?.isNotEmpty()?.let {
            if (it) {
                Picasso.get().load(info.userImage).into(binding.profileImage)
            }
        }
    }

    private fun initUpdateProfile() {
        if (binding.inputFullName.isEnabled) {
            //Update profile
            updateProfile()
        }

        binding.inputFullName.isEnabled = !binding.inputFullName.isEnabled
        binding.inputPhoneNumber.isEnabled = binding.inputFullName.isEnabled
        binding.inputAddress.isEnabled = binding.inputFullName.isEnabled
        binding.inputAbout.isEnabled = binding.inputFullName.isEnabled
        binding.inputWeight.isEnabled = binding.inputFullName.isEnabled
        binding.inputHeight.isEnabled = binding.inputFullName.isEnabled
        binding.btnSelectImage.isEnabled = binding.inputFullName.isEnabled
    }

    private fun updateProfile() {
        if (validate()) {
            val updatedInfo = UserInfo(
                fullName = binding.inputFullName.editText?.text.toString(),
                email = "",
                phoneNumber = binding.inputPhoneNumber.editText?.text.toString(),
                gender = "",
                dateOfBirth = "",
                height = if (binding.inputHeight.editText?.text.toString().isEmpty()) {
                    "0"
                } else {
                    binding.inputHeight.editText?.text.toString()
                },
                weight = if (binding.inputWeight.editText?.text.toString().isEmpty()) {
                    "0"
                } else {
                    binding.inputWeight.editText?.text.toString()
                },
                userImage = "",
                address = binding.inputAddress.editText?.text.toString(),
                about = binding.inputAbout.editText?.text.toString(),
            )
            FirestoreManager().updateUserInfo(updateImg, updatedInfo) { mainViewModel.refresh() }
        }
    }

    private fun validate(): Boolean {
        val info = mainViewModel.userInfo.value!!

        return binding.inputFullName.editText?.text.toString() != info.fullName || binding.inputPhoneNumber.editText?.text.toString() != info.phoneNumber
                || binding.inputAddress.editText?.text.toString() != info.address || binding.inputAbout.editText?.text.toString() != info.address
                || binding.inputWeight.editText?.text.toString() != info.weight || binding.inputHeight.editText?.text.toString() != info.height || updateImg != null
    }

    override fun onClick(btn: View) {
        when (btn.id) {
            binding.btnUpdateProfile.id -> initUpdateProfile()
            binding.profileLayout.id -> hideKeyboard(requireActivity(), requireView().findFocus())
        }
    }
}