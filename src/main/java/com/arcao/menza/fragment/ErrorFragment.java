package com.arcao.menza.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcao.menza.R;

public class ErrorFragment extends Fragment {
    private static final String ARG_ERROR_MESSAGE = "ERROR_MESSAGE";

    public static ErrorFragment newInstance(int resErrorMessage) {
        ErrorFragment fragment = new ErrorFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ERROR_MESSAGE, resErrorMessage);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_message, container, false);

        ((TextView) view.findViewById(R.id.message)).setText(getArguments().getInt(ARG_ERROR_MESSAGE));

        return view;
    }
}
