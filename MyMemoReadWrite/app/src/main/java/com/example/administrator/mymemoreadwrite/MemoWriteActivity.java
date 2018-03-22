package com.example.administrator.mymemoreadwrite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MemoWriteActivity extends AppCompatActivity {
    private static final int ADD_CONTENT = 1; // 요청 변수
    private EditText title, content; // 메모 제목과 내용 입력을 위한 에딧텍스트 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        title = (EditText)findViewById(R.id.TITLE); // 레이아웃에 정의된 뷰들과 연결한다.
        content = (EditText)findViewById(R.id.CONTENT);
    }

    public void onClickAdd(View v) {
        Intent intent = new Intent(this, MainActivity.class); // 메인 액티비티로 보낼 인텐트 생성
        intent.putExtra("title", title.getText().toString()); // 제목과 내용을 인텐트에 싣는다.
        intent.putExtra("content", content.getText().toString());
        setResult(ADD_CONTENT, intent); // 요청변수와 인텐트를 결과값으로 전송한다.
        finish(); // 액티비티를 종료한다.
    }
}
