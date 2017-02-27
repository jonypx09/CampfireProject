package com.example.jonat.campfire;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MyCampfireFragment extends Fragment {

    private ListView listView;
    private String names[] = {
            "Nicky",
            "Paula",
            "Rick",
            "Mike"
    };

    private String desc[] = {
            "Sick at UI",
            "I like kernel programming",
            "Code with Java Script!!!",
            "Photoshop is my jam!"
    };


    private Integer imageid[] = {
            R.drawable.person_icon,
            R.drawable.person_icon,
            R.drawable.person_icon,
            R.drawable.person_icon
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_my_campfire, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyCampfireList customList = new MyCampfireList(getActivity(), names, desc, imageid);

        listView = (ListView) getView().findViewById(R.id.listOfTeam);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(names[i])
                        .setMessage(desc[i])
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(imageid[i])
                        .show();
            }
        });
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Campfire");
    }
}
