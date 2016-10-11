package com.javathlon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javathlon.R;

/**
 * Created by talha on 30.07.2015.
 */
public class RecentFilesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lastbrowsed, container);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
