package com.example.jonat.campfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

import static com.example.jonat.campfire.HomeFragment.swipedRight;

/**
 * Created by jonat on 25-Feb-2017.
 */

public class MessagesFragment extends Fragment{

    private String[] newStudentID;
    DatabaseAdapter db;
    private String uEmail;
    private ListView listView;
    private String messengers[];

    private String display[];


    private Integer imageid[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newStudentID = getArguments().getStringArray("identity");
        uEmail = newStudentID[2];

        //TODO: Change this from local to integrated with database.
        ArrayList<Student> peopleToMessage = swipedRight;
        messengers = new String[peopleToMessage.size()];
        display = new String[peopleToMessage.size()];
        imageid = new Integer[peopleToMessage.size()];
        int i = 0;
        for (Student s : peopleToMessage) {
            messengers[i] = s.getFname();
            display[i] = "";
            imageid[i] = R.drawable.person_icon;
            i++;
        }

        //returning our layout file
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyCampfireListAdapter customList = new MyCampfireListAdapter(getActivity(), messengers, display, imageid);

        listView = (ListView) getView().findViewById(R.id.listOfMessages);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MessengerActivity.class);
                //intent.putExtra(); for when we are passing which messages to look at
                intent.putExtra("identity", newStudentID);
                startActivity(intent);
            }
        });

        //Connect to the database
        db = new DatabaseAdapter(getActivity());

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Messages");
    }
}
