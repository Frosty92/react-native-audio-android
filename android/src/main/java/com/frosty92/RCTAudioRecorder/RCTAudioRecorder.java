/**
 * Created by Hamza Hassan (hamza.hassan92@gmail.com).
 */

package com.frosty92.modules.RCTAudioRecorder;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import com.facebook.react.bridge.*;
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

public class RCTAudioRecorder extends ReactContextBaseJavaModule {
    private final ReactApplicationContext _reactContext;
     private MediaRecorder audioRecorder = null;
     private File audioFile;
     private String audioFilePath = null;
     private String audioFileName = null;
     private Promise audioRecorderPromise = null;


    public RCTAudioRecorder(ReactApplicationContext reactContext) {
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
        audioFileName += "/" + timeStamp;
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
    public void startAudioRecording(final Promise promise) {
        if (audioRecorderPromise == null) {
            if (prepareAudioRecorder()) {
                try {
                    audioRecorder.start();
                    audioRecorderPromise = promise;
                } catch (final Exception e) {
                    promise.reject("error: unable to invoke audioRecorder.start(): " + e.getMessage());
                }  
            } else {
                promise.reject("AudioRecorder returned false");
            }
        } else {
            promise.reject("AudioRecorderPromise was not null");
        }        
    }

    @ReactMethod
    public void stopAudioRecording(final Promise promise) {     
        if (audioRecorderPromise != null) {
            audioFile = new File(audioFileName);
            audioFilePath = audioFile.getAbsolutePath();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);         
            intent.setData(Uri.fromFile(audioFile));
            _reactContext.sendBroadcast(intent);
            releaseAudioRecorder();
            promise.resolve("finished recording");
        } else {
            promise.resolve("not recording");
        }
    }
    private void releaseAudioRecorder() {
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
             audioRecorder = null;
            if (audioRecorderPromise != null) {
                audioRecorderPromise.resolve(audioFileName);
                audioRecorderPromise = null;
            }   
        }       
    }
}