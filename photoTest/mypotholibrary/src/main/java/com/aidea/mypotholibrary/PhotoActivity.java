package com.aidea.mypotholibrary;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

public class PhotoActivity extends UnityPlayerActivity {

    public static final int PHOTOHRAPH = 2;// 拍照


    private static final int PHOTO_REQUEST_CODE = 1;// 相册
    public static final int PHOTORESOULT = 3;// 结果
    private Uri cropImageUri;
    private String gameObjectName;

    private static String path= Environment.getExternalStorageDirectory()+"/aideadora/";//sd路径


    /**
     * 申请运行时权限
     */
    public void requestPermission(String permissions) {

        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perArr = permissions.split(",");
            XPermission.requestPermissions(this, 10, perArr, new XPermission.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    callUnity("MyHeadPortraitHelper","permissStatus", "true");
                    Log.i("UnityTest","requestPermission Permission true");
                }

                @Override
                public void onPermissionDenied() {
                    callUnity("MyHeadPortraitHelper","permissStatus", "false");
                    XPermission.showTipsDialog(PhotoActivity.this);
                    Log.i("UnityTest","requestPermission Permission false");
                }
            });

        }else{
            Log.i("UnityTest","Permission true");
            callUnity("MyHeadPortraitHelper","permissStatus", "true");
        }

    }

    public void callUnity(String obj, String arg1, String arg2) {
        UnityPlayer.UnitySendMessage("MyHeadPortraitHelper", arg1, arg2);
    }


    public void openCamera(){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        String photofileName = "head.jpg";
//        //将拍好的照片存储在指定文件夹中，文件名为"temp.jpg"
//        Uri uri;
//        if (Build.VERSION.SDK_INT >= 24) {
//
//            System.out.println("##  > android7.0");
//            uri = FileProvider.getUriForFile(this,"com.aidea.mypotholibrary.fileprovider", new File(Environment
//                    .getExternalStorageDirectory() + "/" + "path", photofileName));
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        } else {
//            System.out.println("## < android7.0以下");
//            uri = Uri.fromFile(new File(Environment
//                    .getExternalStorageDirectory() + "/" + "path", photofileName));
//
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//保存照片
//        //开始跳转到拍照界面，当从拍照界面返回后调用onActivityResult()函数。并将requestCode = PHOTOHRAPH以参数返回
//        startActivityForResult(intent, PHOTOHRAPH);

        File file=new File(path,"head.jpg");//原图保存地址

        Uri imageUri = getUriForFile(this,file);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, PHOTOHRAPH);

    }
    //解决Android 7.0之后的Uri安全问题
    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider
                    .getUriForFile(context.getApplicationContext(), "com.aidea.mypotholibrary.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
    //相册
    public void  openPhotoAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }
    public void setHead() {
        String permissions = "android.permission.WRITE_EXTERNAL_STORAGE,android.permission.READ_EXTERNAL_STORAGE,android.permission.CAMERA";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            XPermission.requestPermissions(this, 10, permissions.split(","), new XPermission.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    //callUnity("UnityAndroidCommunicationObj","permissStatus", "true");
                    setUserHead();
                    Log.i("UnityTest","requestPermission Permission true");
                }

                @Override
                public void onPermissionDenied() {
                    callUnity("UnityAndroidCommunicationObj","permissStatus", "false");
                    XPermission.showTipsDialog(PhotoActivity.this);
                    Log.i("UnityTest","requestPermission Permission false");
                }
            });
        }else{
            Log.i("UnityTest","Permission true");
            setUserHead();
        }
    }


    /**设置个人头像*/
    public void setUserHead() {

        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                openCamera();
                            }
                        })
                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                openPhotoAlbum();
                            }
                        }).show();
    }


//    // 读取相册缩放图片
//    public void startPhotoZoom(Uri uri) {
//        if (null == uri) {
//            System.out.println("### 读取相册缩放图片 ==>> uri为Null");
//        }
//        File CropPhoto = new File(getExternalCacheDir(), "crop_image.jpg");
//        try {
//            if (CropPhoto.exists()) {
//                CropPhoto.delete();
//            }
//            CropPhoto.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        cropImageUri = Uri.fromFile(CropPhoto);
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 添加这一句表示对目标应用临时授权该Uri所代表的文件
//        }
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);
//
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 300);
//
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
//        intent.putExtra("noFaceDetection", true); // no face detection
//        System.out.println("## 开始缩放");
//        try {
//            startActivityForResult(intent, PHOTORESOULT);
//        } catch (Exception e) {
//            // 解决截取后部分机型秒退
//            System.out.println("##  裁剪错误： "+e.getMessage());
//
//        }
//    }

    public void SaveBitmap(Bitmap bitmap) throws IOException {

        if(null == bitmap) {
            System.out.println("## SaveBitmap=>bitmap为Null");
        }
        FileOutputStream fOut = null;
        // 注解1
        try {
            // 查看这个路径是否存在，
            // 如果并没有这个路径，
            // 创建这个路径
            File destDir = new File(path);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            fOut = new FileOutputStream(path + "image.jpg");
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        // 将Bitmap对象写入本地路径中，Unity在去相同的路径来读取这个文件
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            System.out.println("## fOut.flush");
            fOut.flush();

        } catch (IOException e) {
            System.out.println("## fOut.flush  faile");
            e.printStackTrace();
        }
        try {
            System.out.println("## fOut.close");
            fOut.close();
        } catch (IOException e) {
            System.out.println("## fOut.close  faile");
            e.printStackTrace();
        }

        System.out.println("## UnitySendMessage");
        callUnity("MyHeadPortraitHelper","onActivityResultCallback", path + "/image.jpg");
       // UnityPlayer.UnitySendMessage("Canvas", "onActivityResultCallback", path + "/image.jpg");
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("####    onActivityResult    ");

        if (data == null)
            return;
        if (requestCode == PHOTO_REQUEST_CODE) {
            System.out.println("## 选取相册图片完毕");
          //  startPhotoZoom(data.getData());
            Intent mIntent = cropPhoto(data.getData(),300,300);//裁剪图片
            startActivityForResult(mIntent, PHOTORESOULT);

        } else if (requestCode == PHOTOHRAPH) {
            System.out.println("## 相机");
            File temp = new File(path+ "head.jpg");
            //Intent mIntent = ImageUtils.cropPhoto(Uri.fromFile(temp),new File(CropUtils.getPath()));//裁剪图片
            Intent mIntent = cropPhoto(Uri.fromFile(temp),300,300);//裁剪图片
            startActivityForResult(mIntent, PHOTORESOULT);
        }else if (requestCode == PHOTORESOULT) {
            try {
                System.out.println("## 缩放图片完毕，准备保存Bitmap");
                Bitmap headShot = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropImageUri));
                SaveBitmap(headShot);
            } catch (IOException e) {
                System.out.println("## 存Bitmap错误： "+e.getMessage());
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("####    onRequestPermissionsResult    ");
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public Intent cropPhoto(Uri uri, int width, int height) {

        File CropPhoto = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (CropPhoto.exists()) {
                CropPhoto.delete();
            }
            CropPhoto.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImageUri = Uri.fromFile(CropPhoto);

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (Build.MANUFACTURER.contains("HUAWEI")) {// 华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);

        return intent;

    }



}
