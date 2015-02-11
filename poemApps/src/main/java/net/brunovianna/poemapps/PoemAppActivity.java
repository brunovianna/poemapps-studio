package net.brunovianna.poemapps;

import java.util.ArrayList;

import net.brunovianna.poemapps.pelacidade.*;
import net.brunovianna.poemapps.doublet.DoubletSplashActivity;
import net.brunovianna.poemapps.fatorial.Fatorial;
import net.brunovianna.poemapps.pelacidade.*;
import net.brunovianna.poemapps.rgb.*;
import net.brunovianna.poemapps.bussola.*;
import net.brunovianna.poemapps.sopro.*;
import net.brunovianna.poemapps.relogio.*;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class PoemAppActivity extends ListActivity {
    /** Called when the activity is first created. */
 
	public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      MyAdapter adapter = new MyAdapter(this, generateData());
      
      // if extending Activity 2. Get ListView from activity_main.xml
      //ListView listView = (ListView) findViewById(R.id.listview);

      // 3. setListAdapter
      //listView.setAdapter(adapter); if extending Activity
      setListAdapter(adapter);
      //setListAdapter(new ArrayAdapter<String>(this, R.layout.poemapp_layout, POEMAS));

      ListView lv = getListView();
      lv.setTextFilterEnabled(true);

      lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {
        	Intent intent;
        	switch(position) {

        	case(0): //ampulheta
    	    	intent = new Intent (PoemAppActivity.this, DoubletSplashActivity.class);
    	        startActivity(intent);
    	        break;
        	case(1): //cidade
    	    	intent = new Intent (PoemAppActivity.this, PelacidadeActivity.class);
    	        startActivity(intent);
    	        break;
        	case(2): //rgb
		    	intent = new Intent (PoemAppActivity.this, Rgb.class);
		        startActivity(intent);
		        break;
        	case(3): //oriente
		    	intent = new Intent (PoemAppActivity.this, Bussola.class);
		        startActivity(intent);
		        break;
        	case(4): //fatorial
		    	intent = new Intent (PoemAppActivity.this, Fatorial.class);
		        startActivity(intent);
		        break;
        	case(5): //12 tempos
		    	intent = new Intent (PoemAppActivity.this, Relogio.class);
		        startActivity(intent);
		        break;
        	case(6): //sopro
		    	intent = new Intent (PoemAppActivity.this, SoproSplash.class);
		        startActivity(intent);
		        break;
        	
        	}

          
        }
      });
    }
	

	private ArrayList<Model> generateData(){
	    ArrayList<Model> models = new ArrayList<Model>();
		models.add(new Model(R.drawable.icon_doublets,"Doublets"));
		models.add(new Model(R.drawable.icon_poema_pela_cidade,"Poema Pela Cidade"));
		models.add(new Model(R.drawable.icon_poema_rgb,"Poema RGB"));
		models.add(new Model(R.drawable.icon_oriente,"Oriente"));
		models.add(new Model(R.drawable.icon_poema_fatorial,"Poema Fatorial"));
		models.add(new Model(R.drawable.icon_em_12_tempos,"Poema em 12 tempos"));
		models.add(new Model(R.drawable.icon_sopro,"Sopro"));
	
	    return models;
	}

}