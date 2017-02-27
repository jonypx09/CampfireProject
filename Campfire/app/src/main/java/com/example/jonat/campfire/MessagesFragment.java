package com.example.jonat.campfire;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static android.R.attr.fragment;

/**
 * Created by jonat on 25-Feb-2017.
 */

public class MessagesFragment extends Fragment{

    private ListView listView;
    private String messengers[] = {
            "John",
    };

    private String display[] = {
            "Hey, do you want to team up?"
    };


    private Integer imageid[] = {
            R.drawable.person_icon,
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyCampfireList customList = new MyCampfireList(getActivity(), messengers, display, imageid);

        listView = (ListView) getView().findViewById(R.id.listOfMessages);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MessengerActivity.class);
                //intent.putExtra(); for when we are passing which messages to look at
                startActivity(intent);
            }
        });
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Messages");
    }
}
