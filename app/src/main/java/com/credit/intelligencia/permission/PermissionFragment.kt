package com.credit.intelligencia.permission


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentPermissionBinding
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import com.credit.intelligencia.utils.Event
import com.credit.intelligencia.utils.EventObject
import com.credit.intelligencia.utils.SMS_PERMISSION_REQUEST
import com.ekn.gruzer.gaugelibrary.Range
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class PermissionFragment : Fragment(R.layout.fragment_permission) {
    private val viewModel: PermissionViewModel by viewModel()
    private val binding by viewBinding(FragmentPermissionBinding::bind)
    private val args: PermissionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSmsPermission()
        setUpObservers()
        onClick()
        getScoreLimit()
        //renderScoreLimit(700,1000)
//        showDataUi()
    }

    private fun getScoreLimit() {
        binding.loading.isVisible = true
        viewModel.getScoreLimitImmediately(args.email)
        onPermissionFinished()

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            SMS_PERMISSION_REQUEST -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                if (
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_PHONE_NUMBERS
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_CALL_LOG
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    EventObject.statusMessage.value =
                        Event("Permission Granted")
                }
            } else {
                EventObject.statusMessage.value =
                    Event("Permission Denied")
            }
        }
    }

    private fun getSmsPermission() {
        if (ContextCompat.checkSelfPermission
                (
                requireContext(),
                Manifest.permission.READ_SMS,
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission
                (
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission
                (
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission
                (
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG
                ),
                SMS_PERMISSION_REQUEST
            )

        } else {
            binding.apply {
                getEverything()
            }
        }

        EventObject.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { message ->
                if (message == "Permission Granted") {
                    binding.apply {
                        loading.isVisible = true
                        getEverything()
                        onPermissionFinished()

                    }

                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        })
    }




    private fun getEverything() {
        binding.loading.isVisible = true
        viewModel.getEverything(
            email = args.email
        )
        onPermissionFinished()
    }

    private fun setUpObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                PermissionUIState.Loading -> {
                    binding.loading.isVisible = false
                    onPermissionFinished()

                }  //renderLoading()
                is PermissionUIState.Error -> {
                    //renderError(errorTitle = it.title, errorMessage = it.message)
                }
                is PermissionUIState.LimitScore -> {
                    renderScoreLimit(score = it.score, limit = it.limits)
                    onPermissionFinished()

                }
                else -> {}
            }
        }

        viewModel.interactions.observeEvent(viewLifecycleOwner) {
            when (it) {
                is PermissionActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun onClick() {
        binding.apply {
            applyButton.setOnClickListener {
                viewModel.getScoreLimit(
                    email = args.email
                )

            }
        }
    }

    private fun renderScoreLimit(score: Int, limit: Int) {
        val range = Range()
        range.color = Color.parseColor("#ce0000")
        range.from = 0.0
        range.to = 333.0
        val range2 = Range()
        range2.color = Color.parseColor("#E3E500")
        range2.from = 333.0
        range2.to = 666.0
        val range3 = Range()
        range3.color = Color.parseColor("#00b20b")
        range3.from = 666.0
        range3.to = 1000.0
        binding.apply {
            halfGauge.addRange(range)
            halfGauge.addRange(range2)
            halfGauge.addRange(range3)

            //set min max and current value
            //set min max and current value
            halfGauge.minValue = 0.0
            halfGauge.maxValue = (1000).toDouble()
            halfGauge.value = (score).toDouble()

            halfGauge.setNeedleColor(Color.DKGRAY)
            halfGauge.valueColor = Color.BLUE
            halfGauge.minValueTextColor = Color.RED
            halfGauge.maxValueTextColor = Color.GREEN
            limitValue.text = limit.toString()

        }
    }

    private fun onPermissionFinished() {
        val sharedPref = requireActivity().getSharedPreferences("Permission", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}







