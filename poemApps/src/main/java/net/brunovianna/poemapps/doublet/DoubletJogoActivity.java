package net.brunovianna.poemapps.doublet;

import net.brunovianna.poemapps.R;


import processing.core.*;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;





public class DoubletJogoActivity extends PApplet {

	Resources res;
	
	String primeira;
	ArrayList<String> dicionario = new ArrayList<String> ();
	ArrayList<String> possiveis = new ArrayList<String> ();
	ArrayList<String> palavras = new ArrayList<String> ();
	String abecedario = "abcdefghijklmnopqrstuvxyzáéóúãõç";
	PFont monoFont;
	float letterWidth, letterHeight, fontSize;
	int indexLinha = 0;
	int indexPalavra = 0;
	int indexPossivel = 0;
	int ultimaLetra = -1;
	int ultimaLinha = -1;
	int currentDoublet = 0;
	private CharSequence[] doublets_array;
	public boolean wonGame = true;
	public static final int JOGAR_NOVAMENTE = 1000;
	public static final int NOVO_JOGO = 1001;
	public static final int OPCAO_FIM_DE_JOGO = 1002;

	public static final String PREFS_NAME = "DoubletJogoPrefs";
	public String mensagem = "";

	Button buttonWin, buttonAgain, buttonNew;
	View processingView;

	class Doublet {;
	String comeco;
	String fim;
	int comprimento;
	}

	ArrayList<Doublet> doublets = new ArrayList<Doublet>();

	@SuppressLint("NewApi")
	public void setup() {

		res = getResources();
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		currentDoublet = settings.getInt("currentDoublet", 0);

		
		preparePoem();


	}


	public void draw() {


		int[] linha_e_letra = new int[2];
		background (204);
		orientation(PORTRAIT);

		fill(255,255,255);
		for (int i=0;i<indexLinha+1;i++) {
			text(palavras.get(i),0,(i+1)*letterHeight);

		}

		text(doublets.get(currentDoublet).fim, 0, (doublets.get(currentDoublet).comprimento) * letterHeight);

		if (mousePressed) {


			linha_e_letra = locateTouch();

			//se estou tocando uma linha acima da ultima, reordene
			if (linha_e_letra[0]<indexLinha) {
				indexLinha = linha_e_letra[0];
				for (int i=palavras.size()-1;i>linha_e_letra[0];i--)
					palavras.remove(i);
			}

			//highlight na letra
			fill(255,0,0);
			text(palavras.get(indexLinha).charAt(linha_e_letra[1]),linha_e_letra[1]*letterWidth, (indexLinha+1)*letterHeight);

			// se for a primeira vez que roda, ou se estiver tocando na ultima linha
			// procure palavras
			if ((ultimaLinha==-1)||(ultimaLinha!=linha_e_letra[0])||(ultimaLetra==-1)||(ultimaLetra!=linha_e_letra[1])) {
				possiveis = buscaPalavras(palavras.get(indexLinha), linha_e_letra[1]);
				indexPossivel = 0;
				ultimaLinha=linha_e_letra[0];
				ultimaLetra=linha_e_letra[1];

			}

			//se já estivermos no limite de palavras, cheque se ganhou o jogo
			if (indexLinha == doublets.get(currentDoublet).comprimento - 2 ) {
				
				// caso haja alguma palavra que sirva...
				if (possiveis.size() != 0) {
					
					Intent intent;
				
						// se uma das palavras é o fim do doublet, acertou
						if (possiveis.contains(doublets.get(currentDoublet).fim)) {

							intent = new Intent (this, DoubletEndGameActivity.class);
							intent.putExtra("mensagem", res.getText(  R.string.notificacao_doublet_ganhou));
							startActivityForResult(intent, OPCAO_FIM_DE_JOGO);

						} else {

							intent = new Intent (this, DoubletEndGameActivity.class);
							intent.putExtra("mensagem",res.getText( R.string.notificacao_doublet_perdeu));
							startActivityForResult(intent, OPCAO_FIM_DE_JOGO);
						}

				// se nao tem nenhuma palavra, perdeu tambem
				} else {
					Intent intent = new Intent (this, DoubletEndGameActivity.class);
					intent.putExtra("mensagem", res.getText(R.string.notificacao_doublet_perdeu));
					startActivityForResult(intent, OPCAO_FIM_DE_JOGO);

					
				}
				
			// se não tá na ultima linha, o jogo continua	
			} else {
						
				// caso haja alguma palavra que sirva...
				if (possiveis.size() != 0) {
					indexLinha++;

					palavras.add(possiveis.get(indexPossivel));
					if (indexPossivel+1==possiveis.size())
						indexPossivel=0;
					else
						indexPossivel++;
					fill(255,255,255);
					text(palavras.get(indexLinha),0, letterHeight*(indexLinha+1));
				}
			}

		}

	}


