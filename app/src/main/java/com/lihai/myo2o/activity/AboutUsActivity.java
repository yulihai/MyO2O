package com.lihai.myo2o.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.lihai.myo2o.R;
import com.lihai.myo2o.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by yulihai-海 on 2016/12/10.
 */
@TargetApi(value = 21)
public class AboutUsActivity extends Activity implements View.OnClickListener {


    ImageView imageView ,exit_Img;

    Button exit_Btn;

    LinearLayout linearLayout;

    //打开图库的请求码
    private static final int REQUEST_CODE_CAPTURE = 1;

    //打开相机的请求码
    private static final int REQUEST_CODE_CAMERA = 2;

    //裁剪的请求码
    private static final int REQUEST_CODE_CROP = 3;

    Bitmap bitmap;

    Uri uri;
    Uri targetUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_about_us);

        exit_Btn= (Button) findViewById(R.id.exit_Btn);

        imageView = (ImageView) findViewById(R.id.header);
        exit_Img = (ImageView) findViewById(R.id.exit_Img);

        imageView.setOnClickListener(this);
        exit_Img.setOnClickListener(this);
        exit_Btn.setOnClickListener(this);

        linearLayout = (LinearLayout) findViewById(R.id.about_us_root);


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.header:

                View view  = getLayoutInflater().inflate(R.layout.layout_popup_select_img,null);

                final PopupWindow popupWindow = new PopupWindow(view , ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);


                //取消按钮
                Button cancel = (Button) view.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                //打开图库按钮
                Button openCaptureBtn = (Button) view.findViewById(R.id.openCapture);
                openCaptureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //打开相册
                        openCapture();
                    }
                });

                //打开相机按钮
                Button openCameraBtn = (Button) view.findViewById(R.id.openCamera);
                openCameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //打开相机
                        openCamera();
                    }
                });


                Drawable drawable;
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){

                    drawable = getResources().getDrawable(R.drawable.tongcheng_all_bg02,null);

                }else{
                    drawable = getResources().getDrawable(R.drawable.tongcheng_all_bg02);
                }
                popupWindow.setBackgroundDrawable(drawable);
                popupWindow.showAtLocation(linearLayout,Gravity.BOTTOM,0,0);
                break;
            case R.id.exit_Img:
                finish();
                break;
            case R.id.exit_Btn:
                finish();
                break;
        }
    }

    /**
     * 打开图库
     */
    private void openCapture(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_CAPTURE);
    }

    /**
     * 打开相机
     */
    private void openCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String path = FileUtil.getSDPath();
        if(path == null){
            return ;
        }

        path = path + "/" +System.currentTimeMillis()+".jpg";
        File file = new File(path);
        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        startActivityForResult(intent,REQUEST_CODE_CAMERA);
    }

    /**
     *
     * @param requestCode
     * @param resultCode   如果没有选择数据结果码为0
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(bitmap != null){

            bitmap.recycle();
            bitmap = null;
        }

        if(requestCode == RESULT_CANCELED){
            return;
        }

        if (requestCode == REQUEST_CODE_CAPTURE) {  //图库

            Uri uri = data.getData();


            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_CODE_CAMERA){   //相机

            //取数据
            //bitmap = data.getParcelableExtra("data");
            //imageView.setImageBitmap(bitmap);

           /* String path =  uri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);*/

            String targetPath = FileUtil.getSDPath() + "/" + System.currentTimeMillis() + ".jpg";
            File file = new File(targetPath);
            targetUri = Uri.fromFile(file);
            cropImg(uri,targetUri,REQUEST_CODE_CROP);
        }else if(requestCode == REQUEST_CODE_CROP){
            String path = targetUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }else{
            return ;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片
     * @param sourceUri
     * @param targetUri
     * @param requestCode
     */
    public void cropImg(Uri sourceUri ,Uri targetUri ,int requestCode){


        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(sourceUri,"image/*");
        intent.putExtra("CROP","true");

        intent.putExtra("aspectY",1);
        intent.putExtra("aspectX",1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,targetUri);
        startActivityForResult(intent,requestCode);

    }
}


