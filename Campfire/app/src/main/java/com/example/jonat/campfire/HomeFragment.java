package com.example.jonat.campfire;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import backend.algorithms.Category;
import backend.algorithms.Comparable;
import backend.algorithms.HobbiesCriteria;
import backend.algorithms.ProgrammingLanguagesCriteria;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;


/**
 * Created by jonat on 25-Feb-2017.
 */

//Used https://blog.mindorks.com/android-tinder-swipe-view-example-3eca9b0d4794#.lywnigtq7 quite
//heavily.
public class HomeFragment extends Fragment {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Student uStudent;
    private ArrayList<Student> sampleStudents;
    private ArrayList<String> c1;
    private ArrayList<String> c2;
    ArrayList<Comparable> crit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uStudent = ((MainActivity) getActivity()).getCurrentStudent();

        c1 = new ArrayList<>();
        c1.add("Java");
        c1.add("HTML");
        c1.add("CSS");
        c1.add("JavaScript");

        c2 = new ArrayList<>();
        c2.add("Running");
        c2.add("Video Games");
        c2.add("Reading");
        c2.add("Watching Sports");

        ProgrammingLanguagesCriteria languages = new ProgrammingLanguagesCriteria(c1);
        HobbiesCriteria hobbies = new HobbiesCriteria(c2);

        crit = new ArrayList<>();
        crit.add(languages);
        crit.add(hobbies);

        sampleStudents = new ArrayList<>();
        sampleStudents.add(new Student("Adam", "Capparelli", "adam@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Andrew", "Goupil", "andrew@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Fullchee", "Zhang", "fullchee@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Jonathan", "Pelastine", "jonathan@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Quinn", "Daneyko", "quinn@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Rod", "Mazloomi", "rod@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Vlad", "Chapurny", "vlad@mail.com", "12345678", crit));
        for (Student s : sampleStudents) {
            s.setDescription("Sample Description");
        }
        getActivity().setTitle("Home");
        mSwipeView = (SwipePlaceHolderView) getActivity().findViewById(R.id.swipeView);
        mContext = getActivity().getApplicationContext();
        //TODO: Probably not the best way of doing margin adjustments
        int bottomMargin = Utils.dpToPx(180);
        int sideMargin = Utils.dpToPx(32);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x - sideMargin)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        //TODO: Show applicable students, not sample.
        for (Student s : sampleStudents) {
            mSwipeView.addView(new TinderCard(getContext(), mSwipeView, s));
        }

        getActivity().findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        getActivity().findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }
}
