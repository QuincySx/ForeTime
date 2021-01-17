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
package com.smallraw.foretime.app.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

@Throws(IOException::class)
fun is2String(inputStream: InputStream): String {
    val outputStream = ByteArrayOutputStream()
    var i: Int = -1

    i = inputStream.read()
    while (i != -1) {
        outputStream.write(i)
        i = inputStream.read()
    }
    val toByteArray = outputStream.toByteArray()
    outputStream.flush()
    outputStream.close()
    return String(toByteArray, Charsets.UTF_8)
}
