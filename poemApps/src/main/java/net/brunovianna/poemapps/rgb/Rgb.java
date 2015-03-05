package net.brunovianna.poemapps.rgb;


import processing.core.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import net.brunovianna.poemapps.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class Rgb extends PApplet {

    PFont droidFont;
    PGraphics r, b;
    PImage roda, texto;
    boolean dragging, draggingRed;
    int downX, downY, halfWidth, halfHeight;

    double angle = Math.PI;
    double start_phi;

    private Mat textNoRed, textRedComplete, wheel, maskedWheel;
    private Bitmap bmpTextNoRed, bmpTextRedComplete, bmpWheel;
    private boolean connectFlag = false;

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    initCv();
                    connectFlag = true;
                }

                break;

                default: {
                    super.onManagerConnected(status);
                }
                break;
            }

        }
    };

    public void initCv() {


        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;



        bmpTextNoRed = BitmapFactory.decodeResource(getResources(), R.drawable.rgb_textos_r, o);
        bmpTextRedComplete = BitmapFactory.decodeResource(getResources(), R.drawable.rgb_textos_rp, o);
        bmpWheel = BitmapFactory.decodeResource(getResources(), R.drawable.rgb_wheel, o);
        textNoRed = new Mat(bmpTextNoRed.getHeight(), bmpTextNoRed.getWidth(), CvType.CV_8UC4);
        textRedComplete = new Mat(bmpTextRedComplete.getHeight(), bmpTextRedComplete.getWidth(), CvType.CV_8UC4);
        wheel = new Mat(bmpWheel.getHeight(), bmpWheel.getWidth(), CvType.CV_8UC4);

        Utils.bitmapToMat(bmpWheel, wheel);
        Utils.bitmapToMat(bmpTextRedComplete, textRedComplete);

        Size s = new Size(bmpWheel.getHeight(), bmpWheel.getWidth());

        Mat cyan = new Mat(s, CvType.CV_8UC4, new Scalar(255.0d, 0.0d, 0.0d, 0.0d));
        Mat maskedWheel = new Mat();
        Mat invertedWheel = new Mat();

        Core.compare(wheel, textRedComplete, maskedWheel, Core.CMP_EQ);
        Core.bitwise_not(maskedWheel, invertedWheel);
        //Core.subtract (wheel, cyan, maskedWheel);

        Utils.matToBitmap(maskedWheel, bmpWheel);


    }


    public void setup() {
        //size(100,100);
        //droidFont = createFont("DroidSans", 32, true);


        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mOpenCVCallBack)) {
            Log.e("TEST", "Cannot connect to OpenCV Manager");
        }


        background(0);
        halfWidth = width / 2;
        halfHeight = height / 2;
        //image (r, 0,0);
        //image (b,width/2,0);
        dragging = false;
        draggingRed = false;

        angle = (float) Math.PI;



    }

    public void draw() {
        //blendMode();
        background(0);


        if (dragging&&connectFlag) {

            //double delta_phi;

            double end_phi;
            end_phi = Math.atan((double) (mouseY - height / 2) / (mouseX - width / 2));

            angle = end_phi - start_phi;

            Mat r = new Mat();
            Mat dst = new Mat();
            Point center = new Point((double) width, height);

            r = Imgproc.getRotationMatrix2D(center, angle, 1.0);

            Imgproc.warpAffine(wheel, dst, r, new Size(width, height));
            //Utils.matToBitmap(dst, bmpWheel);



		/*
        int direction;
		delta_phi = dist (mouseX, mouseY, downX, downY) / 600.0f * 2.0f * Math.PI;

		if (downY > height/2) {
			if (mouseX>downX) 
				direction = -1;
			 else
				direction = 1;
		} else {
			if (mouseX>downX) 
				direction = 1;
			 else
				direction = -1;
		}
			
			
		delta_phi *= direction;
		
		angle = delta_phi;
		
		*/

        }

        //PImage texto = new PImage (bmpTextNoRed);
        //Bitmap bmpText = (Bitmap)texto.getNative();

        if (connectFlag)  image(new PImage(bmpWheel), 0, 0);


    }

    public void mousePressed() {

        dragging = true;
        downX = mouseX;
        downY = mouseY;
        start_phi = Math.atan((double) (mouseY - height / 2) / (mouseX - width / 2));


    }


    public void mouseReleased() {
        dragging = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mOpenCVCallBack);
    }
}