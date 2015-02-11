package net.brunovianna.poemapps.bussola;


import processing.core.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class Bussola extends PApplet {

	PImage fora, dentro;

	private SensorManager mSensorManager;
	private mySensorListener mListener;



	float angle = 0;


	public void setup() {

		fora = loadImage("fora.png");
		dentro = loadImage("dentro.png");
		
	    // Get an instance of the SensorManager
	    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    
        // Create our Preview view and set it as the content of our
        // Activity
        mListener = new mySensorListener();
        mListener.start();
		
	}

	public void pause() {
		if (mListener!=null) mListener.stop();
	}


	public void resume() {
		if (mListener!=null) mListener.start();
	}


	public void draw() {

		background (204);
		float proporcao = (float) ((float)displayWidth / (float)fora.width);
		
		
		
		//image (fora, (displayWidth-fora.width)/2, (displayHeight- fora.height)/2, displayWidth, displayWidth);
		image (fora, 0, (displayHeight-displayWidth)/2, displayWidth, displayWidth);
		pushMatrix();
		translate (displayWidth/2, displayHeight/2);
		rotate(angle);

		translate (-displayWidth/2, -displayHeight/2);

		float largura = dentro.width * proporcao;
		float altura = dentro.height * proporcao;
		
		image (dentro, (displayWidth-largura)/2, (displayHeight- altura)/2, largura, altura);
		popMatrix();
	}

	class mySensorListener implements SensorEventListener {
        private Sensor mRotationVectorSensor;
        private final float[] mRotationMatrix = new float[16];
        private float[] mOrientation = new float[3];
		
        public mySensorListener() {
            // find the rotation-vector sensor
            mRotationVectorSensor = mSensorManager.getDefaultSensor(
                    Sensor.TYPE_ROTATION_VECTOR);

            // initialize the rotation matrix to identity
            mRotationMatrix[ 0] = 1;
            mRotationMatrix[ 4] = 1;
            mRotationMatrix[ 8] = 1;
            mRotationMatrix[12] = 1;
        }
        
        public void start() {
            // enable our sensor when the activity is resumed, ask for
            // 10 ms updates.
            mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
        }

        public void stop() {
            // make sure to turn our sensor off when the activity is paused
            mSensorManager.unregisterListener(this);
        }
        
        
        public void onSensorChanged(SensorEvent event) {
            // we received a sensor event. it is a good practice to check
            // that we received the proper event
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                // convert the rotation-vector to a 4x4 matrix. the matrix
                // is interpreted by Open GL as the inverse of the
                // rotation-vector, which is what we want.
                SensorManager.getRotationMatrixFromVector(
                        mRotationMatrix , event.values);
                SensorManager.getOrientation(mRotationMatrix, mOrientation);
                angle =  (float) mOrientation[0] * -1.0f;
            }
        }

		
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
	}


}