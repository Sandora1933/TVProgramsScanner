package com.example.tvprogramsscanner.other

import com.example.tvprogramsscanner.service.RequestService
import java.io.IOException

class MyRunnable(var requestService: RequestService, var serialNumber: String,
    var borderId: Int, var direction: Int) : Runnable {

    var jsonResponseString: String? = null

    override fun run() {
        try {
            requestService.makeRequest(serialNumber, borderId, direction)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}