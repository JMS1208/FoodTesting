package com.capstone.foodtesting.ui.survey.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentOverviewBinding
import com.capstone.foodtesting.ui.survey.survey.SurveyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivLogo.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.sticking_on_downward))

        binding.llContainer.setOnClickListener {
            when (count) {
                0 -> {
                    binding.tvAboutRestaurant.apply {
                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.sticking_on_right
                            )
                        )
                    }
                    binding.tvAboutRestaurantExplains.apply {
                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.sticking_on_right
                            )
                        )
                    }
                    binding.tvExplains.visibility = View.GONE

                }
                1 -> {
                    binding.tvAboutRestaurantExplains.apply {
                        visibility = View.GONE
                    }
                    binding.tvAboutMenu.apply {
                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.sticking_on_right
                            )
                        )
                    }
                    binding.tvAboutMenuExplains.apply {
                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.sticking_on_right
                            )
                        )
                    }



                }

                2 -> {
                    binding.tvAboutMenuExplains.apply {
                        visibility = View.GONE
                    }
                    binding.tvAddIndividually.apply{
                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.sticking_on_right
                            )
                        )
                    }
                    binding.tvAddIndividuallyExplains.apply {
                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.sticking_on_right
                            )
                        )
                    }
                }

                3 -> {
                    binding.tvAddIndividuallyExplains.apply {
                        visibility = View.GONE
                    }
                    (parentFragment as SurveyFragment).createBalloon("클릭!")

                }


                else -> Unit
            }

            count += 1

        }


    }
}