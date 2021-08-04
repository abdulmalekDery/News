package com.beinmedia.test.models.network.control

import com.beinmedia.test.BuildConfig
import com.beinmedia.test.config.Urls
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceGenerator @Inject
constructor() {

    //Network constants
    private val TIMEOUT_CONNECT = 60   //In seconds
    private val TIMEOUT_READ = 60   //In seconds
    private val TIMEOUT_WRITE = 60   //In seconds
    private val CONTENT_TYPE = "Content-Type"
    private val CONTENT_TYPE_VALUE = "application/json"

    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    private val retrofit: Retrofit

//    private var headerInterceptor = Interceptor { chain ->
//        val original = chain.request()
//
//        val request = original.newBuilder()
//            .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
//            .addHeader("Authorization", Hawk.get(HawkUtil.Token, ""))
//            .addHeader("locale", Hawk.get(HawkUtil.Lang, "ar"))
//            .method(original.method, original.body)
//            .build()
//
//        val response = chain.proceed(request)
//        val a = 0
//        response
//    }

    private val logger: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply {
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
                }.level = HttpLoggingInterceptor.Level.BODY
            }
            return loggingInterceptor
        }

    init {
//        okHttpBuilder.addInterceptor(headerInterceptor)
        okHttpBuilder.addInterceptor(logger)
        okHttpBuilder.connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(TIMEOUT_WRITE.toLong(), TimeUnit.SECONDS)
        val client = okHttpBuilder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(Urls.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

//    fun createAuthService(): IAuthService {
//        return retrofit.create(IAuthService::class.java)
//    }
}