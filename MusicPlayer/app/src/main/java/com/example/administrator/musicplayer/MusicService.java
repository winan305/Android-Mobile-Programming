package com.example.administrator.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;

// 서비스 클래스를 상속받는 음악 서비스 클래스이다.
public class MusicService extends Service {

    // 노티피케이션 상수
    static final int NOTIFICATION_MUSIC = 100;

    // 음악서비스를 바인딩하기 위한 내부클래스이다.
    public class MusicBinder extends Binder {
        // 호출 시 현재 음악 서비스를 반환한다.
        MusicService getService() { return MusicService.this; }
    }

    private final IBinder serviceBinder = new MusicBinder();
    // 미디어 플레이어 객체
    private MediaPlayer mp;
    // 현재 음악의 인덱스와 음악리스트의 크기(음악의 개수)를 저장하는 변수
    private int musicIndex, musicListSize;
    // 사용자의 음악 재생 여부 플래그 변수, 서비스 받는 사람의 입장에서 다음 음악이 재생되었는지 플래그 변수
    private boolean isUserPlayMusic, isNextMusic, isMusicSet;
    // 현재 음악의 정보를 저장할 MusicInfo 객체
    private MusicInfo nowMusicInfo;
    // 음악 리스트를 저장하는 변수
    private ArrayList<MusicInfo> musicList;
    // 디폴트 생성자
    public MusicService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스 액티비티 생성 시 MediaPlayer 객체를 생성한다.
        mp = new MediaPlayer();
        // 서비스 생성 시 사용자의 음악재생은, 리소스세팅 값은 true 이다.
        isUserPlayMusic = isMusicSet = true;
        // 재생하는 음악이 끝나는 경우에 대한 리스너를 생성한다.
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            // 음악이 끝나면 다음 음악을 실행하도록 한다.
            public void onCompletion(MediaPlayer mp) {
                setNextMusic();
            }
        });
    }

    // 서비스 종료 시 처리를 한다.
    @Override
    public void onDestroy() {
       super.onDestroy();
        // MediaPlayer 객체를 릴리즈하고 null로 초기화한다.
        mp.release();
        mp = null;
    }

    // 서비스 시작 명령을 받은 경우 호출된다.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스 호출 시 실행된 intent가 매개변수로 들어온다.
        // 인텐트로 부터 음악의 리스트와 인덱스를 받아와 변수들을 초기화한다.
        musicList = intent.getParcelableArrayListExtra("musicList");
        musicListSize = musicList.size();
        musicIndex = intent.getIntExtra("musicIndex", 0);
        // 현재 음악(리스트에서 클릭한 음악)을 실행한다.
        playNowMusic();
        // 아래의 값을 반환하면 서비스를 강제 종료해도 서비스를 재시작되지 않는다.
        return START_NOT_STICKY;
    }

    // 인텐트를 받으면 서비스 바인더를 리턴하는 메서드이다.
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return serviceBinder;
    }

    // 현재 음악 인덱스를 사용하여 mp 객체를 초기화하고 세팅하고 음악을 시작하는 함수다.
    public void playNowMusic() {
        // 현재 음악이 실행중이 아닌 경우에만 동작한다.
        if(!isPlayingMusic()) {
            try {
                // 현재 음악에 대한 정보를 리스트에서 가져온다.
                nowMusicInfo = musicList.get(musicIndex);
                // 유저가 일시정지 버튼을 누르지 않았을 경우 음악 리소스를 바꾼다.
                if (isMusicSet){
                    // mp 객체를 리셋한다.
                    mp.reset();
                    // 현재 음악 리소스를 세팅한다.
                    mp.setDataSource(nowMusicInfo.getMusicResource());
                    // mp 객체를 준비시킨다.
                    mp.prepare();
                }
                // 유저가 음악을 재생 했는지 안했는지 여부 검사.
                // 재생을 시킨 상태라면 음악을 재생하지만 아닌경우 준비만 해 둠.
                if(isUserPlayMusic)
                    mp.start();
                // 노티피케이션을 세팅한다.
                setMusicNotification();
                // 다음 음악이 진행되고 있음을 표시한다.
                isNextMusic = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 호출 시 음악 인덱스가 다음 인덱스를 가리키도록 한다.
    public void setNextMusic() {
        // 다음 음악의 인덱스가 끝 음악을 넘어서면 0으로 하고 아니면 1을 더한다.
        musicIndex = (musicIndex + 1) == musicListSize ? 0 : musicIndex + 1;
        // 현재 mp를 멈춘다.
        mp.pause();
        // 음악 리소스를 세팅할 수 있도록 한다.
        isMusicSet = true;
        // 음악시작 함수를 호출한다.
        playNowMusic();
    }
    // 호출 시 음악 인덱스가 이전 인덱스를 가리키도록 한다.
    public void setPrevMusic() {
        // 이전 음악의 인덱스가 -1이면 끝 인덱스를 가리키도록 하고 아니면 1을 뺀다.
        musicIndex = (musicIndex - 1) == -1 ? musicListSize - 1 : musicIndex - 1;
        // 현재 mp를 멈춘다.
        mp.pause();
        // 음악 리소스를 세팅할 수 있도록 한다.
        isMusicSet = true;
        // 음악시작 함수를 호출한다.
        playNowMusic();
    }

    // 노티피케이션을 생성하고 세팅해주는 함수이다.
    public void setMusicNotification() {
        // 노티피케이션 클릭 시 이동하는 인텐트를 생성한다.
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, getMusicIntent(), PendingIntent.FLAG_UPDATE_CURRENT);

        // 노티피케이션을 생성하고 제목과 내용, 아이콘, 컨텐츠를 세팅한다.
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Music Player")
                .setContentText(nowMusicInfo.getMusicTitle())
                .setSmallIcon(R.mipmap.ic_queue_music_black_24dp)
                .setContentIntent(pIntent)
                .build();

        // 생성한 노티피케이션을 보이게 한다.
        startForeground(NOTIFICATION_MUSIC, noti);
    }
    // 노티피케이션을 위한 인텐트를 생성하고 반환하는 함수다.
    public Intent getMusicIntent() {
        // 노티피케이션은 클릭하는 경우 음악 재생 액티비티로 이동한다.
        Intent notiIntent = new Intent(this, MusicActivity.class);

        // 현재 음악에 대한 번호와 리스트를 인텐트에 담는다.
        notiIntent.putExtra("musicIndex", musicIndex);
        notiIntent.putParcelableArrayListExtra("musicList", musicList);

        // 인텐트를 반환한다.
        return notiIntent;
    }

    // 현재 음악을 기준으로 타이틀들에 대한 배열을 반환하는 함수다.
    public String[] getMusicTitles() {
        // 현재 인덱스가 0이라면 이전 음악은 끝 음악이고 아니면 -1 작은 음악이다.
        int prev = musicIndex == 0 ? musicListSize - 1 : musicIndex - 1;
        // 현재 인덱스가 끝 음악이라면 다음 음악은 첫 음악이고 아니면 1 큰 음악이다.
        int next = musicIndex == (musicListSize - 1) ?  0 : musicIndex + 1;

        // 타이틀들을 저장한다. 0 : 이전, 1 : 지금, 2 : 다음
        String[] titles = {
                musicList.get(prev).getMusicTitle(),
                musicList.get(musicIndex).getMusicTitle(),
                musicList.get(next).getMusicTitle()};
        // 타이틀들을 반환한다.
        return titles;
    }

    // 현재 음악 서비스의 시간정보를 반환하는 함수이다.
    public String getMusicTime() {
        // 서비스 중단, mp가 null이 되면 null 반환.
        if(mp == null) return null;
        // 총 시간을 가져온다. 밀리세컨 단위이기 때문에 1000으로 나누어 초로 바꾼다.
        long duration = mp.getDuration()/1000;
        // 현재 음악이 재생된 시간을 가져온다. 밀리세컨 단위이기 때문에 1000 으로 나누어 초로 바꾼다.
        long nowTime  = mp.getCurrentPosition()/1000;
        // 초에 대하여 분:초 로 바꿔주는 함수를 호출하여 시간을 얻어온다.
        // 현재시간 / 최대시간 으로 만들어 반환해준다.
        return getTime(nowTime) + " / " + getTime(duration);
    }
    // 초 단위 시간을 분:초 로 바꿔주는 함수다.
    public String getTime(long time) {
        // 60으로 나눈 몫이 분이 된다.
        // 분이 9보다 작으면 앞에 0을 붙여준다.(01분 02분 처럼 표시)
        String min = time/60 <= 9 ? "0" + time/60 : time/60+"";
        // 초가 9보다 작으면 앞에 0을 붙여준다. (01초 02초 처럼 표시)
        String sec = time%60 <= 9 ? "0" + time%60 : time%60 +"";
        // 분:초 로 반환한다.
        return min +":"+sec;
    }

    // 호출 시 mp 객체가 음악을 재생중인지 반환해준다.
    public boolean isPlayingMusic() {
        return mp.isPlaying();
    }
    // 호출 시 재생중인 음악을 일시정지한다.
    public void pauseMusic() {
        // 유저가 음악재생을 원하지 않는 상태
        isUserPlayMusic = false;
        // 음악재생을 원하지 않으므로 음악에 대한 정보를 유지하게 한다.
        isMusicSet = false;
        // 음악을 일시정지한다.
        mp.pause();
    }
    // 호출 시 멈춰있는 음악을 다시 재생한다.
    public void startMusic() {
        // 유저가 음악재생을 원하는 상태
        isUserPlayMusic = true;
        // 음악재생을 원하므로 음악 리소스를 바꿀 수 있게 한다.
        isMusicSet = false;
        // 음악을 시작한다.
        mp.start();
    }

    // 서비스 받는 사람의 입장에서 현재 서비스되는 음악이 다음 음악인지 알려준다.
    public boolean isPlayNextMusic() {
        return isNextMusic;
    }
    // 서비스 받는 사람의 입장에서 다음 음악이 현재 음악임을 표시한다.
    public void setNext2NowMusic() {
        isNextMusic = false;
    }
}
