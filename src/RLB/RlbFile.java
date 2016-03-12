package RLB;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import RLB.Picture;
import RandomFileCpp.RandomAccessFileCpp;
//import WindowsApp.WindowsFrame;

public class RlbFile {
	long PicNum;
	Picture[] Pic = null;
	boolean IsMod = false;
	File srcFile = null;
	
	/**
	 * 新建rlb的构造方法
	 */
	public RlbFile(){
		PicNum = 0;
		Pic = null;
		IsMod = false;
		srcFile = null;
	}
	
	/**
	 * 载入lib文件的构造方法
	 * @param f
	 * @param IsLib IsLib没有用处，只是用于与载入Rlb的构造方法区别
	 * @throws IOException 
	 */
	public RlbFile(File f,int IsLib) throws IOException{
		RandomAccessFileCpp rf = new RandomAccessFileCpp(f.getPath(),"rw");
		rf.seek(0);
		PicNum = rf.readAntiInt();
		Pic = new Picture[(int) PicNum];
		
		for (int i = 0;i < PicNum;i ++){
			//设置名字
			String tempName;
			tempName = String.valueOf(i + 1);
			//定位读取文件宽高
			long PicPos;
			long PicW;
			long PicH;
			rf.seek(4 + i * 4);
			//定位
			PicPos = rf.readAntiInt();
			rf.seek(PicPos + 4);
			PicW = rf.readAnti2Bit();
			rf.seek(PicPos + 6);
			PicH = rf.readAnti2Bit();
			//图片初始化
			Pic[i] = new Picture((int)PicW,(int)PicH,tempName);
			//读取像素数据
			//定位到图片数据
			long pos = PicPos + 16;
			rf.seek(pos);
			for (int iy = 0;iy < Pic[i].h;iy ++){
				for (int ix = 0;ix < Pic[i].w;ix ++){
					int b0,b1,bc;
					int b,g,r;
					//bgr565
					b0 = rf.readUnsignedByte();
					b1 = rf.readUnsignedByte();
					bc = b1 * 256 + b0;
					r = (bc & 0xf800) >> 11;
					if (r == 31){
						r = 255;
					}
					else{
						r *= 8;
					}
					g = (bc & 0x07e0) >> 5;
					if (g == 63){
						g = 255;
					}
					else{
						g *= 4;
					}
					b = (bc & 0x001f);
					if (b == 31){
						b = 255;
					}
					else{
						b *= 8;
					}
					Pic[i].p[ix][iy] = new Color(r,g,b,255);
				}
			}
		}
		srcFile = null;
		rf.close();
		//Lib文件载入需要保存res
		IsMod = true;
	}
	
