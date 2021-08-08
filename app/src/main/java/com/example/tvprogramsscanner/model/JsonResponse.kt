package com.example.tvprogramsscanner.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JsonResponse(
    @SerializedName("block_id")
    @Expose
    var block_id: String,
    @Expose var block_title: String,
    @Expose var block_type: String,
    @Expose var category_name: String,
    @Expose var first_now_index: Int,
    @Expose var hasMore: Int,
    @Expose var items: Array<ProgramItem>,
    @Expose var items_number: Int,
    @Expose var offset: Int,
    @Expose var total: Int
)