package com.yueer.farmshop.kaoshitest.fragment.home.mycenter;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.base.BaseFragment;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCenterFragment extends BaseFragment {


    @BindView(R.id.mycenter_toolbar_img)
    ImageView mycenterToolbarImg;
    @BindView(R.id.mycenter_bt)
    Button mycenterTv;
    private Uri mImageUri;
    private File mFile;

    @Override
    protected BasePresenter initBasePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_center;
    }

    @Override
    protected void initView() {
        Glide.with(getContext()).load("http://106.13.63.54:8080/sys/image/a.jpg").into(mycenterToolbarImg);
    }


    @OnClick({R.id.mycenter_toolbar_img, R.id.mycenter_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mycenter_toolbar_img:
                showpop();
                break;
            case R.id.mycenter_bt:
                startActivity(new Intent(getContext(),GouWuCheActivity.class));
                break;
        }
    }

    //pop
    private void showpop() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop, null);
        final TextView openAlbum = inflate.findViewById(R.id.openAlbum);
        final TextView openCamera = inflate.findViewById(R.id.openCamera);

        final PopupWindow popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupWindow.showAsDropDown(inflate);


        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePICK();
                popupWindow.dismiss();

            }
        });
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                popupWindow.dismiss();
            }
        });
    }


    private static final int CAMERA_CODE = 100;
    private static final int ALBUM_CODE = 200;

    private void takePICK() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openAlbum();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }

    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 100) {
                openCamera();
            } else if (requestCode == 200) {
                openAlbum();
            }
        }
    }

    //打开相册
    private void openAlbum() {

        //启动相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ALBUM_CODE);
    }

    //打开相机
    private void openCamera() {

        //创建文件用于保存图片
        mFile = new File(getContext().getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //适配7.0,  等到对应的mImageUri路径地址
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mImageUri = Uri.fromFile(mFile);
        } else {

            //第二个参数要和清单文件中的配置保持一致
            mImageUri = FileProvider.getUriForFile(getActivity(), "com.baidu.upload.provider", mFile);
        }

        //启动相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将拍照图片存入mImageUri
        startActivityForResult(intent, CAMERA_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {//判断回调成功

            if (requestCode == CAMERA_CODE) {//拍照

                //显示拍照后的图片
                    /*try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        img.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/

                //拍照后的图片上传
                uploadFile(mFile);
            } else if (requestCode == ALBUM_CODE) {//相册

                //获取到相册选中后的图片URI路径
                Uri imageUri = data.getData();

                //显示相册选中后的图片
                    /*try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        img.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/

                //文件上传，将Uri路径转换为File对象
                //处理uri,7.0以后的fileProvider 把URI 以content provider 方式 对外提供的解析方法
                File file = getFileFromUri(imageUri, getContext());

                if (file.exists()) {
                    uploadFile(file);
                }
            }
        }
    }

    public File getFileFromUri(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        switch (uri.getScheme()) {
            case "content":
                return getFileFromContentUri(uri, context);
            case "file":
                return new File(uri.getPath());
            default:
                return null;
        }
    }

    /**
     * 通过内容解析中查询uri中的文件路径
     */
    private File getFileFromContentUri(Uri contentUri, Context context) {
        if (contentUri == null) {
            return null;
        }
        File file = null;
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(contentUri, filePathColumn, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();

            if (!TextUtils.isEmpty(filePath)) {
                file = new File(filePath);
            }
        }
        return file;
    }

    private void uploadFile(File mFile) {

        String url = "http://yun918.cn/study/public/file_upload.php";

        OkHttpClient client = new OkHttpClient.Builder()
                .build();


        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), mFile);

        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", "H1808C")
                .addFormDataPart("file", mFile.getName(), requestBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                final UpLoadBean upLoadBean = gson.fromJson(string, UpLoadBean.class);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (upLoadBean != null) {
                            if (upLoadBean.getCode() == 200) {
                                Toast.makeText(getContext(), upLoadBean.getRes(), Toast.LENGTH_SHORT).show();

                                Glide.with(MyCenterFragment.this).load(upLoadBean.getData().getUrl()).into(mycenterToolbarImg);

                                Log.e(TAG, "run: " + upLoadBean.getData().getUrl());
                            } else {
                                Toast.makeText(getContext(), upLoadBean.getRes(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}
