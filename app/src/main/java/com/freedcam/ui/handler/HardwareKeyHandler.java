package com.freedcam.ui.handler;

import android.view.KeyEvent;

import com.freedcam.MainActivity;
import com.freedcam.apis.basecamera.interfaces.CameraWrapperInterface;
import com.freedcam.utils.AppSettingsManager;
import com.freedcam.utils.Logger;
import com.freedcam.utils.StringUtils;


/**
 * Created by troop on 29.08.2014.
 */
public class HardwareKeyHandler
{
    private final MainActivity activity;
    private CameraWrapperInterface cameraUiWrapper;
    private final String TAG = HardwareKeyHandler.class.getSimpleName();
    private AppSettingsManager appSettingsManager;


    public HardwareKeyHandler(MainActivity activity, AppSettingsManager appSettingsManager)
    {
        this.activity = activity;
        this.appSettingsManager = appSettingsManager;
    }

    public void SetCameraUIWrapper(CameraWrapperInterface cameraUiWrapper)
    {
        this.cameraUiWrapper = cameraUiWrapper;
    }

    public boolean OnKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            activity.finish();
        return true;
    }

    public boolean OnKeyLongPress(int keyCode, KeyEvent event)
    {
        boolean set = false;
        if(keyCode == KeyEvent.KEYCODE_3D_MODE ||keyCode == KeyEvent.KEYCODE_POWER || keyCode == KeyEvent.KEYCODE_HEADSETHOOK)
        {
            set = true;


        }

        return set;
    }

    public void OnKeyDown(int keyCode, KeyEvent event)
    {
        int appSettingsKeyShutter = 0;

        if (appSettingsManager.getString(AppSettingsManager.SETTING_EXTERNALSHUTTER).equals(StringUtils.VoLP))
            appSettingsKeyShutter = KeyEvent.KEYCODE_VOLUME_UP;
        if (appSettingsManager.getString(AppSettingsManager.SETTING_EXTERNALSHUTTER).equals(StringUtils.VoLM))
            appSettingsKeyShutter = KeyEvent.KEYCODE_VOLUME_DOWN;
        if (appSettingsManager.getString(AppSettingsManager.SETTING_EXTERNALSHUTTER).equals(StringUtils.Hook) || appSettingsManager.getString(AppSettingsManager.SETTING_EXTERNALSHUTTER).equals(""))
            appSettingsKeyShutter = KeyEvent.KEYCODE_HEADSETHOOK;

        if(keyCode == KeyEvent.KEYCODE_3D_MODE ||keyCode == KeyEvent.KEYCODE_POWER || keyCode == appSettingsKeyShutter || keyCode == KeyEvent.KEYCODE_UNKNOWN)
        {
            Logger.d(TAG, "KeyUp");
            cameraUiWrapper.GetModuleHandler().DoWork();
        }
        //shutterbutton full pressed
        if (keyCode == KeyEvent.KEYCODE_CAMERA)
        {
            cameraUiWrapper.GetModuleHandler().DoWork();
        }
        // shutterbutton half pressed
       /* if (keyCode == KeyEvent.KEYCODE_FOCUS)
            cameraUiWrapper.StartFocus();*/
    }
}
