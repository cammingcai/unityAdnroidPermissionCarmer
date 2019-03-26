package com.aidea.mypotholibrary;

import java.util.List;

/**
 * @author chaychan
 * @description: 权限申请回调的接口
 */
public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
