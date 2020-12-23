package org.smallraw.lib.smallpermissions.check.actualTest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class PhoneReadState extends BaseTest {
    private final Context mContext;

    public PhoneReadState(Context context) {
        mContext = context;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    public boolean test() throws Throwable {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) return true;

        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE
                || !TextUtils.isEmpty(telephonyManager.getDeviceId())
                || !TextUtils.isEmpty(telephonyManager.getSubscriberId());
    }
}
