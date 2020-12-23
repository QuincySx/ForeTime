package org.smallraw.lib.smallpermissions.permisson;


import org.smallraw.lib.smallpermissions.callback.PermissionsCallback;

public interface IPermission {
    void requestPermissions(String[] permissions, int requestCode, PermissionsCallback callback);
}