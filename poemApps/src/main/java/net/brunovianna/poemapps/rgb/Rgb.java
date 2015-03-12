package net.brunovianna.poemapps.rgb;


import processing.core.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class Rgb extends PApplet {

    PFont droidFont;
    PGraphics r, b;
    PImage roda, texto;
    boolean dragging, draggingRed;
    int downX, downY, halfWidth, halfHeight;
    float proporcao;
    double angle = Math.PI;
    double start_phi;


    PGraphics buf;
//PShape rodashape;

    public void setup() {
        //size(100,100);
        //droidFont = createFont("DroidSans", 32, true);

        texto = loadImage("rgb_textos_rp.png");
        roda = loadImage("rgb_wheel.png");
        //rodashape = loadShape("rgb-rodavetorial.svg");

        background(255);
        halfWidth = width/2;
        halfHeight = height/2;
        //image (r, 0,0);
        //image (b,width/2,0);
        dragging = false;
        draggingRed = false;

        angle =(float) Math.PI;

        proporcao = (float) ((float)width / (float)roda.width);


    }

    public void draw () {
        //blendMode();
        background(0);


        if (dragging) {

            //double delta_phi;

            double end_phi;
            end_phi = Math.atan((double)(mouseY-halfHeight)/(mouseX-halfWidth));

            angle += (end_phi- start_phi);



            if ((end_phi<0)&&(start_phi>0)&&(downX>halfWidth)&&(mouseX<halfWidth))
                angle +=Math.PI;

            if ((end_phi>0)&&(start_phi<0)&&(downX<halfWidth)&&(mouseX>halfWidth))
                angle -= Math.PI;

            if ((end_phi>0)&&(start_phi<0)&&(downX>halfWidth)&&(mouseX<halfWidth))
                angle += Math.PI;

            if ((end_phi<0)&&(start_phi>0)&&(downX<halfWidth)&&(mouseX>halfWidth))
                angle -= Math.PI;


            Log.i("ANGLE", "Angle = " +angle+", start= "+start_phi+ " nnd = "+end_phi);

            start_phi  = end_phi;
            downX = mouseX;
            downY = mouseY;

        }


        translate(0, (height-width)/2);
        pushMatrix();
        translate(width/2,width/2);
        rotate((float) angle);
        translate(-width/2,-width/2);
        image(roda,0,0, width, width);
        popMatrix();
        image (texto, 0, 0, width, width);

    }

    public void mousePressed() {

        dragging = true;
        downX = mouseX;
        downY = mouseY;
        start_phi = Math.atan((double)(mouseY-height/2)/(mouseX-width/2));


    }



    public void mouseReleased() {
        dragging = false;
    }
}