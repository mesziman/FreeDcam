package freed.cam.apis.camera1.parameters.modes;

import android.hardware.Camera;

import com.troop.freedcam.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera1.parameters.ParametersHandler;
import freed.utils.AppSettingsManager;
import freed.utils.Log;

/**
 * Created by troop on 24.02.2017.
 */

public class AutoHdrMode extends BaseModeParameter {

    final String TAG = AutoHdrMode.class.getSimpleName();
    private boolean visible = true;
    private String state = "";
    private String format = "";
    private String curmodule = "";
    public AutoHdrMode(Camera.Parameters parameters, CameraWrapperInterface cameraUiWrapper, AppSettingsManager.SettingMode settingMode) {
        super(parameters, cameraUiWrapper, settingMode);

        if (parameters.get(cameraUiWrapper.getResString(R.string.auto_hdr_supported))!=null)
            isSupported = false;
        String autohdr = parameters.get(cameraUiWrapper.getResString(R.string.auto_hdr_supported));
        if (autohdr != null && !autohdr.equals("") && autohdr.equals(cameraUiWrapper.getResString(R.string.true_)) && parameters.get(cameraUiWrapper.getResString(R.string.auto_hdr_enable)) != null) {

            List<String> Scenes = new ArrayList<>(Arrays.asList(parameters.get(cameraUiWrapper.getAppSettingsManager().getResString(R.string.scene_mode_values)).split(",")));
            if (Scenes.contains(cameraUiWrapper.getResString(R.string.scene_mode_hdr))) {
                boolean supporton = true;
                isSupported = true;
            }
            if (Scenes.contains(cameraUiWrapper.getResString(R.string.scene_mode_asd))) {
                boolean supportauto = true;
                isSupported = true;
            }

        }
        else
            isSupported = false;

        if (isSupported) {
            cameraUiWrapper.getModuleHandler().addListner(this);
            cameraUiWrapper.getParameterHandler().PictureFormat.addEventListner(this);
        }
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCam) {
        if (valueToSet.equals(cameraUiWrapper.getResString(R.string.on_)))
        {
            parameters.set(cameraUiWrapper.getAppSettingsManager().getResString(R.string.scene_mode), cameraUiWrapper.getResString(R.string.auto));
            parameters.set(cameraUiWrapper.getResString(R.string.auto_hdr_enable), cameraUiWrapper.getResString(R.string.disable_));

        }
        else if (valueToSet.equals(cameraUiWrapper.getResString(R.string.off_)))
        {
            parameters.set(cameraUiWrapper.getAppSettingsManager().getResString(R.string.scene_mode), cameraUiWrapper.getResString(R.string.auto));
            parameters.set(cameraUiWrapper.getResString(R.string.auto_hdr_enable), cameraUiWrapper.getResString(R.string.disable_));
        }
        else if (valueToSet.equals(cameraUiWrapper.getResString(R.string.auto_)))
        {
            parameters.set(cameraUiWrapper.getAppSettingsManager().getResString(R.string.scene_mode), cameraUiWrapper.getResString(R.string.scene_mode_asd));
            parameters.set(cameraUiWrapper.getResString(R.string.auto_hdr_enable), cameraUiWrapper.getResString(R.string.enable_));
        }
        Log.d(TAG, "set auto hdr");
        ((ParametersHandler) cameraUiWrapper.getParameterHandler()).SetParametersToCamera(parameters);
        onStringValueChanged(valueToSet);
    }

    @Override
    public String GetStringValue() {
        if(parameters.get(cameraUiWrapper.getResString(R.string.auto_hdr_enable))!= null)
        {
            if (parameters.get(cameraUiWrapper.getResString(R.string.auto_hdr_enable)).equals(cameraUiWrapper.getResString(R.string.enable_))
                    && parameters.get(cameraUiWrapper.getResString(R.string.scene_mode)).equals(cameraUiWrapper.getResString(R.string.scene_mode_hdr)))
                return cameraUiWrapper.getResString(R.string.on_);
            else if (parameters.get(cameraUiWrapper.getResString(R.string.auto_hdr_enable)).equals(cameraUiWrapper.getResString(R.string.enable_))
                    && parameters.get(cameraUiWrapper.getResString(R.string.scene_mode)).equals(cameraUiWrapper.getResString(R.string.scene_mode_asd)))
                return cameraUiWrapper.getResString(R.string.auto_);
            else
                return cameraUiWrapper.getResString(R.string.off_);
        }
        else
            return cameraUiWrapper.getResString(R.string.off_);
    }

    @Override
    public void onModuleChanged(String module)
    {
        curmodule = module;
        if (curmodule.equals(cameraUiWrapper.getResString(R.string.module_video))|| curmodule.equals(cameraUiWrapper.getResString(R.string.module_video)))
        {
            hide();
            SetValue(cameraUiWrapper.getResString(R.string.off_),true);
        }
        else
        {
            if (format.contains(cameraUiWrapper.getResString(R.string.jpeg_))) {
                show();
                onIsSupportedChanged(true);
            }
            else
            {
                hide();
                SetValue(cameraUiWrapper.getResString(R.string.off_),true);
            }
        }
    }

    @Override
    public void onStringValueChanged(String val) {
        format = val;
        if (val.contains(cameraUiWrapper.getResString(R.string.jpeg_))&&!visible &&!curmodule.equals(cameraUiWrapper.getResString(R.string.module_hdr)))
            show();

        else if (!val.contains(cameraUiWrapper.getResString(R.string.jpeg_))&& visible) {
            hide();
        }
    }

    private void hide()
    {
        state = GetStringValue();
        visible = false;
        SetValue(cameraUiWrapper.getResString(R.string.off_),true);
        onStringValueChanged(cameraUiWrapper.getResString(R.string.off_));
        onIsSupportedChanged(visible);
    }
    private void show()
    {
        visible = true;
        SetValue(state,true);
        onStringValueChanged(state);
        onIsSupportedChanged(visible);
    }
}
