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
package com.smallraw.foretime.app.tomatoBell

import androidx.annotation.IntDef
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.CANCEL
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.FINISH
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.PAUSE
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.READY
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.RUNNING

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@IntDef(READY, RUNNING, PAUSE, FINISH, CANCEL)
public annotation class CountDownStatus {
    companion object {
        public const val READY = 0
        public const val RUNNING = 1
        public const val PAUSE = 2
        public const val FINISH = 3
        public const val CANCEL = 4
    }
}
