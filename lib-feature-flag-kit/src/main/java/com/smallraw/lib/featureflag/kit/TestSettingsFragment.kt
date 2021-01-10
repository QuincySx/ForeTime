package com.smallraw.lib.featureflag.kit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TestSettingsFragment : androidx.fragment.app.Fragment() {

    interface TestSettingsListener {
        fun onFeatureToggleClicked()
        fun onTestSettingClicked()
    }

    var testSettingListener: TestSettingsListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_testsettings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.textview_testsettings_explanation)
            .setOnClickListener { testSettingListener?.onFeatureToggleClicked() }

        view.findViewById<View>(R.id.formattextview_testsettings_testsetting)
            .setOnClickListener { testSettingListener?.onTestSettingClicked() }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "Test Settings"
    }
}