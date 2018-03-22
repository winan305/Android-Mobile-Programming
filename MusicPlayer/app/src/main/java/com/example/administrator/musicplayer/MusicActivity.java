package com.example.administrator.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    // 액티비티 실행 시 전달받은 인텐트를 저장하기 위한 인텐트 객체
    private Intent data;
    // 이전음악, 현재음악(재생, 일시정지), 다음음악을 표시하는 이미지 버튼
    private ImageButton play, prev, next;
    // 이전음악, 현재음악, 다음음악의 제목을 표시할 텍스트뷰
    private TextView nowTitle, prevTitle, nextTitle, musicTime;
    // 음악서비스 객체
    private MusicService musicService;
    // 바운드 서비스에 대한 플래그 변수
    private boolean serviceBound = false;

    // 바운드 서비스를 연결하기 위한 익명 클래스
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스 바인더 객체를 생성하고 음악 서비스를 바인딩한다.
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicService = binder.getService();
            // 음악 서비스가 연결되었으므로 true 로 저장한다.
            serviceBound = true;
            // 서비스로부터 음악 타이틀을 세팅하는 메서드를 호출하여 타이틀을 초기화한다.
            setMusicTitles();

            // 현재 음악 서비스가 진행중인지 아닌지 여부에 따라 이미지 리소스를 초기화한다.
            if (musicService.isPlayingMusic()) {
                play.setImageResource(R.mipmap.ic_pause_circle_outline_black_24dp);
            }
            else {
                play.setImageResource(R.mipmap.ic_play_circle_outline_black_24dp);
            }
        }

        // 서비스 연결이 해제 될 경우 false 로 바꿔준다.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // 뷰들을 레이아웃 파일의 뷰들과 연결한다.
        play = (ImageButton)findViewById(R.id.PLAY);
        prev = (ImageButton)findViewById(R.id.PREV);
        next = (ImageButton)findViewById(R.id.NEXT);
        nowTitle = (TextView)findViewById(R.id.NOW_MUSIC_TITLE);
        prevTitle = (TextView)findViewById(R.id.PREV_MUSIC_TITLE);
        nextTitle = (TextView)findViewById(R.id.NEXT_MUSIC_TITLE);
        musicTime = (TextView)findViewById(R.id.MUSIC_TIME);

        // 액티비티 실행 시 전달받은 인텐트를 얻는다.
        data = getIntent();

        // 음악서비스 바인딩
        bindMusicService();

        // 음악 시간을 지속적으로 저장할 쓰레드를 생성한다.
        MusicInfoThread musicInfoThread = new MusicInfoThread();
        // 데몬 쓰레드로 세팅함으로써 MusicActivity 종료 시 쓰레드가 종료되게 한다.
        musicInfoThread.setDaemon(true);
        // 쓰레드를 시작해서 음악 시간을 표시한다.
        musicInfoThread.start();
    }

    // 음악 서비스 바인딩 메서드. 필요한 인텐트를 세팅하고 바인딩한 뒤 음악서비스 시작 후 제목 세팅
    public void bindMusicService() {
        // 서비스 시작을 위한 인텐트를 생성한다.
        Intent service = new Intent(MusicActivity.this, MusicService.class);

        // data 인텐트로부터 음악 리스트와 실행할 음악의 번호를 얻어온다.
        ArrayList<MusicInfo> musicList = data.getParcelableArrayListExtra("musicList");
        int musicIndex = data.getIntExtra("musicIndex", 0);

        // 서비스 시작을 위한 인텐트에 삽입한다.
        service.putExtra("musicList", musicList);
        service.putExtra("musicIndex", musicIndex);

        // 서비스를 바인딩한다.
        bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
        // 서비스를 시작한다.
        startService(service);
    }

    // 음악 재생/일시정지 버튼을 누르는 경우에 대한 함수다.
    public void onClickMusicPlay(View v) {
        // 음악이 실행중일 때 버튼을 누르는 경우이다.
        // 이 때 버튼은 일시정지 버튼을 나타내고 있다.
        if (musicService.isPlayingMusic()) {
            // 서비스에서 음악을 멈추는 함수를 호출한다.
            musicService.pauseMusic();
            // 이미지를 재생 이미지로 교환한다.
            play.setImageResource(R.mipmap.ic_play_circle_outline_black_24dp);
        }
        // 음악이 재생중이지 않는 경우 이다.
        // 이 때 버튼은 재생 버튼을 나타내고 있다.
        else {
            // 서비스에서 음악을 시작하는 함수를 호출한다.
            musicService.startMusic();
            // 이미지를 일시정지 이미지로 교환한다.
            play.setImageResource(R.mipmap.ic_pause_circle_outline_black_24dp);
        }
    }

    // 이전 음악을 실행하는 경우
    public void onClickMusicPrev(View v) {
        // 음악 서비스에서 이전 음악을 실행하는 함수를 호출한다.
        musicService.setPrevMusic();
        // 타이틀들을 세팅한다.
        setMusicTitles();
    }
    // 다음 음악을 실행하는 경우
    public void onClickMusicNext(View v) {
        // 음악 서비스에서 다음 음악을 실행하는 함수를 호출한다.
        musicService.setNextMusic();
        // 타이틀들을 세팅한다.
        setMusicTitles();
    }

    public void setMusicTitles() {
        // 음악 서비스로부터 타이틀들을 얻어온다.
        String[] titles = musicService.getMusicTitles();
        // 타이틀들을 세팅한다.
        prevTitle.setText(titles[0]);
        nowTitle.setText(titles[1]);
        nextTitle.setText(titles[2]);
    }

    // 액션바를 생성한다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 액션바 클릭 이벤트 처리를 위한 메소드이다.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            // 종료 메뉴를 누른 경우이다.
            case R.id.ACTION_EXIT:
                // 서비스를 종료하고 메인 액티비티를 실행시킨다.
                stopService(new Intent(this, MusicService.class));
                startActivity(new Intent(this, MainActivity.class));
                // 현재 액티비티를 종료한다.
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 서비스 시작 시 음악 정보를 1초간격으로 가져오도록 하는 쓰레드이다.
    // 음악의 재생시간과 타이틀에 대한 정보를 핸들러에서 얻어오고 UI를 변경한다.
    class MusicInfoThread extends Thread {
        public void run() {
            // 쓰레드가 실행되면 종료 전까지 무한 반복한다.
            while (true) {
                // 핸들러에 메세지를 보내어 UI를 바꿔줄 것을 요청한다.
                mHandler.sendEmptyMessage(0);
                try {
                    // 1초마다 메세지를 보내어 1초마다 음악시간이 갱신되게 한다.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // UI를 변경할 핸들러 객체를 생성한다.
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 메세지를 받아 처리한다.
            // 쓰레드에서 빈 메세지에 정수 0을 전달했다.
            // msg.what 이 0 인 경우에 해당한다.
            if (msg.what == 0) {
                // 서비스가 바인딩 되어 있는 경우에만 음악에 대한 정보를 얻어올 수 있다.
                if(serviceBound) {
                    // 음악 서비스에서 시간정보를 전달받아 텍스트뷰를 세팅한다.
                    musicTime.setText(musicService.getMusicTime());
                    // 서비스가 현재 재생하던 음악을 끝내고 다음 음악을 재생중인지 검사한다.
                    if(musicService.isPlayNextMusic()) {
                        // 다음 음악을 재생중이라면 타이틀을 바꾼다.
                        setMusicTitles();
                        // 다음 음악이 현재 음악이 되도록 표시하게 한다.
                        musicService.setNext2NowMusic();
                    }
                }
            }
        }
    };
}