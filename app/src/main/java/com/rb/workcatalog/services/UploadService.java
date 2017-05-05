package com.rb.workcatalog.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;
import com.rb.workcatalog.firebasedatabase.FireDB;
import com.rb.workcatalog.utils.PrefManager;

import java.util.Timer;
import java.util.TimerTask;

public class UploadService extends MyBaseTaskService {
    String TAG = "Uplaod";
    // [START declare_ref]
    PrefManager pref;
    private StorageReference mStorageRef;
    // [END declare_ref]


    public static final String EXTRA_UID_KEY = "extra_uid_key";

    public static final String ACTION_UPLOAD = "action_upload";
    public static final String EXTRA_FILE_URI = "extra_file_uri";


    public UploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // [START get_storage_ref]
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // [END get_storage_ref]
        pref = new PrefManager(this);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("UPLOAD", "onStartCommand:");
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            String fileUri = intent.getStringExtra(EXTRA_FILE_URI);
            String key_uid = intent.getStringExtra(EXTRA_UID_KEY);
            uploadFromUri(fileUri,key_uid);
        }else {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }

        return START_REDELIVER_INTENT;
    }


    // [START upload_from_uri]
    private void uploadFromUri(final String file, final String key_uid) {
        Log.d(TAG, "uploadFromUri:src:" + file.toString());

        // [START_EXCLUDE]
        // [END_EXCLUDE]
        Uri fileUri = Uri.parse("file://"+file);
        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference photoRef = mStorageRef.child("photos").child(key_uid)
                .child(fileUri.getLastPathSegment());
        // [END get_child_ref]

        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
if (photoRef.getActiveUploadTasks().size()>0){
    return;
}
taskStarted();


        photoRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(getString(R.string.progress_uploading),
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri:onSuccess");
                        pref.removePathFromKey(file,key_uid);
                        // Get the public download URL
                        Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                        // [START_EXCLUDE]
                        //showUploadFinishedNotification(downloadUri, fileUri);
                        taskCompleted();
                        dismissProgressNotification();

                        FireDB.getInstance().savePaths(downloadUri.toString(),key_uid,""+taskSnapshot.getMetadata().getCreationTimeMillis());

                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                        // [START_EXCLUDE]
                        //showUploadFinishedNotification(null, fileUri);
                        taskCompleted();
                        dismissProgressNotification();

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END upload_from_uri]


    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {


        // Hide the progress notification
        dismissProgressNotification();

// Make Intent to MainActivity
        Intent intent = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failure);
        showFinishedNotification(caption, intent, success);
    }


}
