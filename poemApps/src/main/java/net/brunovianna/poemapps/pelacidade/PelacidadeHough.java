package net.brunovianna.poemapps.pelacidade;



import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.math.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bru on 12/02/15.
 */



public class PelacidadeHough {

    Mat mGray = new Mat();
    Mat mEdges = new Mat();
    Mat mSmall = new Mat();
    MatOfInt4 mLines = new MatOfInt4();


    public void process(Mat rgbaImage, float ratio) {


        float resizeHeight = rgbaImage.rows() * ratio;
        float resizeWidth = rgbaImage.cols() * ratio;

        Size s = new Size(resizeWidth, resizeHeight );
        Imgproc.resize(rgbaImage, mSmall, s);

        Imgproc.cvtColor(rgbaImage, mGray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.Canny(mGray, mEdges, 50.0d,200.0d);

        //Imgproc.pyrDown(mEdges, mEdges);
        //Imgproc.pyrDown(mEdges, mEdges);

        Imgproc.HoughLinesP(mEdges, mLines,1, Math.PI/180, 350, 150, 60 );


    }

    public MatOfInt4 getLines() {

        return mLines;
    }

    public Mat getSmall() {

        return mSmall;
    }

}