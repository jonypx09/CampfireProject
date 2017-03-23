package com.example.jonat.campfire;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by jonat on 25-Feb-2017.
 */

//Used https://blog.mindorks.com/android-tinder-swipe-view-example-3eca9b0d4794#.lywnigtq7 quite
//heavily.
public class HomeFragment extends Fragment {

    private String[] newStudentID;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Student uStudent;
    private ArrayList<Student> allStudents;
    private ArrayList<Student> otherStudents301;
    private ArrayList<Student> students301;
    private ArrayList<String> c1;
    private ArrayList<String> c2;
    ArrayList<Comparable> crit;
    DatabaseAdapter db;
    Course csc301;

    SharedPreferences prefs = null;

    //TODO: Make this work with database, instead of local.
    public static ArrayList<Student> swipedRight = new ArrayList<Student>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        newStudentID = getArguments().getStringArray("identity");
        prefs = getContext().getSharedPreferences("come.example.jonat.campfire", MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseAdapter(getContext());
        allStudents = db.getAllStudents();
        csc301 = db.getCourse("csc301h1");
        students301 = db.getStudentsInCourse("csc301h1");

        uStudent = ((MainActivity) getActivity()).getCurrentStudent();
        //TODO: Does not work, includes current student.
        otherStudents301 = uStudent.getallOtherCourseStudents(csc301);

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

        // Change swipeOn to:
        //     - allStudents for every student in db.
        //     - otherStudents301 for all other students in csc301h1. (Currently unavailable)
        //     - students301 for all students in csc301h1.

        ArrayList<Student> swipeOn = otherStudents301;
        if (swipeOn.isEmpty()) {
            Toast.makeText(getContext(), "No available matches.", Toast.LENGTH_LONG).show();
        }
        else {
            for (Student s : swipeOn) {
                mSwipeView.addView(new TinderCard(getContext(), mSwipeView, s));
            }
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

        if (prefs.getBoolean("firstrun", true)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Welcome to Campfire!")
                    .setMessage("Get ready to start building a great team!")
                    .setNeutralButton("Join the Campfire", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
   }
}
