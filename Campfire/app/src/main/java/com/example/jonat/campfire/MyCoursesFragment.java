package com.example.jonat.campfire;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

import static com.example.jonat.campfire.MyCampfireFragment.campfireStudents;

public class MyCoursesFragment extends Fragment {

    private String[] newStudentID;
    DatabaseAdapter db;
    private String uEmail;
    private Student uStudent;

    private ListView coursesListView;
    private String[] names;
    private Integer[] images;
    private String[] emails;
    private String [] previousElectives;
    private String [] pastimes;
    private Integer sampleImage = R.drawable.ic_class_white_48dp;
    private Integer courseImage = R.drawable.ic_class_black_48dp;

    private ArrayList<Student> uClassmates;
    private String[] searchResults;

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
        uStudent = db.getStudent(uEmail);

        ArrayList<String> courses = db.enrolledIn(uEmail);
        ArrayList<String> coursesFiltered = new ArrayList<String>();
        for (int i = 1; i < courses.size(); i++){
            coursesFiltered.add(courses.get(i));
        }
        String[] coursesArray = new String[coursesFiltered.size()];
        coursesArray = coursesFiltered.toArray(coursesArray);

        int listSize = coursesArray.length;
        names = new String[listSize];
        images = new Integer[listSize];

        for (int i = 0; i < coursesArray.length; i++){
            names[i] = coursesArray[i];
            images[i] = sampleImage;
        }
        MyCoursesListAdapter customList = new MyCoursesListAdapter(getActivity(), names, images);
        coursesListView = (ListView) getView().findViewById(R.id.allUsersList);
        coursesListView.setAdapter(customList);

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle(names[i])
                        .setMessage("Course Description:\nInstructor:")
                        .setIcon(courseImage)

                        .show();
            }
        });

    }
}
