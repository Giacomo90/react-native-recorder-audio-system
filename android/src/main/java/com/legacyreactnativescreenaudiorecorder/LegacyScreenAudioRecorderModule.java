package com.legacyreactnativescreenaudiorecorder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Objects;

@ReactModule(name = LegacyScreenAudioRecorderModule.NAME)
public class LegacyScreenAudioRecorderModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    public static final String NAME = "ScreenAudioRecorder";
    private final ReactApplicationContext reactContext;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private LegacyScreenAudioRecorderService recordService;
    private ReadableMap options;

    public static final int MEDIA_PROJECTION_REQUEST_CODE = 1001;

    public LegacyScreenAudioRecorderModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(this);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            try {
                LegacyScreenAudioRecorderService.ScreenAudioRecorder binder = (LegacyScreenAudioRecorderService.ScreenAudioRecorder) service;
                recordService = binder.getRecordService();
                
                recordService.setSampleRateInHz(44100);
                if (options.hasKey("sampleRate")) {
                    recordService.setSampleRateInHz(options.getInt("sampleRate"));
                }

                recordService.setChannelConfig(AudioFormat.CHANNEL_IN_MONO);
                if (options.hasKey("channels")) {
                    if (options.getInt("channels") == 2) {
                        recordService.setChannelConfig(AudioFormat.CHANNEL_IN_STEREO);
                    }
                }

                recordService.setAudioFormat(AudioFormat.ENCODING_PCM_16BIT);
                if (options.hasKey("bitsPerSample")) {
                    if (options.getInt("bitsPerSample") == 8) {
                        recordService.setAudioFormat(AudioFormat.ENCODING_PCM_8BIT);
                    }
                }

                recordService.setAudioEmitInterval(100);
                if (options.hasKey("audioEmitInterval")) {
                    recordService.setAudioEmitInterval(options.getInt("audioEmitInterval"));
                }

                Boolean fromMic = false;
                recordService.setFromMic(false);
                if (options.hasKey("fromMic")) {
                    fromMic = options.getBoolean("fromMic");
                    recordService.setFromMic(options.getBoolean("fromMic"));
                }

                String documentDirectoryPath = getReactApplicationContext().getFilesDir().getAbsolutePath();

                recordService.setOutFile(documentDirectoryPath + "/" + "audio.wav");
                if (options.hasKey("fileName")) {
                    recordService.setOutFile(documentDirectoryPath + "/" + options.getString("fileName"));
                }

                recordService.setSaveFile(false);
                if (options.hasKey("saveFile")) {
                    recordService.setSaveFile(options.getBoolean("saveFile"));
                }

                recordService.calcBufferSize();
                recordService.calcRecordingBufferSize();
                recordService.setTmpFile(documentDirectoryPath + "/" + "temp.pcm");
                recordService.setEventEmitter(reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class));

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !fromMic) {
                    // Richiesta di permesso per la registrazione dello schermo
                    mediaProjectionManager = (MediaProjectionManager) getCurrentActivity().getSystemService(reactContext.MEDIA_PROJECTION_SERVICE);
                    Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
                    getCurrentActivity().startActivityForResult(captureIntent, MEDIA_PROJECTION_REQUEST_CODE);
                } else {
                    recordService.startAudioCapture();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == MEDIA_PROJECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Il permesso è stato concesso, avvia la registrazione
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (mediaProjectionManager != null && data != null) {
                    mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                    if (mediaProjection != null) {
                        recordService.setMediaProject(mediaProjection);
                        recordService.startAudioCapture();
                    } else {
                        Log.e(NAME, "MediaProjection is null after permission.");
                    }
                } else {
                    Log.e(NAME, "MediaProjectionManager or data is null.");
                }
            }
        } else {
            Log.e(NAME, "Permission denied or Activity result is not OK.");
            // Gestisci il caso di permesso negato
        }
    }

    @Override
    public void onNewIntent(Intent intent) { }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void init(ReadableMap options) {
        this.options = options;
    }

    @ReactMethod
    public void start() {
        Log.d(NAME, "Avvio del servizio audio...");
        Activity activity = getCurrentActivity();
        if (activity == null) {
            throw new IllegalStateException("Current activity is null. Ensure that start() is called when the app is in the foreground.");
        }
        Intent intent = new Intent(getCurrentActivity(), LegacyScreenAudioRecorderService.class);
        getCurrentActivity().bindService(intent, connection, getCurrentActivity().BIND_AUTO_CREATE);
    }

    @ReactMethod
    public void stop(Promise promise) {
        if (recordService != null) {
            recordService.stopAudioCapture(promise);
            Objects.requireNonNull(getCurrentActivity()).unbindService(connection);
        }
    }

    @ReactMethod
    public string info() {
    string text= "your device is < android 14";
    return text;
}
}


