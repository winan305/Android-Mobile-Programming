package com.example.administrator.bookmark;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Bookmark { // 즐겨찾기 하나의 이름, 링크를 저장할 클래스
    private String name, url; // 즐겨찾기의 이름과 링크 저장 변수를 private 로 선언

    public Bookmark(String name, String url) { // 이름과 링크를 전달받아 객체를 생성하는 생성자 구현
        this.name = name; // 전달받은 이름과 링크로 멤버변수를 초기화한다.
        this.url  = url;
    }

    public String getName() { return name; } // 객체에 저장된 즐겨찾기의 이름을 받환받는 메서드
    public String getURL()  { return url; }  // 객체에 저장된 즐겨찾기의 링크를 반환받는 메서드

    public void setName(String name) { this.name = name; } // 이름을 전달받아 객체의 멤버변수에 저장하는 메서드
    public void setURL(String url)   { this.url = url; }   // 링크를 전달받아 객체의 멤버변수에 저장하는 메서드
}

public class MainActivity extends AppCompatActivity implements ActionMode.Callback{ // 액션모드 콜백함수 인터페이스를 구현한다.
    private static final int ADD_BOOKMARK = 1; // 요청변수를 상수로 선언하고 값을 1로 한다.
    private static final String FILENAME = "bookmarks.txt"; // 즐겨찾기 목록을 저장할 파일이름을 선언한다.
    private ListView bookmarks; // 즐겨찾기 이름들을 표시할 리스트뷰를 선언한다.
    private ArrayAdapter<String> adapter; // 리스트뷰 어댑터를 선언한다. 즐겨찾기 이름의 자료형은 String이다.
    private ArrayList<Bookmark> bookmarkList; // 즐겨찾기들을 저장해둘 리스트를 선언한다.
    private int deleteIndex; // 삭제할 즐겨찾기의 인덱스값을 저장하는 변수다.
    private ActionMode mActionMode; // 액션모드를 위한 액션모드 객체를 선언한다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 초기화 시작 */
        bookmarks = (ListView)findViewById(R.id.BOOKMARKS); // 레이아웃 파일의 리스트뷰와 연결한다.
        bookmarkList = new ArrayList<>(); // 즐겨찾기들을 저장할 리스트를 생성한다.

        // 배열어댑터를 생성하고 레이아웃을 연결해준다.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        bookmarks.setAdapter(adapter); // 리스트뷰에 어댑터를 세팅한다.

