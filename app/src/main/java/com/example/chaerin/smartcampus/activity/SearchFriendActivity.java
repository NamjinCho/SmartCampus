package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaerin.smartcampus.PHPConnector;
import com.example.chaerin.smartcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by ChaeRin on 2016-07-22.
 */
public class SearchFriendActivity extends Activity {
    String[] list = {
            "이름", "학번", "학과"
    };
    private String mSearchChoice;

    Spinner s1; //search spinner
    private Button mBtnSearch;
    private EditText mInput;
    private SharedPreferences shpf;
    ArrayList<FriendInfo> mData = new ArrayList<>();
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private SharedPreferences.Editor editor;
    private int mWhat; // spinner 에서 무엇(이름,학번,학과)을 선택했는지.
    public String myID;
    public SharedPreferences sf;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(((String)msg.obj).equals("invalidate")) {
                mAdapter=null;
                System.gc();
                mAdapter = new ListViewAdapter(getApplicationContext(), mData);
                mListView.setAdapter(mAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchfriend);
        shpf = getSharedPreferences("myFriends",MODE_PRIVATE);
        editor = shpf.edit();
        //editor.putString("",);
        sf=getSharedPreferences("smart",MODE_PRIVATE);
        myID=sf.getString("ID","201233333");
        //리스트뷰
        mListView = (ListView) findViewById(R.id.searchfriendlistview);
        mAdapter = new ListViewAdapter(this, mData);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                final FriendInfo mData = mAdapter.mListData.get(position);
                Toast.makeText(SearchFriendActivity.this, mData.studentName, Toast.LENGTH_SHORT).show();

                //TODO: 자기자신은 친구추가 안되도록 해야 됨.

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchFriendActivity.this);
                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle("친구 추가하기")        // 제목 설정
                        .setMessage(mData.studentName+"님을 친구 추가하시겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //TODO: 서버에 업데이트하기.
                                PHPConnector.getDatas("myid="+myID+"&id="+mData.studentNum,"addfriend.php","addfriend");
                                Toast.makeText(SearchFriendActivity.this,mData.studentName+"님 친구등록이 완료되었습니다",Toast.LENGTH_SHORT);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }
        });

        s1 = (Spinner) findViewById(R.id.search_list); //이름/학번/학과 선택
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        s1.setAdapter(adapter);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        mSearchChoice = "NAME";
                        mWhat = 0;
                        break;
                    case 1:
                        mSearchChoice = "STUDENTNO";
                        mWhat = 1;
                        break;
                    case 2:
                        mSearchChoice = "DEPT_NAME";
                        mWhat = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mInput = (EditText) findViewById(R.id.input);
        mBtnSearch = (Button) findViewById(R.id.search); //검색버튼
        // 버튼 이벤트
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String input = mInput.getText().toString().trim(); //이름or학번or학과 입력란

                Cursor cursor;
                //cursor = db.rawQuery("");

                //입력란 확인 및 학번 9자리 확인
                if (!input.isEmpty() && (mWhat == 0 || mWhat == 2)) {
                    Log.d("이름 or 학과", "ㅇㅎ" + mWhat);
                    searchInput(input, mWhat);
                    //registerUser(name, stu_id, password, myCategory);
                } else if (!input.isEmpty() && input.length() == 9 && mWhat == 1) {
                    Log.d("학번", "ㅎ");
                    if (Pattern.matches("^[0-9]+$", input)) {
                        //숫자만 입력
                        Log.d("학번", "숫자" + mWhat);
                        searchInput(input, mWhat);
                    } else { // 그 외 입력
                        Log.d("학번", "숫자 아님");
                        Toast.makeText(getApplicationContext(),
                                "숫자만 입력해주세요.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "값을 모두 또는 정확히 입력하세요", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public void searchInput(final String input, int mWhat) {

        mWhat += 1;
        PHPConnector.getDatas("id=" + input + "&sel=" + mWhat, "findfriend.php", "findfriend");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (PHPConnector.MessageQue.getString("findfriend") != null) {
                        getFriendFromQue();
                        PHPConnector.MessageQue.putString("findfriend", null);
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    private void getFriendFromQue() {
        String result = PHPConnector.MessageQue.getString("findfriend");

        try {
            Log.d("디버깅", result + "_");
            JSONArray jarray = new JSONArray(result);
            mData = new ArrayList<>();
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                String studentNum = jObject.getString("No");
                String studentName = jObject.getString("name");
                String studentDept = jObject.getString("dept_name");
                FriendInfo mInfo = new FriendInfo();
                mInfo.studentNum = studentNum;
                mInfo.studentName = studentName;
                mInfo.studentDept = studentDept;
                mData.add(mInfo);
            }
            Message msg = mHandler.obtainMessage(1,"invalidate");
            mHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<FriendInfo> mListData = new ArrayList<>();

        public ListViewAdapter(Context mContext, ArrayList<FriendInfo> data) {
            super();
            this.mContext = mContext;
            mListData = data;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        public void addItem(FriendInfo data) {
            mListData.add(data);
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.searchfriendlistview_layout, null);

                TextView number = (TextView) convertView.findViewById(R.id.number);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView dept = (TextView) convertView.findViewById(R.id.dept);

                FriendInfo temp = mListData.get(position);
                number.setText(temp.studentNum);
                name.setText(temp.studentName);
                dept.setText(temp.studentDept);
            }
            return convertView;
        }
    }
}
