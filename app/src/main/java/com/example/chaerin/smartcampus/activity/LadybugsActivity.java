package com.example.chaerin.smartcampus.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.chaerin.smartcampus.R;

/**
 * Created by ChaeRin on 2016-07-02.
 * 무당이
 */
public class LadybugsActivity extends FragmentActivity {
    private Button building_it;
    private Button building_dorm;

    int mCurrentFragmentIndex;
    public final static int FRAGMENT_IT = 0;
    public final static int FRAGMENT_DORM = 1;

    Fragment newFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ladybugs);

        building_it = (Button) findViewById(R.id.building_it);
        building_dorm = (Button) findViewById(R.id.building_dorm);

        mCurrentFragmentIndex = 2;
        fragmentReplace(mCurrentFragmentIndex);

        building_it.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    building_it.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    building_it.getBackground().clearColorFilter();
                    mCurrentFragmentIndex = FRAGMENT_IT;
                    fragmentReplace(mCurrentFragmentIndex);
                }
                return true;
            }
        });
        building_dorm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    building_dorm.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    building_dorm.getBackground().clearColorFilter();
                    mCurrentFragmentIndex = FRAGMENT_DORM;
                    fragmentReplace(mCurrentFragmentIndex);
                }
                return true;
            }
        });
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        //Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
        newFragment = getFragment(reqNewFragmentIndex);
        //newFragment = new DormLadybugsTimetable();
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.ladybugs_timetable, newFragment);
        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        switch (idx) {
            case FRAGMENT_IT:
                newFragment = new ItLadybugsTimetable();
                break;
            case FRAGMENT_DORM:
                newFragment = new DormLadybugsTimetable();
                break;
            case 2:
                newFragment = new NullLadybugsTimetable();
                break;
            default:
                //Log.d(TAG, "Unhandle case");
                break;
        }
        return newFragment;
    }
}