        // 즐겨찾기 클릭(리스트뷰 아이템 클릭) 이벤트를 선언한다.
        bookmarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 현재 선택된 즐겨찾기를 리스트에서 가져온다.
                Bookmark selected = bookmarkList.get(position);
                // 선택된 즐겨찾기에서 링크를 반환받아 Uri로 파싱하고 웹으로 켜지도록 액션을 지정하고 인텐트를 생성한다.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selected.getURL()));
                try {
                    // 인텐트를 실행한다.
                    startActivity(intent);
                }
                catch (Exception e) {
                    // 인텐트 실행 시 에러가 나면 링크가 잘못됬음을 표시한다.
                    Toast.makeText(getApplicationContext(), "잘못된 형식의 URL입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 즐겨찾기 롱클릭 이벤트를 선언한다.
        // 롱클릭 시 컨텍스트 액션모드가 활성화되어 액션바에 메뉴가 나타난다.
        bookmarks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 삭제하려는 즐겨찾기의 인덱스(위치)를 저장한다.
                deleteIndex = position;

                // 액션모드가 이미 활성화되어 있으면 false를 리턴한다.
                if (mActionMode != null) {
                    return false;
                }

                // 활성화되어 있지 않다면 액션모드를 활성화한다.
                mActionMode = startSupportActionMode(MainActivity.this);
                view.setSelected(true);
                return true;
            }
        });
        /* 초기화 끝 */
        loadFile(); // 초기화 과정이 끝나면 파일에서 저장된 즐겨찾기들을 로드한다.
    }

    @Override
    protected void onDestroy() {
        // onDestroy 메서드를 오버라이드한다.
        // 앱이 종료될 때 저장된 즐겨찾기 목록들을 파일에 저장한다.
        saveFile(); // 파일을 저장한다.
        super.onDestroy(); // 부모의 onDestroy 메서드를 호출한다.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션바 메뉴를 생성한다.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 액션바 메뉴 선택 이벤트를 정의한다.
        switch(item.getItemId()) {
            // 메뉴 아이디가 쓰기인 경우 인텐트를 생성해서 AddActivity로 요청변수를 전달하여 실행한다.
            case R.id.action_write:
                startActivityForResult(new Intent(MainActivity.this, AddActivity.class), ADD_BOOKMARK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // AddActivity 에서 결과값을 반환받는다.
        // 요청코드와 결과코드가 ADD_BOOKMARK 인 경우 진행한다.
        if(requestCode == ADD_BOOKMARK) {
            if(resultCode == ADD_BOOKMARK) {
                // 결과 인텐트에서 즐겨찾기의 이름과 링크를 받는다.
                String name = data.getStringExtra("name");
                String url = data.getStringExtra("url");
                // 새 객체를 생성하여 북마크를 저장하는 메서드에 전달한다.
                addBookmark(new Bookmark(name, url));
                // 추가된 즐겨찾기의 이름을 토스트로 알려준다.
                Toast.makeText(this, "즐겨찾기 추가 : " + name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addBookmark(Bookmark newBookmark) {
        // 전달받은 새로운 즐겨찾기를 즐겨찾기 리스트에 저장한다.
        bookmarkList.add(newBookmark);
        // 어댑터에 즐겨찾기의 이름을 전달하여 리스트뷰에 뿌려준다.
        adapter.add(newBookmark.getName());
    }

    public void deleteBookmark() {
        // 북마크를 제거하는 함수다.
        // 롱클릭 이벤트 시 저장된 인덱스를 가지고 리스트에서 삭제한다.
        Bookmark removed = bookmarkList.remove(deleteIndex);
        // 어댑터에서도 삭제해준다.
        adapter.remove(removed.getName());
        // 삭제된 즐겨찾기의 이름을 토스트로 보여준다.
        Toast.makeText(this, "즐겨찾기 삭제 : " + removed.getName(), Toast.LENGTH_SHORT).show();
    }

    public void loadFile() {
        // 파일에서 저장된 즐겨찾기 목록을 가져와 리스트와 리스트뷰에 저장한다.
        try {
            // 파일을 읽기 위해 인풋스트림을 연다.
            FileInputStream fis = openFileInput(FILENAME);
            // 파일의 길이만큼 버퍼를 생성한다.
            byte[] buffer = new byte[fis.available()];
            // 버퍼에 파일의 내용을 읽어들인다.
            fis.read(buffer);
            // 읽어들인 내용을 스트링으로 바꾸어 토큰화한다.
            StringTokenizer load = new StringTokenizer(new String(buffer));
            // 파일은 이름1 링크1 이름2 링크2 이름3 링크3 .. 처럼 저장되어 있다.
            // 즉, 토큰은 두개씩 짝지어야 즐겨찾기 하나가 된다.
            // 토큰이 더 있으면 반복한다.
            while(load.hasMoreTokens()) {
                // 토큰에서 이름과 링크를 가져온다.
                String name = load.nextToken();
                String url  = load.nextToken();
                // 즐겨찾기 객체를 생성해서 즐겨찾기를 더하는 메서드에 전달한다.
                addBookmark(new Bookmark(name, url));
            }
            // 로드 완료 후 파일을 닫는다.
            fis.close();
        }
        // IO처리 예외가 발생하면 에러로그를 출력한다.
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        // 저장된 즐겨찾기를 파일에 저장한다.
        try {
            // 파일을 저장하기 위해 PRIVATE 모드로 연다.
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            // 저장할 목록들을 StringBuilder에 연결한다.
            // String 끼리 더하는 것 보다 StringBuilder를 활용하는 것이 빠르다.
            StringBuilder save = new StringBuilder();
            // 리스트에서 데이터를 가져와 save에 저장한다.
            // save에는 이름1 링크1 이름1 링크1 ... 처럼 저장되어 토큰화 가능하게 한다.
            for(int i = 0; i < bookmarkList.size(); i++) {
                Bookmark data = bookmarkList.get(i);
                String name = data.getName();
                String url  = data.getURL();
                save.append(name + " " + url + " ");
            }
            // save를 스트링화하고 바이트들을 얻어와 파일에 쓴다.
            fos.write(save.toString().getBytes());
            // 파일을 닫는다.
            fos.close();
        }
        // IO처리시 예외가 발생하면 에러로그를 출력한다.
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 컨텍스트 액션 모드에 필요한 메서드 4개를 오버라이드 한다.
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            // 선택된 아이템의 이름이 delete 인 경우 즐겨찾기 제거 메서드를 호출한다.
            case R.id.delete :
                deleteBookmark(); // 제거메서드 호출
                mode.finish(); // 컨텍스트 액션 모드를 종료
                return true; // 이벤트를 처리하였으면 true 반환
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }
}
