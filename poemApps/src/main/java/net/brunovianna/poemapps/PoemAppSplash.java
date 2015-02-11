package net.brunovianna.poemapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PoemAppSplash extends Activity {
    /** Called when the activity is first created. */
 
	public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);

      
      setContentView(R.layout.splash_img_view);
      
//METHOD 1    
       
       /****** Create Thread that will sleep for 5 seconds *************/        
      Thread background = new Thread() {
          public void run() {
               
              try {
                  // Thread will sleep for 5 seconds
                  sleep(5*1000);
                   
                  // After 5 seconds redirect to another intent
                  Intent i=new Intent(getBaseContext(),PoemAppActivity.class);
                  startActivity(i);
                   
                  //Remove activity
                  //finish();
                   
              } catch (Exception e) {
               
              }
          }
      };
       
      // start thread
      background.start();
      
	}
}