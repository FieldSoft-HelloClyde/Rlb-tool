package RLB;

import java.awt.Color;

import WindowsApp.WindowsFrame;

public class Picture {
	String name = null;
	int w,h;
	Color[][] p = null;
	
	public Picture(int ww,int hh){
		this(ww,hh,"");
	}
	
	public Picture(int ww,int hh,String nn){
		w = ww;
		h = hh;
		name = nn;
		p = new Color[w][h];
		for (int i = 0;i < w;i ++){
			for (int j = 0;j < h;j ++){
				p[i][j] = WindowsFrame.SpecialColor;//r g b
			}
		}
	}
	
	public void SetPixel(int x,int y,Color c){
		if (c.getAlpha() == 255){
			p[x][y] = c;
		}
		else{
			p[x][y] = WindowsFrame.SpecialColor;
		}
	}
	
	public Color GetPixel(int x,int y){
		return p[x][y];
	}
	
	public String GetName(){
		return name;
	}
	
	public void SetName(String nn){
		name = nn;
	}
	
	public int GetWidth(){
		return w;
	}
	
	public int GetHeight(){
		return h;
	}
	
}
