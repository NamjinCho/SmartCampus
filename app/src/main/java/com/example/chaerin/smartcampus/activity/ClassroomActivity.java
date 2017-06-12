package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaerin.smartcampus.PHPConnector;
import com.example.chaerin.smartcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ChaeRin on 2016-08-12.
 */
public class ClassroomActivity extends Activity {
    // 가천관 { 층, 강의실 }
    String[][] mGachonClassroom = {{"B2", "B201"}, {"B2", "B202"}, {"B1", "B101"}, {"1", "101"}, {"5", "501"}};
    String[][] mEngineerClassroom = {};
    String[][] mEngineer2Classroom = {};
    String[][] mEducationClassroom = {};
    String[][] mGlobalClassroom = {};
    String[][] mGraduateClassroom = {};
    String[][] mBioClassroom = {};
    String[][] mBioGraduateClassroom = {};
    String[][] mLawClassroom = {};
    String[][] mVisionClassroom = {{"B3", "B301"}, {"B2", "B207"}, {"3", "301"}, {"3", "302"}, {"3", "303"}};
    String[][] mIndustryClassroom = {};
    String[][] mArtClassroom = {};
    String[][] mArt2Classroom = {};
    String[][] mLibraryClassroom = {};
    String[][] mRotcClassroom = {};
    String[][] mStudentClassroom = {};
    String[][] mOrientalClassroom = {};
    String[][] mItClassroom = {{"3", "304"}, {"3", "305"}, {"3", "307"}, {"4", "412"}, {"4", "413"}, {"6", "601"}};
    // String[][] mTemp={{,},{,}};
    String myID;
    TextView layout_category;
    String category;
    String contents;
    String whatFloor;
    int globalPos = -1;
    private ListView mClassList; // 강의실 리스트뷰
    private ArrayList<String> mClassData; // 강의실 명
    private ArrayAdapter<String> mClassAdapter; // 리스트뷰에 사용되는 ArrayAdapter

    private static String TAG = ClassroomActivity.class.getName();
    private static long SLEEP_TIME = 5; // Sleep for some time

