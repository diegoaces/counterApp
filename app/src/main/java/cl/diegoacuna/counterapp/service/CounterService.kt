package cl.diegoacuna.counterapp.service

import androidx.lifecycle.LiveData
import cl.diegoacuna.counterapp.data.Counter
import cl.diegoacuna.counterapp.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CounterService {

    @GET("/api/v1/counters")
    fun getCounters(): Call<List<Counter>>

    @FormUrlEncoded
    @POST("/api/v1/counter")
    fun createCounter(@Field("title") title: String): Call<List<LiveData<Counter>>>

    @FormUrlEncoded
    @POST("/api/v1/counter/inc")
    suspend fun increaseCount(@Field("id") id: String): Call<List<LiveData<Counter>>>

    @FormUrlEncoded
    @POST("/api/v1/counter/dec")
    suspend fun decreaseCount(@Field("id") id: String): Call<List<LiveData<Counter>>>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/v1/counter", hasBody = true)
    fun deleteCount(@Field("id") id: String): Call<List<LiveData<Counter>>>


    object Factory {
        fun create(): CounterService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)


            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constant.URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CounterService::class.java)
        }
    }

}