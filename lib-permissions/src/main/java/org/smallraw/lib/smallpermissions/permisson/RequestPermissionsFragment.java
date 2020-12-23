package org.smallraw.lib.smallpermissions.permisson;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smallraw.lib.smallpermissions.callback.PermissionsCallback;
import org.smallraw.lib.smallpermissions.executor.EngineThread;
import org.smallraw.lib.smallpermissions.executor.MainThread;
import org.smallraw.lib.smallpermissions.permisson.handler.PermissionsHandler;

import java.util.Arrays;
import java.util.concurrent.Executor;

public class RequestPermissionsFragment extends Fragment implements IPermission {
    private final PermissionsHandler mPermissionsHandler = PermissionsHandler.getInstance();
    private final EngineThread mEngineThread = new EngineThread();
    private final Executor mMainExecutor = new MainThread();

    @Override
    public void requestPermissions(final String[] permissions, final int requestCode, final PermissionsCallback callback) {
        mEngineThread.add(new Runnable() {
            @Override
            public void run() {
                String[] permission = mPermissionsHandler.checkPermissions(getActivity(), permissions);
                if (permission.length == 0) {
                    mMainExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onPermissionGranted(Arrays.asList(permissions));
                        }
                    });
                } else {
                    try {
                        mPermissionsHandler.addPermissionCallback(requestCode, callback);
                        requestPermissions(permission, requestCode);
                    } catch (NoSuchMethodError e) {
                        e.printStackTrace();
                        mMainExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onPermissionGranted(Arrays.asList(permissions));
                            }
                        });
                        mPermissionsHandler.removePermissionCallback(requestCode);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEngineThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsHandler.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mEngineThread.quitSafely();
        } else {
            mEngineThread.removeAllMessage();
            mEngineThread.quit();
        }
        super.onDestroy();
    }
}
