package com.example.jonat.campfire;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import backend.algorithms.CampfireGroup;
import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.DbAdapter;

public class MyCampfireFragment extends Fragment {

    private String[] programmingLanguages;

    private HashMap<Course, ArrayList<CampfireGroup>> studentCampfires;
    private ArrayList<CampfireGroup> campfiresForCurrentCourse = new ArrayList<CampfireGroup>();
    private Course currentCourse;
    private String currentCourseString;
    private List<CampfireGroup> currentGroups;

    private ArrayList<String> c1;
    ArrayList<Comparable> crit;
    private ListView listView;

    private String[] newStudentID;
    private String uEmail;
    private Student uStudent;
    private String[] names;
    private Integer[] images;
    private String[] moreInfo;
    private Integer sampleImage = R.drawable.ic_group_white_48dp;
    private Integer groupImageDark = R.drawable.ic_group_work_black_48dp;
    private MaterialDialog loadingGroupsDialog;

    MainActivity main;
    private Handler handler;

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
        currentGroups = main.getCurrentGroups();
        loadGroups();
    }

    public void loadGroups(){
        currentCourse = main.getCurrentCourse();
        studentCampfires = uStudent.getCampfires();
        campfiresForCurrentCourse = studentCampfires.get(currentCourse);

        if (currentGroups != null) {
            names = new String[currentGroups.size()];
            images = new Integer[currentGroups.size()];
            moreInfo = new String[currentGroups.size()];
            final Integer[] groupIDs = new Integer[currentGroups.size()];
            final String[] groupMembers = new String[currentGroups.size()];
            int i = 0;
            for (CampfireGroup g : currentGroups) {
                String members = new String();
                for (Student s: g.getMembers()){
                    members += "-" + s.getFname() + " " + s.getLname() + "\n";
                }
                names[i] = g.getName();
                moreInfo[i] =  "ID: " + Integer.toString(g.getGroupID()) + "\nCurrent Allocation: "
                        + Integer.toString(g.getCurrentSize()) + "\nMaximum Allocation: "
                        + Integer.toString(g.getSize());
                groupMembers[i] = members;
                images[i] = sampleImage;
                groupIDs[i] = g.getGroupID();
                i++;
            }
            MyCampfireListAdapter customList = new MyCampfireListAdapter(getActivity(), names, moreInfo, images);

            listView = (ListView) getView().findViewById(R.id.listOfTeam);
            listView.setAdapter(customList);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    if (currentGroups.get(i).getCurrentSize() < currentGroups.get(i).getSize()){
                        new AlertDialog.Builder(getActivity())
                                .setTitle(names[i])
                                .setMessage(moreInfo[i] + "\nMembers:\n" + groupMembers[i])
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNeutralButton("Add Member", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        displayOptionList(groupIDs[i]);
                                    }
                                })
                                .setNegativeButton("Leave Group", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        confirmLeaveGroup(names[i], groupIDs[i]);
                                    }
                                })
                                .setIcon(groupImageDark)
                                .show();
                    }else{
                        new AlertDialog.Builder(getActivity())
                                .setTitle(names[i])
                                .setMessage(moreInfo[i] + "\nMembers:\n" + groupMembers[i])
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Leave Group", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        confirmLeaveGroup(names[i], groupIDs[i]);
                                    }
                                })
                                .setIcon(groupImageDark)
                                .show();
                    }
                }
            });
        }
    }

    public void displayOptionList(final int groupID){
        final ArrayList<Student> classmates = main.getStudentsInCourse();
        String[] classmatesNames = main.getClassmatesNames();
        new LovelyChoiceDialog(getActivity())
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Choose a Student")
                .setMessage("")
                .setItems(classmatesNames, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        addMember(classmates.get(position).getEmail(), classmates.get(position).getFname() + " " +
                                classmates.get(position).getLname(), groupID);
                    }
                })
                .show();
    }

    public void addMember(final String email, String name, final int groupID){
        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Adding " + name);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAdapter.addStudentToGroup(email, groupID);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                        main.refreshGroupListMemberCount(uEmail);
                    }
                });
            }
        }).start();
    }

    public void confirmLeaveGroup(final String name, final int groupID){
        android.support.v7.app.AlertDialog terminateDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        terminateDialog.setMessage("Are you sure you want to leave this group?");
        terminateDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        terminateDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        leaveGroup(name, groupID);
                    }
                });
        terminateDialog.show();
    }

    public void leaveGroup(String name, final int groupID){
        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Leaving " + name);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAdapter.removeStudentFromGroup(uEmail, groupID);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                        main.refreshGroupListMemberCount(uEmail);
                    }
                });
            }
        }).start();
    }
}
