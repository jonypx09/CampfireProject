package com.example.jonat.campfire;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;

import backend.algorithms.CampfireGroup;
import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.Student;

public class MyCampfireFragment extends Fragment {

    private String[] programmingLanguages;

    private HashMap<Course, ArrayList<CampfireGroup>> studentCampfires;
    private ArrayList<CampfireGroup> campfiresForCurrentCourse = new ArrayList<CampfireGroup>();
    private Course currentCourse;
    private String currentCourseString;

    private ArrayList<String> c1;
    ArrayList<Comparable> crit;
    private ListView listView;

    private String[] newStudentID;
    private String uEmail;
    private Student uStudent;
    private String[] names;
    private Integer[] images;
    private String[] size;
    private Integer sampleImage = R.drawable.ic_group_white_48dp;
    private MaterialDialog loadingGroupsDialog;

    MainActivity main;

    // TODO: Needs to be changed, doesn't actually pull from database, just temp for video.
    public static ArrayList<Student> campfireStudents = new ArrayList<>();
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newStudentID = getArguments().getStringArray("identity");
        currentCourseString = getArguments().getString("currentCourse");
        uEmail = newStudentID[2];
        //returning our layout file
        return inflater.inflate(R.layout.fragment_my_campfire, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Campfire");
        main = (MainActivity) getActivity();
        uStudent = main.getCurrentStudent();
        loadGroups();
    }

    public void loadGroups(){
        currentCourse = main.getCurrentCourse();
        studentCampfires = uStudent.getCampfires();
        campfiresForCurrentCourse = studentCampfires.get(currentCourse);

        if (campfiresForCurrentCourse != null) {
            names = new String[campfiresForCurrentCourse.size()];
            images = new Integer[campfiresForCurrentCourse.size()];
            size = new String[campfiresForCurrentCourse.size()];
            int i = 0;
            for (CampfireGroup g : campfiresForCurrentCourse) {
                names[i] = g.getName();
                size[i] = Integer.toString(g.getSize());
                images[i] = sampleImage;
                i++;
            }
            MyCampfireListAdapter customList = new MyCampfireListAdapter(getActivity(), names, size, images);

            listView = (ListView) getView().findViewById(R.id.listOfTeam);
            listView.setAdapter(customList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(names[i])
                            .setMessage("Current Allocation" + size[i])
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNeutralButton("Message", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Fragment fragment = new MessagesFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArray("identity", newStudentID);
                                    fragment.setArguments(bundle);
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_frame, fragment);
                                    ft.commit();
                                }
                            })
                            .setNegativeButton("View Profile", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent profileIntent = new Intent(getActivity(), ClassmatesProfileActivity.class);
                                    profileIntent.putExtra("Current Allocation:", size[i]);
                                    startActivity(profileIntent);
                                }
                            })
                            .setIcon(images[i])
                            .show();
                }
            });
        }
    }
}
