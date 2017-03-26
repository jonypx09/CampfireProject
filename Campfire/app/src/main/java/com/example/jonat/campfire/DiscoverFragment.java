package com.example.jonat.campfire;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;
import backend.database.DbAdapter;

import static com.example.jonat.campfire.MyCampfireFragment.campfireStudents;

public class DiscoverFragment extends Fragment {

    private String[] newStudentID;
    DatabaseAdapter db;
    private String uEmail;
    private Student uStudent;

    private ListView listView;
    private String[] names;
    private Integer[] images;
    private String[] emails;
    private String [] previousElectives;
    private String [] pastimes;
    private Integer sampleImage = R.drawable.person_icon;

    private ArrayList<Student> uClassmates;
    private String[] searchResults;

    private MaterialDialog loadingUsersDialog;
    private boolean firstTimeLoading = true;

    usersLoadedListener mCallback;

    // Container Activity must implement this interface
    public interface usersLoadedListener {
        public void loadUsers(ArrayList<Student> users);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (usersLoadedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

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
        getActivity().setTitle("Discover");

        //Connect to the database & Obtain Student Object
        uStudent = DbAdapter.getStudent(uEmail);

        if (firstTimeLoading){
            loadingUsersDialog = new MaterialDialog.Builder(getActivity())
                    .title("Loading Users")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();

            CountDownTimer loading = new CountDownTimer(1000, 200){
                public void onFinish(){
                    firstTimeLoading = false;
                    loadUsers();
                    loadingUsersDialog.dismiss();
                }

                public void onTick(long millisUntilFinished){

                }
            };
            loading.start();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Temporary helper for finding if student is in campfire.
    private boolean inCampfire(Student s) {
        for (Student stu : campfireStudents) {
            if (stu.getEmail().equals(s.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public void loadUsers(){

        List<String> enrolledCourses = DbAdapter.allStudentsCourses(uEmail);
        ArrayList<Student> classmates = uStudent.getallOtherCourseStudents(DbAdapter.getCourse(enrolledCourses.get(0)));
        mCallback.loadUsers(classmates);

        int classSize;
        if (searchResults == null){
            classSize = classmates.size() - 1;
        }else{
            classSize = searchResults.length;
        }
        names = new String[classSize];
        images = new Integer[classSize];
        emails = new String[classSize];
        previousElectives = new String[classSize];
        pastimes = new String[classSize];

        if (searchResults == null){
            //Display all results
            int i = 0;
            for (Student s : classmates) {
                if (!s.getEmail().equals(uEmail)){
                    names[i] = s.getFname() + " " + s.getLname();
                    emails[i] = s.getEmail();
                    if (s.getElectives() == null || s.getElectives().size() == 0){
                        previousElectives[i] = "No previous electives.";
                    }else{
                        previousElectives[i] = s.getElectives().get(0);
                    }
                    if (s.getHobbies() == null || s.getHobbies().size() == 0){
                        pastimes[i] = "No previous hobbies.";
                    }else{
                        pastimes[i] = s.getHobbies().get(0);
                    }
                    images[i] = sampleImage;
                    i++;
                }
            }
        }else{
            //Display search results
            for (int i = 0; i < searchResults.length; i++){
                Student result = DbAdapter.getStudent(searchResults[i]);
                names[i] = result.getFname() + " " + result.getLname();
                emails[i] = result.getEmail();
                if (result.getElectives() != null && result.getElectives().size() != 0) {
                    previousElectives[i] = result.getElectives().get(0);
                }
                pastimes[i] = result.getHobbies().get(0);
                images[i] = sampleImage;
            }
        }

        MyCampfireListAdapter customList = new MyCampfireListAdapter(getActivity(), names, emails, images);

        listView = (ListView) getView().findViewById(R.id.allUsersList);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle(names[i])
                        .setMessage("Email: " + emails[i] + "\n\n" +
                                "Previous Elective Course: " + previousElectives[i] + "\n\n" +
                                "Favourite Pastime: " + pastimes[i])
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNeutralButton("Add to Campfire", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Should be if (!uStudent.getCampfire().contains(emails[i])) {
                                if (!inCampfire(DbAdapter.getStudent(emails[i]))) {
                                    // TODO: Doesn't save to database.
//                                    uStudent.addToCampfire(db.getStudent(emails[i]));
                                    campfireStudents.add(DbAdapter.getStudent(emails[i]));
                                    Snackbar.make(getView(), "Successfully added " +
                                            names[i].substring(0, names[i].indexOf(" ")) +
                                            " to your Campfire", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                                else {
                                    Snackbar.make(getView(), "Already added " +
                                            names[i].substring(0, names[i].indexOf(" ")) +
                                            " to your Campfire", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
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
        hideKeyboard(getActivity());
    }
}
