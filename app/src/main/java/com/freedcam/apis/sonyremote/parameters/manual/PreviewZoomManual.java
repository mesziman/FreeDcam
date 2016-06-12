/*
 *
 *     Copyright (C) 2015 Ingo Fuchs
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package com.freedcam.apis.sonyremote.parameters.manual;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import com.freedcam.apis.basecamera.interfaces.CameraWrapperInterface;
import com.freedcam.apis.sonyremote.sonystuff.SimpleStreamSurfaceView;

import java.util.Set;

/**
 * Created by troop on 09.04.2016.
 */
public class PreviewZoomManual extends BaseManualParameterSony
{
    private SimpleStreamSurfaceView surfaceView;
    private int zoomFactor = 1;

    public PreviewZoomManual(SimpleStreamSurfaceView surfaceView, CameraWrapperInterface cameraUiWrapper) {
        super("", "", "", cameraUiWrapper);
        this.surfaceView = surfaceView;
        isSupported = VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;
        stringvalues = new String[] {"1","2","4","8","10","12","14","16","18","20"};
    }

    @Override
    public void SonyApiChanged(Set<String> mAvailableCameraApiSet) {
        super.SonyApiChanged(mAvailableCameraApiSet);
    }

    @Override
    public boolean IsSupported() {
        return isSupported;
    }

    @Override
    public boolean IsSetSupported() {
        return true;
    }

    @Override
    public boolean IsVisible() {
        return isSupported;
    }

    @Override
    public String[] getStringValues() {
        return stringvalues;
    }

    @Override
    public void SetValue(int valueToSet) {
        zoomFactor = Integer.parseInt(stringvalues[valueToSet]);
        surfaceView.PreviewZOOMFactor = zoomFactor;
    }

    @Override
    public String GetStringValue() {
        return surfaceView.PreviewZOOMFactor+"";
    }
}
