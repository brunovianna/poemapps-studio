package net.brunovianna.poemapps.sopro;


import net.brunovianna.poemapps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoproSplash extends Activity {
	
	Button soproButton;
	
	
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sopro);
        
        
        soproButton = (Button) findViewById(R.id.sopro_button);
        //jogoButton.setVisibility(Button.VISIBLE);
        soproButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent;
   		    	intent = new Intent (SoproSplash.this, Sopro.class);
		        startActivity(intent);

            }
        });
    
        
    
    }

    public void onResume() {
        super.onResume();
    }
}
