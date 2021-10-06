package com.run.ultimate_fitness.ui.inbox;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.utils.Constants;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatPage extends AppCompatActivity {

    String messageText;
    private String listenerID = "CHAT_PAGE";
    private String receiverID;
    public String newMessage;
    private ChatView chatView;

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


        chatView = (ChatView) findViewById(R.id.chat_view);



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
                        newMessage = textMessage.toString();
                        chatView.addMessage(new ChatMessage(newMessage, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
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

    }

    private void sendTextMessage() {
        if (Constants.UID != Constants.MASTER_UID){

            receiverID = Constants.MASTER_UID;
        }
        else
        {
            receiverID = "GgeJlS0TGBQ9qbweq7pKGyYzfYb2";
        }
         String receiverType = CometChatConstants.RECEIVER_TYPE_USER;

        TextMessage textMessage = new TextMessage(receiverID, messageText, receiverType);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener <TextMessage> () {
            @Override
            public void onSuccess(TextMessage textMessage) {
                Log.d(TAG, "Message sent successfully: " + textMessage.toString());
            }
            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Message sending failed with exception: " + e.getMessage());
            }
        });
    }
}