package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaerin.smartcampus.R;

import java.util.ArrayList;


/**
 * Created by ChaeRin on 2016-07-02.
 * 출석현황
 */
public class StateCheckActivity extends Activity {
    ArrayList<AttendanceInfo> mData = Timetable.getAttinfo();
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    static boolean fromTimeTable = false;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statecheck);
        t = (TextView) findViewById(R.id.className);
        mListView = (ListView) findViewById(R.id.statelistview);
        t.setText("출석현황");
        if (fromTimeTable) {
            t.setText(TimeTableActivity.tempClass.ClassName);
        }

        if(TimeTableActivity.tempClass!=null) {
            ArrayList<AttendanceInfo> mData2 = new ArrayList<>();
            for(int i=0;i<mData.size();i++)
            {
                if(mData.get(i).Title.equals(TimeTableActivity.tempClass.ClassName))
                {
                    mData2.add(mData.get(i));
                }
            }
            mAdapter = new ListViewAdapter(this,mData2);
        }
        else
            mAdapter = new ListViewAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),mAdapter.getItem(position).Title + "\n"+ mAdapter.getItem(position).Time,Toast.LENGTH_SHORT).show();
            }
        });
        fromTimeTable = false;
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        TimeTableActivity.tempClass=null;
    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<AttendanceInfo> mListData = new ArrayList<>();

        public ListViewAdapter(Context mContext, ArrayList<AttendanceInfo> data) {
            super();
            this.mContext = mContext;
            mListData = data;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public AttendanceInfo getItem(int position) {
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
                convertView = inflater.inflate(R.layout.statelistview_layout, null);
            }
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView week = (TextView) convertView.findViewById(R.id.week);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView state = (TextView) convertView.findViewById(R.id.state);
            AttendanceInfo temp = mListData.get(position);

            title.setText(temp.Title);
            week.setText(temp.Week + " 주차");
            time.setText(temp.Time);
            if (temp.Att_State.equals("0")) {
                state.setText("미출결");
            } else if (temp.Att_State.equals("1")) {
                state.setText("출석");
            } else if (temp.Att_State.equals("2")) {
                state.setText("지각");
            } else if (temp.Att_State.equals("3")) {
                state.setText("결석");
            } else if (temp.Att_State.equals("4")) {
                state.setText("조퇴");
            }

            return convertView;
        }
    }

}