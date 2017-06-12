package com.example.chaerin.smartcampus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaerin.smartcampus.R;

import java.util.ArrayList;

/**
 * Created by ChaeRin on 2016-08-09.
 */
public class FloorAndClassroomActivity extends Activity {

    /** 18개 건물 순서
     * 가천관, 공과대학1, 공과대학2, 교육대학원, 글로벌센터, 대학원, 바이오나노대학, 바이오나노대학연구원,
     * 법과대학, 비전타워, 산학협력관, 예술대학1, 예술대학2, 중앙도서관, 학군단, 학생회관, 한의과대학, IT대학
     */
    // 두 번 째는 지하 층 수 사용여부 (ex. 지하3층~지하1층 -> -3, 지하층 사용x -> 0)
    // ex. {6, -3} : B3, B2, B1, 1, 2, 3, 4, 5, 6
    String[][] mBuildingAndFloor={{"5","-2"},{"3","0"},{"3","0"},{"3","0"},{"3","0"},{"3","0"}
            ,{"3","0"},{"3","0"},{"3","0"},{"6","-3"},{"3","0"},{"3","0"}
            ,{"3","0"},{"3","0"},{"3","0"},{"3","0"},{"3","0"},{"6","0"}};

    TextView layout_category;
    String category;

   // private LinearLayout dynamicLayout;
    private ListView floorListview;
    private ArrayAdapter<String> mFloorAdapter;
    final ArrayList<String> mFloorData= new ArrayList<String>();

    private final int DYNAMIC_VIEW_ID = 0x8000;
    private int mFloor = 0;
   // public static int tempFloor;
    public static String tempFloor;
    private int mClass = 0;
    private int temp; //층출력계산

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floor_and_classroom);

        layout_category = (TextView) findViewById(R.id.category);
        category = FindClassroomActivity.passBuilding;

       // dynamicLayout = (LinearLayout)findViewById(R.id.dynamicLayout);
        floorListview = (ListView)findViewById(R.id.floor_listview);

        switch (category)
        {
            case "가천관":
                layout_category.setText("가천관");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[0][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[0][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[0][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[0][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[0][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[0][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[0][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[0][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[0][0])-1);
                }
                break;
            case "공과대학1":
                layout_category.setText("공과대학1");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[1][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[1][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[1][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[1][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[1][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[1][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[1][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[1][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[1][0])-1);
                }
                 break;
            case "공과대학2":
                layout_category.setText("공과대학2");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[2][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[2][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[2][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[2][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[2][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[2][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[2][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[2][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[2][0])-1);
                }
                break;
            case "교육대학원":
                layout_category.setText("교육대학원");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[3][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[3][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[3][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[3][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[3][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[3][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[3][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[3][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[3][0])-1);
                }
                break;
            case "글로벌센터":
                layout_category.setText("글로벌센터");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[4][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[4][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[4][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[4][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[4][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[4][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[4][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[4][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[4][0])-1);
                }
                break;
            case "대학원":
                layout_category.setText("대학원");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[5][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[5][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[5][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[5][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[5][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[5][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[5][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[5][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[5][0])-1);
                }
                break;
            case "바이오나노대학":
                layout_category.setText("바이오나노대학");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[6][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[6][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[6][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[6][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[6][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[6][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[6][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[6][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[6][0])-1);
                }
                break;
            case "바이오나노연구원":
                layout_category.setText("바이오나노연구원");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[7][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[7][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[7][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[7][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[7][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[7][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[7][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[7][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[7][0])-1);
                }
                break;
            case "법과대학":
                layout_category.setText("법과대학");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[8][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[8][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[8][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[8][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[8][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[8][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[8][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[8][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[8][0])-1);
                }
                break;
            case "비전타워":
                layout_category.setText("비전타워");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[9][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[9][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[9][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[9][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[9][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[9][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[9][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[9][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[9][0])-1);
                }
                break;
            case "산학협력관":
                layout_category.setText("산학협력관");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[10][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[10][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[10][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[10][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[10][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[10][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[10][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[10][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[10][0])-1);
                }
                break;
            case "예술대학1":
                layout_category.setText("예술대학1");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[11][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[11][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[11][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[11][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[11][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[11][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[11][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[11][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[11][0])-1);
                }
                break;
            case "예술대학2":
                layout_category.setText("예술대학2");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[12][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[12][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[12][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[12][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[12][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[12][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[12][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[12][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[12][0])-1);
                }
                break;
            case "중앙도서관":
                layout_category.setText("중앙도서관");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[13][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[13][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[13][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[13][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[13][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[13][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[13][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[13][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[13][0])-1);
                }
                break;
            case "학군단":
                layout_category.setText("학군단");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[14][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[14][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[14][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[14][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[14][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[14][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[14][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[14][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[14][0])-1);
                }
                break;
            case "학생회관":
                layout_category.setText("학생회관");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[15][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[15][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[15][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[15][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[15][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[15][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[15][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[15][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[15][0])-1);
                }
                break;
            case "한의과대학":
                layout_category.setText("한의과대학");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[16][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[16][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[16][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[16][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[16][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[16][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[16][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[16][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[16][0])-1);
                }
                break;
            case "IT대학":
            layout_category.setText("IT대학");
                //지하
                while(Integer.parseInt(mBuildingAndFloor[17][1])<0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[17][1]);
                    pushFloorButton(mFloor);
                    mBuildingAndFloor[17][1] = String.valueOf(Integer.parseInt(mBuildingAndFloor[17][1]) + 1);
                }
                temp = Integer.parseInt(mBuildingAndFloor[17][0]);
                //지상
                while(Integer.parseInt(mBuildingAndFloor[17][0])>0){
                    mFloor = Integer.parseInt(mBuildingAndFloor[17][0]);
                    Log.d("mFloor",""+mFloor);
                    pushFloorButton(temp-mFloor+1);
                    mBuildingAndFloor[17][0] = String.valueOf(Integer.parseInt(mBuildingAndFloor[17][0])-1);
                }
            break;
        }
    }

    private void pushFloorButton(final int floor){
        final String[][] mTemp = {};
        if(floor<0){ //지하
            String str = String.valueOf(floor);
            mFloorData.add("B" + str.substring(1) + "층");
        } else { //지상
            mFloorData.add(String.valueOf(floor) + "층");
        }
        mFloorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,mFloorData);
        floorListview.setAdapter(mFloorAdapter);
        floorListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(FloorAndClassroomActivity.this, mFloorAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                //tempFloor = floor;
                tempFloor = mFloorAdapter.getItem(position);
                Intent i = new Intent(getApplicationContext(),
                        ClassroomActivity.class);
                startActivity(i);
            }
        });
    }
  /**  private void pushFloorButton(final int floor){
        final Button dynamicButton = new Button(this);
        dynamicButton.setId(floor);
        if(floor<0){ //지하
            String str = String.valueOf(floor);
            dynamicButton.setText("B"+str.substring(1) + "층");
        } else { //지상
            dynamicButton.setText(String.valueOf(floor) + "층");
        }
        dynamicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempFloor = floor;
                Intent i = new Intent(getApplicationContext(),
                        ClassroomActivity.class);
                Log.d("다이나믹", "" + dynamicButton.getText());
                startActivity(i);
              //  finish();
            }
        });
        dynamicLayout.addView(dynamicButton, new ActionBar.LayoutParams
                (ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
    }*/
    private void popFloorButton(){

    }
}
