package com.capstone.foodtesting.ui.survey.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.FragmentQueryAboutRestaurantBinding
import com.capstone.foodtesting.ui.survey.QueryAdapter
import com.capstone.foodtesting.ui.survey.survey.SurveyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QueryAboutRestaurantFragment : Fragment() {

    private var _binding : FragmentQueryAboutRestaurantBinding? = null
    private val binding get() = _binding!!

    private lateinit var queryAdapter: QueryAdapter

    private val viewModel by viewModels<QueryAboutRestaurantViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueryAboutRestaurantBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryAdapter = QueryAdapter()

        queryAdapter.setOnItemClickListener {
            (parentFragment as SurveyFragment).addQueryLine(it,listOf(),
                QueryLine.QueryType.type_restaurant
            )
        }

        binding.rvRestaurantQuery.apply {
            adapter = queryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.aboutRestaurantQueryList.observe(viewLifecycleOwner) {
            queryAdapter.submitList(it)
        }

        viewModel.fetchAboutRestaurantQueryList()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}