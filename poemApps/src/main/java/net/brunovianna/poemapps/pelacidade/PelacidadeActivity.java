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


public class PelacidadeActivity extends Activity implements OnTouchListener, CvCameraViewListener2  {

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
    private int                   camHeight, camWidth;

    private StringList           frases;
    private Size[]               frases_width;
    private int                  frase_index;

    private static int            fontFace = Core.FONT_HERSHEY_TRIPLEX;
    private int[]                baseLine;

    private int                  pixCnt1, pixCnt2;
    private byte[]                bArray;
    private int[]                 iArray;
    private PImage              img;


    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(PelacidadeActivity.this);
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

        baseLine = new int[1];

        frases = new StringList();
        frases_width = new Size[frases_cs.length];
        for (int i=0; i<frases_cs.length;i++) {
            frases.append(frases_cs[i].toString());
            //frases_width[i] = Core.getTextSize(frases.get(i), fontFace, 1.0d, 2, baseLine);
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

    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;
        
        int x = (int)event.getX() - xOffset;
        int y = (int)event.getY() - yOffset;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x>4) ? x-4 : 0;
        touchedRect.y = (y>4) ? y-4 : 0;

        touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        mDetector.setHsvColor(mBlobColorHsv);

        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), conf); // this creates a MUTABLE bitmap

        Utils.matToBitmap(mRgba, bmp);

        mHough.process(mRgba);

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

                Integer x1 = it.next();
                Integer y1 = it.next();
                Integer x2 = it.next();
                Integer y2 = it.next();

                if (Math.abs((int)(y2-y1))< maximumHeightDifference) {

                    if (Math.abs((int)(x2-x1))>greatestLineWidth) {
                        greatestLineWidth = Math.abs((int)(x2-x1));
                        thePointA = new Point (x1,y1);
                        thePointB = new Point (x2,y2);
                    }
                }

                Point p1 = new Point(x1, y1);
                Point p2 = new Point(x2, y2);
                Scalar c = new Scalar(255, 255, 0);

                Core.line(mRgba, p1, p2, c, 3);
            }
        }


        if (!thePointA.equals(new Point(0,0))) {

            float scale = 2.0f;
            Canvas canvas = new Canvas(bmp);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(61, 61, 61));
            // text size in pixels
            paint.setTextSize((int) (14 * scale));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

            int thisFrase = (int) (Math.random() * frases.size());
            android.graphics.Rect bounds = new android.graphics.Rect();
            paint.getTextBounds(frases.get(thisFrase), 0, frases.get(thisFrase).length(), bounds);
            canvas.drawText(frases.get(thisFrase), (float)thePointA.x, (float)thePointA.y, paint);
        }

        Utils.bitmapToMat(bmp, mRgba);

        /*
        img = toPImage(mRgba);

        PGraphics pg = createGraphics(img.width, img.height);

        pg.image(img, 0,0);


        fill(200);
        if (!thePointA.equals(new Point(0,0))) {
            pg.text(thisFrase, (int)thePointA.x, (int)thePointA.y);
            pg.ellipse(10,10,100,100);
        }

        graphicsToMat(pg).copyTo(mRgba);

        */


        //double fontScale = (Math.abs(thePointB.x - thePointA.x)) / frases_width[thisFrase].width ;

        //Scalar cor = new Scalar(200.0, 200.0, 200.0);

        //Core.putText(mRgba, frases.get(thisFrase),thePointA, fontFace, 2.0d, cor, 3);




        /*
        if (mIsColorSelected) {
            mDetector.process(mRgba);
            List<MatOfPoint> contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);

            Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel);
        }
        */
        return mRgba;
    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

/*
    // Convert PImage (ARGB) to Mat (CvType = CV_8UC4)
    private Mat toMat(PImage image) {
        int w = image.width;
        int h = image.height;
        Mat mat = new Mat(h, w, CvType.CV_8UC4);
        byte[] data8 = new byte[w*h*4];
        int[] data32 = new int[w*h];
        arrayCopy(image.pixels, data32);
        ByteBuffer bBuf = ByteBuffer.allocate(w*h*4);
        IntBuffer iBuf = bBuf.asIntBuffer();
        iBuf.put(data32);
        bBuf.get(data8);
        mat.put(0, 0, data8);
        return mat;
    }

    // Convert PImage (ARGB) to Mat (CvType = CV_8UC4)
    private Mat graphicsToMat(PGraphics image) {
        int w = image.width;
        int h = image.height;
        Mat mat = new Mat(h, w, CvType.CV_8UC4);
        byte[] data8 = new byte[w*h*4];
        int[] data32 = new int[w*h];
        image.loadPixels();
        arrayCopy(image.pixels, data32);
        ByteBuffer bBuf = ByteBuffer.allocate(w*h*4);
        IntBuffer iBuf = bBuf.asIntBuffer();
        iBuf.put(data32);
        bBuf.get(data8);
        mat.put(0, 0, data8);
        return mat;
    }



    // Convert Mat (CvType=CV_8UC4) to PImage (ARGB)
    private PImage toPImage(Mat mat) {
        int w = mat.width();
        int h = mat.height();
        PImage image = createImage(w, h, ARGB);
        byte[] data8 = new byte[w*h*4];
        int[] data32 = new int[w*h];
        mat.get(0, 0, data8);
        ByteBuffer.wrap(data8).asIntBuffer().get(data32);
        arrayCopy(data32, image.pixels);
        return image;
    }

    */
}
