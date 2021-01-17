/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
