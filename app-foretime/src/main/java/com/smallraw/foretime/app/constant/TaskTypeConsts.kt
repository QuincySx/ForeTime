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
package com.smallraw.foretime.app.constant

import androidx.annotation.IntDef
import com.smallraw.foretime.app.constant.TaskTypeConsts.Companion.ALL
import com.smallraw.foretime.app.constant.TaskTypeConsts.Companion.COUNTDOWN_DAY
import com.smallraw.foretime.app.constant.TaskTypeConsts.Companion.CUMULATIVE_DAY

@IntDef(ALL, CUMULATIVE_DAY, COUNTDOWN_DAY)
annotation class TaskTypeConsts {
    companion object {
        const val ALL = -1
        /**
         * 累计日
         */
        const val CUMULATIVE_DAY = 0
        /**
         * 倒数日
         */
        const val COUNTDOWN_DAY = 1
    }
}