	public ArrayList<String> buscaPalavras(String palavra, int index) {

		ArrayList<String> resultado = new ArrayList<String> (); //palavras com uma letra diferente
		String comeco, fim;
		try {
			comeco = palavra.substring(0,index) ;
		} catch (Exception e) {
			comeco = null;
		}
		try {
			fim = palavra.substring(index+1) ;
		} catch (Exception e) {
			fim = null;
		}
		for (int j=0;j<abecedario.length();j++) {

			if (abecedario.charAt(j)==palavra.charAt(index))
				continue;

			String tentativa = "";

			if (comeco != null)
				tentativa = tentativa.concat(comeco);
			tentativa = tentativa.concat((abecedario.substring(j,j+1)));
			if (fim !=null)
				tentativa = tentativa.concat(fim);

			println(tentativa);

			if ((dicionario.indexOf(tentativa)!=-1) && (palavras.indexOf(tentativa)==-1))
				resultado.add(tentativa);

		}
		return resultado;

	}




	public ArrayList<ArrayList<String>> buscaTodasPalavras(String palavra) {

		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();


		for (int i=0;i<palavra.length();i++) {
			ArrayList<String> palavras = new ArrayList<String> (); //palavras com uma letra diferente
			String comeco, fim;
			try {
				comeco = palavra.substring(0,i-1) ;
			} catch (Exception e) {
				comeco = null;
			}
			try {
				fim = palavra.substring(i+1) ;
			} catch (Exception e) {
				fim = null;
			}
			for (int j=0;j<abecedario.length();j++) {

				String tentativa = "";

				if (comeco != null)
					tentativa = tentativa.concat(comeco);
				tentativa = tentativa.concat((abecedario.substring(j,j+1)));
				if (fim !=null)
					tentativa = tentativa.concat(fim);

				println(tentativa);

				if (dicionario.indexOf(tentativa)!=-1) 
					palavras.add(tentativa);

			}
			resultado.add(palavras);

		}



		return resultado;
	}

	public int[] locateTouch() {

		int[] resultado = new int[2];

		resultado [0] = (int) Math.abs(mouseY / letterHeight);
		resultado [1] = (int) Math.abs(mouseX / letterWidth);

		return resultado;


	}

	public String getMessage() {
		return mensagem;
	}


	public void restart() {
		indexLinha = 0;
		palavras = new ArrayList<String> ();
		palavras.add(doublets.get(currentDoublet).comeco);

	}
	
	public void preparePoem() {
		

		doublets_array = res.getTextArray(R.array.doublets_array);

		for (int i =0; i<doublets_array.length; i+=3) {
			Doublet d = new Doublet();
			d.comeco = doublets_array[i].toString();
			d.fim = doublets_array[i+2].toString();
			d.comprimento = Integer.parseInt(doublets_array[i+1].toString());
			doublets.add(d);
		}

		if (currentDoublet == doublets.size()) currentDoublet = 0;

		
		String words[] = loadStrings("br.txt");

		for (int i=0;i<words.length;i++)
			dicionario.add(words[i]);
		
		palavras.add(doublets.get(currentDoublet).comeco);

		fontSize = 0.9f*displayHeight/(doublets.get(currentDoublet).comprimento);

		monoFont = createFont("DroidSansMono.ttf", fontSize, true);

		textFont(monoFont);

		letterWidth = textWidth("teste") / 5;
		letterHeight = textAscent() + textDescent();


		
	}

	@Override
	protected void onStop(){
		super.onStop();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("currentDoublet", currentDoublet);

		// Commit the edits!
		editor.commit();
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == OPCAO_FIM_DE_JOGO) {
			if (resultCode == JOGAR_NOVAMENTE) {
				restart();
			} else if (resultCode == NOVO_JOGO) {
				palavras = new ArrayList<String> ();
				indexLinha = 0;
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				currentDoublet = settings.getInt("currentDoublet", 0);
				if (currentDoublet < doublets.size()-1)
					currentDoublet++;
				else 
					currentDoublet = 0;

				preparePoem();
			}
		}
	}

	public void onResume() {
		super.onResume();
		//        setContentView(R.layout.doublet_splash_layout);
		//        jogoButton.setVisibility(Button.INVISIBLE);
		//        livreButton.setVisibility(Button.INVISIBLE);
		//        //if (DoubletJogoActivity.class.)
		//        winButton.setVisibility(Button.VISIBLE);
		//        setup();

	}

}