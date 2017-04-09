package com.uoft.jonathan.campfire;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import backend.algorithms.Student;

public class MyCoursesFragment extends Fragment {

    private String[] newStudentID;
    private String uEmail;
    private Student uStudent;
    private String currentCourseCode;

    private ListView coursesListView;
    private String[] names;
    private String[] description;
    private Integer[] images;
    private Integer sampleImage = R.drawable.ic_class_white_48dp;
    private Integer courseImage = R.drawable.ic_class_black_48dp;
    private Integer openCourseImage = R.drawable.ic_import_contacts_white_48dp;

    private ArrayList<Student> uClassmates;
    private String[] searchResults;

    private String[] courseCodes;
    private String[] courseNames;
    private String[] courseInstructors;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newStudentID = getArguments().getStringArray("identity");
        searchResults = getArguments().getStringArray("search");
        courseCodes = getArguments().getStringArray("courseCodes");
        courseNames = getArguments().getStringArray("courseNames");
        courseInstructors = getArguments().getStringArray("courseInstructors");
        currentCourseCode = getArguments().getString("currentCourseCode");

        uEmail = newStudentID[2];
        handler = new Handler();

        //returning our layout file
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Courses");

        loadCourses();
    }

    public void loadCourses(){
        int listSize = courseCodes.length;
        names = new String[listSize];
        description = new String[listSize];
        images = new Integer[listSize];

        for (int i = 0; i < courseCodes.length; i++){
            names[i] = courseCodes[i];
            description[i] = courseNames[i];
            if (courseCodes[i].equals(currentCourseCode)){
                images[i] = openCourseImage;
            }else{
                images[i] = sampleImage;
            }
        }
        MyCoursesListAdapter customList = new MyCoursesListAdapter(getActivity(), names, description, images);
        coursesListView = (ListView) getView().findViewById(R.id.allUsersList);
        coursesListView.setAdapter(customList);

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle(names[i])
                        .setMessage(courseNames[i] + "\n\n" +
                                "Instructor: " + courseInstructors[i])
                        .setIcon(courseImage)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNeutralButton("Switch Course", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Switch course here by refreshing tinder cards
                                switchCourse(names[i]);
                            }
                        })
                        .show();
            }
        });
    }

    public void switchCourse(String courseCode){
        if (courseCode.equals(currentCourseCode)){
            new MaterialDialog.Builder(getActivity())
                    .title("Warning")
                    .content("You are already in this course! Choose another one.")
                    .positiveText("Ok")
                    .show();
        }else{
            final MainActivity main = (MainActivity) getActivity();
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            final String newCourseCode = courseCode;
            progressDialog.setMessage("Please wait....");
            progressDialog.setTitle("Switching Courses");
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    main.updateCourse(newCourseCode);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Snackbar.make(getView(), "Switched to " +
                                    newCourseCode, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("identity", newStudentID);
                            bundle.putString("currentCourse", newCourseCode);
                            Fragment fragment = null;
                            fragment = new HomeFragment();
                            fragment.setArguments(bundle);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }
                    });
                }
            }).start();
        }

    }
}
