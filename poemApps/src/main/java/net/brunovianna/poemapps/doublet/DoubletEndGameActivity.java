package net.brunovianna.poemapps.doublet;

import net.brunovianna.poemapps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoubletEndGameActivity extends Activity {
	
	Button novoButton, novamenteButton;
	TextView notificacao;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.doublet_endgame_layout);
        
        Bundle extras = getIntent().getExtras();
        

        notificacao = (TextView) findViewById(R.id.textView_doublet_notificacao);
        Log.i("endgame", "mid notifi");
        notificacao.setText((CharSequence) extras.get("mensagem"));


        
        novoButton = (Button) findViewById(R.id.button_doublet_jogo);
        //jogoButton.setVisibility(Button.VISIBLE);
        novoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	DoubletEndGameActivity.this.setResult(DoubletJogoActivity.NOVO_JOGO);
            	//DoubletEndGameActivity.this.finishActivity(DoubletJogoActivity.OPCAO_FIM_DE_JOGO);
            	DoubletEndGameActivity.this.finish();
            }
        });
    
        novamenteButton = (Button) findViewById(R.id.button_doublet_refresh);
        //jogoButton.setVisibility(Button.VISIBLE);
        novamenteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	DoubletEndGameActivity.this.setResult(DoubletJogoActivity.JOGAR_NOVAMENTE);
            	//DoubletEndGameActivity.this.finishActivity(DoubletJogoActivity.OPCAO_FIM_DE_JOGO);
            	DoubletEndGameActivity.this.finish();
            
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
