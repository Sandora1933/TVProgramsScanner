package com.example.tvprogramsscanner.service

import android.app.Activity
import com.example.tvprogramsscanner.other.ProgramAdapter
import com.example.tvprogramsscanner.model.JsonResponse
import com.example.tvprogramsscanner.model.ProgramItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.IOException

class RequestService(var activity: Activity, var retrofit: Retrofit,
    val list: MutableList<ProgramItem?>, val adapter: ProgramAdapter
) {

    @Throws(IOException::class)
    fun makeRequest(serialNumber: String, borderId: Int?, direction: Int) {

        val currentUrl: String = "https://oll.tv/api/demo?" + "serial_number=1" +
                "&" + "borderId=$borderId" + "&" + "direction=$direction"

        val api = retrofit.create(OllTvApi::class.java)
        val call = api.getJsonResponse(currentUrl)
        val response = call.execute()

        val jsonResponseRaw: JsonResponse = response.body()!!

        var newItems: Array<ProgramItem> = jsonResponseRaw.items

        activity.runOnUiThread {
            newItems.forEachIndexed {
                    index, programItem -> if (index != 0) list.add(programItem) }

            adapter.notifyDataSetChanged()
        }

    }

    // Retrofit interface
    internal interface OllTvApi {
        @GET
        fun getJsonResponse(@Url url: String): Call<JsonResponse?>
    }

}