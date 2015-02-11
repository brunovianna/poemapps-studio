package net.brunovianna.poemapps.doublet;

import processing.core.*;
import java.util.ArrayList;



public class DoubletLivreActivity extends PApplet {

	String primeira;
	ArrayList<String> dicionario = new ArrayList<String> ();
	ArrayList<String> possiveis = new ArrayList<String> ();
	ArrayList<String> palavras = new ArrayList<String> ();
	String abecedario = "abcdefghijklmnopqrstuvxyzáéóúãõç";
	PFont monoFont;
	float letterWidth, letterHeight;
	int maxLinhas;
	int indexLinha = 0;
	int indexPalavra = 0;
	int indexPossivel = 0;
	int ultimaLetra = -1;
	int ultimaLinha = -1;
	

	public void setup() {

		String words[] = loadStrings("br.txt");

		for (int i=0;i<words.length;i++)
			dicionario.add(words[i]);

		palavras.add("mente");



		monoFont = createFont("DroidSansMono.ttf", displayWidth/5, true);

		textFont(monoFont);

		letterWidth = textWidth("teste") / 5;
		letterHeight = textAscent() + textDescent();
		
		maxLinhas = (int) Math.floor(displayHeight / letterHeight);

	}


	public void draw() {


		int[] linha_e_letra = new int[2];
		background (204);
		orientation(PORTRAIT);

		fill(255,255,255);
		for (int i=0;i<indexLinha+1;i++) {
			text(palavras.get(i),0,(i+1)*letterHeight);

		}

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

			// caso haja alguma palavra que sirva, escrevemos mais uma linha
			if (possiveis.size() != 0) {
				
				
				// caso estejamos no limite de linhas da tela, subimos as palavras
				if (indexLinha == maxLinhas - 1) {
					for (int i = 0; i < palavras.size() - 1; i++) {
						palavras.set(i, palavras.get(i+1));	
					}
					palavras.set(palavras.size()-1, possiveis.get(indexPossivel));	
					
				} else {
					indexLinha++;
					palavras.add(possiveis.get(indexPossivel));
					
				}
				if (indexPossivel+1==possiveis.size())
					indexPossivel=0;
				else
					indexPossivel++;
				fill(255,255,255);
				text(palavras.get(indexLinha),0, letterHeight*(indexLinha+1));

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

}