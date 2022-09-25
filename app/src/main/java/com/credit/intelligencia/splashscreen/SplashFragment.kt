package com.credit.intelligencia.splashscreen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentRegisterBinding
import com.credit.intelligencia.databinding.FragmentSplashBinding
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import kotlinx.coroutines.launch



class SplashFragment : Fragment(R.layout.fragment_splash) {
    lateinit var binding: FragmentSplashBinding
    private val handler = Handler()
    private val runnable = Runnable {
        lifecycleScope.launch {
            if (onBoardingFinished()) {
                requireView().findNavController().navigate(R.id.toPermissionFragment)
            } else {
                requireView().findNavController().navigate(R.id.toOnboardingFragment)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,3000)
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onDetach() {
        super.onDetach()
        handler.removeCallbacks(runnable)
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}

