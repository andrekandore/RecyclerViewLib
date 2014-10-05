package com.twotoasters.recycled;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;
import com.twotoasters.android.support.v7.widget.RecyclerView.ItemAnimator;
import com.twotoasters.layoutmanager.GridLayoutManager;
import com.twotoasters.recycled.factory.ItemAnimationFactory;
import com.twotoasters.recycled.factory.NameFactory;

import java.util.ArrayList;


public class RecycleActivity extends FragmentActivity {

    public static final String KEY_ANIMATION_INDEX = "animationIndex";
    public static final String KEY_LAYOUT_GRID = "layoutManager";

    private int mAnimationIndex = 0;

    private String[] mAnimationArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycle);

        mAnimationArray = getResources().getStringArray(R.array.animations);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();

    }


    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mAnimationIndex = savedInstanceState.getInt(KEY_ANIMATION_INDEX);
        } else {
            Toast.makeText(this, R.string.tap_to_remove, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_ANIMATION_INDEX, mAnimationIndex);
        super.onSaveInstanceState(outState);
    }

    public <T extends View> T findWidgetById(int resId) {
        return (T) findViewById(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recycle, menu);
        for (int i = 0; i < mAnimationArray.length; i++) {
            menu.add(1, i, i, mAnimationArray[i]);
            MenuItem item = menu.findItem(i);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
        setupSwitch();
        return true;
    }

    private void setupSwitch() {
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.layout_switcher);

        Switch switcher = (Switch) getActionBar().getCustomView();
        switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                setLayoutManager(checked);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar card clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        ItemAnimator mItemAnimator = new com.twotoasters.android.support.v7.widget.DefaultItemAnimator();

        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
//                getAdapter().addToList(NameFactory.getRandomName());
                return true;
            default:
                if (changeAnimation(id)) return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isInAnimationArray(int id) {
        return id >= 0 && id < mAnimationArray.length;
    }

    public ItemAnimator currentItemAnimator() {
        ItemAnimator itemAnimator = ItemAnimationFactory.getAnimator(mAnimationIndex);
        return itemAnimator;
    }

    private boolean changeAnimation(int index) {
        if (!isInAnimationArray(mAnimationIndex)) return false;

        mAnimationIndex = index;
        ItemAnimator itemAnimator = ItemAnimationFactory.getAnimator(index);

//        mRecyclerView.setItemAnimator(itemAnimator);
        getActionBar().setTitle(mAnimationArray[index]);
        return true;
    }
}
