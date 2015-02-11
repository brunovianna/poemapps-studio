package net.brunovianna.poemapps.relogio;

import processing.core.*;


public class Relogio extends PApplet {

	PImage frases[] = new PImage[12];
	PImage ponteiro_horas, ponteiro_minutos, ponteiro_segundos;
	private static final int zero_x = 25; 
	private static final int zero_y = 38; 
	private float proporcao;
	
	public void setup() {
		
		
		for (int i=1; i<13;i++) {
			frases[i-1] = loadImage("relogio-"+i+".png");
		}
		
		
		//frases[0] = loadImage("relogio-3.png");
		
		ponteiro_horas = loadImage("nao.png");
		ponteiro_minutos = loadImage("sim.png");
		ponteiro_segundos = loadImage("talvez.png");
		
		proporcao = (width*0.45f)/ponteiro_segundos.width;
		 
		
	}
	
	public void draw() {

		background(200);
		
		translate (width/2 , height/2 ); 
		scale(proporcao);
		
		
		int hora;
		if (hour() > 11)
			hora = (hour() - 12);
		else
			hora = hour() ;
		
		
		
		int hora_anterior;
		if (hora == 0)
			hora_anterior = 11;
		else 
			hora_anterior = hora - 1;
			

		//rotate (hora * PI / 6 + PI);
		//image(frases[10],-frases[10].width/2,-frases[hora].height/2);
		
		
		
		PGraphics buf;
		buf = createGraphics(frases[11].width,frases[11].height,JAVA2D);
		buf.beginDraw(); 
		//buf.rotate(PI);
		buf.imageMode(CENTER);
		buf.image(frases[11],frases[11].width/2,frases[11].height/2);
		buf.fill (200);
		buf.noStroke();
		buf.arc(frases[11].width/2,frases[11].height/2, frases[11].width, frases[11].width,(minute()-1) * PI / 30 - HALF_PI,  TWO_PI-HALF_PI,PIE);  
		buf.endDraw();
		
		image(buf,-frases[11].width/2,-frases[11].height/2);
		
		rotate((minute()-1) * PI / 30 );
		image(frases[10],-frases[10].width/2 , -frases[10].height/2);		
		rotate((-minute()+1) * PI / 30 );
		
		
		rotate ((second() -1)*  PI / 30 - HALF_PI);
		image (ponteiro_segundos, - zero_x,- zero_y);
		
		
		rotate ( (-second() +1) *  PI / 30 + HALF_PI );
		
		rotate ((minute()-1) * PI / 30 - HALF_PI);
		image (ponteiro_minutos, - zero_x,- zero_y);
		
		rotate ((-minute()+1) * PI / 30 - HALF_PI);
	
		rotate (hora * PI / 6 +HALF_PI);
		image (ponteiro_horas, - zero_x,- zero_y);
		rotate (-hora * PI / 6 -HALF_PI);

		
	}
	
}
