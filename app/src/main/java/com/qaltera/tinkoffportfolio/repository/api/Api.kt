package com.qaltera.tinkoffportfolio.repository.api

import com.google.gson.GsonBuilder
import com.qaltera.tinkoffportfolio.BuildConfig
import okhttp3.Interceptor
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Api {

    val api: ApiInterface

    init {
        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(getHeaderInterceptor())
            .build()
        val gson = GsonBuilder()
            //.registerTypeAdapter(Value::class.java, ValueDeserializer())
            .create()
        var retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api-invest.tinkoff.ru/")
            .build()
        api = retrofit.create(ApiInterface::class.java)
    }

    private fun getHeaderInterceptor() = Interceptor { chain ->
        val request =
            chain.request().newBuilder()
                .header("Authorization", "Bearer $TOKEN")
                .build()
        chain.proceed(request)
    }

    suspend fun getPortfolio(accountId: String? = null) =
        api.getPortfolio(accountId)

    suspend fun getOperations(figi: String,
    from: String, to: String) = api.getOperations(figi, from, to)

    companion object {
        val TOKEN = BuildConfig.Token
    }
    //    internal class ValueDeserializer : JsonDeserializer<Value?> {
    //
    //        @Throws(JsonParseException::class)
    //        override fun deserialize(
    //            json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?
    //        ): Value {
    //            val str: String = json.getAsString()
    //            var intVal: Int? = null
    //            var floatVal: Float? = null
    //            try {
    //                floatVal = str.toFloat()
    //            } catch (ex: NumberFormatException) {
    //                println(ex)
    //            }
    //            if (floatVal == null) {
    //                try {
    //                    intVal = str.toInt()
    //                } catch (ex: NumberFormatException) {
    //                    println(ex)
    //                }
    //            }
    //            return Value(floatVal, intVal, str.takeIf
    //            { floatVal == null && intVal == null }
    //            )
    //        }
}
