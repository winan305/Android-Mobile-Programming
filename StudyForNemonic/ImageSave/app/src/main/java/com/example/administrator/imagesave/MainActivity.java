package com.example.administrator.imagesave;


import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Environment;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.util.Log;
import android.view.View;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.squareup.picasso.Picasso;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    ImageView[] imageView;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRuntimePermission();
        layout = (LinearLayout)findViewById(R.id.layout);

        imageView = new ImageView[20];

        for(int i = 0; i < imageView.length; i++) {
            imageView[i] = new ImageView(this);
            layout.addView(imageView[i]);
            Picasso.get().load("http://catholicmom.com/wp-content/uploads/2015/03/Small-Success-dark-blue-outline-800x800.png")
                    .into(imageView[i]);
        }

    }

    public void onClickDownload(View v) {
        Log.d("Download", "Start!");
        downloadImages();
        Toast.makeText(getApplicationContext(), "Download Finish!", Toast.LENGTH_SHORT).show();
        Log.d("Download", "Finish!");
    }
    public void downloadImages() {
        for(int i = 0; i < imageView.length; i++) {
            BitmapDrawable drawable = (BitmapDrawable) imageView[i].getDrawable();
            Bitmap bitmap;
            bitmap = drawable.getBitmap();
            Log.d("IMAGE SIZE", bitmap.getHeight() + "/" + bitmap.getWidth());
            String name = "image" + i;
            saveBitmapToPNG(bitmap,name);
        }
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
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
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
}

