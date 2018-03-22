package com.example.administrator.bookmark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// 즐겨찾기 저장 액티비티이다.
public class AddActivity extends AppCompatActivity {
    private static final int ADD_BOOKMARK = 1; // 결과 변수를 상수로 선언하고 값을 1로 한다.
    EditText name, url; // 즐겨찾기의 이름과 링크를 입력받을 에딧텍스트를 선언한다.
    Button add, cac; // 즐겨찾기 추가, 추가 휘소 버튼을 선언한다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // 레이아웃 파일에 선언된 뷰들과 연결한다.
        name = (EditText)findViewById(R.id.NAME);
        url  = (EditText)findViewById(R.id.URL);
        add  = (Button)findViewById(R.id.ADD);
        cac  = (Button)findViewById(R.id.CANCEL);
    }

    // add버튼 클릭 시 입력된 즐겨찾기를 추가하도록 메인액티비티에 결과값을 보내도록
    // onClick 메서드를 정의한다. 이름은 addBookmark 로 한다.
    public void addBookmark(View view) {
        // 반환될 결과값 인텐트 data를 생성하고 MatinActivity 를 실행하도록 한다.
        Intent data = new Intent(AddActivity.this, MainActivity.class);
        // 인텐트에 데이터를 삽입한다.
        // 이름 데이터를 인텐트에 삽입
        data.putExtra("name", name.getText().toString());

        // url은 앞에 http:// 또는 https:// 로 시작하지 않는 경우 에러를 발생시킨다.
        // 검사 후 "http://" 를 앞에 붙여준다.
        String _url = url.getText().toString();
        if(!_url.startsWith("http://") && !_url.startsWith("https://")) _url = "http://" + _url;
        data.putExtra("url", _url);
        // setResult메서드에 결과변수와 data를 전달하여 결과를 세팅한다.
        setResult(ADD_BOOKMARK, data);
        // 액티비티를 종료한다.
        finish();
    }

    public void cancel(View view) {
        // 취소 버튼을 누를 경우 어떤 행동도 하지 않고 액티비티를 종료한다.
        finish();
    }
}
