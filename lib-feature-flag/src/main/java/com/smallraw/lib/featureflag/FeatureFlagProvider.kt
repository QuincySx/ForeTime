package com.smallraw.lib.featureflag

import org.smallraw.lib.featureflag.Feature

/**
 * every provider has an explicit priority so they can override each other (e.g. "remote config tool" > store).
 *
 * not every provider has to provide a flag value for every feature. this is to avoid implicitly relying on build-in
 * defaults (e.g. "remote config tool" returns false when no value for a feature) and to avoid that every provider has to provide a
 * value for every feature. (e.g. no "remote config tool" configuration needed, unless you want the toggle to be remote)
 */
interface FeatureFlagProvider {

    val priority: Int
    fun isFeatureEnabled(feature: Feature): Boolean
    fun hasFeature(feature: Feature): Boolean
}

interface RemoteFeatureFlagProvider {
    fun refreshFeatureFlags()
}

const val TEST_PRIORITY = 0
const val MAX_PRIORITY = 1
const val MEDIUM_PRIORITY = 2
const val MIN_PRIORITY = 3
