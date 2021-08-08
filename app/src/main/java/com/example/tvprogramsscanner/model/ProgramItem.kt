package com.example.tvprogramsscanner.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProgramItem(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("icon")
    @Expose
    var imageUrl: String,
    @SerializedName("name")
    @Expose
    var title: String,
    @SerializedName("description")
    @Expose
    var description: String
    )