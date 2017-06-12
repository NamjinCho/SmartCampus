package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.chaerin.smartcampus.BluetoothService;
import com.example.chaerin.smartcampus.PHPConnector;
import com.example.chaerin.smartcampus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChaeRin on 2016-07-03.
 * 스플래시화면
 */
public class SplashActivity extends Activity {
    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 5; // Sleep for some time

    // Debugging
    //private static final String TAG = "Main";

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothService btService = null;
    public String myID;
    public SharedPreferences sf;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 타이틀바 제거
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // notification바 제거
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        sf=getSharedPreferences("smart",MODE_PRIVATE);
        myID = sf.getString("ID","201233333");
        Log.d("아이디",myID);
        // 타이머 설정 및 런처 실행
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();

        // BluetoothService 클래스 생성
        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }
        if(btService.getDeviceState()) {
            // 블루투스가 지원 가능한 기기일 때
            btService.enableBluetooth();
        } else {
            finish();
        }
    }
    public void init()
    {
        PHPConnector.getDatas("ID="+myID,"getclass.php","Class");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(PHPConnector.MessageQue.getString("Class")!=null)
                    {
                        getDataFromQue();
                        PHPConnector.MessageQue.putString("Class",null);
                        break;
                    }
                }
            }
        });
        thread.start();
        PHPConnector.getDatas("ID="+myID,"getclassbeacon.php","classroom_beaconid");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(PHPConnector.MessageQue.getString("classroom_beaconid")!=null)
                    {
                        Log.d("안녕","친구들");
                        getC_BFromQue();
                        PHPConnector.MessageQue.putString("classroom_beaconid",null);
                        break;
                    }
                }
            }
        });
        thread.start();
        PHPConnector.getDatas("id="+myID,"attendancestate.php","attendancestate");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(PHPConnector.MessageQue.getString("attendancestate")!=null)
                    {
                        Log.d("안녕","친구들2");
                        getStateFromQue();
                        PHPConnector.MessageQue.putString("attendancestate",null);
                        break;
                    }
                }
            }
        });
        thread.start();
        PHPConnector.getDatas("","getnbeacon.php","nBeacon");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(PHPConnector.MessageQue.getString("nBeacon")!=null)
                    {
                        Log.d("안녕","친구들2");
                        getNBeaconFromQue();
                        PHPConnector.MessageQue.putString("nBeacon",null);
                        break;
                    }
                }
            }
        });
        thread.start();
    }
    public void getStateFromQue()
    {
        String result = PHPConnector.MessageQue.getString("attendancestate");
        try {
            Log.d("디버깅",result+"_");
            JSONArray jarray = new JSONArray(result);
            for(int i=0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                String Day = jObject.getString("day");
                String Week = jObject.getString("week");
                String Time = jObject.getString("time");
                String Title = jObject.getString("title");
                String state = jObject.getString("state");
                String coureseID=jObject.getString("courseid");
                AttendanceInfo mInfo = new AttendanceInfo();
                mInfo.Title=Title;
                mInfo.Day=Day;
                mInfo.Week=Week;
                mInfo.Time=Time;
                mInfo.Att_State = state;
                mInfo.courseid = coureseID;
                Timetable.putAttinfo(mInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getC_BFromQue()
    {
        String result= PHPConnector.MessageQue.getString("classroom_beaconid");
        Log.d("디버깅_", "_" + result);
        try {
            JSONArray jarray = new JSONArray(result);
            for(int i=0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                String Building = jObject.getString("buliding");
                String beacon =jObject.getString("beaconID");
                String room = jObject.getString("classroom");
                ClassroomBeacon.BeaconID.add(beacon);
                ClassroomBeacon.Building.put(beacon,Building);
                ClassroomBeacon.Room.put(beacon,room);
                Log.d("디버깅","-"+ClassroomBeacon.BeaconID.get(i)+ClassroomBeacon.Building.get(ClassroomBeacon.BeaconID.get(i))+
                        ClassroomBeacon.Room.get(ClassroomBeacon.BeaconID.get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getDataFromQue()
    {
        Timetable.initHashMap();
        //시간표 데이터 가져오기
        if(PHPConnector.MessageQue.getString("Class")!=null)
            Log.d("디버깅_",PHPConnector.MessageQue.getString("Class"));
        try {
            JSONArray jarray = new JSONArray(PHPConnector.MessageQue.getString("Class"));
            Timetable.initHashMap();
            //Log.d("디버깅","_"+Timetable.mapForConvertFromTimeSlot.get("1"));
            int index=1;
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                while(index<5) {
                    String time_slot_id =jObject.getString("time"+index);
                    Log.d("디버깅_",time_slot_id);
                    if(time_slot_id.equals("null"))
                        break;
                    index++;
                    String Building = jObject.getString("building");
                    String classroom = jObject.getString("classroom");
                    String title = jObject.getString("title");
                    String professor = jObject.getString("instructor_name");
                    Timetable.insertData(time_slot_id,Building,classroom,title,professor);
                }
                index=1;
            }
            Timetable.sort_Class();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getNBeaconFromQue()
    {
        String result = PHPConnector.MessageQue.getString("nBeacon");
        try {
            Log.d("디버깅",result+"엔");

            JSONArray jarray = new JSONArray(result);
            Log.d("안녕",jarray.length()+"_");
            for(int i=0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                Log.d("안녕","안녕");
                String BeaconID = jObject.get("beaconID").toString();
                Log.d("안녕","안녕"+BeaconID);
                String Building = jObject.get("buliding").toString();
                String location = jObject.get("classroom").toString();
                nBeacon.BeaconID.add(BeaconID);
                nBeacon.Building.put(BeaconID,Building);
                nBeacon.location.put(BeaconID,location);
                nBeacon.flags.put(BeaconID,true);
            }
            for(int i=0;i<nBeacon.BeaconID.size();i++)
            {
                Log.d("왓더",nBeacon.BeaconID.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {

                } else {

                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * 몇초 쉰 후 , 새로운 액티비티 실행
         */
        public void run() {
            try {
                //Thread.sleep(SLEEP_TIME*1500);
                init();
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            Intent ServiceIntent = new Intent(SplashActivity.this,ScanService.class);
            startService(ServiceIntent);

            //TODO: LoginActivity로 바꾸기
            Intent intent = new Intent(SplashActivity.this,
                    MenuActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
