/**
 * Created by Hamza Hassan (hamza.hassan92@gmail.com).
 */

package com.frosty92.RNAudioRecorder;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import com.facebook.react.bridge.*;
import com.facebook.react.bridge.Callback;
import android.media.MediaScannerConnection;
import javax.annotation.Nullable;
import java.io.*;
import android.media.MediaRecorder;
import android.media.CamcorderProfile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Intent;
import android.content.Context;

public class RNAudioRecorderModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext _reactContext;
     private MediaRecorder audioRecorder = null;
     private File audioFile;
     private String audioFilePath = null;
     private String audioFileName = null;
     private Callback audioRecorderCallback = null;


    public RNAudioRecorderModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _reactContext = reactContext;      
    }
    @Override
    public String getName() {
        return "RNAudioRecorder";
    }

    private void setFileName() {
        audioFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName += "/" + "AUDIO_:" +timeStamp + ".3gp";
    }

    private boolean prepareAudioRecorder() {
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        setFileName();
        audioRecorder.setOutputFile(audioFileName);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            audioRecorder.prepare();
            return true;
        } catch (IOException e) {
            return false;
        }           
    }
    @ReactMethod
    public void startAudioRecording(final Callback successCallback, final Callback errorCallback) {
        if (audioRecorderCallback == null) {
            if (prepareAudioRecorder()) {
                try {
                    audioRecorder.start();
                    audioRecorderCallback = successCallback;
                } catch (final Exception e) {
                    errorCallback.invoke("error: unable to invoke audioRecorder.start(): " + e.getMessage());
                }  
            } else {
                errorCallback.invoke("AudioRecorder returned false");
            }
        } else {
            errorCallback.invoke("AudioRecorderCallback was not null");
        }        
    }

    @ReactMethod
    public void stopAudioRecording(final Callback callback) {     
        if (audioRecorderCallback != null) {
            audioFile = new File(audioFileName);
            audioFilePath = audioFile.getAbsolutePath();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);         
            intent.setData(Uri.fromFile(audioFile));
            _reactContext.sendBroadcast(intent);
            releaseAudioRecorder();
        } else {
            callback.invoke("not recording");
        }
    }
    private void releaseAudioRecorder() {
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
             audioRecorder = null;
            if (audioRecorderCallback != null) {
                audioRecorderCallback.invoke(audioFileName);
                audioRecorderCallback = null;
            }   
        }       
    }
}