package com.capstone.foodtesting.ui.review

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.FragmentReviewBinding
import com.capstone.foodtesting.util.CommonFunc.showTooltip


class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var hashTagReviewAdapter: HashTagReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

//  private var hashTagList: List<String> = emptyList<String>()
    private var hashTagList: HashSet<String> = hashSetOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ratingBar.solidColor
        hashTagReviewAdapter = HashTagReviewAdapter()
        binding.rvHashTag.apply {
            adapter = hashTagReviewAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        binding.ivHashTagExplains.setOnClickListener {
            showTooltip(requireContext(), it,"#를 이용하여 해시태그를 추가할 수 있어요!")
        }

        binding.etReview.apply {
            setOnFocusChangeListener { _, hasFocused ->
                if(hasFocused) {
                    val x = binding.tvHashTagExplains.x
                    val y = binding.tvHashTagExplains.y+binding.tvHashTagExplains.height
                    binding.nestedScrollView.scrollTo(x.toInt(), y.toInt())

                }

            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val extractedHashTagList = hashSetOf<String>()

                    if (inputText?.contains("#") == true) {

                        try {

                            val tmpText= "$inputText "

                            tmpText.split(" ").filter {
                                it.contains("#") && it != "#"
                            }.map {
                                val selectedText = if(it.indexOf("#") != 0) {
                                    it.removeRange(0, it.indexOf("#"))
                                } else {
                                    it
                                }
                                val startIndex = tmpText.indexOf(selectedText)
                                val endIndex = startIndex + selectedText.length
                                val span: Spannable = binding.etReview.text as Spannable
                                span.setSpan(
                                    ForegroundColorSpan(resources.getColor(R.color.main_color_1)),
                                    startIndex,
                                    endIndex,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                extractedHashTagList.add(selectedText)

                            }


                        } catch (E: Exception) {
                            Log.d("TAG", "리뷰 EditText 예외 ${E.message}")

                        }

                    }

                    hashTagList = extractedHashTagList
                    hashTagReviewAdapter.submitList(hashTagList.toList())


                }

                override fun afterTextChanged(p0: Editable?) = Unit

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}