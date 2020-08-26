package com.smallraw.lib.monitor.view

import android.util.Log
import io.github.inflationx.viewpump.InflateResult
import io.github.inflationx.viewpump.Interceptor
import kotlin.system.measureTimeMillis

class ViewInitTimeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): InflateResult {
        val request = chain.request()

        var result: InflateResult
        val measureTimeMillis = measureTimeMillis {
            result = chain.proceed(request)
        }
        Log.d(
            "DumpViewTimeInterceptor",
            "DumpView: ${request.name} load time is $measureTimeMillis ms"
        )
        return result
    }
}