package com.example.administrator.pizzaorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 도우, 토핑 값을 저장할 변수와 합한 총 변수를 저장할 변수를 선언한다.
    private static int dough, topping, total;
    // 총 값을 출력할 텍스트뷰를 선언한다.
    private static TextView totalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalView = (TextView)findViewById(R.id.TOTAL);
        dough = topping = total = 0;
    }

    // 총 값을 계산하여 텍스트뷰를 세팅하는 함수다.
    private void setTotal() {
        total = dough + topping; // 총 값은 도우값 + 토핑값이다.
        totalView.setText(total + ""); // total값에 ""를 더하여 스트링으로 만들고 텍스트뷰를 세팅한다.
    }

    // 도우를 선택하면 발생하는 라디오버튼의 onClick 메서드다.
    public void selectDough(View v) {
        // 라디오버튼이 체크되었는지 검사한다.
        boolean isChecked = ((RadioButton)v).isChecked();

        // 라디오버튼의 아이디를 받아온 후 switch문으로 분기한다.
        // 각각의 도우가 선택되었을 때 도우값을 저장한다.
        switch (v.getId()) {
            case R.id.ORIGINAL : {
                if(isChecked) dough = 10000;
                break;
            }

            case R.id.NAPOLI : {
                if(isChecked) dough = 15000;
                break;
            }

            case R.id.THIN : {
                if(isChecked) dough = 13000;
                break;
            }
        }
        setTotal(); // 체크하면 값이 변환되므로 텍스트뷰를 갱신한다.
    }

    // 토핑을 선택하면 발생하는 체크박스의 onClick 메서드다/
    public void selectTopping(View v) {
        // 체크박스가 체크되었는지 검사한다.
        boolean isChecked = ((CheckBox)v).isChecked();
        // 체크박스의 아이디를 받아온 후 switch문으로 분기한다.
        // 각각의 토핑이 체크되면 토핑값을 더하고, 체크가 해제되면 토핑값을 뺀다.
        switch (v.getId()) {
            case R.id.PEPERANI : {
                if(isChecked) topping += 3000;
                else topping -= 3000;
                break;
            }

            case R.id.POTATO : {
                if(isChecked)topping += 2000;
                else topping -= 2000;
                break;
            }

            case R.id.CHEEZE : {
                if(isChecked) topping += 4000;
                else topping -= 4000;
                break;
            }
        }
        setTotal(); // 체크하면 값이 변환되므로 텍스트뷰를 갱신한다.
    }
}
