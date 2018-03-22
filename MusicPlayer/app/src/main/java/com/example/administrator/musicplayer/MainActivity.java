package com.example.administrator.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // 파일 권한에 대한 정수 상수
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    // 파일의 디렉토리를 얻어오기 위한 파일 객체
    private File musicDir;
    // 음악의 목록을 보여줄 리스트뷰
    private ListView musicListView;
    // 리스트뷰에 음악의 제목을 뿌려줄 어댑터
    private ArrayAdapter<String> musicAdapter;
    // 음악들의 정보를 저장하는 음악 리스트
    private ArrayList<MusicInfo> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 어댑터를 생성한다.
        musicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // 리스트뷰를 레이아웃과 연결하고 어댑터와 클릭 리스너를 등록한다.
        musicListView = (ListView)findViewById(R.id.MUSIC_LIST);
        musicListView.setAdapter(musicAdapter);
        musicListView.setOnItemClickListener(onClickListItem);
        // 음악 리스트 객체를 생성한다.
        musicList = new ArrayList<>();
        // 퍼미션을 체크한다.
        checkPermission();
    }
    // 리스트뷰의 아이템을 클릭하면 리스트뷰의 위치를 참고하여 음악 재생 액티비티를 실행한다.
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 음악재생 액티비티를 실행하는 함수에 아이템의 위치를 전달한다.
            startMusicActivity(position);
            finish();
        }
    };

    // 음악재생 액티비티를 실행하는 함수
    public void startMusicActivity(int musicIndex) {
        // 인텐트를 생성하고 음악의 위치(리스트뷰의 위치와 음악리스트 위치가 동일함)
        // 음악 리스트를 인텐트에 삽입한다.
        Intent data = new Intent(MainActivity.this, MusicActivity.class);
        data.putExtra("musicIndex", musicIndex);
        data.putParcelableArrayListExtra("musicList", musicList);
        // 음악 액티비티 실행
        startActivity(data);
    }

    // 파일 읽기에 대한 퍼미션을 체크한다.
    public void checkPermission() {
        // 권한이 없는 경우
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 사용자가 권한 설정을 거부한 경우
            // 권한 설정이 필요함을 토스트로 출력해준다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "음악 파일을 읽기 위해 파일 읽기 권한을 설정해야 합니다.", Toast.LENGTH_LONG).show();
            }
            // 권한 설정을 수락하면 파일 읽기 권한을 설정한다.
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        // READ_EXTERNAL_STORAGE 권한이 있는 상태
        else {
            // 음악 디렉토리에서 파일을 읽어오는 메서드를 호출한다.
            prepareAccessToMusicDirectory();
        }
    }

    // Music 디렉토리의 절대 경로를 저장하고 파일들을 저장 가져옴
    // 파일들의 이름들을 얻어와 어댑터에 뿌려주고 음악 리스트에 저장
    public void prepareAccessToMusicDirectory() {
        // Public Directory 중에 Music 디렉토리에 대한 File 객체를 반환한다
        musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        String path = null; // Music 디렉토리의 절대경로를 저장하기 위한 변수를 선언하고 null로 초기화한다.

        if(musicDir.isDirectory()) {
            // 절대 경로를 저장한다.
            path = musicDir.getAbsolutePath();
        }

        // 파일들을 저장하기 위한 파일객체 배열을 선언한다.
        File files[] = {};
        // 저장된 파일의 개수를 저장하기 위한 변수를 선언한다.
        int num = 0;

        try {
            // listFiles(): 디렉토리에 있는 파일(디렉토리 포함)들을 나타내는 File 객체들의 배열을 반환한다
            files = musicDir.listFiles();

            // 객체가 null인 경우 음악 폴더를 읽는 데 문제가 발생한 것이다.
            // 에러발생을 토스트로 알리고 함수를 종료한다.
            if(files == null) {
                Toast.makeText(this, "Music 폴더 읽기에 에러가 발생했습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            // 객체를 반환받는 데 성공한 경우
            else {
                // 파일의 길이(개수)를 가져온다.
                num = files.length;

                // 파일이 없으면 음악 파일이 없음을 토스트로 알리고 함수를 종료한다.
                if (num == 0) {
                    Toast.makeText(this, "음악 파일이 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                // listFiles() 메소드가 반환하는 File 객체 배열을 이용하여
                // musicDir 안에 있는 모든 파일들의 이름을 알아낼 수 있다

                // 아래 코드는 File 객체 배열의 길이만큼 for 루프를 돌면서 파일(혹은 디렉토리)의 이름을 로그로 출력한다
                for (int i = 0; i < num; i++) {
                    // 파일 리스트에서 파일의 이름을 얻어온다.
                    String fileName = files[i].getName();

                    // 파일의 이름이 .mp3 로 끝나는 경우 음악파일로 간주한다.
                    if(isMusicFile(fileName)) {
                        // 어댑터에 파일의 이름(음악의 제목이 됨)을 저장하여 리스트뷰에 뿌려준다.
                        musicAdapter.add(fileName);
                        // 음악리스트에 새로운 음악정보(경로, 파일이름)을 저장한다.
                        musicList.add(new MusicInfo(path, fileName));
                    }
                }
            }
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_EXTERNAL_STORAGE 권한을 얻었으므로
                    // 관련 작업을 수행할 수 있다
                    prepareAccessToMusicDirectory();

                } else {
                    // 권한을 얻지 못 하였으므로 파일 읽기를 할 수 없음을 토스트로 표시하고 함수를 종료한다.
                    Toast.makeText(this, "권한을 설정하지 않아 파일을 읽을 수 없습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    // 파일의 끝이 mp3로 끝나는지 검사한다.
    // 현재는 mp3 만 검사하지만 실제로는 여러 확장자가 있을 수 있다.
    // 미리 함수로 만들어 두어 확장자만 추가함으로써 파일을 검사할 수 있다.
    public boolean isMusicFile(String fileName) {
        return fileName.endsWith(".mp3");
    }
}
