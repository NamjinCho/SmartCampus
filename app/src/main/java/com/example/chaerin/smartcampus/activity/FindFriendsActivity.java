package com.example.chaerin.smartcampus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaerin.smartcampus.PHPConnector;
import com.example.chaerin.smartcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ChaeRin on 2016-07-02.
 * 내 친구 찾기
 */
public class FindFriendsActivity extends AppCompatActivity {
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    public ListView mListView2 = null;     //NON-FAVORITE FRIEND
    public ListViewAdapter mAdapter2 = null;
    public ArrayList<MyFriendInfo> tempList;
    private Button mSearchFriend;
    public SharedPreferences sf;
    public String myID;
    UpdateMyFriend threadss = new UpdateMyFriend();
    Building mBuilding = new Building();
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            String result = (String)msg.obj;
            if(result.startsWith("update"))
            {
                String []sub = result.split(",");
                //MyFriendInfo temp = tempList.get(Integer.parseInt(sub[1]));
                mAdapter2.mListData = tempList;
                mAdapter2.dataChange();

            }
            if(result.startsWith("clear"))
            {
                mAdapter2.removeAll();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriends);
        sf = getSharedPreferences("smart",MODE_PRIVATE);
        myID=sf.getString("ID","201233333");
        //즐찾친구
        mListView = (ListView) findViewById(R.id.favorite_friend_listview);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //TODO : 클릭시 해당 건물 층을 고른 다음 층 지도로 강의실 표현하기
            }
        });


        //즐찾친구x
        mListView2 = (ListView) findViewById(R.id.friend_listview);

        mAdapter2 = new ListViewAdapter(this);
        mListView2.setAdapter(mAdapter2);

        mAdapter2.sort();


        threadss.start();
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MyFriendInfo mData = mAdapter2.mListData.get(position);
                Log.d("clickListener2", mData.Name + "");
                Toast.makeText(FindFriendsActivity.this, mData.Name, Toast.LENGTH_SHORT).show();
            }
        });

        mSearchFriend = (Button) findViewById(R.id.search_friends);
        mSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SearchFriendActivity.class);
                startActivity(i);
                //finish();
            }
        });

    }
    public void onRestart()
    {
        super.onRestart();
        threadss = new UpdateMyFriend();
        threadss.start();
    }
    public void onStop()
    {
        super.onStop();
        threadss.falg=false;
        threadss = null;
        System.gc();
    }
    private class ViewHolder {
        public ImageButton mStar;
        public TextView mFriend;
        public TextView Location;
        public TextView UpdateTime;
    }
    private class UpdateMyFriend extends Thread{

        boolean falg = true;
        public void run()
        {
            while (falg)
            {
                PHPConnector.getDatas("id="+myID,"friend2.php","friend2");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(PHPConnector.MessageQue.getString("friend2")==null  ){}
                        String result = PHPConnector.MessageQue.getString("friend2");
                        PHPConnector.MessageQue.putString("friend2", null);
                        Log.d("디버깅", result);
                        tempList =null;
                        if(tempList==null)
                            tempList = new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(result);
                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject object = array.getJSONObject(i);
                                MyFriendInfo tempdata = new MyFriendInfo();
                                tempdata.No = object.getString("studentNo");
                                tempdata.Name=object.getString("name");
                                tempdata.dept=object.getString("dept_name");
                                tempdata.Phone = object.getString("phoneNo");
                                tempdata.building = object.getString("building");
                                tempdata.room=object.getString("class");
                                tempdata.Time=object.getString("UpdateTime");
                                //Log.d("디버깅",object.toString());
                                tempList.add(tempdata);
                                Message msg = mHandler.obtainMessage(1,"update,"+(tempList.size()-1));
                                mHandler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    sleep(60000);
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<MyFriendInfo> mListData = new ArrayList<>();

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
        public void removeAll(){
            mListData.clear();
        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                Log.d("디버깅",mListData.get(position).Name);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item2, null);

                holder.mFriend = (TextView) convertView.findViewById(R.id.class_name);
                holder.mStar = (ImageButton) convertView.findViewById(R.id.star);
                holder.Location = (TextView) convertView.findViewById(R.id.location);
                holder.UpdateTime=(TextView) convertView.findViewById(R.id.time);
                holder.mFriend.setFocusable(false);
                holder.mStar.setFocusable(false);

                if (mBuilding.isFavorite(holder.mFriend.getText().toString()))
                    holder.mStar.setImageDrawable(getResources().getDrawable(R.drawable.star_favorite));
                else
                    holder.mStar.setImageDrawable(getResources().getDrawable(R.drawable.star));

                holder.mStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("a", "" + holder.mFriend.getText().toString());
                        int length = mAdapter.getCount();
                        /*
                        if (length == 0) {
                            //즐겨찾는 친구 0명일 때
                            mAdapter.addItem(getResources().getDrawable(R.drawable.star_favorite),
                                    holder.mFriend.getText().toString());
                            mAdapter.sort(); //ㄱㄴㄷ순
                            holder.mStar.setImageResource(R.drawable.star_favorite);
                            //mAdapter2.dataChange();
                            mBuilding.setFavorite(holder.mFriend.getText().toString(), true);
                        }*/
                        for (int i = 0; i < length; i++) {
                            //즐겨찾는 친구 제거
                            MyFriendInfo mData = mAdapter.mListData.get(i);
                            if (holder.mFriend.getText().toString().equals(mData.Name)) {
                                Log.d("삭제", mData.Name + "를 삭제합니다");
                                holder.mStar.setImageResource(R.drawable.star);
                                mAdapter.remove(i);
                                mBuilding.setFavorite(holder.mFriend.getText().toString(), false);
                                break;
                            } else {
                                //즐겨찾기 추가
                                if (i == length - 1) {
                                    mAdapter.addItem(getResources().getDrawable(R.drawable.star_favorite),
                                            mListData.get(i));
                                    mAdapter.sort();
                                    holder.mStar.setImageResource(R.drawable.star_favorite);
                                    mBuilding.setFavorite(holder.mFriend.getText().toString(), true);
                                    break;
                                }
                            }
                        }
                        for (int i = 0; i < mAdapter2.getCount(); i++) {
                            MyFriendInfo mData = mAdapter2.mListData.get(i);
                            if (holder.mFriend.getText().toString().equals(mData.Name)) {
                                ListUpdate(mListData.get(i), i);
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
            MyFriendInfo mData = mListData.get(position);

            if (mData.mStar != null) {
                holder.mStar.setVisibility(View.VISIBLE);
                holder.mStar.setImageDrawable(mData.mStar);
            } else {
                holder.mStar.setVisibility(View.GONE);
            }

            holder.mFriend.setText(mData.Name);
            holder.Location.setText(mData.building+" / "+mData.room+" 호");
            holder.UpdateTime.setText(mData.Time);

            return convertView;
        }

        public void addItem(Drawable icon, MyFriendInfo data) {
            MyFriendInfo addInfo = null;
            addInfo = data;
            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mListData, MyFriendInfo.ALPHA_COMPARATOR);
            dataChange();
        }

        public void ListUpdate(MyFriendInfo data, int i) {
            mAdapter2.remove(i);
            if (data.favorite) {
                mAdapter2.addItem(getResources().getDrawable(R.drawable.star_favorite), data);
            } else {
                mAdapter2.addItem(getResources().getDrawable(R.drawable.star), data);
            }

        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }

    }
}