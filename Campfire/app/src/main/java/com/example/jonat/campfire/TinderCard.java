package com.example.jonat.campfire;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import backend.algorithms.Student;


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

    public TinderCard(Context context, SwipePlaceHolderView swipeView, Student student) {
        this.mContext = context;
        this.mSwipeView = swipeView;
        this.student = student;

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
        userHobbieTextView.setText(" Favourite Hobbie: " + student.getHobbies().get(new Random().nextInt(student.getHobbies().size())));
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
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        if (!swipedYet(this.student)) {
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
