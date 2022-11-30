package com.capstone.foodtesting.ui.restaurant.questionnaire.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.FragmentRestaurantQueryRegisterAboutRestaurantBinding
import com.capstone.foodtesting.ui.restaurant.questionnaire.QueryAdapter
import com.capstone.foodtesting.ui.restaurant.questionnaire.RestaurantQueryRegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantQueryRegisterAboutRestaurantFragment : Fragment() {

    private var _binding : FragmentRestaurantQueryRegisterAboutRestaurantBinding? = null
    private val binding get() = _binding!!

    private lateinit var queryAdapter: QueryAdapter

    private val viewModel by viewModels<RestaurantQueryRegisterAboutRestaurantViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantQueryRegisterAboutRestaurantBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryAdapter = QueryAdapter()

        queryAdapter.setOnItemClickListener {
            (parentFragment as RestaurantQueryRegisterFragment).addQueryLine(it)
        }

        binding.rvRestaurantQuery.apply {
            adapter = queryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.aboutRestaurantQueryList.observe(viewLifecycleOwner) { queryList->
            queryList?.let {
                queryAdapter.submitList(it)
            }

        }

        viewModel.fetchAboutRestaurantQueryList(QueryLine.TypeRestaurant)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}