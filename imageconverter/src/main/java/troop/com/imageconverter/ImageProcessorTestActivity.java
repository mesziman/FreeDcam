package troop.com.imageconverter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by troop on 08.08.2015.
 */
public class ImageProcessorTestActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback
{
    SurfaceView surfaceView;
    Camera camera;

    ImageProcessorWrapper imageProcessor;
    int w,h;

    Button buttonProcessFrame;
    ImageView maskImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView_camera);
        surfaceView.getHolder().addCallback(this);
        maskImageView = (ImageView)findViewById(R.id.imageView);
        buttonProcessFrame = (Button)findViewById(R.id.button_processFrame);
        buttonProcessFrame.setOnClickListener(processFrameClick);
        imageProcessor = new ImageProcessorWrapper();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        camera = Camera.open(0);
        Camera.Parameters parameters = camera.getParameters();
        //parameters.setPreviewSize(640,480);
        parameters.set("preview-format","yuv420sp");
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(surfaceView.getHolder());

        } catch (IOException e) {
            e.printStackTrace();
        }
        w = camera.getParameters().getPreviewSize().width;
        h = camera.getParameters().getPreviewSize().height;
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        camera.setPreviewCallback(null);
        imageProcessor.Init();
        imageProcessor.ProcessFrame(data.clone(), w, h);
        Bitmap bimap = imageProcessor.GetNativeBitmap();
        //Bitmap bimap = Bitmap.createBitmap(imageProcessor.GetPixelData(), w,h, Bitmap.Config.ARGB_8888);

        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path, "test.jpg"); // the File to save to
        try {
            fOut = new FileOutputStream(file);
            bimap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close(); // do not forget to close the stream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21,w,h,null);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        yuvImage.compressToJpeg(new Rect(0,0,w,h),50,byteArrayOutputStream);
        try {
            FileOutputStream fos = new FileOutputStream (new File(path, "test2.jpg"));
            byteArrayOutputStream.writeTo(fos);
            byteArrayOutputStream.flush();
            fos.close();
            byteArrayOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        maskImageView.setImageBitmap(bimap);
        imageProcessor.ReleaseNative();
    }

    OnClickListener processFrameClick = new OnClickListener()
    {

        @Override
        public void onClick(View v) {
            camera.setPreviewCallback(ImageProcessorTestActivity.this);
        }
    };
}
