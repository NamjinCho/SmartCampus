package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chaerin.smartcampus.R;

/**
 * Created by ChaeRin on 2016-07-02.
 * 시간표
 */
public class TimeTableActivity extends Activity {
    private static final String TAG_CLASSNAME = "classname";
    private static final String TAG_BUILDING = "building";
    private static final String TAG_CLASSROOM = "classroom";

    private ScrollView sv ;
    private LinearLayout mMonRl, mTueRl, mWedRl, mThuRl,mFriRl;
//private RelativeLayout mMonRl, mTueRl, mWedRl, mThuRl,mFriRl;

    int []beforeHour = {8,8,8,8,8}; //8시
    int []beforeMinute = {0,0,0,0,0}; //00분
    TextView mBlank;

    public static Class tempClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        sv = (ScrollView) findViewById(R.id.scr1);
        mMonRl = (LinearLayout) findViewById(R.id.col1main);
        mTueRl = (LinearLayout) findViewById(R.id.col2main);
        mWedRl = (LinearLayout) findViewById(R.id.col3main);
        mThuRl = (LinearLayout) findViewById(R.id.col4main);
        mFriRl = (LinearLayout) findViewById(R.id.col5main);

        //화면길이
        sv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Log.d("getMeasuredHeight",sv.getMeasuredHeight()+"");

        //TODO : 연속강의인 경우 하나로 출력하도록 처리

        int cumulHeight=0;
        //Monday
        String curClassTitle="";
        String oldClassTitle="";
        for(int i=0;i<Timetable.getMonAllClasses().size();i++){
            Class newClass = Timetable.getMonAllClasses().get(i);

            if(oldClassTitle.equals(""))
            {
                curClassTitle=newClass.ClassName;
                oldClassTitle=curClassTitle;
                //공강시간 표현하기
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[0]+beforeMinute[0]){
                    mBlank = new TextView(this);
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[0]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[0]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[0] = newClass.endHour;
                    beforeMinute[0] = newClass.endMinute;
                    mMonRl.addView(mBlank);
                }
            }
            else{
                oldClassTitle=curClassTitle;
                curClassTitle=newClass.ClassName;
            }
            Log.d("start",""+newClass.startHour*100+newClass.startMinute);