	/**
	 * 载入rlb文件的构造方法
	 * @param f
	 * @throws IOException
	 */
	public RlbFile(File f) throws IOException{
		RandomAccessFileCpp rf = new RandomAccessFileCpp(f.getPath(),"rw");
		rf.seek(0);
		PicNum = rf.readAntiInt();
		Pic = new Picture[(int) PicNum];
		//读取图片名字
		
		for (int i = 0;i < PicNum;i ++){
			//定位读取图片名字
			String tempName;
			byte[] tempString = new byte[32];
			rf.seek(4 + 4 * PicNum + i * 32);
			rf.read(tempString);
			int tempLength = 0;
			for (tempLength = 0;tempLength < tempString.length && tempString[tempLength] != 0;tempLength ++);
			tempName = new String(tempString,0,tempLength);
			//定位读取文件宽高
			long PicPos;
			long PicW;
			long PicH;
			//颜色位数,24表示24位图，32表示32位图
			int BitCount;
			rf.seek(4 + i * 4);
			PicPos = rf.readAntiInt();
			rf.seek(PicPos + 22);
			PicW = rf.readAntiInt();
			rf.seek(PicPos + 26);
			PicH = rf.readAntiInt();
			//读取颜色位数
			rf.seek(PicPos + 32);
			BitCount = rf.readUnsignedByte();
			//图片初始化
			Pic[i] = new Picture((int)PicW,(int)PicH,tempName);
			
			
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
			DataSizePerLine = (Pic[i].w * BitCount+31)/8;
			DataSizePerLine= DataSizePerLine/4*4;
			long previousPos;
			for (int iy = Pic[i].h - 1;iy >= 0;iy --){
				previousPos = rf.getFilePointer();
				for (int ix = 0;ix < Pic[i].w;ix ++){
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
						Pic[i].p[ix][iy] = Cqueue.poll();
					}
					else if (BitCount == 4){
						int b,g,r;
						if (Cqueue.isEmpty()){
							Bit = rf.readUnsignedByte();
							Cqueue.offer(ColorDatas[Bit / 16]);
							Cqueue.offer(ColorDatas[Bit % 16]);
						}
						Pic[i].p[ix][iy] = Cqueue.poll();
					}
					else if (BitCount == 24){
						int b,g,r;
						b = rf.readUnsignedByte();
						g = rf.readUnsignedByte();
						r = rf.readUnsignedByte();
						Pic[i].p[ix][iy] = new Color(r,g,b,255);
					}
					else if (BitCount == 32){
						int b,g,r;
						b = rf.readUnsignedByte();
						g = rf.readUnsignedByte();
						r = rf.readUnsignedByte();
						//抛弃alpha
						rf.readUnsignedByte();
						Pic[i].p[ix][iy] = new Color(r,g,b,255);
					}
				}
				Cqueue.clear();
				rf.seek(previousPos + DataSizePerLine); 
			}
		}
		srcFile = f;
		rf.close();
	}
	
	public void SaveLib(File f) throws IOException{
		RandomAccessFileCpp rf = new RandomAccessFileCpp(f.getPath(),"rw");
		rf.seek(0);
		rf.writeAntiInt(PicNum);
		//定位至写入数据处
		rf.seek(4 + 4 * PicNum);
		long PicStartAdd;
		
		for (int i = 0;i < PicNum;i ++){
			PicStartAdd = rf.getFilePointer();
			//定位至偏移
			rf.seek(4 + i * 4);
			rf.writeAntiInt(PicStartAdd);
			rf.seek(PicStartAdd);
			
			//写入单张图片长度
			rf.writeAntiInt(Pic[i].GetWidth() * Pic[i].GetHeight() * 2 + 12);
			//写文件宽高
			rf.writeByte(Pic[i].GetWidth() % 256);
			rf.writeByte(Pic[i].GetWidth() / 256);
			rf.writeByte(Pic[i].GetHeight() % 256);
			rf.writeByte(Pic[i].GetHeight() / 256);
			//写其他信息
			rf.writeInt(256);
			rf.writeInt(0);
			//开始写入图片数据
			for (int iy = 0;iy < Pic[i].h;iy ++){
				for (int ix = 0;ix < Pic[i].w;ix ++){
					int r,g,b;
					int bc;
					r = Pic[i].GetPixel(ix, iy).getRed();
					g = Pic[i].GetPixel(ix, iy).getGreen();
					b = Pic[i].GetPixel(ix, iy).getBlue();
					bc = (r >> 3 << 11) | (g >> 2 << 5) | (b >> 3);
					rf.writeByte(bc % 256);
					rf.writeByte(bc / 256);
				}
			}
		}
		rf.close();
		
	}
	
	public void SaveRlb(File f) throws IOException{
		final int PicAddLength = 4;
		final int PicNameLength = 32;
		RandomAccessFileCpp rf = new RandomAccessFileCpp(f.getPath(),"rw");
		rf.seek(0);
		//写入图片张数
		rf.writeAntiInt(PicNum);
		
		//定位写入图片数据位置
		long PicPosAdd;
		long PicBmpAdd;
		long PicNextAdd;
		PicPosAdd = 4 + (PicAddLength + PicNameLength) * PicNum;
		for (int i = 0;i < PicNum;i ++){
			PicBmpAdd = PicPosAdd + 4;
			//开始写入图片数据
			rf.seek(PicPosAdd + 4);
			char[] BmpHead = {
					0x42,0x4D,0xC6,0x1B,0x00,0x00,0x00,0x00,0x00,0x00,0x36,0x00,0x00,0x00,0x28,0x00,
					0x00,0x00,0xFF,0x00,0x00,0x00,0xFF,0x00,0x00,0x00,0x01,0x00,0x18,0x00,0x00,0x00,
					0x00,0x00,0x90,0x1B,0x00,0x00,0xC4,0x0E,0x00,0x00,0xC4,0x0E,0x00,0x00,0x00,0x00,
					0x00,0x00,0x00,0x00,0x00,0x00
					};
			String TempStr = String.valueOf(BmpHead);
			rf.seek(PicBmpAdd);
			rf.writeBytes(TempStr);
			rf.seek(PicBmpAdd + 18);
			rf.write(Pic[i].w % 256);
			rf.write(Pic[i].w / 256);
			rf.seek(PicBmpAdd + 22);
			rf.write(Pic[i].h % 256);
			rf.write(Pic[i].h / 256);
			rf.seek(PicBmpAdd + 54);
			for (int iy = Pic[i].h - 1;iy >= 0;iy --){
				for (int ix = 0;ix < Pic[i].w;ix ++){
					int b,g,r;
					b = Pic[i].p[ix][iy].getBlue();
					g = Pic[i].p[ix][iy].getGreen();
					r = Pic[i].p[ix][iy].getRed();
					rf.writeByte(b);
					rf.writeByte(g);
					rf.writeByte(r);
					if (Pic[i].w * 3 % 4 != 0 && ix == Pic[i].w - 1){
						for (int t = 0;t < 4 - Pic[i].w * 3 % 4;t ++){
							rf.writeByte(0);
						}
					}
				}
			}
			PicNextAdd = rf.getFilePointer();
			//写入图片偏移
			rf.seek(4 + PicAddLength * i);
			rf.writeAntiInt(PicPosAdd);
			//写入图片名称
			rf.seek(4 + PicAddLength * PicNum + PicNameLength * i);
			rf.writeBytes(Pic[i].name);
			//写入图片长度
			rf.seek(PicPosAdd);
			rf.writeAntiInt(PicNextAdd - PicPosAdd - 4);
			rf.seek(PicNextAdd);
			PicPosAdd = PicNextAdd;
		}
		rf.close();
		this.IsMod = false;
		srcFile = f;
	}
	
	public Picture GetPic(int id){
		return Pic[id];
	}
	
	public long GetPicNum(){
		return PicNum;
	}
	
	public void DeletePicture(int id){
		if (id >= 0 && id < PicNum){
			Picture[] TempPic = new Picture[(int) (PicNum - 1)];
			for (int i = 0;i < id;i ++){
				TempPic[i] = Pic[i];
			}
			for (int i = id + 1;i < PicNum;i ++){
				TempPic[i - 1] = Pic[i];
			}
			Pic = TempPic;
			PicNum --;
			IsMod = true;
		}
	}
	
	public void AddPicture(int id,Picture g){
		if (id >= 0 && id < PicNum){
			Picture[] TempPic = new Picture[(int) (PicNum + 1)];
			for (int i = 0;i < id;i ++){
				TempPic[i] = Pic[i];
			}
			TempPic[id] = g;
			for (int i = id;i < PicNum;i ++){
				TempPic[i + 1] = Pic[i];
			}
			Pic = TempPic;
			PicNum ++;
			IsMod = true;
		}
	}
	
	public void AddPicture(Picture g){
		Picture[] TempPic = new Picture[(int) (PicNum + 1)];
		for (int i = 0;i < PicNum;i ++){
			TempPic[i] = Pic[i];
		}
		TempPic[(int) PicNum] = g;
		Pic = TempPic;
		PicNum ++;
		IsMod = true;
	}
	
	public boolean IsMod(){
		return this.IsMod;
	}
	
	public File getSrcFile(){
		return srcFile;
	}
	
	public void ChangePic(int id,Picture srcPic){
		Pic[id] = srcPic;
		IsMod = true;
	}
}
