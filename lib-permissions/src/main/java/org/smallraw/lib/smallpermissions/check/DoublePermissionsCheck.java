package org.smallraw.lib.smallpermissions.check;

import android.content.Context;

/**
 * @author QuincySx
 * @date 2018/7/30 下午2:22
 */
public class DoublePermissionsCheck implements IPermissionsCheck {
    private final IPermissionsCheck mActualPermissionsCheck = new ActualPermissionsCheck();
    private final IPermissionsCheck mNormalPermissionsCheck = new NormalPermissionsCheck();

    @Override
    public boolean checkPermissions(Context context, String permission) {
        boolean check = mActualPermissionsCheck.checkPermissions(context, permission);
        CheckLog.print("严格权限验证:" + check + " Actual:" + permission);

        boolean normalCheck = mNormalPermissionsCheck.checkPermissions(context, permission);
        CheckLog.print("普通权限验证:" + normalCheck + " permission:" + permission);

        return check && normalCheck;
    }
}
