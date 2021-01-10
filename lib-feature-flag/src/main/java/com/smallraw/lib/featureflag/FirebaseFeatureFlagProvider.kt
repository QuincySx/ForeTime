package org.smallraw.lib.featureflag

import com.smallraw.lib.featureflag.FeatureFlagProvider
import com.smallraw.lib.featureflag.MAX_PRIORITY
import com.smallraw.lib.featureflag.RemoteFeatureFlagProvider

abstract class FirebaseFeatureFlagProvider(private val isDevModeEnabled: Boolean,private val cacheExpirationSecs:Long = 1 * 60 * 60L) : FeatureFlagProvider,
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