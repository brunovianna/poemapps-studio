package net.brunovianna.poemapps.doublet;

import net.brunovianna.poemapps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DoubletSplashActivity extends Activity {
	
	Button livreButton, jogoButton, winButton;
	
	
	public boolean wonGame;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doublet_splash_layout);
        
        
        livreButton = (Button) findViewById(R.id.button_doublet_livre);
        //jogoButton.setVisibility(Button.VISIBLE);
        livreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent;
   		    	intent = new Intent (DoubletSplashActivity.this, DoubletLivreActivity.class);
		        startActivity(intent);

            }
        });
    
        
        jogoButton = (Button) findViewById(R.id.button_doublet_jogo);
        //jogoButton.setVisibility(Button.VISIBLE);
        jogoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent;
   		    	intent = new Intent (DoubletSplashActivity.this, DoubletJogoActivity.class);
		        startActivity(intent);

            }
        });

        winButton = (Button) findViewById(R.id.button_doublet_win);
		winButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				winButton.setVisibility(View.INVISIBLE);
				Intent intent;
   		    	intent = new Intent (DoubletSplashActivity.this, DoubletJogoActivity.class);
		        startActivity(intent);

			}
		});

    
    }

    public void onResume() {
        super.onResume();
//        setContentView(R.layout.doublet_splash_layout);
//        jogoButton.setVisibility(Button.INVISIBLE);
//        livreButton.setVisibility(Button.INVISIBLE);
//        //if (DoubletJogoActivity.class.)
//        winButton.setVisibility(Button.VISIBLE);
    }
}
