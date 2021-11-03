package com.run.ultimate_fitness.ui.inbox;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.utils.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatPage extends AppCompatActivity {

    String messageText;
    private String listenerID = "CHAT_PAGE";
    public String receiverID;
    public String newMessage;
    private ChatView chatView;
    public String fullName;

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";


    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String USER_UID = "uid";
    public String userUID;
    private String temp_key;

    public String uid ="";

    public  DatabaseReference root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("qwA1Ou5vbWPb2SHpUd55tjA5wWF2");

    public TextMessage textMessage;



    @Override
    protected void onPause() {
        CometChat.removeMessageListener(listenerID);
        super.onPause();
    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        getSupportActionBar().hide();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        loadImage();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        userUID = sharedPreferences.getString(USER_UID, "uid");




        chatView = (ChatView) findViewById(R.id.chat_view);


        //updateChatConversation();
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                // perform actual message sending
                messageText = chatView.getTypedMessage();

                sendTextMessage();

                CometChat.addMessageListener(listenerID, new CometChat.MessageListener() {
                    @Override
                    public void onTextMessageReceived(TextMessage textMessage) {
                        Log.d(TAG, "Text message received successfully: " + textMessage.toString());
                        //newMessage = textMessage.toString();
                        newMessage = textMessage.getText();
                        //chatView.addMessage(new ChatMessage(newMessage, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
                    }
                    @Override
                    public void onMediaMessageReceived(MediaMessage mediaMessage) {
                    }
                    @Override
                    public void onCustomMessageReceived(CustomMessage customMessage) {
                    }
                });


                return true;
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                updateChatConversation(snapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                updateChatConversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private String chatMessage, userName, temp_uid;

    private void updateChatConversation(DataSnapshot snapshot) {

        Iterator i = snapshot.getChildren().iterator();
        while (i.hasNext()){



            chatMessage = (String) ((DataSnapshot)i.next()).getValue();
            userName = (String) ((DataSnapshot)i.next()).getValue();
           temp_uid = (String) ((DataSnapshot)i.next()).getValue();

            if (temp_uid.equals(uid)){
                chatView.addMessage(new ChatMessage(chatMessage, System.currentTimeMillis(), ChatMessage.Type.SENT));
            }

            else {
                chatView.addMessage(new ChatMessage(chatMessage, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
            }


        }
    }

    private void sendTextMessage() {
        if (userUID.equals("lHRkYjOj2YNQnK4NNIPHw4nO8pg1")){

            receiverID = "69wADqnIpqYUnmidwcZwaO5F8RL2";
        }
        if (userUID.equals("69wADqnIpqYUnmidwcZwaO5F8RL2") )
        {
            receiverID = "lHRkYjOj2YNQnK4NNIPHw4nO8pg1";
        }
         String receiverType = CometChatConstants.RECEIVER_TYPE_USER;

        textMessage = new TextMessage(receiverID, messageText, receiverType);


        sendFirebaseMessage();
    }

    private void sendFirebaseMessage() {
        root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("qwA1Ou5vbWPb2SHpUd55tjA5wWF2");

        Map<String,Object> map1 = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        //map1.put(uid, "");
        root.updateChildren(map1);

        DatabaseReference message_root = root.child(temp_key);
        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("name", fullName);
        map2.put("message", textMessage.getText());
        map2.put("uid", uid);

        message_root.updateChildren(map2);

    }

    public  void loadImage(){
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");
        fullName = firstName + " " + lastName;


    }

}