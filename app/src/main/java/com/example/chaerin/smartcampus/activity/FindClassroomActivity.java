package com.example.chaerin.smartcampus.activity;

/**
 * 강의실찾기
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaerin.smartcampus.R;

import java.util.ArrayList;
import java.util.Collections;

public class FindClassroomActivity extends AppCompatActivity {
    private ListView mListView = null;      //FAVORITE CLASSROOM
    private ListViewAdapter mAdapter = null;
    private ListView mListView2 = null;     //NON-FAVORITE CLASSROOM
    private ListViewAdapter mAdapter2 = null;
    Building mBuilding = new Building();
    public static String alarmMessage;
    private SharedPreferences shpf;
    private SharedPreferences.Editor editor;

    public static String passBuilding="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findclassroom);
        //String intentD = getIntent().getStringExtra("dialogT");
        // String intentC = getIntent().getStringExtra("dialogTitle");
        if(alarmMessage!=null);
        {
           // Log.d("디버깅",intentD);
            //getIntent().putExtra("dialog","");
            AlertDialog.Builder builder = new AlertDialog.Builder(FindClassroomActivity.this);
            builder.setTitle("강의실 알람")        // 제목 설정
                    .setMessage(alarmMessage)        // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();// 알림창 객체 생성
            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();    // 알림창 띄우기
            alarmMessage = null;
        }
        shpf = getSharedPreferences("myClass",MODE_PRIVATE);
        editor = shpf.edit();


        //TODO : 즐겨찾는 건물 프레그먼트 수정하기
        // 해야 할 것 : 즐겨찾는 건물 프레그먼트에 있는 경우 : 노란 별
        // 별 클릭하면 즐찾 건물 프레그먼트에서 사라지고 회색 별로 변한다.
        mListView = (ListView) findViewById(R.id.favorite_class_listview);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        Log.d("as", "aa");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //TODO : 클릭시 해당 건물 층을 고른 다음 층 지도로 강의실 표현하기
                ListData mData = mAdapter.mListData.get(position);
                Log.d("clickListener", mData.mClass + "");
                Toast.makeText(FindClassroomActivity.this, mData.mClass, Toast.LENGTH_SHORT).show();

                passBuilding = mData.mClass;
                Intent i = new Intent(getApplicationContext(),
                        FloorAndClassroomActivity.class);
                startActivity(i);
               // finish();
            }
        });
        Log.d("ss", "dd");
        //TODO:: 즐겨찾는 건물 프레그먼트 수정 여기까지!!

        mListView2 = (ListView) findViewById(R.id.class_listview);

        mAdapter2 = new ListViewAdapter(this);
        mListView2.setAdapter(mAdapter2);

        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "가천관");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "예술대학1");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "바이오나노대학");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "공과대학1");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "중앙도서관");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "학군단");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "학생회관");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "예술대학2");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "교육대학원");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "IT대학");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "글로벌센터");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "법과대학");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "비전타워");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "바이오나노연구원");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "한의과대학");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "공과대학2");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "산학협력관");
        mAdapter2.addItem(getResources().getDrawable(R.drawable.star),
                "대학원");
        mAdapter2.sort();


        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListData mData = mAdapter2.mListData.get(position);
                Log.d("clickListener2", mData.mClass+"");
                Toast.makeText(FindClassroomActivity.this, mData.mClass, Toast.LENGTH_SHORT).show();

                passBuilding = mData.mClass;
                Intent i = new Intent(getApplicationContext(),
                        FloorAndClassroomActivity.class);
                startActivity(i);
               // finish();
            }
        });

    }

    private class ViewHolder {
        public ImageButton mStar;
        public TextView mClass;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.mClass = (TextView) convertView.findViewById(R.id.class_name);
                holder.mStar = (ImageButton) convertView.findViewById(R.id.star);

                //setFocusable(false)안할경우 textview 클릭 이벤트 실행 안됨.
                holder.mClass.setFocusable(false);
                holder.mStar.setFocusable(false);


                if (mBuilding.isFavorite(holder.mClass.getText().toString()))
                    holder.mStar.setImageDrawable(getResources().getDrawable(R.drawable.star_favorite));
                else
                    holder.mStar.setImageDrawable(getResources().getDrawable(R.drawable.star));
                holder.mStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("a", "" + holder.mClass.getText().toString());
                        int length = mAdapter.getCount();
                        if (length == 0 ) {
                            //즐겨찾기건물 0개일 때
                            mAdapter.addItem(getResources().getDrawable(R.drawable.star_favorite),
                                    holder.mClass.getText().toString());
                            mAdapter.sort(); //ㄱㄴㄷ순
                            holder.mStar.setImageResource(R.drawable.star_favorite);
                            //mAdapter2.dataChange();
                            mBuilding.setFavorite(holder.mClass.getText().toString(), true);
                        }
                        for (int i = 0; i < length; i++) {
                            //즐겨찾기건물 제거
                            ListData mData = mAdapter.mListData.get(i);
                            if (holder.mClass.getText().toString().equals(mData.mClass)) {
                                Log.d("삭제", mData.mClass + "를 삭제합니다");
                                holder.mStar.setImageResource(R.drawable.star);
                                mAdapter.remove(i);
                                mBuilding.setFavorite(holder.mClass.getText().toString(), false);
                                break;
                            } else {
                                //즐겨찾기 추가
                                if (i == length - 1) {
                                    if(i==5){ //즐찾건물 최대 6개까지만 가능
                                        Log.d("즐찾건물","즐찾");
                                        Toast.makeText(FindClassroomActivity.this,"즐겨찾는 건물은 최대 6개까지만 지원됩니다.",Toast.LENGTH_SHORT);
                                        break;
                                    }
                                    mAdapter.addItem(getResources().getDrawable(R.drawable.star_favorite),
                                            holder.mClass.getText().toString());
                                    mAdapter.sort();
                                    holder.mStar.setImageResource(R.drawable.star_favorite);
                                    mBuilding.setFavorite(holder.mClass.getText().toString(), true);
                                    break;
                                }
                            }
                        }
                        for(int i =0;i<mAdapter2.getCount();i++)
                        {
                            ListData mData = mAdapter2.mListData.get(i);
                            if (holder.mClass.getText().toString().equals(mData.mClass)) {
                                ListUpdate(holder.mClass.getText().toString(),i);
                                mAdapter2.sort();
                                break;
                            }
                        }
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ListData mData = mListData.get(position);

            if (mData.mStar != null) {
                holder.mStar.setVisibility(View.VISIBLE);
                holder.mStar.setImageDrawable(mData.mStar);
            } else {
                holder.mStar.setVisibility(View.GONE);
            }

            holder.mClass.setText(mData.mClass);

            return convertView;
        }

        public void addItem(Drawable icon, String mClass) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mStar = icon;
            addInfo.mClass = mClass;

            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void ListUpdate(String str, int i) {
            mAdapter2.remove(i);
            if (mBuilding.isFavorite(str)) {
                mAdapter2.addItem(getResources().getDrawable(R.drawable.star_favorite), str);
            } else {
                mAdapter2.addItem(getResources().getDrawable(R.drawable.star), str);
            }

        }


        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

    }
}
