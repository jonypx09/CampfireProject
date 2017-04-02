package com.example.jonat.campfire;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.DbAdapter;


import static backend.database.DbAdapter.newChat;
import static com.example.jonat.campfire.HomeFragment.loadedStudents;
import static com.example.jonat.campfire.HomeFragment.swipedRight;


/**
 * Created by Quinn on 2/28/2017.
 */
@Layout(R.layout.tinder_card_view)
public class TinderCard {
    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameTxt)
    private TextView nameTxt;

    @View(R.id.quickFactTxt)
    private TextView quickBioTxt;

    @View(R.id.userPLanguagesListView)
    private ListView userInfoListView;

    @View(R.id.hobbieTextView)
    private TextView userHobbieTextView;

    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private Student student;
    private String uEmail;
    private Course currCourse;
    private Student uStudent;

    public TinderCard(Context context, SwipePlaceHolderView swipeView, Student student, Student uStudent, Course currCourse) {
        this.mContext = context;
        this.mSwipeView = swipeView;
        this.student = student;
        this.uEmail = uStudent.getEmail();
        this.uStudent = uStudent;
        this.currCourse = currCourse;

        if (!(student.equals(null))) {
            loadedStudents.add(student);
        }
    }

    @Resolve
    private void onResolved() {

        //Glide.with(mContext).load(R.drawable.person_icon).into(profileImageView);
        nameTxt.setText(student.getFname() + " " + student.getLname());
        quickBioTxt.setText(student.getDescription());
        ArrayList<String> stuLangs = new ArrayList();
        for (String s : student.getProgramming()) {
            if (s != null) {
                stuLangs.add(" " + s);
            }
        }
        userHobbieTextView.setText(" Favourite Hobby: " + student.getHobbies().get(new Random().nextInt(student.getHobbies().size())));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.mContext, R.layout.criteria_list_item, stuLangs);
        userInfoListView.setAdapter(adapter);

    }

    @Click(R.id.profileImageView)
    private void onClick() {
        Log.d("EVENT", "profileImageView click");
        mSwipeView.addView(this);
    }

    @SwipeOut
    private void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
        loadedStudents.remove(0);
        loadedStudents.add(this.student);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        loadedStudents.remove(0);
        if (!swipedYet(this.student)) {
            List<Student> stuList;
            if (this.uStudent.getMatchedStudents().get(currCourse.getName()) == null) {
                stuList = new ArrayList<>();
            }
            else {
                stuList = this.uStudent.getMatchedStudents().get(currCourse.getName());
            }
            stuList.add(this.student);
            System.out.println("SWIPED RIGHT");
            this.uStudent.getMatchedStudents().put(currCourse.getName(), stuList);
            DbAdapter.updateStudent(this.uStudent);
            System.out.println("uSTUDENT: " + this.uStudent.getMatchedStudents());
            System.out.println("student: " + this.student.getMatchedStudents());
            for (Student s : this.student.getMatchedStudents().get(this.currCourse.getName())) {
                if (s.getEmail().equals(uEmail)) {
                    System.out.println("NEW CHAT");
                    newChat(uEmail,this.student.getEmail());
                }
            }
            swipedRight.add(this.student);
        }
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }

    //Helper for checking if swiped right yet.
    private boolean swipedYet(Student s) {
        for (Student stu : swipedRight) {
            if (stu.getEmail().equals(s.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
