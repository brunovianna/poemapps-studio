package net.brunovianna.poemapps.pelacidade;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.nio.*;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.JavaCameraView;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.graphics.Bitmap;

import net.brunovianna.poemapps.R;
import net.brunovianna.poemapps.pelacidade.PelacidadeActivity;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.StringList;


public class PelacidadeActivity extends Activity implements CvCameraViewListener2  {

	private static final String  TAG              = "OCVSample::Activity";
    private boolean              mIsColorSelected = false;
    private Mat                  mRgba;
    private Scalar               mBlobColorRgba;
    private Scalar               mBlobColorHsv;
    private PelacidadeDetector    mDetector;
    private PelacidadeHough       mHough;
    private Mat                  mSpectrum;
    private Size                 SPECTRUM_SIZE;
    private Scalar               CONTOUR_COLOR;

    private StringList          frases;
    private int[]              frases_width;
    private float               fontScale;



    private final float resizeWidth = 500.0f;


    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

   // public PelacidadeActivity() {
   //     Log.i(TAG, "Instantiated new " + this.getClass());
   // }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Resources res = getResources();
        CharSequence[] frases_cs = res.getTextArray(R.array.pelacidade_frases);


        fontScale = res.getDisplayMetrics().density;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        frases = new StringList();
        frases_width = new int[frases_cs.length];
        for (int i=0; i<frases_cs.length;i++) {
            frases.append(frases_cs[i].toString());
            // text color - #3D3D3D
            paint.setColor(Color.rgb(61, 61, 61));
            // text size in pixels
            paint.setTextSize(14);

            android.graphics.Rect bounds = new android.graphics.Rect();
            paint.getTextBounds(frases_cs[i].toString(), 0, frases_cs[i].toString().length(), bounds);


            frases_width[i] = bounds.width();
        }

        setContentView(R.layout.pelacidade_surface_view);

        LinearLayout l = (LinearLayout) findViewById(R.id.pelacidade_surface_view);
        JavaCameraView jcv = new JavaCameraView(this,0); //context attributeset

        l.addView(jcv);

        mOpenCvCameraView = (CameraBridgeViewBase) jcv;//findViewById(R.id.pelacidade_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mHough = new PelacidadeHough();
        mDetector = new PelacidadeDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255,0,0,255);

    }

    public void onCameraViewStopped() {
        mRgba.release();
    }


    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        Mat mSmall = new Mat();

        mRgba = inputFrame.rgba();
        float ratio = resizeWidth / mRgba.cols();


        mHough.process(mRgba, ratio);
        mSmall = mHough.getSmall();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(mSmall.cols(), mSmall.rows(), conf); // this creates a MUTABLE bitmap


        MatOfInt4 lines = mHough.getLines();

        List lineList = new ArrayList();

        int greatestLineWidth = 0;
        int maximumHeightDifference = 10;
        Point thePointA = new Point();
        Point thePointB = new Point();

        if (!lines.empty()) {

            lineList = lines.toList();

            ListIterator<Integer> it = lineList.listIterator();

            while (it.hasNext()) {

                Integer x1 = (int)((float)it.next() * ratio);
                Integer y1 = (int)((float)it.next() * ratio);
                Integer x2 = (int)((float)it.next() * ratio);
                Integer y2 = (int)((float)it.next() * ratio);

                if (Math.abs((int)(y2-y1))< maximumHeightDifference) {

                    if (Math.abs((int)(x2-x1))>greatestLineWidth) {
                        greatestLineWidth = Math.abs((int)(x2-x1));
                        thePointA = new Point (x1,y1);
                        thePointB = new Point (x2,y2);
                    }
                }


                /*
                Point p1 = new Point(x1, y1);
                Point p2 = new Point(x2, y2);
                Scalar c = new Scalar(255, 255, 0);

                Core.line(mSmall, p1, p2, c, 3);
                */
            }
        }

        Utils.matToBitmap(mSmall, bmp);

        if (!thePointA.equals(new Point(0,0))) {

            Canvas canvas = new Canvas(bmp);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(246, 225, 71));
            // text shadow
            paint.setShadowLayer(3f, 2f, 2f, Color.BLACK);

            int thisFrase = (int) (Math.random() * frases.size());

            // text size in pixels
            int line_width = (int)Math.abs(thePointB.x - thePointA.x);
            paint.setTextSize((int) (14.0 * (float)(line_width/frases_width[thisFrase])));

            canvas.drawText(frases.get(thisFrase), (float)thePointA.x, (float)thePointA.y, paint);
        }

        Utils.bitmapToMat(bmp, mSmall);


        Size s = new Size(mRgba.cols(), mRgba.rows() );
        Imgproc.resize(mSmall, mRgba, s);


        return mRgba;
    }


}
