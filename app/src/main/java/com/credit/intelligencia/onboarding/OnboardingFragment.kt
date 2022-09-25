package com.credit.intelligencia.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.credit.intelligencia.R
import com.credit.intelligencia.data.IntroSlide
import com.credit.intelligencia.databinding.FragmentOnboardingBinding
import kotlinx.coroutines.launch




class OnboardingFragment : Fragment() {
    private var binding: FragmentOnboardingBinding? = null
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Mobile Loan",
                "Apply for a loan instantly",
                "bank.json"
            ),
            IntroSlide(
                "Fast Delivery",
                "Pata Mkopo kwa urahisi",
                "loann.json"
            ),
            IntroSlide(
                "Easy Payment",
                "No bank statement required",
                "financial.json"
            )
        )
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set the adapter to the viewpager2
        binding?.viewPager?.adapter = introSliderAdapter
        //sets the viewpager2 to the indicator
        binding?.indicator?.setViewPager(binding?.viewPager)

        binding?.viewPager?.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    if (position == introSliderAdapter.itemCount - 1) {
                        val animation = AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.app_name_animation
                        )
                        binding?.buttonNext?.animation = animation
                        binding?.buttonNext?.text = "Finish"
                        binding?.buttonNext?.setOnClickListener {
                            lifecycleScope.launch {
                                onBoardingFinished()
                            }
                            requireView().findNavController().navigate(R.id.toAuthFragment)
                        }
                    } else {
                        binding?.buttonNext?.text = "Next"
                        binding?.buttonNext?.setOnClickListener {
                            binding?.viewPager?.currentItem?.let {
                                binding?.viewPager?.setCurrentItem(it + 1, false)
                            }
                        }
                    }
                }
            })
    }
    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", false)
        editor.apply()
    }

}