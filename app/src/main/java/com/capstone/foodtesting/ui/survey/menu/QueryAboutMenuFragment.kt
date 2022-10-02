package com.capstone.foodtesting.ui.survey.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.FragmentQueryAboutMenuBinding
import com.capstone.foodtesting.ui.survey.QueryAdapter
import com.capstone.foodtesting.ui.survey.survey.SurveyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QueryAboutMenuFragment : Fragment() {

    private var _binding : FragmentQueryAboutMenuBinding?= null
    private val binding get() = _binding!!

    private lateinit var queryAdapter: QueryAdapter

    private val viewModel by viewModels<QueryAboutMenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueryAboutMenuBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryAdapter = QueryAdapter()

        queryAdapter.setOnItemClickListener {
            (parentFragment as SurveyFragment).addQueryLine(it,listOf(),
                QueryLine.QueryType.type_menu
            )
        }

        binding.rvMenuQuery.apply {
            adapter = queryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.aboutMenuQueryList.observe(viewLifecycleOwner) {
            queryAdapter.submitList(it)
        }

        viewModel.fetchAboutMenuQueryList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}