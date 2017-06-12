package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.chaerin.smartcampus.R;

public class MenuActivity extends Activity {
    private Button btnTimetable;
    private Button btnStatecheck;
    private Button btnFindclassroom;
    private Button btnFindfriends;
    private Button btnAlarmcheck;
    private Button btnLadybugs;

    private AlertDialog mDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        btnTimetable = (Button) findViewById(R.id.menu_timetable);
        btnStatecheck = (Button) findViewById(R.id.menu_statecheck);
        btnFindclassroom = (Button) findViewById(R.id.menu_findclassroom);
        btnFindfriends = (Button) findViewById(R.id.menu_findfriends);
        btnAlarmcheck = (Button) findViewById(R.id.menu_alarmcheck);
        btnLadybugs = (Button) findViewById(R.id.menu_ladybugs);

        //시간표 버튼 클릭시 이벤트
        btnTimetable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTimetable.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTimetable.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), TimeTableActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
        //출석현황 버튼 클릭시 이벤트
        btnStatecheck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnStatecheck.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnStatecheck.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), StateCheckActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
        //강의실찾기 버튼 클릭시 이벤트
        btnFindclassroom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnFindclassroom.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnFindclassroom.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), FindClassroomActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
        //내 친구 찾기 버튼 클릭시 이벤트
        btnFindfriends.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnFindfriends.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnFindfriends.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), FindFriendsActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
        //알림보관함 버튼 클릭시 이벤트
        btnAlarmcheck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnAlarmcheck.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnAlarmcheck.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), AlarmCheckActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
        //무당이 버튼 클릭시 이벤트
        btnLadybugs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnLadybugs.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnLadybugs.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), LadybugsActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
    }

    //BACK버튼 눌렀을 때 종료확인 Dialog pop-up
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //mDialog =
                new AlertDialog.Builder(this).setTitle("가천대학교 스마트캠퍼스")
                        .setMessage("종료하시겠습니까?")
                        .setIcon(R.drawable.dialog_gachon)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }).setNegativeButton("취소", null).show();
                //TODO : Dialog 색깔 바꾸기
                //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //mDialog.show();
                return false;
            default:
                return false;
        }
    }
}
