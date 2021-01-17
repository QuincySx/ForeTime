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
package com.smallraw.library.core.extensions

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

fun View.expandTouchArea(size: Int = 10) {
    val parentView: View = this.parent as View
    parentView.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= size
        rect.bottom += size
        rect.left -= size
        rect.right += size
        parentView.touchDelegate = TouchDelegate(rect, this)
    }
}