            if(curClassTitle.equals(oldClassTitle))
            {
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[0]+beforeMinute[0]){

                    cumulHeight+=(newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[0]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[0]) * sv.getMeasuredHeight()/16 / 60);
                    beforeHour[0] = newClass.endHour;
                    beforeMinute[0] = newClass.endMinute;
                }
                cumulHeight+=((newClass.endHour-newClass.startHour)*sv.getMeasuredHeight()/16
                        +(newClass.endMinute-newClass.startMinute)*sv.getMeasuredHeight()/16/60);
                if(i+1==Timetable.getMonAllClasses().size())
                {
                    TextView mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getMonAllClasses().get(i).ClassName + "\n"
                            + Timetable.getMonAllClasses().get(i).Building + "-" + Timetable.getMonAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight(cumulHeight);
                    mMonRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                }
            }
            else{
                TextView mClassAndBuilding = new TextView(this);

                mClassAndBuilding.setText(Timetable.getMonAllClasses().get(i - 1).ClassName + "\n"
                        + Timetable.getMonAllClasses().get(i - 1).Building + "-" + Timetable.getMonAllClasses().get(i - 1).Room);
                mClassAndBuilding.setTextSize(11); //텍스트크기설정
                mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                mClassAndBuilding.setHeight(cumulHeight);
                mMonRl.addView(mClassAndBuilding);
                cumulHeight=0;
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[0]+beforeMinute[0]){
                    mBlank = new TextView(this);
                    Log.d("before blank setHeight", (newClass.startHour * 100 - beforeHour[1] * 100 + (newClass.startMinute - beforeMinute[1]) + ""));
                    //mBlank.setHeight((newClass.startHour * 100 - beforeHour[0]*100 + (newClass.startMinute-beforeMinute[0]) * 100 / 60));
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[0]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[0]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[0] = newClass.endHour;
                    beforeMinute[0] = newClass.endMinute;
                    mMonRl.addView(mBlank);
                }
                if(i+1==Timetable.getMonAllClasses().size())
                {
                    mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getMonAllClasses().get(i).ClassName + "\n"
                            + Timetable.getMonAllClasses().get(i).Building + "-" + Timetable.getMonAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*sv.getMeasuredHeight()/16
                            +(newClass.endMinute-newClass.startMinute)*sv.getMeasuredHeight()/16/60);
                    mMonRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                }
            }
        }
        curClassTitle="";
        oldClassTitle="";
        //tue
        for(int i=0;i<Timetable.getTueAllClasses().size();i++){
            Class newClass = Timetable.getTueAllClasses().get(i);
            //
            if(oldClassTitle.equals(""))
            {
                curClassTitle=newClass.ClassName;
                oldClassTitle=curClassTitle;
                //공강시간 표현하기
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[1]+beforeMinute[1]){
                    mBlank = new TextView(this);
                    Log.d("before blank setHeight", (newClass.startHour * 100 - beforeHour[1] * 100 + (newClass.startMinute - beforeMinute[1]) + ""));
                    //mBlank.setHeight((newClass.startHour * 100 - beforeHour[0]*100 + (newClass.startMinute-beforeMinute[0]) * 100 / 60));
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight() / 16
                            - beforeHour[1] * sv.getMeasuredHeight() / 16
                            + (newClass.startMinute - beforeMinute[1]) * sv.getMeasuredHeight() / 16 / 60));
                    beforeHour[1] = newClass.endHour;
                    beforeMinute[1] = newClass.endMinute;
                    mTueRl.addView(mBlank);
                }
            }
            else{
                oldClassTitle=curClassTitle;
                curClassTitle=newClass.ClassName;
            }
            Log.d("start",""+newClass.startHour*100+newClass.startMinute);
            //연강인 경우
            if(curClassTitle.equals(oldClassTitle))
            {
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[1]+beforeMinute[1]){

                    cumulHeight+=(newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[1]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[1]) * sv.getMeasuredHeight()/16 / 60);
                    beforeHour[1] = newClass.endHour;
                    beforeMinute[1] = newClass.endMinute;
                }
                cumulHeight+=((newClass.endHour-newClass.startHour)*sv.getMeasuredHeight()/16
                        +(newClass.endMinute-newClass.startMinute)*sv.getMeasuredHeight()/16/60);
                if(i+1==Timetable.getTueAllClasses().size())
                {
                    TextView mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getTueAllClasses().get(i).ClassName + "\n"
                            + Timetable.getTueAllClasses().get(i).Building + "-" + Timetable.getTueAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight(cumulHeight);
                    Log.d("Tue1", newClass.startHour + "" + newClass.startMinute + " " + newClass.endHour + "" + newClass.endMinute);
                    /////////////////
                    clickClass(Timetable.getTueAllClasses().get(i),mClassAndBuilding);
                    mTueRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                }
            }
            else{ //연강x
                TextView mClassAndBuilding = new TextView(this);

                mClassAndBuilding.setText(Timetable.getTueAllClasses().get(i-1).ClassName + "\n"
                        + Timetable.getTueAllClasses().get(i-1).Building + "-" + Timetable.getTueAllClasses().get(i-1).Room);
                mClassAndBuilding.setTextSize(11); //텍스트크기설정
                mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                mClassAndBuilding.setHeight(cumulHeight);
                Log.d("Tue2", newClass.startHour + "" + newClass.startMinute + " " + newClass.endHour + "" + newClass.endMinute);
                /////////////////
                clickClass(Timetable.getTueAllClasses().get(i-1),mClassAndBuilding);
                mTueRl.addView(mClassAndBuilding);
                cumulHeight=0;
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[1]+beforeMinute[1]){
                    mBlank = new TextView(this);
                    Log.d("before blank setHeight", (newClass.startHour * 100 - beforeHour[1] * 100 + (newClass.startMinute - beforeMinute[1]) + ""));
                    //mBlank.setHeight((newClass.startHour * 100 - beforeHour[0]*100 + (newClass.startMinute-beforeMinute[0]) * 100 / 60));
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[1]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[1]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[1] = newClass.endHour;
                    beforeMinute[1] = newClass.endMinute;
                    mTueRl.addView(mBlank);
                }
                if(i+1==Timetable.getTueAllClasses().size())
                {
                    mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getTueAllClasses().get(i).ClassName + "\n"
                            + Timetable.getTueAllClasses().get(i).Building + "-" + Timetable.getTueAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight((newClass.endHour - newClass.startHour) * sv.getMeasuredHeight() / 16
                            + (newClass.endMinute - newClass.startMinute) * sv.getMeasuredHeight() / 16 / 60);
                    Log.d("Tue3",newClass.startHour+""+newClass.startMinute+" "+newClass.endHour+""+newClass.endMinute);
                    /////////////////
                    clickClass(Timetable.getTueAllClasses().get(i), mClassAndBuilding);
                    mTueRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                }
            }
        }
        //Wednesday
        curClassTitle="";
        oldClassTitle="";
        for(int i=0;i<Timetable.getWedAllClasses().size();i++)
        {
            Log.d("시간표 ",Timetable.getWedAllClasses().get(i).ClassName+"/"+Timetable.getWedAllClasses().get(i).Period);
        }
        cumulHeight=0;
        for(int i=0;i<Timetable.getWedAllClasses().size();i++){
            Class newClass = Timetable.getWedAllClasses().get(i);

            if(oldClassTitle.equals(""))
            {
                curClassTitle=newClass.ClassName;
                oldClassTitle=curClassTitle;
                //공강시간 표현하기
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[2]+beforeMinute[2]){
                    mBlank = new TextView(this);
                    Log.d("before blank setHeight", (newClass.startHour * 100 - beforeHour[2] * 100 + (newClass.startMinute - beforeMinute[2]) + ""));
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[2]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[2]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[2] = newClass.endHour;
                    beforeMinute[2] = newClass.endMinute;
                    mWedRl.addView(mBlank);
                }
            }
            else{
                oldClassTitle=curClassTitle;
                curClassTitle=newClass.ClassName;
            }
            Log.d("start", "" + newClass.startHour * 100 + newClass.startMinute);

            if(curClassTitle.equals(oldClassTitle))
            {
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[2]+beforeMinute[2]){

                    cumulHeight+=(newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[2]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[2]) * sv.getMeasuredHeight()/16 / 60);
                    beforeHour[2] = newClass.endHour;
                    beforeMinute[2] = newClass.endMinute;
                }
                cumulHeight+=((newClass.endHour-newClass.startHour)*sv.getMeasuredHeight()/16
                        +(newClass.endMinute-newClass.startMinute)*sv.getMeasuredHeight()/16/60);
                if(i+1==Timetable.getWedAllClasses().size())
                {
                    TextView mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getWedAllClasses().get(i).ClassName + "\n"
                            + Timetable.getWedAllClasses().get(i).Building + "-" + Timetable.getWedAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight(cumulHeight);
                    /////////////////
                    clickClass(Timetable.getWedAllClasses().get(i), mClassAndBuilding);
                    mWedRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                    break;
                }
            }
            else{
                TextView mClassAndBuilding = new TextView(this);
                mClassAndBuilding.setText(Timetable.getWedAllClasses().get(i-1).ClassName + "\n"
                        + Timetable.getWedAllClasses().get(i-1).Building + "-" + Timetable.getWedAllClasses().get(i-1).Room);
                mClassAndBuilding.setTextSize(11); //텍스트크기설정
                mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                mClassAndBuilding.setHeight(cumulHeight);
                /////////////////
                clickClass(Timetable.getWedAllClasses().get(i-1), mClassAndBuilding);
                mWedRl.addView(mClassAndBuilding);
                cumulHeight=0;
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[2]+beforeMinute[2]){
                    mBlank = new TextView(this);
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[2]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[2]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[2] = newClass.endHour;
                    beforeMinute[2] = newClass.endMinute;
                    mWedRl.addView(mBlank);
                }
                if(i+1==Timetable.getWedAllClasses().size())
                {
                    mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getWedAllClasses().get(i).ClassName + "\n"
                            + Timetable.getWedAllClasses().get(i).Building + "-" + Timetable.getWedAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight((newClass.endHour - newClass.startHour) * sv.getMeasuredHeight() / 16
                            + (newClass.endMinute - newClass.startMinute) * sv.getMeasuredHeight() / 16 / 60);
                    /////////////////
                    clickClass(Timetable.getWedAllClasses().get(i), mClassAndBuilding);
                    mWedRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                }
            }
        }

        for(int i=0;i<Timetable.getThuAllClasses().size();i++)
        {
            Log.d("시간표 ",Timetable.getThuAllClasses().get(i).ClassName+"/"+Timetable.getThuAllClasses().get(i).Period);
        }
        cumulHeight=0;
        //Thursday
        curClassTitle="";
        oldClassTitle="";
        for(int i=0;i<Timetable.getThuAllClasses().size();i++){
            Class newClass = Timetable.getThuAllClasses().get(i);

            if(oldClassTitle.equals(""))
            {
                curClassTitle=newClass.ClassName;
                oldClassTitle=curClassTitle;
                //공강시간 표현하기
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[3]+beforeMinute[3]){
                    mBlank = new TextView(this);
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[3]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[3]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[3] = newClass.endHour;
                    beforeMinute[3] = newClass.endMinute;
                    mThuRl.addView(mBlank);
                }
            }
            else{
                oldClassTitle=curClassTitle;
                curClassTitle=newClass.ClassName;
            }
            Log.d("start", "" + newClass.startHour * 100 + newClass.startMinute);

            if(curClassTitle.equals(oldClassTitle))
            {
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[3]+beforeMinute[3]){

                    cumulHeight+=(newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[3]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[3]) * sv.getMeasuredHeight()/16 / 60);
                    beforeHour[3] = newClass.endHour;
                    beforeMinute[3] = newClass.endMinute;
                }
                cumulHeight+=((newClass.endHour-newClass.startHour)*sv.getMeasuredHeight()/16
                        +(newClass.endMinute-newClass.startMinute)*sv.getMeasuredHeight()/16/60);
                if(i+1==Timetable.getThuAllClasses().size())
                {
                    TextView mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getThuAllClasses().get(i).ClassName + "\n"
                            + Timetable.getThuAllClasses().get(i).Building + "-" + Timetable.getThuAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight(cumulHeight);
                    /////////////////
                    clickClass(Timetable.getThuAllClasses().get(i), mClassAndBuilding);
                    mThuRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                    Log.d("목요일",Timetable.getThuAllClasses().get(i).ClassName + "\n"
                            + Timetable.getThuAllClasses().get(i).Building + "-" + Timetable.getThuAllClasses().get(i).Room);
                }
            }
            else{
                TextView mClassAndBuilding = new TextView(this);
                mClassAndBuilding.setText(Timetable.getThuAllClasses().get(i-1).ClassName + "\n"
                        + Timetable.getThuAllClasses().get(i-1).Building + "-" + Timetable.getThuAllClasses().get(i-1).Room);
                mClassAndBuilding.setTextSize(11); //텍스트크기설정
                mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                mClassAndBuilding.setHeight(cumulHeight);
                /////////////////
                clickClass(Timetable.getThuAllClasses().get(i-1), mClassAndBuilding);
                mThuRl.addView(mClassAndBuilding);
                Log.d("목요일2", Timetable.getThuAllClasses().get(i - 1).ClassName + "\n"
                        + Timetable.getThuAllClasses().get(i - 1).Building + "-" + Timetable.getThuAllClasses().get(i - 1).Room);
                cumulHeight=0;
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[3]+beforeMinute[3]){
                    mBlank = new TextView(this);
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[3]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[3]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[3] = newClass.endHour;
                    beforeMinute[3] = newClass.endMinute;
                    mThuRl.addView(mBlank);
                }
                if(i+1==Timetable.getThuAllClasses().size())
                {
                    mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getThuAllClasses().get(i).ClassName + "\n"
                            + Timetable.getThuAllClasses().get(i).Building + "-" + Timetable.getThuAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight((newClass.endHour - newClass.startHour) * sv.getMeasuredHeight() / 16
                            + (newClass.endMinute - newClass.startMinute) * sv.getMeasuredHeight() / 16 / 60);
                    /////////////////
                    clickClass(Timetable.getThuAllClasses().get(i), mClassAndBuilding);
                    mThuRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                    Log.d("목요일", Timetable.getThuAllClasses().get(i).ClassName + "\n"
                            + Timetable.getThuAllClasses().get(i).Building + "-" + Timetable.getThuAllClasses().get(i).Room);
                }
            }
        }
        //Friday
        curClassTitle="";
        oldClassTitle="";
        for(int i=0;i<Timetable.getFriAllClasses().size();i++)
        {
            Log.d("시간표 ",Timetable.getFriAllClasses().get(i).ClassName+"/"+Timetable.getFriAllClasses().get(i).Period);
        }
        cumulHeight=0;
        for(int i=0;i<Timetable.getFriAllClasses().size();i++){
            Class newClass = Timetable.getFriAllClasses().get(i);

            if(oldClassTitle.equals(""))
            {
                curClassTitle=newClass.ClassName;
                oldClassTitle=curClassTitle;
                //공강시간 표현하기
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[4]+beforeMinute[4]){
                    mBlank = new TextView(this);
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[4]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[4]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[4] = newClass.endHour;
                    beforeMinute[4] = newClass.endMinute;
                    mFriRl.addView(mBlank);
                }
            }
            else{
                oldClassTitle=curClassTitle;
                curClassTitle=newClass.ClassName;
            }
            Log.d("start", "" + newClass.startHour * 100 + newClass.startMinute);

            if(curClassTitle.equals(oldClassTitle))
            {
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[4]+beforeMinute[4]){

                    cumulHeight+=(newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[4]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[4]) * sv.getMeasuredHeight()/16 / 60);
                    beforeHour[4] = newClass.endHour;
                    beforeMinute[4] = newClass.endMinute;
                }
                cumulHeight+=((newClass.endHour-newClass.startHour)*sv.getMeasuredHeight()/16
                        +(newClass.endMinute-newClass.startMinute)*sv.getMeasuredHeight()/16/60);
                if(i+1==Timetable.getFriAllClasses().size())
                {
                    TextView mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getFriAllClasses().get(i).ClassName + "\n"
                            + Timetable.getFriAllClasses().get(i).Building + "-" + Timetable.getFriAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight(cumulHeight);
                    mFriRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                    break;
                }
            }
            else{
                TextView mClassAndBuilding = new TextView(this);
                mClassAndBuilding.setText(Timetable.getFriAllClasses().get(i-1).ClassName + "\n"
                        + Timetable.getFriAllClasses().get(i-1).Building + "-" + Timetable.getFriAllClasses().get(i-1).Room);
                mClassAndBuilding.setTextSize(11); //텍스트크기설정
                mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                mClassAndBuilding.setHeight(cumulHeight);
                mFriRl.addView(mClassAndBuilding);
                cumulHeight=0;
                if(newClass.startHour*100+newClass.startMinute >= 100*beforeHour[4]+beforeMinute[4]){
                    mBlank = new TextView(this);
                    mBlank.setHeight((newClass.startHour * sv.getMeasuredHeight()/16
                            - beforeHour[4]*sv.getMeasuredHeight()/16
                            + (newClass.startMinute-beforeMinute[4]) * sv.getMeasuredHeight()/16 / 60));
                    beforeHour[4] = newClass.endHour;
                    beforeMinute[4] = newClass.endMinute;
                    mFriRl.addView(mBlank);
                }
                if(i+1==Timetable.getFriAllClasses().size())
                {
                    mClassAndBuilding = new TextView(this);

                    mClassAndBuilding.setText(Timetable.getFriAllClasses().get(i).ClassName + "\n"
                            + Timetable.getFriAllClasses().get(i).Building + "-" + Timetable.getFriAllClasses().get(i).Room);
                    mClassAndBuilding.setTextSize(11); //텍스트크기설정
                    mClassAndBuilding.setTypeface(Typeface.DEFAULT_BOLD);
                    mClassAndBuilding.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mClassAndBuilding.setBackgroundColor(0xFFE3E3E3);
                    //mClassAndBuilding.setHeight((newClass.endHour-newClass.startHour)*100+(newClass.endMinute-newClass.startMinute)*100/60);
                    mClassAndBuilding.setHeight((newClass.endHour - newClass.startHour) * sv.getMeasuredHeight() / 16
                            + (newClass.endMinute - newClass.startMinute) * sv.getMeasuredHeight() / 16 / 60);
                    mFriRl.addView(mClassAndBuilding);
                    cumulHeight=0;
                }
            }
        }
    }

    private void clickClass(final Class theClass, final TextView classTextView){
        Log.d("TueclickClass",theClass.ClassName+" / "+classTextView.getText());
        classTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                tempClass = theClass;
                Log.d("TueclickClass2temp",tempClass.ClassName+"");
                Log.d("TueclickClass2",theClass.ClassName+" / "+classTextView.getText());
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeTableActivity.this);
                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle(""+theClass.ClassName)        // 제목 설정
                        .setMessage("과목명 : "+theClass.ClassName
                               // +"\n시간 : "+theClass.startHour+"시"+theClass.startMinute+"분"
                                +"\n교수 : "+theClass.Profesor
                                +"\n강의실 : "+theClass.Building+" "+theClass.Room+"호")
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("출석현황 보기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(getApplicationContext(),
                                        StateCheckActivity.class);
                                Log.d("fromTimetable", StateCheckActivity.fromTimeTable+"");
                                StateCheckActivity.fromTimeTable = true;
                                Log.d("fromTimetable2", StateCheckActivity.fromTimeTable+"");
                                //TODO: 해당과목 출석현황만 보도록 수정
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
                StateCheckActivity.fromTimeTable = false;
            }
        });

    }
    //화면길이 알아오기
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        Log.d("Height", sv.getHeight() + ""); //1070
        Log.d("Height", mMonRl.getHeight() + ""); //1600
        // --> 채린핸드폰기준 1시간당 크기 100
    }
}