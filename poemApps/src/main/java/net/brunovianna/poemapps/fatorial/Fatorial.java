package net.brunovianna.poemapps.fatorial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class Fatorial extends PApplet {

	ArrayList<Bolavra> bolavraList;
	Bolavra centro, bolavra;
	SensorManager myManager;
	private List<Sensor> sensors;
	private Sensor accSensor;
	private long shakeTime, lastUpdate = -1;
	private float acc_x, acc_y, acc_z;
	private float acc_last_x, acc_last_y, acc_last_z;
	private static final int SHAKE_THRESHOLD = 400;
	private static final int SWAP_STEPS = 40;
	public boolean animating = false;


	
	public void setup() {
		int fontsize = (int)(width/33.75f);
		
		bolavraList = new ArrayList<Bolavra>();
		
		
		
		centro = new Bolavra (0,0,0,"de",fontsize, this);
		centro.setCentro(true);
		
		bolavra = new Bolavra (121,139,0,"nuca",fontsize, this);
		bolavra.setPosition(0);
		bolavraList.add(bolavra);
		
		bolavra = new Bolavra (0,64,107,"sorte",fontsize, this);
		bolavra.setPosition(1);
		bolavraList.add(bolavra);
		
		bolavra = new Bolavra (246,128,133,"jogo",fontsize, this);
		bolavra.setPosition(2);
		bolavraList.add(bolavra);
		
		bolavra = new Bolavra (208,15,0,"si",fontsize, this);
		bolavra.setPosition(3);
		bolavraList.add(bolavra);
		
		bolavra = new Bolavra (247,189,0,"arrepio",fontsize, this);
		bolavra.setPosition(4);
		bolavraList.add(bolavra);
		
		bolavra = new Bolavra (7,101,65,"beijo",fontsize,this);
		bolavra.setPosition(5);
		bolavraList.add(bolavra);
	
		
		// Set Sensor + Manager
		myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensors = myManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(sensors.size() > 0)
		{
			accSensor = sensors.get(0);
		}
		
		myManager.registerListener(mySensorListener, accSensor, SensorManager.SENSOR_DELAY_GAME); 	
		
		desenha_bolas();
	}

	public void draw() {
		background(202);
		
		desenha_bolas();
		
	}
	
	class Bolavra  {
		
		String m_palavra;
		boolean m_centro, m_trocando, m_quicando;
		int m_cor, m_pos, m_nova_pos, m_animation_counter;
		double m_angle, m_novo_angle;
		float m_x, m_y, m_novo_x, m_novo_y, m_velho_x, m_velho_y;
		float m_diam, m_fontsize;
		PGraphics m_graphics;
		Fatorial m_fat;
		
		Bolavra (int r, int g, int b, String palavra, float fontsize, Fatorial fat) {
		
			m_fat = fat;
			m_fontsize = fontsize;
			m_palavra = palavra;
			m_diam = width / 6.0f;
			m_graphics = createGraphics((int)m_diam,(int)m_diam,JAVA2D);
			m_graphics.beginDraw();
			//m_graphics.background(255);
			m_graphics.imageMode(CENTER);
			m_graphics.noStroke();
			m_graphics.fill(r,g,b);
			m_graphics.ellipseMode(CENTER);
			m_graphics.ellipse(m_diam/2,m_diam/2,m_diam,m_diam);
			m_graphics.fill(220);
			m_graphics.textSize(fontsize);
			m_graphics.textAlign(CENTER,CENTER);
			m_graphics.text(palavra, m_diam/2,m_diam/2);
			m_graphics.endDraw();
		}
		
		public void setCentro (boolean centro) {
			m_centro = centro;
		}
		
		public void animatePosition (int pos) {
			if (!m_trocando)
				if (!m_fat.animating) {
					m_trocando = true;
					m_nova_pos = pos;
					m_novo_angle = Math.PI / 6.0f + pos *  Math.PI / 3.0f; 
					float raio = (width-m_diam)/2.1f;
					m_novo_x = width/2 + (float) (raio * Math.sin(m_novo_angle)); 
					m_novo_y = height/2 + (float) (raio * Math.cos(m_novo_angle));
					m_velho_x = m_x;
					m_velho_y = m_y;
				}
			
		}
		
		public void setPosition (int pos) {
			m_pos = pos;
			m_angle = Math.PI / 6.0f + pos *  Math.PI / 3.0f; 
			float raio = (width-m_diam)/2.1f;
			m_x = width/2 + (float) (raio * Math.sin(m_angle)); 
			m_y = height/2 + (float) (raio * Math.cos(m_angle));

		
		}
		
		public int getPosition() {
			return m_pos;
		}
		
		
		
		public void draw() {
			
			if (m_trocando) {
				float delta_x = (m_novo_x - m_velho_x) /  SWAP_STEPS;
				float delta_y = (m_novo_y - m_velho_y) /  SWAP_STEPS;
				m_x += delta_x; 
				m_y += delta_y; 
				
				if ((abs(m_novo_x-m_x) <  abs(delta_x * 2 ))||(abs(m_novo_y-m_y) <  abs(delta_y * 2))){
					m_trocando = false;
					m_fat.animating = false;
					m_pos = m_nova_pos;
					m_angle = Math.PI / 6.0f + m_nova_pos *  Math.PI / 3.0f; 
					float raio = (width-m_diam)/2.1f;
					m_x = width/2 + (float) (raio * Math.sin(m_angle)); 
					m_y = height/2 + (float) (raio * Math.cos(m_angle));
					
				}
					
				
			} 

			if (m_centro) {
				
				imageMode(CENTER);
				image(m_graphics, width/2, height/2); 
				
			} else {
				
				imageMode(CENTER);
				image(m_graphics, m_x, m_y); 
				
			}
			
			
			
		}
		
		
	}
	
	private final SensorEventListener mySensorListener = new SensorEventListener()
	{

		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			//sRandom random = new Random();
			long curTime = System.currentTimeMillis();
			// only allow one update every 100ms.
			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				acc_x = event.values[SensorManager.DATA_X];
				acc_y = event.values[SensorManager.DATA_Y];
				acc_z = event.values[SensorManager.DATA_Z];

				float speed = Math.abs(acc_x+acc_y+acc_z - acc_last_x - acc_last_y - acc_last_z) / diffTime * 10000;
				if ((speed > SHAKE_THRESHOLD)&&(curTime-shakeTime > 1000)) {
					//if (!animating) {
						// yes, this is a shake action! Do something about it!
						shakeTime = curTime;
	
						
						int nova_pos = (int)(random(6));
						int velha_pos = (int)(random(6));
						
						while (nova_pos==velha_pos) 
							velha_pos = (int)(random(6));
							
						int i=0;
						while (bolavraList.get(i).getPosition() != nova_pos)
							i++;
						
						int j=0;
						while (bolavraList.get(j).getPosition() != velha_pos)
							j++;
						
						bolavraList.get(i).animatePosition(velha_pos);
						bolavraList.get(j).animatePosition(nova_pos);
						
						//desenha_bolas();
						//}
						
					}
					
				}
				acc_last_x = acc_x;
				acc_last_y = acc_y;
				acc_last_z = acc_z;
			}
		
	};


	public void desenha_bolas() {
		pushMatrix();
		float largura = width*0.9f;
		//float comeco = width*0.05f;
		float altura = largura * 0.03f;
		rectMode(CENTER);
		translate (width/2,height/2);
		rect (0,0,largura,altura);
		rotate(PI/3.0f);
		rect (0,0,largura,altura);
		rotate(PI/3.0f);
		rect (0,0,largura,altura);
		rotate(PI/3.0f);
		popMatrix();
		
		centro.draw();
		
		
		
		for (int i=0;i<bolavraList.size();i++) {
			bolavraList.get(i).draw();
			
		}

	}
	
}


