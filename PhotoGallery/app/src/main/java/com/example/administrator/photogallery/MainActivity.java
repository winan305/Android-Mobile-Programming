package com.example.administrator.photogallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 이미지들의 id값을 배열로 저장한다. id는 정수형 값이다.
    private static int[] ids = {R.drawable.java, R.drawable.python, R.drawable.cpp, R.drawable.js};
    // 크게 표기할 메인 이미지뷰를 선언한다.
    private static ImageView main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // xml에 작성된 메인 이미지뷰의 아이디를 불러와 main객체가 참조하게 한다.
        main = (ImageView)findViewById(R.id.MAIN);
        // 앱 실행 시, 메인 이미지를 썸네일 이미지 중 첫 이미지로 설정한다.
        main.setImageResource(ids[0]);
    }

    // 썸네일 이미지를 선택하여 메인 이미지를 선택하는 onClick 메서드이다.
    public void selectImg(View v) {
        int idx = 0; // 선택한 썸네일 이미지의 인덱스 저장한다.
        switch (v.getId()) { // 선택한 썸네일 이미지의 아이디를 얻어온 뒤 switch로 분기한다.
            case R.id.IMG_1 : idx = 0; break; // 어떤 이미지를 클릭했느냐에 따라 idx를 저장한다.
            case R.id.IMG_2 : idx = 1; break;
            case R.id.IMG_3 : idx = 2; break;
            case R.id.IMG_4 : idx = 3; break;
        }
        main.setImageResource(ids[idx]); // 선택이 끝나면 인덱스로 이미지의 아이디에 접근한다.
                                         // 그 다음, 메인 이미지를 저장한다.
    }
}
