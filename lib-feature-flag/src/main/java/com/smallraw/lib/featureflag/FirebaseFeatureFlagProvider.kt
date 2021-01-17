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
package org.smallraw.lib.featureflag

import com.smallraw.lib.featureflag.FeatureFlagProvider
import com.smallraw.lib.featureflag.MAX_PRIORITY
import com.smallraw.lib.featureflag.RemoteFeatureFlagProvider

abstract class FirebaseFeatureFlagProvider(
    private val isDevModeEnabled: Boolean,
    private val cacheExpirationSecs: Long = 1 * 60 * 60L
) :
    FeatureFlagProvider,
    RemoteFeatureFlagProvider {

    override val priority: Int = MAX_PRIORITY

    override fun hasFeature(feature: Feature): Boolean {
        return when (feature) {
            FeatureFlag.DARK_MODE -> true
            else -> false
        }
    }

    open fun getCacheExpirationSeconds(isDevModeEnabled: Boolean): Long = cacheExpirationSecs
}
