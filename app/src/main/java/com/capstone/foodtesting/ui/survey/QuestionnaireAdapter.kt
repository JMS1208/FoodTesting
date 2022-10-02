package com.capstone.foodtesting.ui.survey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.foodtesting.R
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.databinding.ItemQueryLineBinding
import com.capstone.foodtesting.ui.review.KeywordAdapter


class QuestionnaireAdapter: ListAdapter<QueryLine, QuestionnaireAdapter.ViewHolder>(queryLineDiffCallback) {

    private var onItemRemoveListener: ((QueryLine) -> Unit)? = null

    fun setOnItemRemoveListener(listener: (QueryLine) -> Unit) {
        onItemRemoveListener = listener
    }

    private var onItemModifyListener: ((QueryLine, View) -> Unit)? = null

    fun setOnItemModifyListener(listener: (QueryLine, View) -> Unit) {
        onItemModifyListener = listener
    }

    inner class ViewHolder(val itemBinding: ItemQueryLineBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var keywordAdapter: KeywordAdapter

        var queryLine: QueryLine? = null


        //type 1,2,3 순서대로 매장,메뉴,기타(사장님추가)
        fun bind(queryLine: QueryLine) {
            this.queryLine = queryLine
            when(queryLine.queryType) {
                QueryLine.QueryType.type_restaurant -> {
                    itemBinding.ivIcon.setImageResource(R.drawable.ic_restaurant)
                }
                QueryLine.QueryType.type_menu -> {
                    itemBinding.ivIcon.setImageResource(R.drawable.ic_cutlery)
                }
                QueryLine.QueryType.type_add-> {
                    itemBinding.ivIcon.setImageResource(R.drawable.ic_checklist)
                }
            }

            itemBinding.apply {
                tvQueryContents.text = "\" ${queryLine.query} \""
                keywordAdapter = KeywordAdapter()
                keywordAdapter.setChipCloseBtnEnabled(false)

                rvKeyword.apply {
                    adapter = keywordAdapter
                    layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
                }

                keywordAdapter.submitList(queryLine.keywords)

                tvRemove.setOnClickListener {
                    onItemRemoveListener?.let {
                        it(queryLine)
                        itemView.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.disapearing_on_left))

                    }
                }
                tvModify.setOnClickListener {
                    onItemModifyListener?.let {
                        it(queryLine, itemView)
                    }
                }
                swipeView.animate().translationX(0f).setDuration(100L).start()
            }

            itemView.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.sticking_on_downward))



        }
    }


    companion object {
        private val queryLineDiffCallback = object : DiffUtil.ItemCallback<QueryLine>() {
            override fun areItemsTheSame(oldItem: QueryLine, newItem: QueryLine): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: QueryLine, newItem: QueryLine): Boolean {
                return oldItem.query == newItem.query && oldItem.keywords == newItem.keywords
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireAdapter.ViewHolder {
        val itemBinding = ItemQueryLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: QuestionnaireAdapter.ViewHolder, position: Int) {
        val queryLine = currentList[position]
        holder.bind(queryLine)
    }


}