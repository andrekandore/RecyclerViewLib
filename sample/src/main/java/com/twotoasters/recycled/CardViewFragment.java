package com.twotoasters.recycled;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;
import com.twotoasters.layoutmanager.GridLayoutManager;
import com.twotoasters.recycled.factory.NameFactory;

import java.util.ArrayList;


public class CardViewFragment extends Fragment {

    private NameAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private static final String KEY_NAMES = "names";
    private ArrayList<Item> mNames = NameFactory.getListOfNames();

    public static CardViewFragment newInstance() {
        CardViewFragment fragment = new CardViewFragment();
        return fragment;
    }

    public CardViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mNames = (ArrayList<Item>) savedInstanceState.getSerializable(KEY_NAMES);
        } else {
            Toast.makeText(this.getActivity(), R.string.tap_to_remove, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((ViewGroup)view).setClipToPadding(false);
        ((ViewGroup)view).setClipChildren(false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);

        if (null != savedInstanceState) {
            setLayoutManager(savedInstanceState.getBoolean(RecycleActivity.KEY_LAYOUT_GRID, false));
        } else {
            setLayoutManager(false);
        }

        mRecyclerView.setAdapter(getAdapter());
    }

    private void setLayoutManager(boolean shouldBeGrid) {
        if (mRecyclerView != null) {
            mRecyclerView.getItemAnimator().endAnimations();
            if (shouldBeGrid) {
                mRecyclerView.setLayoutManager(new GridLayoutManager((RecycleActivity)this.getActivity()));
            } else {
                mRecyclerView.setLayoutManager(new LinearLayoutManager((RecycleActivity)this.getActivity()));
            }
            // The recycle pool has to be cleared after the layout manager is changed.
            mRecyclerView.getRecycledViewPool().clear();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_NAMES, mNames);
//        if (mRecyclerView != null) {
//            outState.putBoolean(KEY_LAYOUT_GRID, mRecyclerView.getLayoutManager() instanceof GridLayoutManager);
//        }
//        outState.putSerializable(KEY_NAMES, mNames);

        super.onSaveInstanceState(outState);
    }

    public <T extends View> T findWidgetById(int resId) {
        return (T) this.getView().findViewById(resId);
    }

    private NameAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new NameAdapter(mNames,mRecyclerView,(RecycleActivity)this.getActivity());
        }
        return mAdapter;
    }
}
