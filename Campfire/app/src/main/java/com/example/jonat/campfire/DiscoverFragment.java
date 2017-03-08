package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

/**
 * Created by jonat on 25-Feb-2017.
 */

public class DiscoverFragment extends Fragment {

    private String[] newStudentID;
    DatabaseAdapter db;
    private String uEmail;
    private Student uStudent;

    private ArrayList<Student> uClassmates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newStudentID = getArguments().getStringArray("identity");
        uEmail = newStudentID[2];

        //returning our layout file
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Discover");

        //Connect to the database
        db = new DatabaseAdapter(getActivity());

        uStudent = db.getStudent(uEmail);

        ArrayList<String> enrolledCourses = db.enrolledIn(uEmail);
        ArrayList<Student> classmates = db.getStudentsInCourse(enrolledCourses.get(0));

//        ListView userList = (ListView) getActivity().findViewById(R.id.userList);
        LinearLayout userList = (LinearLayout) getActivity().findViewById(R.id.userList);

        for (int i = 0; i < classmates.size(); i++){
            Button newButton = new Button(getActivity());
            newButton.setText(classmates.get(i).getFname() + " " + classmates.get(i).getLname());
            userList.addView(newButton);
        }



    }
}
