package com.example.administrator.imagedownloadfromurl;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018-07-07.
 */

public class DownloadService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Service", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("Download Service", "onStartCommand");
        String url = intent.getStringExtra("url");
        DownloadFilesTask task = new DownloadFilesTask();
        task.execute(url);
        return super.onStartCommand(intent, flags, startId);
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

    private class DownloadFilesTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {
            Bitmap bitmap = getImageFromURL(urls[0]);
            saveBitmapToPNG(bitmap, "savedImageFromService");
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
