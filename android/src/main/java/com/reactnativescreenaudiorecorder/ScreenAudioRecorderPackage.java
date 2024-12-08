package com.reactnativescreenaudiorecorder;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import android.os.Build;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.legacyreactnativescreenaudiorecorder.LegacyScreenAudioRecorderModule;


public class ScreenAudioRecorderPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
       if(Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
           modules.add(new LegacyScreenAudioRecorderModule(reactContext));
        return modules;
        }else{
            modules.add(new ScreenAudioRecorderModule(reactContext));
            return modules;  
        }

    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
