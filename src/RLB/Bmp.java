package RLB;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import RandomFileCpp.RandomAccessFileCpp;
import WindowsApp.WindowsFrame;

public class Bmp {
	
	public static void main(String args[]){
		int ii = Integer.parseInt("aabbcc",16);
		System.out.println(ii);
		System.out.println(ii & 0x0000ff);
		System.out.println((ii & 0x00ff00) >> 8);
		System.out.println((ii & 0xff0000) >> 16);
	}
	
	/**
	 * 导入PNG为PICTURE
	 */
	public static Picture PngToPicture(File f) throws IOException{
		Picture desPic = null;
		BufferedImage Img=ImageIO.read(f);
		int ImgW = Img.getWidth();
		int ImgH = Img.getHeight();
		desPic = new Picture(ImgW,ImgH,f.getName());
		for (int i = 0;i < ImgW;i ++){
			for (int j = 0;j < ImgH;j ++){
				int srgb = Img.getRGB(i, j);
				int a = (srgb >> 24) & 0xff;
		        int r   = (srgb >> 16) & 0xff;
		        int g = (srgb >>  8) & 0xff;
		        int b  = (srgb) & 0xff;
				desPic.SetPixel(i, j, new Color(r,g,b,a));
			}
		}
		return desPic;
	}
	
	/**
	 * 导入BMP为Picture
	 * @throws IOException 
	 */
	public static Picture BmpToPicture(File f) throws IOException{
		Picture desPic = null;
		RandomAccessFileCpp rf = new RandomAccessFileCpp(f.getPath(),"rw");
		//定位读取文件宽高
		long PicPos;
		long PicW;
		long PicH;
		//颜色位数,24表示24位图，32表示32位图
		int BitCount;
		PicPos = -4;
		rf.seek(PicPos + 22);
		PicW = rf.readAntiInt();
		rf.seek(PicPos + 26);
		PicH = rf.readAntiInt();
		//读取颜色位数
		rf.seek(PicPos + 32);
		BitCount = rf.readUnsignedByte();
		//图片初始化
		desPic = new Picture((int)PicW,(int)PicH,f.getName());
		
		//读取像素数据
		//定位到图片数据
		long PicDataPos;
		rf.seek(PicPos + 14);
		PicDataPos = rf.readAntiInt();
		long pos = PicPos + PicDataPos + 4;
		//调色板位置
		long ColorPos = PicPos + 4 +0x36;
		Color[] ColorDatas = null;
		//获取调色板
		if (BitCount == 4){
			ColorDatas = new Color[16];
			rf.seek(ColorPos);
			for (int ci = 0;ci < 16;ci ++){
				int b,g,r;
				b = rf.readUnsignedByte();
				g = rf.readUnsignedByte();
				r = rf.readUnsignedByte();
				rf.readUnsignedByte();
				ColorDatas[ci] = new Color(r,g,b);
			}
		}
		rf.seek(pos);
		//Color 队列
		Queue<Color> Cqueue = new LinkedList<Color>();
		int DataSizePerLine;
		DataSizePerLine = (desPic.w * BitCount+31)/8;
		DataSizePerLine= DataSizePerLine/4*4;
		long previousPos;
		for (int iy = desPic.h - 1;iy >= 0;iy --){
			previousPos = rf.getFilePointer();
			for (int ix = 0;ix < desPic.w;ix ++){
				int Bit;
				if (BitCount == 1){
					int b,g,r;
					if (Cqueue.isEmpty()){
						Bit = rf.readUnsignedByte();
						for (int bi = 0;bi < 8;bi ++){
							if (((Bit & 0x80) >> 7) == 0){
								Cqueue.offer(new Color(0,0,0));
							}
							else{
								Cqueue.offer(new Color(255,255,255));
							}
							Bit = Bit << 1;
						}
					}
					desPic.p[ix][iy] = Cqueue.poll();
				}
				else if (BitCount == 4){
					int b,g,r;
					if (Cqueue.isEmpty()){
						Bit = rf.readUnsignedByte();
						Cqueue.offer(ColorDatas[Bit / 16]);
						Cqueue.offer(ColorDatas[Bit % 16]);
					}
					desPic.p[ix][iy] = Cqueue.poll();
				}
				else if (BitCount == 24){
					int b,g,r;
					b = rf.readUnsignedByte();
					g = rf.readUnsignedByte();
					r = rf.readUnsignedByte();
					desPic.p[ix][iy] = new Color(r,g,b,255);
				}
				else if (BitCount == 32){
					int b,g,r;
					b = rf.readUnsignedByte();
					g = rf.readUnsignedByte();
					r = rf.readUnsignedByte();
					//抛弃alpha
					rf.readUnsignedByte();
					desPic.p[ix][iy] = new Color(r,g,b,255);
				}
			}
			Cqueue.clear();
			rf.seek(previousPos + DataSizePerLine); 
		}
		return desPic;
	}
	
