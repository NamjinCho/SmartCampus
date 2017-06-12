package com.example.chaerin.smartcampus.activity;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.chaerin.smartcampus.DeviceAdapter;
import com.example.chaerin.smartcampus.PHPConnector;
import com.example.chaerin.smartcampus.R;
import com.example.chaerin.smartcampus.ibeacon.IBeacon;
import com.example.chaerin.smartcampus.util.BleUtil;
import com.example.chaerin.smartcampus.util.ScannedDevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ScanService extends Service implements BluetoothAdapter.LeScanCallback {
    public ArrayList<ScannedDevice> mScanResult;
    public ArrayList<ScannedDevice> mOldScanResult;
    public String buil;
    public String loca;
    public String myID;
    public ArrayList<Boolean> mStackList = new ArrayList<>();
    public int StackCount = 0;
    public SharedPreferences sf;
    int notificationID = 1;
    //DBAdapter db;
    String nowuuid;
    private BluetoothAdapter mBTAdapter;
    private DeviceAdapter mDeviceAdapter;
    private boolean mIsScanning;
    private Scanner mScanner;
    private MyLocationUpdater mlud;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    public int attCheck=0;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
// We want this service to continue running until it is explicitly
// stopped, so return sticky.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        sf = getSharedPreferences("smart", MODE_PRIVATE);
        myID = sf.getString("ID", "201233333");
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        }
        init();
        mScanner = new Scanner();
        mScanner.execute();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AttendanceChecker thread = new AttendanceChecker();
            thread.start();
        }
        mlud = new MyLocationUpdater(getApplicationContext());
        mlud.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        mScanner.StopScanner();
        mlud.stop();
        mlud = null;
        System.gc();
    }

    //Get Beacon Advertsing Data
    //Make List of Beacon
    @Override
    public void onLeScan(final BluetoothDevice newDeivce, final int newRssi,
                         final byte[] newScanRecord) {
        String summary = mDeviceAdapter.update(newDeivce, newRssi, newScanRecord);

    }



    private void init() {
        // BLE check
        if (!BleUtil.isBLESupported(this)) {
            //Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            //finish();
            onDestroy();
            return;
        }

        // BT check
        BluetoothManager manager = BleUtil.getManager(this);
        if (manager != null) {
            mBTAdapter = manager.getAdapter();
        }
        if (mBTAdapter == null) {
            //Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
            onDestroy();
            return;
        }

        mDeviceAdapter = new DeviceAdapter(getApplicationContext(), R.layout.listitem_device,
                new ArrayList<ScannedDevice>());
        mScanResult = new ArrayList<>();
        //mOldScanResult = new ArrayList<>();
        //stopScan();
    }
    //Start Beacon Scanning
    private void startScan() {
        if ((mBTAdapter != null) && (!mIsScanning)) {
            mBTAdapter.startLeScan(this);
            mIsScanning = true;
            //setProgressBarIndeterminateVisibility(true);
            //invalidateOptionsMenu();
        }
    }

    private void stopScan() {
        if (mBTAdapter != null) {
            mBTAdapter.stopLeScan(this);
        }
        mIsScanning = false;
    }
    // Toast Message
    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
    //Advertising as Beacon
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class VirtualBeacon{
        private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i("AD", "LE Advertise Started.");
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.w("AD", "LE Advertise Failed: " + errorCode);
            }
        };

        public void beaconMode(String BeaconID,int Major) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startAdvertising(BeaconID,Major);

                Thread threadss = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20 * 1000);
                            stopAdvertising();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                threadss.start();
            }
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void startAdvertising(String uid,int Major) {
            if (mBluetoothLeAdvertiser == null) return;

            AdvertiseSettings settings = new AdvertiseSettings.Builder()
                    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                    .setConnectable(false)
                    .setTimeout(0)
                    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                    .build();


            mBluetoothLeAdvertiser.startAdvertising(settings, createIBeaconAdvertiseData(

                    UUID.fromString(uid),

                    (short) Major, (short) Major, (byte) 0xc5), mAdvertiseCallback);
        }


        public AdvertiseData createIBeaconAdvertiseData(UUID proximityUuid, short major,

                                                        short minor, byte txPower) {

            if (proximityUuid == null) {

                throw new IllegalArgumentException("proximitiUuid null");

            }
            byte[] manufacturerData = new byte[23];

            ByteBuffer bb = ByteBuffer.wrap(manufacturerData);

            bb.order(ByteOrder.BIG_ENDIAN);

            bb.put((byte) 0x02);

            bb.put((byte) 0x15);

            bb.putLong(proximityUuid.getMostSignificantBits());

            bb.putLong(proximityUuid.getLeastSignificantBits());

            bb.putShort(major);

            bb.putShort(minor);

            bb.put(txPower);


            AdvertiseData.Builder builder = new AdvertiseData.Builder();

            builder.addManufacturerData(0x004c, manufacturerData);

            AdvertiseData adv = builder.build();

            return adv;

        }

        private void stopAdvertising() {
            if (mBluetoothLeAdvertiser == null) return;

            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
    }
    //Class is for Checking Beacon List
    //This has Algorithms about Attendance, Updating My Location, Shuttle Bus Alarm
    private class Scanner extends AsyncTask<Void, Void, Void> {

        private boolean flag;
        private boolean controller;
        String todays;
        int distCheckCount=0;
        // Begin - can use UI thread here
        protected void onPreExecute() {
            flag = false;
            controller = true;
        }

        public void StopScanner() {
            controller = false;
        }
        // Method for Converting Calendar data to String
        private String gettime() {
            Calendar cal = Calendar.getInstance();
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
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

        // this is the SLOW background thread taking care of heavy tasks
        // cannot directly change UI
        protected Void doInBackground(final Void... args) {
            while (controller) {
                publishProgress();
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (1 != 1)
                    break;
            }
            return null;
        }

        // periodic updates - it is OK to change UI
        @Override
        protected void onProgressUpdate(Void... value) {

            if (flag) {
                stopScan();
                //올드는 새스캔 전에 스캔된 목록
                mScanResult = mDeviceAdapter.getScanedDevice();
                if (mScanResult != null) {
                    Log.d("디버깅2",mScanResult.size()+"");
                    for (int i = 0; i < mScanResult.size(); i++) {
                        if (mScanResult.get(i).getIBeacon().getMajor() == 123  && attCheck==1) { // Major==123 은 Virtual Beacon으로 Advertising 한 User가
                                                                                                    // 신호가 약하여 Relaying Beaconing을 요청하는것
                            if (AttendanceInfo.latestBeaconID != null) { // 기존에 출석체크가 되어있는 유저는 최근에 출석체크에 사용된 BeaconID를 가지고 있음
                                String BeaconID = mScanResult.get(i).getIBeacon().getProximityUuid();
                                BeaconID = BeaconID.replaceAll("-", "");
                                BeaconID = BeaconID.toUpperCase();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            VirtualBeacon temp = new VirtualBeacon();
                                            temp.beaconMode(AttendanceInfo.latestBeaconID,501);
                                        }
                                    });
                                    thread.start();
                                }
                            }
                        }
                        final String tempBeaconID=mScanResult.get(i).getIBeacon().getProximityUuid().replace("-", "").toUpperCase();
                        if (nBeacon.BeaconID.contains(tempBeaconID) &&
                                nBeacon.flags.get(tempBeaconID)) {
                            nBeacon.flags.put(tempBeaconID,false);
                            Thread flagOut=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000 * 60);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    nBeacon.flags.put(tempBeaconID,true);
                                }
                            });
                            flagOut.start();
                            Log.d("무당이1", "안녕");
                            String Content ="";
                            if (nBeacon.location.get(tempBeaconID).startsWith("Lady")) {
                                //startActivity(new Intent(getApplicationContext(),LadybugsActivity.class));
                                for(int t=0;t<3;t++)
                                {
                                    String Start;
                                    String End;
                                    Start = LadyBugTimetable.LadyTime[t].split("-")[0];
                                    End = LadyBugTimetable.LadyTime[t].split("-")[1];
                                    int endTime = Integer.parseInt(End.split(":")[0])*100;
                                    endTime += Integer.parseInt(End.split(":")[1]);
                                    int startTime = Integer.parseInt(Start.split(":")[0])*100;
                                    startTime += Integer.parseInt(Start.split(":")[1]);
                                    Calendar cal = Calendar.getInstance();
                                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                                    int min = cal.get(Calendar.MINUTE);
                                    Log.d("디버깅",startTime +"/" + endTime +"/" + (hour*100 +min));
                                    if(endTime > (hour * 100 + min))
                                    {
                                        int increase =0;
                                        if(t == 0)
                                        {
                                            increase=5;
                                        }else
                                        {
                                            increase=10;
                                        }
                                        while (startTime < (hour*100 +min))
                                            startTime+=increase;
                                        startTime = startTime - (hour*100 +min);
                                        if(nBeacon.location.get(tempBeaconID).contains("up"))
                                            Content="교육 대학원 방향으로 가는 버스가 " + startTime+"분 뒤에 도착합니다";
                                        else
                                            Content="It대학 방향으로 가는 버스가"+startTime+"분 뒤에 도착 합니다";

                                        break;
                                    }
                                }

                               if(!Content.equals("")) {
                                   PendingIntent pendingIntent =
                                           PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(), LadybugsActivity.class), 0);
                                   NotificationManager nm = (NotificationManager)
                                           getSystemService(NOTIFICATION_SERVICE);
                                   NotificationCompat.Builder mBuilder =
                                           new NotificationCompat.Builder(getBaseContext())
                                                   .setSmallIcon(R.drawable.dialog_gachon)
                                                   .setContentTitle("셔틀버스 알람 입니다.")
                                                   .setContentText(Content);

                                   mBuilder.setContentIntent(pendingIntent);
                                   mBuilder.setVibrate(new long[]{100, 250, 100, 500});
                                   nm.notify(notificationID + 1, mBuilder.build());
                                   Log.d("무당이2", "안녕");
                               }else
                               {
                                   PendingIntent pendingIntent =
                                           PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(), LadybugsActivity.class), 0);
                                   NotificationManager nm = (NotificationManager)
                                           getSystemService(NOTIFICATION_SERVICE);
                                   NotificationCompat.Builder mBuilder =
                                           new NotificationCompat.Builder(getBaseContext())
                                                   .setSmallIcon(R.drawable.dialog_gachon)
                                                   .setContentTitle("셔틀버스 알람 입니다.")
                                                   .setContentText("현재 셔틀버스가 운행중이지 않습니다.");

                                   mBuilder.setContentIntent(pendingIntent);
                                   nm.notify(notificationID + 1, mBuilder.build());

                               }
                            }else
                            {
                                if(ClassroomBeacon.BeaconID.contains(tempBeaconID)){
//요일 & 날짜
                                    Calendar cal = Calendar.getInstance();

                                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                                    int min = cal.get(Calendar.MINUTE);
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

                                    String week = gettime();
                                    String param1 = "build=" + ClassroomBeacon.Building.get(tempBeaconID) +
                                            "&class=" + ClassroomBeacon.Room.get(tempBeaconID)
                                            + "&time="+"10"+":"+"23"
                                            + "&day=" + week;
                                    PHPConnector.getDatas(param1, "getc.php", "getclassinfo1");
                                    String param2 = "id="+myID+"&class=" + ClassroomBeacon.Room.get(tempBeaconID)
                                            + "&build=" + ClassroomBeacon.Building.get(tempBeaconID);
                                    PHPConnector.getDatas(param2, "findclassnum.php", "findclass1");
                                    //String param3=
                                    Log.d("디버깅zero2",param1+"\n\n"+param2);


                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String result;
                                            String result2;
                                            while (true) {
                                                if (PHPConnector.MessageQue.getString("getclassinfo1") != null) {
                                                    result= PHPConnector.MessageQue.getString("getclassinfo1");
                                                    //PHPConnector.MessageQue.putString("getclassinfo", null);
                                                    break;
                                                }
                                            }
                                            while (true) {
                                                if (PHPConnector.MessageQue.getString("findclass1") != null) {
                                                    result2 = PHPConnector.MessageQue.getString("findclass1");
                                                    break;
                                                }
                                            }


                                           PHPConnector.MessageQue.putString("getclassinfo1", null);
                                            PHPConnector.MessageQue.putString("findclass1", null);

                                            String contents="";
                                            Log.d("디버깅",result+"");

                                            Log.d("디버깅2",result2+"");
                                            if(result.equals("[]"))
                                            {
                                                contents = "현재 강의실"+ClassroomBeacon.Room.get(tempBeaconID)+"은 수업이 없습니다.\n";
                                                if(!result2.contains("fcount")) {
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
                                                    if (!result2.contains("fcount")) {
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
                                            FindClassroomActivity.alarmMessage=contents;
                                            //intent.putExtra("dialogT",contents);
                                            Log.d("디버깅",contents);
                                            PendingIntent pendingIntent =
                                                    PendingIntent.getActivity(getBaseContext(), 0,new Intent(getBaseContext(), FindClassroomActivity.class), 0);
                                            NotificationManager nm = (NotificationManager)
                                                    getSystemService(NOTIFICATION_SERVICE);
                                            NotificationCompat.Builder mBuilder =
                                                    new NotificationCompat.Builder(getBaseContext())
                                                            .setSmallIcon(R.drawable.dialog_gachon)
                                                            .setContentTitle("강의실 알람 입니다.")
                                                            .setContentText(contents);
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setVibrate(new long[]{100, 250, 100, 500});
                                            nm.notify(notificationID, mBuilder.build());
                                        }
                                    });
                                    thread.start();
                                    // 알림

                                }else
                                {

                                }
                                break;
                            }
                        }
                    }
                }
                if (PHPConnector.MessageQue.getString("updateAttendance") != null) ;
                //Log.d("디버깅",PHPConnector.MessageQue.getString("updateAttendance"));
            } else {
                todays=gettime();
                Log.d("Bluetooth Adpater",todays);
                if(!todays.equals("Sun") && !todays.equals("Sat")) {
                   // mScanResult.clear();
                    startScan();
                }

            }
            flag = !flag;
        }

        // End - can use UI thread here
        protected void onPostExecute(final Void unused) {
        }
    }
    // Convert class time to 09:00~
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
    //this class is for Attendance checking
  //  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class AttendanceChecker extends Thread {
        int CurPos = -1;


        //public ArrayList<StartTime> myList;
        public void displayatten(String att_state) {
            Intent i = new Intent(getApplicationContext(), NotificationView.class);
            i.putExtra("notificationID", 1);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getBaseContext(), 0, i, 0);
            NotificationManager nm = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            if (att_state == "1") {
                mBuilder =
                        new NotificationCompat.Builder(getBaseContext())
                                .setSmallIcon(R.drawable.dialog_gachon)
                                .setContentTitle("출석체크")
                                .setContentText("출석체크가 완료되엇습니다");
                mBuilder.setContentIntent(pendingIntent);

                mBuilder.setVibrate(new long[]{100, 250, 100, 500});
                nm.notify(1, mBuilder.build());
            } else if (att_state == "2") {
                mBuilder =
                        new NotificationCompat.Builder(getBaseContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("출석체크")
                                .setContentText("지각처리되엇습니다");
                mBuilder.setContentIntent(pendingIntent);

                mBuilder.setVibrate(new long[]{100, 250, 100, 500});
                nm.notify(1, mBuilder.build());
            }
        }

        @Override
        public void run() {
            int Count = 1;
            while(attCheck!=1) {


                try {
                    sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("디벅스", "디벅스2");
                //This while statement checks current time and class start time
                //and searches beacon list for attendance check
                //and checks attendance
                while (true) {
                    long sleep_time = sleepTime();
                    if (sleep_time != Long.MAX_VALUE) { //

                        Log.d("디벅스", sleep_time + "초 후 다음동작");

                        if (sleep_time >= 0) {
                            long ss = sleep_time / 1000;
                            postToastMessage(ss + "초 후 다음 수업 출석 체크가 시작 됩니다.");
                            Log.d("디벅스", ss + "초 후 다음동작");
                            try {
                                Thread.sleep(sleep_time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                        break;

                    if (mScanResult != null) {
                        IBeacon curBeacon;
                        Log.d("디벅스222", mScanResult.size() + "");
                        for (int i = 0; i < mScanResult.size(); i++) {
                            curBeacon = mScanResult.get(i).getIBeacon();
                            Log.d("디벅스222", curBeacon.getProximityUuid());
                            String BeaconID = curBeacon.getProximityUuid();
                            BeaconID = BeaconID.replaceAll("-", "").toUpperCase();
                            for (int s = 0; s < ClassroomBeacon.BeaconID.size(); s++) {
                                Log.d("디보깅", ClassroomBeacon.BeaconID.get(s));
                            }
                            if (ClassroomBeacon.BeaconID.contains(BeaconID)) {
                                String Building = ClassroomBeacon.Building.get(BeaconID);
                                String Room = ClassroomBeacon.Room.get(BeaconID);
                                Log.d("디버깅", "여기까지");
                                if (CurPos != -1) {
                                    AttendanceInfo CurAtt = Timetable.getAttinfo().get(CurPos);
                                    Log.d("디벅스", CurAtt.Title + "/" + CurAtt.Day);
                                    Class findedClass = find_Current_Class(CurAtt.Title, CurAtt.Day);

                                    if ((Building != null && findedClass.Building.equals(Building)) &&
                                            (Room != null && findedClass.Room.equals(Room))) {
                                        if (curBeacon.getMajor() != 123) {
                                            long stateTime = Math.abs(sleep_time) / 1000;
                                            String att_state = "";
                                            if (stateTime < 600)
                                                att_state = "1";
                                            else if (stateTime < 1800)
                                                att_state = "2";

                                            String Params = "week=" + CurAtt.Week + "&time="
                                                    + CurAtt.Time + "&day=" + CurAtt.Day + "&state=" + att_state + "&sem=1&id=" + myID + "&courseid=" + CurAtt.courseid;
                                            PHPConnector.getDatas(Params, "addattendance.php", "updateAttendance");

                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String result = PHPConnector.MessageQue.getString("updateAttendance");

                                                    while (result == null)
                                                        result = PHPConnector.MessageQue.getString("updateAttendance");

                                                    Intent i = new Intent(getApplicationContext(), NotificationView.class);
                                                    i.putExtra("notificationID", 9);
                                                    PendingIntent pendingIntent =
                                                            PendingIntent.getActivity(getBaseContext(), 0, i, 0);
                                                    NotificationManager nm = (NotificationManager)
                                                            getSystemService(NOTIFICATION_SERVICE);
                                                    NotificationCompat.Builder mBuilder;

                                                    mBuilder =
                                                            new NotificationCompat.Builder(getBaseContext())
                                                                    .setSmallIcon(R.drawable.dialog_gachon)
                                                                    .setContentTitle("공지사항")
                                                                    .setContentText(result);
                                                    mBuilder.setContentIntent(pendingIntent);

                                                    mBuilder.setVibrate(new long[]{100, 250, 100, 500});
                                                    nm.notify(9, mBuilder.build());
                                                }
                                            });
                                            thread.start();
                                            CurAtt.Att_State = att_state;
                                            displayatten(att_state);
                                            //beaconMode(curBeacon.getProximityUuid().toUpperCase());
                                            Log.d("디버깅", Params + "-");
                                            AttendanceInfo.latestBeaconID = BeaconID;
                                            postToastMessage("출석 체크가 되었습니다.");
                                            mStackList.clear();
                                        } else { // 강의실 비콘은 수신 되었으나 Major == 123이여서 실제 강의실 비콘을 수신하지 못하였을 때
                                            VirtualBeacon virtualBeacon = new VirtualBeacon();
                                            if (mStackList.size() < 3) {
                                                postToastMessage("출석체크 진행 중 입니다.\n비콘신호가 약해서 계속 확인 중입니다.\n"
                                                        + mStackList.size() + "/" + 10 + "진행");
                                                Log.d("디벅스", "출석체크 진행 중 입니다.\n비콘신호가 약해서 계속 확인 중입니다.\n"
                                                        + mStackList.size() + "/" + 10 + "진행");
                                                mStackList.add(true);
                                            } else {
                                                virtualBeacon.beaconMode("24ddf411-8cf1-440c-87cd-e368daf9c934",123); // 가상 비콘
                                                try {
                                                    sleep(30000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } else {// when beacon list dosen't have class beacon
                                        if (mStackList.size() < 3) {
                                            postToastMessage("출석체크 진행 중 입니다.\n비콘신호가 약해서 계속 확인 중입니다.\n"
                                                    + mStackList.size() + "/" + 10 + "진행");
                                            Log.d("디벅스", "출석체크 진행 중 입니다.\n비콘신호가 약해서 계속 확인 중입니다.\n"
                                                    + mStackList.size() + "/" + 10 + "진행");
                                            mStackList.add(true);
                                        } else {
                                            VirtualBeacon virtualBeacon = new VirtualBeacon();
                                            virtualBeacon.beaconMode("24ddf411-8cf1-440c-87cd-e368daf9c934",123);
                                            try {
                                                sleep(30000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {//when device dosen't scan beacons
                                    mStackList.add(false);
                                    if (mStackList.size() < 3) {
                                        postToastMessage("출석체크 진행 중 입니다.\n비콘신호가 약해서 계속 확인 중입니다.\n"
                                                + mStackList.size() + "/" + 10 + "진행");
                                        Log.d("디벅스", "출석체크 진행 중 입니다.\n비콘신호가 약해서 계속 확인 중입니다.\n"
                                                + mStackList.size() + "/" + 10 + "진행");
                                        mStackList.add(true);
                                    } else {// request relaying
                                        VirtualBeacon virtualBeacon = new VirtualBeacon();
                                        virtualBeacon.beaconMode("24ddf411-8cf1-440c-87cd-e368daf9c934",123);
                                        try {
                                            sleep(30000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Class find_Current_Class(String title, String day) {
            Class result = null;
            if (day.startsWith("Mon")) {
                for (int i = 0; i < Timetable.getMonAllClasses().size(); i++) {
                    if (Timetable.getMonAllClasses().get(i).ClassName.startsWith(title)) {
                        result = Timetable.getMonAllClasses().get(i);
                        break;
                    }
                }
            } else if (day.startsWith("Tue")) {
                for (int i = 0; i < Timetable.getTueAllClasses().size(); i++) {
                    if (Timetable.getTueAllClasses().get(i).ClassName.startsWith(title)) {
                        result = Timetable.getTueAllClasses().get(i);
                        break;
                    }
                }
            } else if (day.startsWith("Wed")) {
                for (int i = 0; i < Timetable.getWedAllClasses().size(); i++) {
                    if (Timetable.getWedAllClasses().get(i).ClassName.startsWith(title)) {
                        result = Timetable.getWedAllClasses().get(i);
                        break;
                    }
                }
            } else if (day.startsWith("Thu")) {
                for (int i = 0; i < Timetable.getThuAllClasses().size(); i++) {
                    if (Timetable.getThuAllClasses().get(i).ClassName.startsWith(title)) {
                        result = Timetable.getThuAllClasses().get(i);
                        break;
                    }
                }
            } else if (day.startsWith("Fri")) {
                for (int i = 0; i < Timetable.getFriAllClasses().size(); i++) {
                    if (Timetable.getFriAllClasses().get(i).ClassName.startsWith(title)) {
                        result = Timetable.getFriAllClasses().get(i);
                        break;
                    }
                }
            }
            return result;
        }

        public long sleepTime() {//Calculate about difference of current time and class start time for sleep thread
            ArrayList<AttendanceInfo> localArray = Timetable.getAttinfo();
            long time = System.currentTimeMillis();
            long min = Long.MAX_VALUE;
            Log.d("디벅스2", localArray.size() + "/");
            for (int i = 0; i < localArray.size(); i++) {
                Log.d("디벅스", "모징");
                String[] sub = localArray.get(i).Time.split(" ");
                int Year = Integer.parseInt(sub[0].split("-")[0]);
                int Month = Integer.parseInt(sub[0].split("-")[1]) - 1;
                int Day = Integer.parseInt(sub[0].split("-")[2]);
                int Hour = Integer.parseInt(sub[1].split(":")[0]);
                int Minute = Integer.parseInt(sub[1].split(":")[1]);
                int Second = Integer.parseInt(sub[1].split(":")[2]);
                Calendar cal1 = Calendar.getInstance();
                cal1.set(Year, Month, Day, Hour, Minute, Second);
                long tempMills = cal1.getTimeInMillis();
                Log.d("디벅스22", (Math.abs(tempMills - time) / 1000) + "/" + (tempMills - time) + "/" + localArray.get(i).Att_State);
                if ((tempMills - time) < 0 && (Math.abs(tempMills - time) / 1000) > 1800) {
                    if (localArray.get(i).Att_State.equals("0")) {
                        String Params = "week=" + localArray.get(i).Week + "&time="
                                + localArray.get(i).Time + "&day=" + localArray.get(i).Day + "&sem=1&id=" + myID + "&state=" + "3&"
                                + "courseid=" + localArray.get(i).courseid;
                        PHPConnector.getDatas(Params, "addattendance.php", "updateAttendance");
                        localArray.get(i).Att_State = "3";
                    }
                } else if (tempMills - time < min && localArray.get(i).Att_State.equals("0")) {
                    min = tempMills - time;
                    CurPos = i;

                    Log.d("디벅스", "모징2");
                }
            }
            Log.d("디벅스2", min + "/" + Long.MAX_VALUE);
            return min;

        }
    }
    // class is for updating my location
    public class MyLocationUpdater extends Thread {
        int[] mStartPoint = {9, 13, 19, 29, 39, 49, 59};
        int mNowMinute = 0;
        boolean flags = false;
        Calendar mNow = Calendar.getInstance();
        Context mContext;

        public MyLocationUpdater(Context context) {
            mContext = context;
            mNowMinute = mNow.get(Calendar.MINUTE);
        }

        public void run() {
            long sleep_time = 0;
            sleep_time = getSleepTime();
            sleep_time = sleep_time * 1000;
            if (sleep_time < 0)
                sleep_time = sleep_time * -1;
            Log.d("디버깅", sleep_time + "초 후 시작 안녕");
            try {
                sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                IBeacon curBeacon;
                int maxRssi = Integer.MIN_VALUE;
                int maxIndex = -1;
                for (int i = 0; i < mScanResult.size(); i++) {
                    if(mScanResult==null)
                        break;
                    curBeacon = mScanResult.get(i).getIBeacon();
                    if (curBeacon != null && maxRssi < curBeacon.getRssi() && curBeacon.getRssi() != 127) {
                        maxRssi = curBeacon.getRssi();
                        maxIndex = i;
                    }
                }
                if (maxIndex != -1) {
                    curBeacon = mScanResult.get(maxIndex).getIBeacon();
                    String tempBeaconID = curBeacon.getProximityUuid().replaceAll("-","").toUpperCase();
                   // Log.d("디버깅 id",tempBeaconID);
                    if(!ClassroomBeacon.Room.containsKey(tempBeaconID)) {
                      return;
                    }
                    String BeaconID = curBeacon.getProximityUuid();
                    BeaconID = BeaconID.replaceAll("-", "").toUpperCase();
                    if (nBeacon.BeaconID.contains(BeaconID)) {
                        String Building = nBeacon.Building.get(BeaconID);
                        String location = nBeacon.location.get(BeaconID);
                        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
                        Date date = new Date();
                        String currentTime = dateFormat.format(date);
                        //String currentTime = getCurrentTime();
                        String Params = "id=" + myID + "&time=" + currentTime + "&build=" + Building + "&class=" + location;
                        Log.d("디버깅zz", Params);
                        PHPConnector.getDatas(Params, "updatelocation.php", "uLocation");
                        flags = true;
                        try {
                            sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (flags) {
                    try {
                        sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flags = false;
                }

            }
        }

        public long getSleepTime() {
            long min = Long.MAX_VALUE;
            for (int i = 0; i < 6; i++) {
                if ((mStartPoint[i] - mNowMinute) > 0 && (mStartPoint[i] - mNowMinute) < min) {
                    min = (mStartPoint[i] - mNowMinute);
                }
            }
            return min;
        }

    }
}