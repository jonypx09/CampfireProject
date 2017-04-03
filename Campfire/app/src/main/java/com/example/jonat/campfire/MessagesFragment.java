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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import backend.algorithms.Student;
import backend.database.Chat;
import backend.database.Message;

import static backend.database.DbAdapter.getAllChatsForUser;
import static backend.database.DbAdapter.getAllStudents;
import static backend.database.DbAdapter.getAllStudentsInChat;
import static backend.database.DbAdapter.getStudent;
import static backend.database.DbAdapter.userInChat;

/**
 * Created by jonat on 25-Feb-2017.
 */

public class MessagesFragment extends Fragment{

    private String[] newStudentID;
    private String uEmail;
    private ListView listView;
    private String messengers[];
    private String chat_id[];
    private List<Chat> chats;
    private String display[];
    private String emails[];
    private Integer imageid[];
    private MainActivity main;
    private String[] temp_emails;

    private String emails[];
    private String[] temp_emails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newStudentID = getArguments().getStringArray("identity");
        uEmail = newStudentID[2];
        main = (MainActivity) getActivity();

        chats = main.getChats();
        //chats = getAllChatsForUser(uEmail);

        if (chats != null) {
           messengers = new String[chats.size()];
            chat_id = new String[chats.size()];
            display = new String[chats.size()];
            imageid = new Integer[chats.size()];

            int i = 0;
            for (Chat c : chats) {

                chat_id[i] = String.valueOf(c.getChatID());

                // temps for the name display
                List<String> temp_names_list = getAllStudentsInChat(Integer.parseInt(chat_id[i]));
                List<Student> temp_student_list = main.getAllStudents();
                temp_emails = new String[temp_names_list.size()];
                String temp_names = "";

                temp_names_list.remove(uEmail);

                int idx = 0;
                for (String n: temp_names_list) {

                    Student tempStudent = null;

                    for (Student s: temp_student_list){
                        if (s.getEmail().equals(n)){
                            tempStudent = s;
                         }
                    }
                    temp_emails[idx] = tempStudent.getEmail();

                    if (temp_names_list.size() == 1) {
                        temp_names += tempStudent.getFname();
                    } else if (idx == temp_names_list.size() - 1) {
                        temp_names += " " + tempStudent.getFname();
                    } else {
                        temp_names += " " + tempStudent.getFname() + ",";
                    }

                    idx ++;
                }
                messengers[i] = temp_names;

                if (c.getMessages().size() > 0) {
                    // set display of the last message in the chat
                    display[i] = String.valueOf(c.getMessages().get(c.getMessages().size() - 1).getText());
                } else {
                    // set display to blank
                    display[i] = "";
                }

                imageid[i] = R.drawable.person_icon;
                i++;
            }
        } else {
            messengers = new String[1];
            chat_id = new String[1];
            display = new String[1];
            imageid = new Integer[1];
            messengers[0] = "";
            display[0] = "";
            chat_id[0] = "";
            imageid[0] = null;
        }

        //returning our layout file
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        MyCampfireListAdapter customList = new MyCampfireListAdapter(getActivity(), messengers, temp_emails, display, imageid);

        listView = (ListView) getView().findViewById(R.id.listOfMessages);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MessengerActivity.class);
                //intent.putExtra(); for when we are passing which messages to look at
                intent.putExtra("identity", newStudentID);
                intent.putExtra("messengers", messengers[i]);
                intent.putExtra("chat_id", chat_id[i]);
                startActivity(intent);

            }
        });

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Messages");
    }
}

