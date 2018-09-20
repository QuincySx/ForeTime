package com.smallraw.foretime.app.ui.tomatoBell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smallraw.foretime.app.R;
import com.smallraw.time.base.BaseFragment;

public class TomatoBellFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomato_bell, container, false);
        return view;
    }
}
