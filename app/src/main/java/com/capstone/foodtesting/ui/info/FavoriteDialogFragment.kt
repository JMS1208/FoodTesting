package com.capstone.foodtesting.ui.info

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.capstone.foodtesting.R
import com.capstone.foodtesting.databinding.DialogFragmentFavoriteBinding
import com.capstone.foodtesting.util.dialogFragmentResize
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.abs


class FavoriteDialogFragment(): DialogFragment() {

    private var _binding : DialogFragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return BottomSheetDialog(requireContext()
            , R.style.TransparentDialog
        ).apply {
            window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            behavior.state=BottomSheetBehavior.STATE_EXPANDED
            behavior.isFitToContents = false
        }
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 1f, 1f)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpFavorite.apply {
            val items = ArrayList<String>()
            for (i in 1 until 11){
                items.add("https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1587&q=80")
            }
            adapter = FavoriteAdapter(items)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer().apply{
                addTransformer(MarginPageTransformer(40))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.95f + r * 0.05f

                }
            }

            setPageTransformer(compositePageTransformer)

        }



    }


}