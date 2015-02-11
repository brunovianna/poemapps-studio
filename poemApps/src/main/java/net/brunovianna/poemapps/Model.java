package net.brunovianna.poemapps;


public class Model{
 
    private int icon;
    private String title;
 
 
    public Model(String title) {
        this(-1,title);
    }
    public Model(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }
 
    public int getIcon() {
    	return this.icon;
    }
    
    public String getTitle() {
    	return this.title;
    }
    
}