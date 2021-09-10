package com.example.fasterr;

import android.content.Intent;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<CategoryDataObject> source;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_search_screen, container, false);
        AppBarLayout appBar = root.findViewById(R.id.appBarLayout);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                View v = root.findViewById(R.id.divider);
                v.setAlpha(1.0f - Math.abs(verticalOffset / (float)
                        appBarLayout.getTotalScrollRange()));
            }
        });
        CardView card=root.findViewById(R.id.searchView);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(),ActivitySearch.class));
            }
        });
        recyclerView = root.findViewById(R.id.categoryRecycler);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
        CategoryAdapter adapter = new CategoryAdapter(source);
        recyclerView.setAdapter(adapter);
        ImageView libraries=root.findViewById(R.id.libraries);
        libraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LibrariesActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        String names[]= {
                "Psychology",
                "Science",
                "Economics",
                "Productivity",
                "Communication Skills",
                "Mindfulness & Happiness",
                "Biography & Memoir",
                "Parenting",
                "Marketing & Sales",
                "History",
                "Personal Development",
                "Philosophy",
                "Motivation & Inspiration",
                "Health & Nutrition",
                "Entrepreneurship",
                "Creativity",
                "Corporate Culture",
                "Education",
                "Religion & Spirituality",
                "Career & Success",
                "Management & Leadership",
                "Technology & the Future",
                "Society & Culture",
                "Nature & Environment",
                "Politics",
                "Money & Investments"
        };
        Integer dr[]={
                R.drawable.ic_category_psychology,
                R.drawable.ic_category_time_management,
                R.drawable.ic_category_communication,
                R.drawable.ic_category_mindfulness,
                R.drawable.ic_category_biography,
                R.drawable.ic_category_economics,
                R.drawable.ic_category_parenting,
                R.drawable.ic_category_marketing_sales,
                R.drawable.ic_category_history,
                R.drawable.ic_category_personal_growth,
                R.drawable.ic_category_philosophy,
                R.drawable.ic_category_inspiration,
                R.drawable.ic_category_health_fitness,
                R.drawable.ic_category_entrepreneurship,
                R.drawable.ic_category_creativity,
                R.drawable.ic_category_corporate_culture,
                R.drawable.ic_category_education,
                R.drawable.ic_category_religion,
                R.drawable.ic_category_career_success,
                R.drawable.ic_category_leadership,
                R.drawable.ic_category_science,
                R.drawable.ic_category_tech_future,
                R.drawable.ic_category_society,
                R.drawable.ic_category_nature_environment,
                R.drawable.ic_category_politics,
                R.drawable.ic_category_money_investment,
        };
        source=new ArrayList<CategoryDataObject>();
        for(int i=0;i<names.length;i++){
            CategoryDataObject obj = new CategoryDataObject();
            obj.setCategory_name(names[i]);
            obj.setDraw(dr[i]);
            source.add(obj);
        }

    }
}