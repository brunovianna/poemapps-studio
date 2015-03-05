package net.brunovianna.poemapps.rgb;


import processing.core.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class Rgb extends PApplet {

    PFont droidFont;
    PGraphics r, b;
    PImage roda, texto;
    boolean dragging, draggingRed;
    int downX, downY, halfWidth, halfHeight;

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

        buf = createGraphics(roda.height*2,roda.width*2,JAVA2D);


        background(255);
        halfWidth = width/2;
        halfHeight = height/2;
        //image (r, 0,0);
        //image (b,width/2,0);
        dragging = false;
        draggingRed = false;

        angle =(float) Math.PI;

    }

    public void draw () {
        //blendMode();
        background(0);

        float proporcao = (float) ((float)displayWidth / (float)roda.width);

        if (dragging) {

            //double delta_phi;

            double end_phi;
            end_phi = Math.atan((double)(mouseY-height/2)/(mouseX-width/2));

            angle = end_phi- start_phi;
		
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


        //image (fora, (displayWidth-fora.width)/2, (displayHeight- fora.height)/2, displayWidth, displayWidth);
        //image (texto, 0, halfHeight-texto.height*proporcao/2, width, texto.height*proporcao);

        pushMatrix();
        translate(roda.width/2,roda.height/2);
        rotate((float) angle);
        translate(-roda.width/2,-roda.height/2);
        image(roda,0,0);
        popMatrix();
        image (texto, 0, 0);

        /*
        buf.beginDraw();
        buf.imageMode(CENTER);
        buf.pushMatrix();
        buf.translate(roda.width/2,roda.height/2);
        buf.rotate((float) angle);
        buf.image(roda,0,0, roda.width, roda.height);
        //buf.shapeMode(CENTER);
        //buf.shape(rodashape,0,0);
        buf.popMatrix();
        buf.endDraw();

        blend (buf,0,0,roda.width,roda.height,0,(height-width)/2,width,width,SUBTRACT);
        //blend (buf,0,0,roda.width,roda.height,0,(int)(halfHeight-texto.height*proporcao/2),(int) displayWidth, (int)(texto.height*proporcao),SUBTRACT);
        //image (roda, (displayWidth-largura)/2, (displayHeight- altura)/2, largura, altura);
        */
	/*
  	if (!dragging)  {
    image (r, 0,0);
    image (b,width/2,0);
  } else {
    if (draggingRed) {
      println ( "r");
      blend (r,0,0,rectWidth,rectHeight,mouseX-downX+rectWidth,mouseY-downY,rectWidth,rectHeight,SUBTRACT);
      image (r, 0,0);
    } else {
      println ( "b");
      blend (b,0,0,rectWidth,rectHeight,mouseX-downX,mouseY-downY,rectWidth,rectHeight,SUBTRACT);
      image (b,width/2,0);
    }
  } 
  
  
  fill (51,255,255);
  text ("texto azul", width/3,height/3);
  fill (204,0,0);
  text ("texto vermelho", width/3,height/3*2);
  //blendMode(ADD);
  //blend (r,0,0,150,150,mouseX,mouseY,150,150,SUBTRACT);
  */
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