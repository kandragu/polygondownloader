package com.theeasiestway.net

import java.io.BufferedInputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class NetworkService {

    private lateinit var connection: HttpURLConnection
    private var connTimeout: Int = 0
    private var readTimeout: Int = 0

    fun setConnTimeout(timeout: Int) : NetworkService {
        connTimeout = if (timeout < 0) 0 else timeout
        return this
    }

    fun setReadTimeout(timeout: Int) : NetworkService {
        readTimeout = if (timeout < 0) 0 else timeout
        return this
    }

    fun request(urlString: String) : BufferedInputStream {
        try {
            var url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = connTimeout
            connection.readTimeout = readTimeout
            var responceCode = connection.responseCode
            if (responceCode == HttpURLConnection.HTTP_OK) return BufferedInputStream(connection.inputStream)
            else throw ResponseServerException("response code: $responceCode")
        } catch (e: Exception) {
            disconnect()
            throw e
        }
    }

    fun disconnect() {
        if (this::connection.isInitialized) connection.disconnect()
    }
}