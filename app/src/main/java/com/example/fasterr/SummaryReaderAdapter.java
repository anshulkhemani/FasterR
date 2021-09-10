package com.example.fasterr;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.List;
import androidx.viewpager.widget.PagerAdapter;

public class SummaryReaderAdapter extends PagerAdapter {

    TextView heading,summary;
    private List<SummaryDataObject> list;


    public SummaryReaderAdapter(List<SummaryDataObject> list)
    {
        this.list = list;
    }
    @Override
    public int getCount()
    {
        return list.size();
    }
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View itemView
                = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.summary_expanded_tab,
                        container,
                        false);
        container.addView(itemView);
        heading= itemView.findViewById(R.id.summary_heading);
        heading.setText(list.get(position).getSummaryHeading());
        summary= itemView.findViewById(R.id.summary_content);
        summary.setText(list.get(position).getContent());
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }
}