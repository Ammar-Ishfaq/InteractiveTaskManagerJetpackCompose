package com.m.ammar.itaskmanager.data.providers

import com.m.ammar.itaskmanager.data.local.model.RandomQuote
import com.m.ammar.itaskmanager.utility.Constants.API_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put


abstract class BaseApiService(
    protected val httpClient: HttpClient,
    protected val baseUrl: String,
) {
    protected suspend inline fun <reified T> get(
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return httpClient.get(urlString = baseUrl + endpoint, block = block).body()
    }

    protected suspend inline fun <reified T> post(
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit
    ): T {
        return httpClient.post(urlString = baseUrl + endpoint, block = block).body()
    }

    protected suspend inline fun <reified T> put(
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return httpClient.put(urlString = baseUrl + endpoint, block = block).body()
    }

    protected suspend inline fun <reified T> patch(
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit
    ): T {
        return httpClient.patch(urlString = baseUrl + endpoint, block = block).body()
    }

    protected suspend inline fun <reified T> delete(
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return httpClient.delete(urlString = baseUrl + endpoint, block = block).body()
    }
}

class ApiProvider(client: HttpClient) :
    BaseApiService(client, API_BASE_URL) {
    suspend fun getRandomQuote(): List<RandomQuote> = get(endpoint = "random")
}
