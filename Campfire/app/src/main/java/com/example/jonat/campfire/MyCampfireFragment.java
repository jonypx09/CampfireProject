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

import java.util.ArrayList;

import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class MyCampfireFragment extends Fragment {

    private String[] programmingLanguages;
    DatabaseAdapter db;

    private ArrayList<Student> studentCampfire;
    private ArrayList<String> c1;
    ArrayList<Comparable> crit;
    private ListView listView;

    private String[] newStudentID;
    private String uEmail;
    private Student uStudent;
    private String[] names;
    private Integer[] images;
    private String[] emails;
    private Integer sampleImage = R.drawable.person_icon;

    // TODO: Needs to be changed, doesn't actually pull from database, just temp for video.
    public static ArrayList<Student> campfireStudents = new ArrayList<>();
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newStudentID = getArguments().getStringArray("identity");
        //returning our layout file
        return inflater.inflate(R.layout.fragment_my_campfire, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uStudent = ((MainActivity) getActivity()).getCurrentStudent();
        uEmail = uStudent.getEmail();

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        Course csc301 = db.getCourse("csc301h1");
        // Should be:
        // studentCampfire = uStudent.getCampfire();
        studentCampfire = campfireStudents;
        names = new String[studentCampfire.size()];
        images = new Integer[studentCampfire.size()];
        emails = new String[studentCampfire.size()];
        int i = 0;
        for (Student s : studentCampfire) {
            names[i] = s.getFname() + " " + s.getLname();
            emails[i] = s.getEmail();
            images[i] = sampleImage;
            i++;
        }
        MyCampfireListAdapter customList = new MyCampfireListAdapter(getActivity(), names, emails, images);

        listView = (ListView) getView().findViewById(R.id.listOfTeam);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(names[i])
                        .setMessage(emails[i])
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
                                profileIntent.putExtra("studentEmail", emails[i]);
                                startActivity(profileIntent);
                            }
                        })
                        .setIcon(images[i])
                        .show();
            }
        });
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Campfire");
    }
}
