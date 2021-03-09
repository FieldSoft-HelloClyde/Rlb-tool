package WindowsApp;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;

import RLB.Picture;

public class MyCanvas extends Panel {
	/**
	 * 显示模式，0表示原图，1表示过滤紫色为透明
	 */
	public int ShowMode = 0;
	Picture res = new Picture(0,0);
	public void paint(Graphics g){
		super.paint(g);
		g.create(0, 0, res.GetWidth(), res.GetHeight());
		for (int ix = 0;ix < res.GetWidth();ix ++){
			for (int iy = 0;iy < res.GetHeight();iy ++){
				if (ShowMode == 0){
					g.setColor(res.GetPixel(ix, iy));
					g.drawLine(ix, iy, ix, iy);
				}
				else if (ShowMode == 1){
					Color TempColor;
					TempColor = res.GetPixel(ix, iy);
					if (!(TempColor.getBlue() == WindowsFrame.RLB_SpecialColor.getBlue() &&
							TempColor.getGreen() == WindowsFrame.RLB_SpecialColor.getGreen() &&
							TempColor.getRed() == WindowsFrame.RLB_SpecialColor.getRed())){
						g.setColor(res.GetPixel(ix, iy));
						g.drawLine(ix, iy, ix, iy);
					}
				}
			}
		}
	}
	
	public void ToRlb(Picture t){
		this.setLayout(null);
		this.setSize(t.GetWidth(), t.GetHeight());
		res = t;
	}
	
}
