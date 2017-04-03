package com.example.jonat.campfire;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
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
    private Handler handler;
    private FragmentActivity activity;

    public TinderCard(Context context, SwipePlaceHolderView swipeView, Student student, Student uStudent, Course currCourse, FragmentActivity activity) {
        this.mContext = context;
        this.mSwipeView = swipeView;
        this.student = student;
        this.uEmail = uStudent.getEmail();
        this.uStudent = uStudent;
        this.currCourse = currCourse;
        this.activity = activity;

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
        loadedStudents.remove(0);
        if (loadedStudents.isEmpty()) {
            Toast.makeText(this.activity, "You've Swiped on Everyone! All you can do now is wait!",
                    Toast.LENGTH_LONG).show();
        }
        swipedRight.add(this.student);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        loadedStudents.remove(0);
        if (loadedStudents.isEmpty()) {
            Toast.makeText(this.activity, "You've Swiped on Everyone! All you can do now is wait!",
                    Toast.LENGTH_LONG).show();
        }whi
//            List<Student> stuList;
//            if (this.uStudent.getMatchedStudents().get(currCourse.getName()) == null) {
//                stuList = new ArrayList<>();
//            }
//            else {
//                stuList = this.uStudent.getMatchedStudents().get(currCourse.getName());
//            }
//            stuList.add(this.student);
            System.out.println("SWIPED RIGHT");
//            this.uStudent.getMatchedStudents().put(currCourse.getName(), stuList);

            handler = new Handler();
            final Student tempStu = this.uStudent;
            final Student tempCurStu = this.student;
            final Course tempCourse = this.currCourse;
            final FragmentActivity tempAct = this.activity;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    DbAdapter.updateStudent(tempStu);
//                    DbAdapter.addMatch(tempStu.getEmail(), tempCourse.getName(),tempCurStu.getEmail());
//                    for (Student s : DbAdapter.getMatchedMap(tempStu.getEmail()).get(tempCourse.getName())) {
//                        System.out.println(s.getEmail() + " : " + uEmail);
//                        if (s.getEmail().equals(tempCurStu.getEmail())) {
//                            System.out.println("NEW CHAT");
//                            newChat(tempCurStu.getEmail(), tempStu.getEmail());
//                            Toast.makeText(tempAct, "You matched with "+ tempCurStu.getFname() + " ! Head to Messages to build your Dream Team!",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    System.out.println("Done swipe thread");
//                }
//            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    DbAdapter.addMatch(tempStu.getEmail(), tempCourse.getName(),tempCurStu.getEmail());
                    final List<Student> potentialMatches = DbAdapter.getMatchedMap(tempStu.getEmail()).get(tempCourse.getName());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (Student s : potentialMatches) {
                                System.out.println(s.getEmail() + " : " + uEmail);
                                if (s.getEmail().equals(tempCurStu.getEmail())) {
                                    System.out.println("NEW CHAT");
                                    newChat(tempCurStu.getEmail(), tempStu.getEmail());
                                    Toast.makeText(tempAct, "You matched with "+ tempCurStu.getFname() + " ! Head to Messages to build your Dream Team!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            System.out.println("Done swipe thread");
                        }
                    });
                }
            }).start();

            swipedRight.add(this.student);
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

}
