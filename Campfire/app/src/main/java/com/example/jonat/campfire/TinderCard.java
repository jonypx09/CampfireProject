package com.example.jonat.campfire;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

/**
 * Created by Quinn on 2/28/2017.
 */
@Layout(R.layout.tinder_card_view)
public class TinderCard {
    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public TinderCard(Context context, SwipePlaceHolderView swipeView) {
        mContext = context;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(R.drawable.person_icon).into(profileImageView);
        nameAgeTxt.setText("Quinn Daneyko");
        locationNameTxt.setText("Front-End Android Dev");
    }

    @Click(R.id.profileImageView)
    private void onClick(){
        Log.d("EVENT", "profileImageView click");
        mSwipeView.addView(this);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

}
