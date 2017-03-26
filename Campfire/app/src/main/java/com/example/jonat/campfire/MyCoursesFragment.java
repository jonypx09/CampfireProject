package com.example.jonat.campfire;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;
import backend.database.DbAdapter;

import static com.example.jonat.campfire.MyCampfireFragment.campfireStudents;

public class MyCoursesFragment extends Fragment {

    private String[] newStudentID;
    DatabaseAdapter db;
    private String uEmail;
    private Student uStudent;

    private ListView coursesListView;
    private String[] names;
    private String[] description;
    private Integer[] images;
    private String[] emails;
    private String [] previousElectives;
    private String [] pastimes;
    private Integer sampleImage = R.drawable.ic_class_white_48dp;
    private Integer courseImage = R.drawable.ic_class_black_48dp;

    private ArrayList<Student> uClassmates;
    private String[] searchResults;
    private MaterialDialog loadingUsersDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newStudentID = getArguments().getStringArray("identity");
        searchResults = getArguments().getStringArray("search");
        uEmail = newStudentID[2];

        //returning our layout file
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Courses");

        //Connect to the database & Obtain Student Object
        db = new DatabaseAdapter(getActivity());
        uStudent = DbAdapter.getStudent(uEmail);

        loadingUsersDialog = new MaterialDialog.Builder(getActivity())
                .title("Loading Courses")
                .content("Please wait...")
                .progress(true, 0)
                .show();

        CountDownTimer loading = new CountDownTimer(1000, 200){
            public void onFinish(){
                loadCourses();
                loadingUsersDialog.dismiss();
            }

            public void onTick(long millisUntilFinished){

            }
        };
        loading.start();
    }

    public void loadCourses(){
        List<String> courses = DbAdapter.allStudentsCourses(uEmail);
        ArrayList<String> coursesFiltered = new ArrayList<String>();
        for (int i = 0; i < courses.size(); i++){
            coursesFiltered.add(courses.get(i));
        }
        String[] coursesArray = new String[coursesFiltered.size()];
        coursesArray = coursesFiltered.toArray(coursesArray);

        int listSize = coursesArray.length;
        names = new String[listSize];
        description = new String[listSize];
        images = new Integer[listSize];

        for (int i = 0; i < coursesArray.length; i++){
            names[i] = coursesArray[i];
            description[i] = DbAdapter.getCourse(coursesArray[i]).getName();
            images[i] = sampleImage;
        }
        MyCoursesListAdapter customList = new MyCoursesListAdapter(getActivity(), names, description, images);
        coursesListView = (ListView) getView().findViewById(R.id.allUsersList);
        coursesListView.setAdapter(customList);

        final Course currentCourse = DbAdapter.getCourse(coursesArray[0]);

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle(names[i])
                        .setMessage(currentCourse.getName() + "\n\n" +
                                "Instructor: " + currentCourse.getInstructor())
                        .setIcon(courseImage)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNeutralButton("Switch Course", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //Switch course here by refreshing tinder cards
                            }
                        })
                        .show();
            }
        });
    }
}