	/**
	 * 获取2色色差
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int GetColorV(Color c1,Color c2){
		return (int)Math.sqrt(Math.pow(c1.getRed() - c2.getRed(),2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));
	}
	
	public static Picture ClearPup(Picture g,int cs){
		Picture desPic = new Picture(g.GetWidth(),g.GetHeight());
		for (int ix = 0;ix < g.GetWidth();ix ++){
			for (int iy = 0;iy < g.GetHeight();iy ++){
				if (GetColorV(g.GetPixel(ix, iy),WindowsFrame.SpecialColor) < cs){
					desPic.SetPixel(ix, iy, WindowsFrame.SpecialColor);
				}
				else{
					desPic.SetPixel(ix, iy, g.GetPixel(ix, iy));
				}
			}
		}
		return desPic;
	}
	
	public static void BmpSave(Picture srcG,File fp) throws IOException{
		RandomAccessFileCpp rf = new RandomAccessFileCpp(fp.getPath(),"rw");
		char[] BmpHead = {
				0x42,0x4D,0xC6,0x1B,0x00,0x00,0x00,0x00,0x00,0x00,0x36,0x00,0x00,0x00,0x28,0x00,
				0x00,0x00,0xFF,0x00,0x00,0x00,0xFF,0x00,0x00,0x00,0x01,0x00,0x18,0x00,0x00,0x00,
				0x00,0x00,0x90,0x1B,0x00,0x00,0xC4,0x0E,0x00,0x00,0xC4,0x0E,0x00,0x00,0x00,0x00,
				0x00,0x00,0x00,0x00,0x00,0x00
				};
		String TempStr = String.valueOf(BmpHead);
		rf.writeBytes(TempStr);
		//写入图片的宽和高
		rf.seek(18);
		rf.write(srcG.w % 256);
		rf.write(srcG.w / 256);
		rf.seek(22);
		rf.write(srcG.h % 256);
		rf.write(srcG.h / 256);
		//写入像素数据
		rf.seek(54);
		for (int iy = srcG.h - 1;iy >= 0;iy --){
			for (int ix = 0;ix < srcG.w;ix ++){
				int b,g,r;
				b = srcG.p[ix][iy].getBlue();
				g = srcG.p[ix][iy].getGreen();
				r = srcG.p[ix][iy].getRed();
				rf.writeByte(b);
				rf.writeByte(g);
				rf.writeByte(r);
				//不能被4整除并且读到行末时要添加0
				if (srcG.w * 3 % 4 != 0 && ix == srcG.w - 1){
					for (int t = 0;t < 4 - srcG.w * 3 % 4;t ++){
						rf.writeByte(0);
					}
				}
			}
		}
		//写入文件大小
		long fileLength = rf.getFilePointer() + 1;
		rf.seek(0x2);
		rf.writeAntiInt(fileLength);
		//写入数据大小
		rf.write(0x22);
		rf.writeAntiInt(fileLength - 0x36);
		rf.close();
	}
	
	public static Picture Rotation90(Picture g){
		Picture desPic = new Picture(g.GetHeight(),g.GetWidth());
		for (int ix = 0;ix < g.GetWidth();ix ++){
			for (int iy = 0;iy < g.GetHeight();iy ++){
				desPic.SetPixel(g.GetHeight() - 1 - iy, ix, g.GetPixel(ix, iy));
			}
		}
		return desPic;
	}
	
	public static Picture RotationLR(Picture g){
		Picture desPic = new Picture(g.GetWidth(),g.GetHeight());
		for (int ix = 0;ix < g.GetWidth();ix ++){
			for (int iy = 0;iy < g.GetHeight();iy ++){
				desPic.SetPixel(g.GetWidth() - 1 - ix, iy, g.GetPixel(ix, iy));
			}
		}
		return desPic;
	}
	
	public static Picture RotationUD(Picture g){
		Picture desPic = new Picture(g.GetWidth(),g.GetHeight());
		for (int ix = 0;ix < g.GetWidth();ix ++){
			for (int iy = 0;iy < g.GetHeight();iy ++){
				desPic.SetPixel(ix, g.GetHeight() - 1 - iy, g.GetPixel(ix, iy));
			}
		}
		return desPic;
	}
	
	public static void PngSave(Picture srcG,File f) throws IOException{
		BufferedImage Img = new BufferedImage(srcG.GetWidth(), srcG.GetHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int ix = 0;ix < srcG.GetWidth();ix ++){
			for (int iy = 0;iy < srcG.GetHeight();iy ++){
				int r,g,b,a;
				r = srcG.GetPixel(ix, iy).getRed();
				g = srcG.GetPixel(ix, iy).getGreen();
				b = srcG.GetPixel(ix, iy).getBlue();
				if (r == 255 && g == 0 && b == 255){
					a = 0;
				}
				else{
					a = 255;
				}
				Img.setRGB(ix, iy, new Color(r,g,b,a).getRGB());
			}
			ImageIO.write(Img, "png", f);
		}
	}
	
}
