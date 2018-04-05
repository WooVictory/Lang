package com.sopt.lang.Chatting;

/**
 * Created by user on 2018-01-13.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sopt.lang.Chatting.Room.ChatDetail;
import com.sopt.lang.R;
import com.sopt.lang.SharedPreferencesService;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sopt.lang.HomeFragment.ChattingFragment.SERVER_KEY;

public class ChattingRoom extends AppCompatActivity implements View.OnClickListener{
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    ProgressDialog progressDialog;

    RecyclerView chatRcv;                      //recyclerView생성
    EditText chatContent;
    TextView regChatText;

    ChatListAdapter chatListAdapter;
    DatabaseReference reference, user;
    List<ChatDetail> chatList;
    StorageReference storageReference;
    StorageReference fileRef;
    StorageReference storageRef;
    String userName, name, roomName, key, key2, message, token;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        SharedPreferencesService.Companion.getInstance().load(this);

        chatRcv = (RecyclerView) findViewById(R.id.chatting_room_item);
        chatContent = (EditText) findViewById(R.id.chatting_content_edit);
        regChatText = (TextView) findViewById(R.id.register_chatting_text);
        regChatText.setOnClickListener(this);

        fileRef = null;
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fcm-lang.appspot.com/");
        progressDialog = new ProgressDialog(this);
        name = SharedPreferencesService.Companion.getInstance().getPrefStringData("chat");
        roomName = getIntent().getExtras().get("chat_room_name").toString();
        key2 = getIntent().getExtras().getString("roomkey");
//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        SharedPreferencesService.Companion.getInstance().setPrefData(roomName, 0);

        storageReference = FirebaseStorage.getInstance().getReference("image");
        user = FirebaseDatabase.getInstance().getReference("user");

        reference = FirebaseDatabase.getInstance().getReference("roomList").child("테스트1").child("content");


        setTitle(roomName + " 채팅방");

        setRecycler();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatConversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setRecycler() {
        chatList = new ArrayList<>();
        chatRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chatListAdapter = new ChatListAdapter(chatList, getApplicationContext(), onClickListener);
        chatRcv.setAdapter(chatListAdapter);

        chatRcv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {

                chatRcv.scrollToPosition(chatList.size()-1);

            }
        });
    }

    public void chatConversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            String content = (String) ((DataSnapshot) i.next()).getValue();
            String filename = (String) ((DataSnapshot) i.next()).getValue();
            int type = ((DataSnapshot) i.next()).getValue(Integer.class);
            String name = (String) ((DataSnapshot) i.next()).getValue();
            String token = (String) ((DataSnapshot) i.next()).getValue();
            String time = (String) ((DataSnapshot) i.next()).getValue();

            chatList.add(new ChatDetail(name, token, content, type, filename));
        }

        Log.d("size", chatList.size() + "");
        chatRcv.scrollToPosition(chatList.size()-1);

        chatListAdapter.updateList(chatList);
    }

    private void sendPostToFCM(final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", message);
                    notification.put("name", roomName);
                    notification.put("title", getString(R.string.app_name));
                    root.put("data", notification);
                    root.put("to", "/topics/LANG");

                    URL Url = new URL(FCM_MESSAGE_URL);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void uploadImage(Uri uri) {

        final Uri tempUri = uri;

        StorageReference imgReference = storageReference.child(getFileName(tempUri));

        BitmapFactory.Options options = new BitmapFactory.Options();

        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = imgReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String photoUri = String.valueOf(downloadUrl);
                Log.d("url", photoUri);

                Map<String, Object> map = new HashMap<String, Object>();
                key = reference.push().getKey();

                reference.updateChildren(map);

                DatabaseReference root = reference.child(key);

                Map<String, Object> objectMap = new HashMap<String, Object>();

                objectMap.put("senderName", name);
                objectMap.put("senderToken", SharedPreferencesService.Companion.getInstance().getPrefStringData("fcm_token"));
                objectMap.put("content", photoUri);
                objectMap.put("mediaType", 102);
                objectMap.put("fileName",getFileName(tempUri));

                root.updateChildren(objectMap);

                sendPostToFCM("사진");


            }
        });
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = chatRcv.getChildAdapterPosition(view);
            if (chatList.get(pos).getMediaType() == 102) {
                fileRef = storageRef.child("image").child(chatList.get(pos).getFileName());
                downLoadImg(fileRef, "lang"+chatList.get(pos).getFileName());
            }
        }

    };

    public void downLoadImg(StorageReference fileRef, String name) {
        if (fileRef != null) {
            progressDialog.setTitle("Downloading...");
            progressDialog.setMessage(null);
            progressDialog.show();

            File rootPath = new File(Environment.getExternalStorageDirectory(), "lang");
            if(!rootPath.exists()) {
                rootPath.mkdirs();
            }

            final File localFile = new File(rootPath, name);

            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    getApplicationContext().sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(localFile)) );

                    Log.e("firebase ",";local tem file created  created " +localFile.toString());
                    progressDialog.dismiss();
                    Toast.makeText(ChattingRoom.this, "성공", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(ChattingRoom.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // progress percentage
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    // percentage in progress dialog
                    progressDialog.setMessage("Downloaded " + ((int) progress) + "%...");
                }
            });
        } else {
            Toast.makeText(ChattingRoom.this, "null이다~", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//        case R.id.reg_more_btn:
//            TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ChattingRoom.this)
//                    .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
//                        @Override
//                        public void onImageSelected(Uri uri) {
//                            uploadImage(uri);
//                        }
//                    })
//                    .create();
//
//            tedBottomPicker.show(getSupportFragmentManager());
//            break;
            case R.id.register_chatting_text:
                Map<String, Object> map = new HashMap<String, Object>();
                key = reference.push().getKey();

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = Calendar.getInstance().getTime();
                String postdate = dateformat.format(date);

                reference.updateChildren(map);
                DatabaseReference root = reference.getParent();
                root.child("recentMessage").setValue(chatContent.getText().toString());
                root.child("recentMessageTime").setValue(postdate);
                root.child("totalCount").setValue(chatList.size());


                DatabaseReference content = reference.child(key);

                Map<String, Object> objectMap = new HashMap<String, Object>();

                objectMap.put("senderName", name);
                objectMap.put("senderToken", SharedPreferencesService.Companion.getInstance().getPrefStringData("fcm_token"));
                objectMap.put("content", chatContent.getText().toString());
                objectMap.put("mediaType", 101);
                objectMap.put("fileName", "");
                objectMap.put("timeStamp", postdate);

                content.updateChildren(objectMap);

                sendPostToFCM(chatContent.getText().toString());

                chatContent.setText("");
                break;


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferencesService.Companion.getInstance().setPrefData(roomName, chatList.size());
        Log.d("count2", SharedPreferencesService.Companion.getInstance().getPrefIntegerData(roomName)+"");

    }
}