    private ArrayList<ClassroomInfo> mClass = Timetable.getClassinfo();
   // ArrayList<ClassroomInfo> mClass = new ArrayList<>();

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(((String)msg.obj).equals("update")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassroomActivity.this);
                builder.setTitle(mClassAdapter.getItem(globalPos) + "호\n")        // 제목 설정
                        .setMessage(contents)        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }else {

            }
        }
    };
    private String serverName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classroom);
        SharedPreferences sf = getSharedPreferences("smart", MODE_PRIVATE);
        myID = sf.getString("ID", "201233333");
        /////// 건물 입력 //////
        layout_category = (TextView) findViewById(R.id.building_and_floor);
        /////// 층 입력 ///////
        category = FindClassroomActivity.passBuilding;

        /*(
        if(FloorAndClassroomActivity.tempFloor<0){ //지하
            String str = String.valueOf(FloorAndClassroomActivity.tempFloor);
            whatFloor = "B"+str.substring(1) + "층";
        } else { //지상
            whatFloor = String.valueOf(FloorAndClassroomActivity.tempFloor) + "층";
        }*/
        whatFloor = FloorAndClassroomActivity.tempFloor;

        ////// 강의실 리스트뷰 //////
        mClassList = (ListView) findViewById(R.id.class_listview);
        mClassData = new ArrayList<String>();
        //mClassAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mClassData);


        ////// 건물과 층에 따라 화면 출력 설정 //////
        switch (category) {
            case "가천관":
                switch (whatFloor) {
                    case "B2층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "B2층");
                        break;
                    case "B1층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "B1층");
                        break;
                    case "1층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "1층");
                        break;
                    case "2층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "2층");
                        break;
                    case "3층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "3층");
                        break;
                    case "4층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "4층");
                        break;
                    case "5층":
                        pushFloorList(category + " " + whatFloor, mGachonClassroom, "5층");
                        break;
                }
                break;
            case "공과대학1":
                layout_category.setText("공과대학1");
                break;
            case "공과대학2":
                layout_category.setText("공과대학2");
                break;
            case "교육대학원":
                layout_category.setText("교육대학원");
                break;
            case "글로벌센터":
                layout_category.setText("글로벌센터");
                break;
            case "대학원":
                layout_category.setText("대학원");
                break;
            case "바이오나노대학":
                layout_category.setText("바이오나노대학");
                break;
            case "바이오나노연구원":
                layout_category.setText("바이오나노연구원");
                break;
            case "법과대학":
                layout_category.setText("법과대학");
                break;
            case "비전타워":
                switch (whatFloor) {
                    case "B3층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "B3층");
                        break;
                    case "B2층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "B2층");
                        break;
                    case "B1층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "B1층");
                        break;
                    case "1층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "1층");
                        break;
                    case "2층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "2층");
                        break;
                    case "3층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "3층");
                        break;
                    case "4층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "4층");
                        break;
                    case "5층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "5층");
                        break;
                    case "6층":
                        pushFloorList(category + " " + whatFloor, mVisionClassroom, "6층");
                        break;
                }
                break;
            case "산학협력관":
                layout_category.setText("산학협력관");
                break;
            case "예술대학1":
                layout_category.setText("예술대학1");
                break;
            case "예술대학2":
                layout_category.setText("예술대학2");
                break;
            case "중앙도서관":
                layout_category.setText("중앙도서관");
                break;
            case "학군단":
                layout_category.setText("학군단");
                break;
            case "학생회관":
                layout_category.setText("학생회관");
                break;
            case "한의과대학":
                layout_category.setText("한의과대학");
                break;
            case "IT대학":
                serverName = "IT_collage";
                switch (whatFloor) {
                    case "1층":
                        pushFloorList(category + " " + whatFloor, mItClassroom, "1층");
                        break;
                    case "2층":
                        pushFloorList(category + " " + whatFloor, mItClassroom, "2층");
                        break;
                    case "3층":
                        pushFloorList(category + " " + whatFloor, mItClassroom, "3층");
                        break;
                    case "4층":
                        pushFloorList(category + " " + whatFloor, mItClassroom, "4층");
                        break;
                    case "5층":
                        pushFloorList(category + " " + whatFloor, mItClassroom, "5층");
                        break;
                    case "6층":
                        pushFloorList(category + " " + whatFloor, mItClassroom, "6층");
                        break;
                }
                break;
        }
    }

    private void pushFloorList(String title, final String mTempClassroom[][], String temp) {
        String[][] mTemp = {};
        String str = String.valueOf(temp);

        layout_category.setText(title);

        for (int n = 0; n < mTempClassroom.length; n++) {
            if (mTempClassroom[n][0].equals(str.substring(0, temp.length() - 1))) {
                mTemp = mTempClassroom;
            }
        }
        for (int n = 0; n < mTemp.length; n++) {
            if (mTemp[n][0].equals(str.substring(0, temp.length() - 1))) {
                mClassData.add(("" + mTemp[n][1]));
            }
        }
        mClassAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mClassData);
        mClassList.setAdapter(mClassAdapter);
        mClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(ClassroomActivity.this, mClassAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                //요일 & 날짜
                Calendar cal = Calendar.getInstance();
                int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                String week = returnDay(day_of_week);
                globalPos = position;
                String minT="";
                String hourT="";
                if (min < 10)
                    minT = "0"+min;
                else
                    minT=min+"";
                if(hour<10)
                    hourT="0"+hour;
                else
                    hourT=""+hour;

                PHPConnector.getDatas("build=" + serverName +
                        "&class=" + mClassAdapter.getItem(position)
                        + "&time=" + hourT+":"+minT
                        + "&day=" + week, "getc.php", "getclassinfo");
                PHPConnector.getDatas("id="+myID+"&classroom=" + mClassAdapter.getItem(position)
                        + "&building=" + serverName, "findclassnum.php", "findclass");
                Log.d("디버깅ㅇ", "" + "build=" + serverName +
                        "&class=" + mClassAdapter.getItem(position)
                        + "&time=" + hourT+":"+minT
                        + "&day=" + week);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (PHPConnector.MessageQue.getString("getclassinfo") != null) {
                                //PHPConnector.MessageQue.putString("getclassinfo", null);
                                break;
                            }
                        }
                        while (true) {
                            if (PHPConnector.MessageQue.getString("findclass") != null) {
                                break;
                            }
                        }
                        getClassFromQue();
                    }
                });
                thread.start();


                // 여기서 부터는 알림창의 속성 설정
                Log.d("activity", "" + position);
                //TODO : 강의중일 때와 공강일 경우 나눠서 dialog 뜨게 하기.

            }
        });
    }

    public void getClassFromQue() {
        String result = PHPConnector.MessageQue.getString("getclassinfo");
        String result2 = PHPConnector.MessageQue.getString("findclass");
        PHPConnector.MessageQue.putString("getclassinfo", null);
        PHPConnector.MessageQue.putString("findclass", null);
        contents="";
        Log.d("디버깅",result);

        Log.d("디버깅2",result2);
        if(result.equals("[]"))
        {
            contents = "현재 이 강의실은 수업이 없습니다.\n";
            if(!result2.equals("[]")) {
                contents+="현재 강의실 내 친구는 "+"0 명 입니다\n";
            }else {
                JSONArray jarray = null;
                try {
                    jarray = new JSONArray(result2.split("@#")[0]);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jObject = jarray.getJSONObject(i);
                        String freindsNum = jObject.getString("fcount");
                        contents += "현재 강의실 내 친구는 " + freindsNum + " 명 입니다\n";
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Log.d("activity,phpconnector", "" + result);
            try {
                JSONArray jarray = new JSONArray(result);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = jarray.getJSONObject(i);
                    String CourseID = jObject.getString("courseID");
                    String Title = jObject.getString("title");
                    String Time1 = jObject.getString("time1");
                    String Time2 = jObject.getString("time2");
                    String Time3 = jObject.getString("time3");
                    String Time4 = jObject.getString("time4");
                    String DeptName = jObject.getString("dept_name");
                    if(!Time1.equals("null")){
                        Time1= Time1.trim();
                        Time1 = charToTime(Time1);
                        contents = "학수번호 : " + CourseID + "\n" +
                                "과목명 : " + Title + "\n" +
                                "강의 시간표 \n" + Time1 + "\n";
                    }
                    if (!Time2.equals("null"))
                        Time2 = charToTime(Time2);
                        contents += Time2 + "\n";
                    if (!Time3.equals("null"))
                        Time3 = charToTime(Time3);
                        contents += Time3 + "\n";
                    if (!Time4.equals("null"))
                        Time4 = charToTime(Time4);
                        contents += Time4 + "\n";
                    break;
                }
                if (!result2.equals("[]")) {
                    contents += "현재 강의실 내 친구는 " + "0 명 입니다\n";
                } else {
                    jarray = new JSONArray(result2.split("@#")[0]);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jObject = jarray.getJSONObject(i);
                        String freindsNum = jObject.getString("fcount");
                        contents += "현재 강의실 내 친구는 " + freindsNum + " 명 입니다\n";
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Message msg = mHandler.obtainMessage(1,"update");
        mHandler.sendMessage(msg);
    }

    //요일
    public String returnDay(int day_of_week){
        String week;
        if (day_of_week == 1) {
            week = "Sun";
        } else if (day_of_week == 2) {
            week = "Mon";
        } else if (day_of_week == 3) {
            week = "Tue";
        } else if (day_of_week == 4) {
            week = "Wed";
        } else if (day_of_week == 5) {
            week = "Thu";
        } else if (day_of_week == 6) {
            week = "Fri";
        } else if (day_of_week == 7) {
            week = "Sat";
        } else {
            week = "요일을 불러오지 못했습니다.";
        }
        return week;
    }

    public String charToTime(String Day){
        String d="";
        if(Day.length()==4) {
            d = Day.substring(Day.length() - 1, Day.length());
        } else if(Day.length()==5){
            d = Day.substring(Day.length() - 2, Day.length());
        }
        String returnD = "";

        switch (d) {
            case "1":
                returnD = "09:00 ~ 09:50";
                break;
            case "2":
                returnD = "10:00 ~ 10:50";
                break;
            case "3":
                returnD = "11:00 ~ 11:50";
                break;
            case "4":
                returnD = "12:00 ~ 12:50";
                break;
            case "5":
                returnD = "13:00 ~ 13:50";
                break;
            case "6":
                returnD = "14:00 ~ 14:50";
                break;
            case "7":
                returnD = "15:00 ~ 15:50";
                break;
            case "8":
                returnD = "16:00 ~ 16:50";
                break;
            case "9":
                returnD = "17:00 ~ 17:50";
                break;
            case "10":
                returnD = "18:00 ~ 18:50";
                break;
            case "11":
                returnD = "19:00 ~ 19:50";
                break;
            case "12":
                returnD = "20:00 ~ 20:50";
                break;
            case "13":
                returnD = "21:00 ~ 21:50";
                break;
            case "14":
                returnD = "22:00 ~ 22:50";
                break;
            case "A":
                returnD = "09:30 ~ 10:45";
                break;
            case "B":
                returnD = "11:00 ~ 12:15";
                break;
            case "C":
                returnD = "12:30 ~ 13:45";
                break;
            case "D":
                returnD = "14:00 ~ 15:15";
                break;
            case "E":
                returnD = "15:30 ~ 16:45";
                break;
        }
        return returnD;
    }
}
