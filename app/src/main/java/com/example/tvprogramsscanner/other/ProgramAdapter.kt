package com.example.tvprogramsscanner.other

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvprogramsscanner.R
import com.example.tvprogramsscanner.model.ProgramItem
import com.example.tvprogramsscanner.service.ILoadMore
import com.squareup.picasso.Picasso

class ProgramAdapter(recyclerView: RecyclerView, activity: Activity, private val list: MutableList<ProgramItem?>,
        val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM: Int = 0
    val VIEW_TYPE_LOAD: Int = 1

    var loadMore: ILoadMore? = null
    var isLoading: Boolean = false

    lateinit var activity: Activity
    lateinit var programs: MutableList<ProgramItem>

    val visibleThreshold: Int = 5
    var lastVisibleItem: Int = -1
    var totalCount: Int = -1

    init {

        this.activity = activity

        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalCount = layoutManager.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalCount <= (lastVisibleItem+visibleThreshold)){
                    if (loadMore != null){
                        loadMore!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.get(position) == null) VIEW_TYPE_LOAD else VIEW_TYPE_ITEM
    }

    fun setILoadMore(loadMore: ILoadMore){
        this.loadMore = loadMore
    }

    fun setLoaded(){
        isLoading = false
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    inner class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val channelImageView: ImageView = itemView.findViewById(R.id.logoImageView)
        val programTextView: TextView = itemView.findViewById(R.id.programTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM){
            val view: View = LayoutInflater.from(activity).inflate(R.layout.card_movie, parent, false)
            return ProgramViewHolder(view)
        }
        else {
            val view: View = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val programItem: ProgramItem? = list.get(position)

        if (holder is ProgramViewHolder){
            Picasso.get().load(Uri.parse(programItem?.imageUrl)).into(holder.channelImageView)
            holder.programTextView.text = programItem?.title
        }
        else if (holder is LoadingViewHolder){
            holder.progressBar.isIndeterminate = true
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

}