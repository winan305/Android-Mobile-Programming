package com.example.administrator.mymemoreadwrite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MemoTextActivity extends AppCompatActivity {
    // 메모의 제목과 내용을 담을 텍스트 뷰 선언
    private TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // 리소스 파일과 연결한다.
        title = (TextView)findViewById(R.id.TITLE);
        content = (TextView)findViewById(R.id.CONTENT);

        // 이전 액티비티로부터 전달된 인텐트를 가져온다.
        Intent intent = getIntent();
        // 전달받은 메모의 제목과 내용을 꺼내어 텍스트뷰에 세팅한다.
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 액션바 메뉴를 생성한다.
        getMenuInflater().inflate(R.menu.action_bar2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 액션바 메뉴 선택 이벤트를 정의한다.
        return true;
    }
}
