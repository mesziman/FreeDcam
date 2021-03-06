package freed.cam.apis.camera2.parameters.huawei;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Rational;

import com.huawei.camera2ex.CaptureRequestEx;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.parameters.manual.AbstractManualShutter;
import freed.cam.apis.camera2.parameters.AeHandler;

/**
 * Created by troop on 08.06.2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HuaweiAeHandler extends AeHandler {
    public HuaweiAeHandler(CameraWrapperInterface cameraUiWrapper) {
        super(cameraUiWrapper);
        manualExposureTimeApi2.fireIsReadOnlyChanged(true);
    }

    @Override
    protected void setManualItemsSetSupport(boolean off) {
    }


    @Override
    protected void setExposureTime(int valueToSet) {
        cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_PROFESSIONAL_MODE,CaptureRequestEx.HUAWEI_PROFESSIONAL_MODE_ENABLED);
        if (valueToSet > 0) {
            String shutter = manualExposureTimeApi2.getStringValues()[valueToSet];
            Rational rational;
            if (shutter.contains("/"))
            {
                String[]split = shutter.split("/");
                rational = new Rational(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
            }
            else if (shutter.contains("."))
            {
                double sh = Double.parseDouble(shutter);
                double sh10 = sh*10;
                rational = new Rational((int)sh10,10);
            }
            else
            {
                rational = new Rational(Integer.parseInt(shutter),1);
            }
            int val = (int) AbstractManualShutter.getMilliSecondStringFromShutterString(manualExposureTimeApi2.getStringValues()[valueToSet]);
            cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_SENSOR_EXPOSURE_TIME, val);
            cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_PROF_EXPOSURE_TIME, rational);
            manualExposureTimeApi2.fireIntValueChanged(valueToSet);

        }
        else
        {
            cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_SENSOR_EXPOSURE_TIME, 0);
        }
        cameraHolder.captureSessionHandler.capture();
    }

    @Override
    protected boolean isExposureTimeSetSupported() {
        return true;
    }

    @Override
    protected void setIso(int valueToSet) {
        if (cameraHolder == null || cameraHolder.captureSessionHandler.GetActiveCameraCaptureSession() == null)
            return;
        cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_PROFESSIONAL_MODE,CaptureRequestEx.HUAWEI_PROFESSIONAL_MODE_ENABLED);
        if (valueToSet == 0)
        {
            //aeModeApi2.SetValue(cameraUiWrapper.getResString(R.string.on),true);
            cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_SENSOR_ISO_VALUE, 0);
        }
        else
        {
            cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_SENSOR_ISO_VALUE, Integer.parseInt(manualISoApi2.getStringValues()[valueToSet]));
        }
        cameraHolder.captureSessionHandler.capture();
    }

    @Override
    protected void setExpoCompensation(int valueToSet) {
        float t = Float.parseFloat(manualExposureApi2.getStringValues()[valueToSet].replace(",","."));
        cameraHolder.captureSessionHandler.SetParameterRepeating(CaptureRequestEx.HUAWEI_EXPOSURE_COMP_VALUE, t);
    }

    @Override
    protected boolean isExposureCompSetSupported() {
        return true;
    }
}
