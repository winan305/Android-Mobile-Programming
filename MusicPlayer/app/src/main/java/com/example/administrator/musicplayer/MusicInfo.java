package com.example.administrator.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-11-22.
 */

// 음악의 정보 하나를 저장하는 클래스이다.
// Parcelable 인터페이스를 구현함으로써 인텐트에 Parcelable 객체로 삽입할 수 있다.
// Serializable 과 유사하지만 더 빠르다.

public class MusicInfo implements Parcelable {
    // 음악의 경로와 제목을 필드로 갖는다.
    private String path;
    private String title;

    // 음악의 경로와 제목을 전달받아 객체를 생성하는 생성자다.
    public MusicInfo(String path, String title) {
        this.path = path;
        this.title = title;
    }

    // Parcel 객체를 입력받아 객체를 생성하는 생성자다.
    public MusicInfo(Parcel in) {
        // Parcle 객체로부터 필드를 읽는 함수를 호출한다.
        readFromParcel(in);
    }

    // 음악의 제목을 반환받는 함수다.
    public String getMusicTitle() { return title; }
    // 음악의 리소스는 경로/음악제목 이다. 리소르를 반환하는 함수이다.
    public String getMusicResource() { return path + "/" + title; }

    // dest에 객체의 필드를 쓰는(저장하는) 함수다.
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(path);
        dest.writeString(title);
    }

    // Parcel 객체로부터 필드를 읽어오는 함수다.
    private void readFromParcel(Parcel in){
        path = in.readString();
        title = in.readString();
    }

    //
    public int describeContents(){
        return 0;
    }

    // Pacelable 객체를 생성한다.
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        // Parcel 객체를 전달하여 MusicInfo 객체를 생성한다.
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }
        // 객체들을 사이즈만큼 배열로 만들어 반환한다.
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };
}
