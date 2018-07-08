package com.example.administrator.imagedownloadfromurl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    ImageView imageView;
    String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Googlelogo.png/1199px-Googlelogo.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRuntimePermission();
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DownloadFilesTask task = new DownloadFilesTask();
                //task.execute(url);
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.putExtra("url", url);
                startService(intent);
            }
        });
        Picasso.get().load(url).into(imageView);
    }

    public Bitmap getImageFromURL(String ImageUrl) {
        Bitmap bitmap = null;

        try {
            URL url = new URL(ImageUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();

            int size = conn.getContentLength();
            Log.d("IMAGE SIZE", size+"");
            InputStream in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);

            in.close();
        }
        catch(Exception e) {
            Log.d("IMAGE SIZE", "fail");
            e.printStackTrace();
        }

        return bitmap;
    }

    public void saveBitmapToPNG(Bitmap bitmap, String filename) {
        OutputStream fOut = null;
        // 외부 공용 디렉토리 중 Download 디렉토리에 대한 File 객체 얻음
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // 절대 경로 String 값을 얻음
        String folderPath = folder.getAbsolutePath();
        // 로그 파일 절대 경로 생성 (String 값)
        String filePath = folderPath + "/" + filename +".png";
        try {
            fOut = new FileOutputStream(filePath);
        } catch (Exception e) {
            Toast.makeText(this, "Error occured. Please try again later.",
                    Toast.LENGTH_SHORT).show();
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            Log.d("Save image", "success");
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.d("Save image", "fail");
        }
    }

    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************

        // File IO Permission check
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // WRITE_EXTERNAL_STORAGE 권한이 있는 것
        }
        //*********************************************************************
        //*********************************************************************
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // read_external_storage-related task you need to do.

                    // WRITE_EXTERNAL_STORAGE 권한을 얻음

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    // 권한을 얻지 못 하였으므로 location 요청 작업을 수행할 수 없다
                    // 적절히 대처한다

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {
            Bitmap bitmap = getImageFromURL(url);
            saveBitmapToPNG(bitmap, "savedImage");
            // 전달된 URL 사용 작업
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
            // 파일 다운로드 퍼센티지 표시 작업
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // doInBackground 에서 받아온 total 값 사용 장소
        }
    }
}
