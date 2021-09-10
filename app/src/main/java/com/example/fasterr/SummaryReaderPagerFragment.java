package com.example.fasterr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SummaryReaderPagerFragment extends Fragment {
    TextView summary_heading,summary_content;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.summary_expanded_tab, container, false);
        summary_heading = rootView.findViewById(R.id.summary_heading);
        summary_content = rootView.findViewById(R.id.summary_content);

        return rootView;
    }
}