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
import static backend.database.DbAdapter.getStudent;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newStudentID = getArguments().getStringArray("identity");
        uEmail = newStudentID[2];
        main = (MainActivity) getActivity();

        chats = main.getChats();
        messengers = new String[chats.size()];
        chat_id = new String[chats.size()];
        display = new String[chats.size()];
        imageid = new Integer[chats.size()];

        //TODO: need to get messenger name from the chat even if there exists no messages
        int i = 0;
        for (Chat c : chats) {
            if (c.getMessages().size() != 0) {
                chat_id[i] = String.valueOf(c.getChatID());
                int j = 0;
                List<String> temp_names_list = new ArrayList<>();
                List<Student> temp_student_list = new ArrayList<Student>();
                List<Student> allStudents = main.getAllStudents();
                String temp_names = "";
                for (Message m : c.getMessages()) {
                    // get all people in the specific chat, check duplicates
                    if (m.getSender_email() != uEmail) {
                        temp_names_list.add(m.getSender_email());
                        for (Student s: allStudents){
                            if (s.getEmail().equals(m.getSender_email())){
                                temp_student_list.add(s);
                            }
                        }
                    }
                }
                // remove duplicates
                Set<String> temp_names_set = new HashSet<>();
                temp_names_set.addAll(temp_names_list);
                temp_names_list.clear();
                temp_names_list.addAll(temp_names_set);

                temp_emails = new String[temp_names_list.size()];

                int idx = 0;
                for (String n: temp_names_list) {
//                    Student tempStudent = getStudent(n);
                    Student tempStudent = null;
                    for (Student s: temp_student_list){
                        if (s.getEmail().equals(n)){
                            tempStudent = s;
                        }
                    }
                    temp_emails[idx] = tempStudent.getEmail();
                    if (temp_names_list.size() == 1) {
                       temp_names += tempStudent.getFname();
//                        temp_names += tempStudent.getEmail();
                    } else if (idx == temp_names_list.size() - 1) {
                        temp_names += " " + tempStudent.getFname();
//                        temp_names += " " + tempStudent.getEmail();
                    } else {
                       temp_names += " " + tempStudent.getFname() + ",";
//                        temp_names += " " + tempStudent.getEmail() + ",";
                    }

                    idx ++;
                }
                messengers[i] = temp_names;
                display[i] = String.valueOf(c.getMessages().get(c.getMessages().size() - 1).getText());
            } else {
                //TODO
                display[i] = "";

                // get messengers even if there exists no messages in the chat
                //messengers[i] = String.valueOf(c.getChatID());
            }
            imageid[i] = R.drawable.person_icon;
            i++;
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
