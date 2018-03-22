package com.example.administrator.mymemoreadwrite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class Memo { // 메모 하나를 저장하는 클래스
    private String title, content; // 메모의 제목과 내용

    public Memo(String title, String content) {
        // 제목과 내용을 전달받는 생성자
        this.title = title;
        this.content = content;
    }

    public String getTitle()   { return title; }
    public String getContent() { return content; }
}
public class MainActivity extends AppCompatActivity {
    private static final int GET_MEMO = 1; // 요청 변수
    private static int count = 0; // 메모 개수 저장변수
    private static ArrayList<Memo> memos = new ArrayList<>(); // 저장된 메모를 담을 리스트
    private LinearLayout main; // 메인 레이아웃 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (LinearLayout)findViewById(R.id.MAIN); // 레이아웃과 연결한다.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 액션바 메뉴를 생성한다.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 액션바 메뉴 선택 이벤트를 정의한다.
        switch(item.getItemId()) {
            // 쓰기 메뉴 선택 시 MemoWriteActivity 를 실행한다.
            case R.id.action_write:
                startActivityForResult(new Intent(MainActivity.this, MemoWriteActivity.class), GET_MEMO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_MEMO) { // 요청변수가 GET_MEMO 일때 동작한다.
            if (resultCode == 1) { // 결과 코드가 1인 경우 동작한다.
                String title = data.getStringExtra("title"); // 인텐트에서 메모 제목을 얻어온다.
                String content = data.getStringExtra("content"); // 인텐트에서 메모 내용을 얻어온다.
                TextView memo = new TextView(this); // 새로운 텍스트뷰를 생성한다.
                memo.setId(count); // 메모에 아이디를 세팅한다.
                memos.add(new Memo(title, content));

                // 온클릭 리스너를 구현하여 메모 제목 클릭 시 MemoTextActivity 로 이동하게 한다.
                memo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Memo item = memos.get(v.getId()); // 메모의 아이디(리스트의 인덱스 값과 동일)로 리스트에 저장된 메모를 가져온다.
                        // MemoTextAcitity 로 보낼 인텐트를 만들어 메모의 제목과 내용을 담는다.
                        Intent intent = new Intent(MainActivity.this, MemoTextActivity.class);
                        intent.putExtra("title", item.getTitle());
                        intent.putExtra("content", item.getContent());
                        startActivity(intent); // 액티비티를 실행한다.
                    }
                });

                memo.setText(title); // 텍스트뷰의 텍스트를 세팅한다.
                memo.setTextSize(30);
                main.addView(memo); // 뷰에 텍스트뷰를 추가한다.
                count++; // 저장된 메모의 개수를 늘린다.
            }
        }
    }
}
