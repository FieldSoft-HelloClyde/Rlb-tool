package WindowsApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JScrollBar;

import java.awt.Canvas;

import javax.swing.JScrollPane;

import Dialog.FileOpenDialog;
import Dialog.FileSaveDialog;
import RLB.Bmp;
import RLB.Picture;
import RLB.RlbFile;
import WindowsApp.MyCanvas;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

public class WindowsFrame extends JFrame {

	JLabel label;
	JList list;
	//public static JProgressBar progressBar;
	JProgressBar progressBar;
	//public static int ProgressBarValue = 100;
	MyCanvas canvas;
	/**
	 * ��ת��ת���ã�>0��ʾ˳ʱ����ת������-1��ʾˮƽ��ת��-2��ʾ��ֱ��ת
	 */
	int RotationConfig = 90;
	/**
	 * ɫ�ʹ��˵ķ�ֵ
	 */
	int ColorS = 128;
	/**
	 * ����ɫ����
	 */
	static public Color SpecialColor = new Color(255, 0, 255);
	
	JLabel lblX;
	RlbFile ResFile = new RlbFile();
	
	File DesFile;
	
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowsFrame frame = new WindowsFrame();
					frame.setVisible(true); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public WindowsFrame() {
		setTitle("ResTool");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );
		setBounds(100, 100, 1067, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1061, 30);
		contentPane.add(menuBar);
		
		JMenu menu = new JMenu("\u6587\u4EF6");
		menuBar.add(menu);
		
		/**
		 * ����ر�
		 */
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO �Զ����ɵķ������
				if (ResFile.IsMod() == true){
					if (ShowIsSaveDialog() == 2)
						return;
				}
				dispose();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}
			
		});
		
		/**
		 * �˵� - �ļ� - �½�
		 */
		JMenuItem menuItem_1 = new JMenuItem("\u65B0\u5EFA");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ResFile != null && ResFile.IsMod() == true){
					if (ShowIsSaveDialog() == 2)
						return;
				}
				ResFile = new RlbFile();
				canvas.ToRlb(new Picture(0,0));
				canvas.repaint();
				RefreshListData();
				lblX.setText("\u56FE\u7247\u4FE1\u606F\u672A\u77E5");
			}
		});
		menu.add(menuItem_1);
		
		/**
		 * �˵� - �ļ� - ��
		 */
		JMenuItem menuItem = new JMenuItem("\u6253\u5F00");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ResFile.IsMod() == true){
					if (ShowIsSaveDialog() == 2)
						return;
				}
				FileOpenDialog FileOpenDialog1 = new FileOpenDialog(new String[][]{{".lib","lib�Ѿ�ת������Դ�ļ�(*.lib)"},{".rlb","Rlb��Դ�ļ�(*.rlb)"}});
				DesFile = FileOpenDialog1.SelectedFile();
				if (DesFile != null){
					if (DesFile.getName().toLowerCase().endsWith(".rlb")){
						LoadRlb(0);
					}
					else if (DesFile.getName().toLowerCase().endsWith(".lib")){
						LoadRlb(1);
					}
					else{
						JOptionPane.showMessageDialog(null, "��֧�ֵ��ļ����ͣ�");
					}
				}
			}
		});
		menu.add(menuItem);
		
		/*
		 * �˵�-�ļ�-����
		 */
		JMenuItem menuItem_2 = new JMenuItem("\u4FDD\u5B58");
		menu.add(menuItem_2);
		menuItem_2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				if (ResFile.getSrcFile() == null){
					SaveAs();
				}
				else{
					try {
						ResFile.SaveRlb(ResFile.getSrcFile());
					} catch (IOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
			}
			
		});
		/*
		 * �˵�-�ļ�-���Ϊ
		 */
		JMenuItem menuItem_3 = new JMenuItem("\u53E6\u5B58\u4E3A");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveAs();
			}
		});
		menu.add(menuItem_3);
		
		/*
		 * �˵�-�ļ�-�˳�
		 */
		JMenuItem menuItem_4 = new JMenuItem("\u9000\u51FA");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ResFile.IsMod() == true){
					if (ShowIsSaveDialog() == 2)
						return;
				}
				dispose();
				System.exit(0);
			}
		});
		menu.add(menuItem_4);
		
		JMenu menu_1 = new JMenu("\u7F16\u8F91");
		menuBar.add(menu_1);
		
		/**
		 * �˵� - �༭ - ����
		 */
		JMenuItem menuItem_5 = new JMenuItem("\u63D2\u5165");
		menuItem_5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				try {
					FileOpenDialog FileOpenDialog1 = new FileOpenDialog(null,".",new String[][]{{".bmp","bmpͼƬ(*.bmp)"},{".png","pngͼƬ(*.png)"}},true);
					if (FileOpenDialog1.SelectedFiles() != null){
						int Index;
						Index = list.getSelectedIndex();
						for (int i = 0;i < FileOpenDialog1.SelectedFiles().length;i ++){
							File doFile;
							Picture g = null;
							doFile = FileOpenDialog1.SelectedFiles()[i];
							if (doFile.getName().toLowerCase().endsWith(".bmp")){
									g = Bmp.BmpToPicture(doFile);
							}
							else if (doFile.getName().toLowerCase().endsWith(".png")){
									g = Bmp.PngToPicture(doFile);
							}
							else{
								System.out.println("��֧�ֵ��ļ�����" + doFile.getName());
							}
							
							if (Index == -1){
								ResFile.AddPicture(g);
							}
							else{
								ResFile.AddPicture(Index, g);
							}
						}
						RefreshListData();
						list.setSelectedIndex(Index + 1);
						canvas.ToRlb(ResFile.GetPic(Index + 1));
						lblX.setText(ResFile.GetPic(Index + 1).GetWidth() + "*" + ResFile.GetPic(Index + 1).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
						canvas.repaint();
					}
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
			}
			
		});
		menu_1.add(menuItem_5);
		
		/**
		 * �˵� - �༭ - ɾ��
		 */
		JMenuItem menuItem_6 = new JMenuItem("\u5220\u9664");
		menuItem_6.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				int Index;
				Index = list.getSelectedIndex();
				if (Index != -1){
					ResFile.DeletePicture(Index);
					RefreshListData();
					if (Index == 0){
						canvas.ToRlb(new Picture(0, 0));
						canvas.repaint();
						lblX.setText("\u56FE\u7247\u4FE1\u606F\u672A\u77E5");
					}
					else{
						canvas.ToRlb(ResFile.GetPic(Index - 1));
						canvas.repaint();
						lblX.setText(ResFile.GetPic(Index - 1).GetWidth() + "*" + ResFile.GetPic(Index - 1).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "�����б���ѡ��һ��ͼƬ��");
				}
			}
			
		});
		menu_1.add(menuItem_6);
		
		/**
		 * �˵� - �༭ - �滻
		 */
		JMenuItem menuItem_7 = new JMenuItem("\u66FF\u6362");
		menuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileOpenDialog FileOpenDialog1 = new FileOpenDialog(new String[][]{{".bmp","bmpͼƬ(*.bmp)"},{".png","pngͼƬ(*.png)"}});
				DesFile = FileOpenDialog1.SelectedFile();
				if (DesFile != null){
					try {
						int SelectedId;
						SelectedId = list.getSelectedIndex();
						if (SelectedId != -1){
								if (DesFile.getName().toLowerCase().endsWith(".bmp")){
									ResFile.ChangePic(SelectedId, Bmp.BmpToPicture(DesFile));
								}
								else if (DesFile.getName().toLowerCase().endsWith(".png")){
									ResFile.ChangePic(SelectedId, Bmp.PngToPicture(DesFile));
								}
								else{
									JOptionPane.showMessageDialog(null, "��֧�ֵ��ļ����ͣ�");
								}
								RefreshListData();
								list.setSelectedIndex(SelectedId);
								canvas.ToRlb(ResFile.GetPic(SelectedId));
								canvas.repaint();
								lblX.setText(ResFile.GetPic(SelectedId).GetWidth() + "*" + ResFile.GetPic(SelectedId).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
							}
						else{
							JOptionPane.showMessageDialog(null, "�����б����ѡ��һ��ͼƬ��");
						}
					} catch (IOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
			}
		});
		menu_1.add(menuItem_7);
		
		
		
		JMenu menu_2 = new JMenu("����");
		menuBar.add(menu_2);
		
		JMenu menu_6 = new JMenu("BMPͼƬ");
		menu_2.add(menu_6);
		
		/*
		 * ��������BMPͼƬ
		 */
		JMenuItem menuItem_8 = new JMenuItem("\u5F53\u524D\u56FE\u7247");
		menuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileSaveDialog FileOpenDialog2 = new FileSaveDialog(new String[][]{{".bmp","bmp24λλͼ(*.bmp)"}});
				File DesBmpFile = FileOpenDialog2.SelectedFile();
				if (DesBmpFile != null){
					if (!DesBmpFile.getAbsolutePath().toLowerCase().endsWith(".bmp")){
						DesBmpFile = new File(DesBmpFile.getAbsolutePath() + ".bmp");
					}
					int id = list.getSelectedIndex();
					//System.out.println(id);
					if (id == -1){
						JOptionPane.showMessageDialog(null, "�����б��ѡ��һ��ͼƬ!");
					}
					else{
						try {
							Bmp.BmpSave(ResFile.GetPic(id),DesBmpFile);
							JOptionPane.showMessageDialog(null, "����ͼƬ�ɹ�!");
						} catch (IOException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
					}
				}
			}
		});
		menu_6.add(menuItem_8);
		
		/*
		 * ����ȫ��ͼƬ���ļ���
		 */
		JMenuItem menuItem_14 = new JMenuItem("\u5E93\u5185\u5168\u90E8\u56FE\u7247");
		menuItem_14.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				File SaveFolder;
				JFileChooser chooser = new JFileChooser();

				// DIRECTORIES_ONLY����ֻѡĿ¼
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION){
				
					if (chooser.getSelectedFile() != null){
						SaveFolder = chooser.getSelectedFile();
						for (int i = 0;i < ResFile.GetPicNum();i ++){
							try {
								Bmp.BmpSave(ResFile.GetPic(i), new File(SaveFolder.getAbsolutePath() + "/" +  ResFile.GetPic(i).GetName() + ".bmp"));
							} catch (IOException e1) {
								// TODO �Զ����ɵ� catch ��
								e1.printStackTrace();
							}
						}
						JOptionPane.showMessageDialog(null, "�Ѿ�����ȫ��ͼƬ!");
					}
				}
			}
			
		});
		menu_6.add(menuItem_14);
		
		JMenu menu_png = new JMenu("PNGͼƬ");
		menu_2.add(menu_png);
		
		/*
		 * ��������PNGͼƬ
		 */
		JMenuItem menuItem_PngSingle = new JMenuItem("\u5F53\u524D\u56FE\u7247");
		menuItem_PngSingle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileSaveDialog FileOpenDialog2 = new FileSaveDialog(new String[][]{{".png","pngͼƬ(*.png)"}});
				File DesBmpFile = FileOpenDialog2.SelectedFile();
				if (DesBmpFile != null){
					if (!DesBmpFile.getAbsolutePath().toLowerCase().endsWith(".png")){
						DesBmpFile = new File(DesBmpFile.getAbsolutePath() + ".png");
					}
					int id = list.getSelectedIndex();
					//System.out.println(id);
					if (id == -1){
						JOptionPane.showMessageDialog(null, "�����б��ѡ��һ��ͼƬ!");
					}
					else{
						try {
							Bmp.PngSave(ResFile.GetPic(id),DesBmpFile);
							JOptionPane.showMessageDialog(null, "����ͼƬ�ɹ�!");
						} catch (IOException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
					}
				}
			}
		});
		menu_png.add(menuItem_PngSingle);
		
		/*
		 * ����ȫ��ͼƬ���ļ���
		 */
		JMenuItem menuItem_PngAll = new JMenuItem("\u5E93\u5185\u5168\u90E8\u56FE\u7247");
		menuItem_PngAll.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				File SaveFolder;
				JFileChooser chooser = new JFileChooser();

				// DIRECTORIES_ONLY����ֻѡĿ¼
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION){
				
					if (chooser.getSelectedFile() != null){
						SaveFolder = chooser.getSelectedFile();
						for (int i = 0;i < ResFile.GetPicNum();i ++){
							try {
								Bmp.PngSave(ResFile.GetPic(i), new File(SaveFolder.getAbsolutePath() + "/" +  ResFile.GetPic(i).GetName() + ".png"));
							} catch (IOException e1) {
								// TODO �Զ����ɵ� catch ��
								e1.printStackTrace();
							}
						}
						JOptionPane.showMessageDialog(null, "�Ѿ�����ȫ��ͼƬ!");
					}
				}
			}
			
		});
		menu_png.add(menuItem_PngAll);
		
		JMenu mnLib = new JMenu("Lib\u8D44\u6E90\u5E93");
		menu_2.add(mnLib);
		
		/**
		 * �˵� - ���� - Lib��Դ�� - 9X88
		 */
		JMenuItem menuItem_10 = new JMenuItem("9X88Lib��Դ��");
		menuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaveDialog FileOpenDialog2 = new FileSaveDialog(new String[][]{{".lib","lib16λͼƬ��Դ��(*.lib)"}});
				File DesRlbFile = FileOpenDialog2.SelectedFile();
				if (DesRlbFile != null){
					if (!DesRlbFile.getAbsolutePath().toLowerCase().endsWith(".lib")){
						DesRlbFile = new File(DesRlbFile.getAbsolutePath() + ".lib");
					}
					try {
						ResFile.SaveLib(DesRlbFile);
						JOptionPane.showMessageDialog(null, "�Ѿ�����LibͼƬ��!");
					} catch (IOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
			}
		});
		mnLib.add(menuItem_10);
		
		JMenu menu_5 = new JMenu("\u56FE\u7247\u5904\u7406");
		menuBar.add(menu_5);
		

		JMenu menu_Rotation = new JMenu("��ת");
		menu_5.add(menu_Rotation);
		
		JMenu menu_RotationSet = new JMenu("��ת�Ƕ�");
		menu_Rotation.add(menu_RotationSet);
		
		JMenuItem menu_RotationSingle = new JMenuItem("��ת��ǰͼƬ");
		menu_Rotation.add(menu_RotationSingle);
		menu_RotationSingle.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				if (list.getSelectedIndex() != -1){
					if (RotationConfig == 90){
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.Rotation90(ResFile.GetPic(list.getSelectedIndex())));
					}
					else if (RotationConfig == 180){
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.Rotation90(ResFile.GetPic(list.getSelectedIndex())));
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.Rotation90(ResFile.GetPic(list.getSelectedIndex())));
					}
					else if (RotationConfig == 270){
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.Rotation90(ResFile.GetPic(list.getSelectedIndex())));
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.Rotation90(ResFile.GetPic(list.getSelectedIndex())));
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.Rotation90(ResFile.GetPic(list.getSelectedIndex())));
					}
					else if (RotationConfig == -1){
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.RotationLR(ResFile.GetPic(list.getSelectedIndex())));
					}
					else if (RotationConfig == -2){
						ResFile.ChangePic(list.getSelectedIndex(), Bmp.RotationUD(ResFile.GetPic(list.getSelectedIndex())));
					}
					
					canvas.ToRlb(ResFile.GetPic(list.getSelectedIndex()));
					canvas.repaint();
					lblX.setText(ResFile.GetPic(list.getSelectedIndex()).GetWidth() + "*" + ResFile.GetPic(list.getSelectedIndex()).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
					JOptionPane.showMessageDialog(null, "�Ѿ���ת��ǰͼƬ��");
				}
				else{
					JOptionPane.showMessageDialog(null, "�����б��ѡ��һ��ͼƬ��");
				}
			}
			
		});
		
		JMenuItem menu_RotationAll = new JMenuItem("��ת��������ͼƬ");
		menu_Rotation.add(menu_RotationAll);
		menu_RotationAll.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				for (int i = 0;i < ResFile.GetPicNum();i ++){
					if (RotationConfig == 90){
						ResFile.ChangePic(i, Bmp.Rotation90(ResFile.GetPic(i)));
					}
					else if (RotationConfig == 180){
						ResFile.ChangePic(i, Bmp.Rotation90(ResFile.GetPic(i)));
						ResFile.ChangePic(i, Bmp.Rotation90(ResFile.GetPic(i)));
					}
					else if (RotationConfig == 270){
						ResFile.ChangePic(i, Bmp.Rotation90(ResFile.GetPic(i)));
						ResFile.ChangePic(i, Bmp.Rotation90(ResFile.GetPic(i)));
						ResFile.ChangePic(i, Bmp.Rotation90(ResFile.GetPic(i)));
					}
					else if (RotationConfig == -1){
						ResFile.ChangePic(i, Bmp.RotationLR(ResFile.GetPic(i)));
					}
					else if (RotationConfig == -2){
						ResFile.ChangePic(i, Bmp.RotationUD(ResFile.GetPic(i)));
					}
				}

				if (list.getSelectedIndex() != -1){
					canvas.ToRlb(ResFile.GetPic(list.getSelectedIndex()));
					canvas.repaint();
					lblX.setText(ResFile.GetPic(list.getSelectedIndex()).GetWidth() + "*" + ResFile.GetPic(list.getSelectedIndex()).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
				}
				JOptionPane.showMessageDialog(null, "�Ѿ���ת����ͼƬ!");
			}
			
		});
		
		JRadioButton menu_Rotation90 = new JRadioButton("90��");
		menu_RotationSet.add(menu_Rotation90);
		menu_Rotation90.setSelected(true);
		menu_Rotation90.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				RotationConfig = 90;
			}
			
		});
		
		JRadioButton menu_Rotation180 = new JRadioButton("180��");
		menu_RotationSet.add(menu_Rotation180);
		menu_Rotation180.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				RotationConfig = 180;
			}
			
		});
		
		JRadioButton menu_Rotation270 = new JRadioButton("270��");
		menu_RotationSet.add(menu_Rotation270);
		menu_Rotation270.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				RotationConfig = 270;
			}
			
		});
		
		JRadioButton menu_RotationLR = new JRadioButton("ˮƽ��ת");
		menu_RotationSet.add(menu_RotationLR);
		menu_RotationLR.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				RotationConfig = -1;
			}
			
		});
		
		JRadioButton menu_RotationUD = new JRadioButton("��ֱ��ת");
		menu_RotationSet.add(menu_RotationUD);
		menu_RotationUD.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				RotationConfig = -2;
			}
			
		});
		
		ButtonGroup tempButtonGroup = new ButtonGroup();
		tempButtonGroup.add(menu_Rotation90);
		tempButtonGroup.add(menu_Rotation180);
		tempButtonGroup.add(menu_Rotation270);
		tempButtonGroup.add(menu_RotationLR);
		tempButtonGroup.add(menu_RotationUD);
		
		
		JMenu mnNewMenu = new JMenu("������ɫ");
		menu_5.add(mnNewMenu);
		
		/**
		 * �Ե�ǰͼƬ������ɫ
		 */
		JMenuItem menuItem_11 = new JMenuItem("\u5BF9\u5F53\u524D\u56FE\u7247\u5904\u7406");
		menuItem_11.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				if (list.getSelectedIndex() != -1){
					ResFile.ChangePic(list.getSelectedIndex(), Bmp.ClearPup(ResFile.GetPic(list.getSelectedIndex()), ColorS));
					canvas.ToRlb(ResFile.GetPic(list.getSelectedIndex()));
					canvas.repaint();
					lblX.setText(ResFile.GetPic(list.getSelectedIndex()).GetWidth() + "*" + ResFile.GetPic(list.getSelectedIndex()).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
					JOptionPane.showMessageDialog(null, "�Ѿ�������ͼƬ��ɫ!");
				}
				else{
					JOptionPane.showMessageDialog(null, "�����б����ѡ��һ��ͼƬ!");
				}
			}
			
		});
		mnNewMenu.add(menuItem_11);
		
		/**
		 * ��ȫ��ͼƬ������ɫ
		 */
		JMenuItem menuItem_12 = new JMenuItem("\u5BF9\u5E93\u5185\u6240\u6709\u56FE\u7247\u5904\u7406");
		mnNewMenu.add(menuItem_12);
		menuItem_12.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				for (int i = 0;i < ResFile.GetPicNum();i ++){
					ResFile.ChangePic(i, Bmp.ClearPup(ResFile.GetPic(i), ColorS));
				}
				
				if (list.getSelectedIndex() != -1){
					canvas.ToRlb(ResFile.GetPic(list.getSelectedIndex()));
					canvas.repaint();
					lblX.setText(ResFile.GetPic(list.getSelectedIndex()).GetWidth() + "*" + ResFile.GetPic(list.getSelectedIndex()).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
					
				}
				JOptionPane.showMessageDialog(null, "�Ѿ�����ȫ��ͼƬ��ɫ��");
			}
			
		});
		
		/**
		 * �˵� - ������ɫ - ����
		 */
		JMenuItem menuItem_13 = new JMenuItem("������ɫ��ֵ");
		menuItem_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempStr;
				tempStr = JOptionPane.showInputDialog(null, "��������ɫ��ֵ��Ĭ��128", "��ɫ��ֵ����", JOptionPane.INFORMATION_MESSAGE);
				if (tempStr != null){
					ColorS = Integer.parseInt(tempStr, 16);
					//System.out.println(ColorS);
				}
			}
		});
		mnNewMenu.add(menuItem_13);
		
		/**
		 * �˵� - ������ɫ - ����
		 */
		JMenuItem menuItem_ColorSet = new JMenuItem("���ù���ɫ");
		menuItem_ColorSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempStr;
				tempStr = JOptionPane.showInputDialog(null, "���������ɫ��˳��Ϊbgr,Ĭ��ff00ff", "����ɫ����", JOptionPane.INFORMATION_MESSAGE);
				if (tempStr != null){
					int TempInt;
					int b,g,r;
					TempInt = Integer.parseInt(tempStr,16);
					if (TempInt >= 0 && TempInt <= 16777215){
						r = TempInt & 0x0000ff;
						g = (TempInt & 0x00ff00) >> 8;
						b = (TempInt & 0xff0000) >> 16;
						SpecialColor = new Color(r,g,b);
					}
					else{
						JOptionPane.showMessageDialog(null, "��������,��ΧӦ����0x000000~0xffffff֮�䡣");
						SpecialColor = new Color(255,0,255);
					}
				}
			}
		});
		mnNewMenu.add(menuItem_ColorSet);
		
		JMenu menu_3 = new JMenu("\u67E5\u770B");
		menuBar.add(menu_3);
		
		JRadioButton radioButton = new JRadioButton("ԭͼ");
		menu_3.add(radioButton);
		radioButton.setSelected(true);
		radioButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				canvas.ShowMode = 0;
				canvas.repaint();
			}
			
		});
		
		JRadioButton radioButton_1 = new JRadioButton("͸��ͼ");
		menu_3.add(radioButton_1);
		radioButton_1.setSelected(false);
		radioButton_1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				canvas.ShowMode = 1;
				canvas.repaint();
			}
			
		});
		
		ButtonGroup mButtonGroup = new ButtonGroup();
		mButtonGroup.add(radioButton);
		mButtonGroup.add(radioButton_1);
		
		JMenu menu_4 = new JMenu("\u5173\u4E8E");
		menuBar.add(menu_4);
		
		/*
		 * ��������
		 */
		JMenuItem mntmrestool = new JMenuItem("\u5173\u4E8EResTool");
		mntmrestool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "ResTool\n����:HelloClyde\n�汾:1.0\n�ٶ�BBK9588����");
			}
		});
		menu_4.add(mntmrestool);
		
		/*
		 * ��ϵ����
		 */
		JMenuItem menuItem_16 = new JMenuItem("\u8054\u7CFB\u4F5C\u8005");
		menuItem_16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URI uri;
				try {
					uri = new java.net.URI("http://tieba.baidu.com/f?kw=bbk9588&fr=index");
					java.awt.Desktop.getDesktop().browse(uri);
				} catch (URISyntaxException | IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} 
			}
		});
		menu_4.add(menuItem_16);
		
		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int id=((JList)e.getSource()).getSelectedIndex();
				if (id >= 0 && id < ResFile.GetPicNum()){
					label.setText("��ͼ��");
					canvas.ToRlb(ResFile.GetPic(id));
					//canvas.repaint();
					label.setText("����");
					lblX.setText(ResFile.GetPic(id).GetWidth() + "*" + ResFile.GetPic(id).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
				}
			}
		});
		//list.setBounds(10, 40, 214, 455);
		//contentPane.add(list);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(10, 40, 214, 455);
		contentPane.add(scrollPane2);
		scrollPane2.setViewportView(list);
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setBounds(346, 481, 538, 14);
		contentPane.add(progressBar);
		
		label = new JLabel("\u5C31\u7EEA");
		label.setBounds(238, 480, 100, 15);
		contentPane.add(label);
		
		
		canvas = new MyCanvas();
		canvas.setBackground(Color.LIGHT_GRAY);
		canvas.setForeground(Color.WHITE);
		//canvas.setSize(800, 400);
		canvas.setBounds(234, 40, 817, 426);
		
		JScrollPane scrollBar = new JScrollPane();
		scrollBar.setBounds(234, 40, 817, 426);
		contentPane.add(scrollBar);
		scrollBar.setViewportView(canvas);
		
		
		lblX = new JLabel("\u56FE\u7247\u4FE1\u606F\u672A\u77E5");
		lblX.setBounds(894, 481, 157, 13);
		contentPane.add(lblX);
		
	}
	
	/*
	 * ����ֵ��
	 * 0���Ѿ�����
	 * 1:������
	 * 2��ȡ���Ի���
	 * */
	public int ShowIsSaveDialog(){
		int RN = JOptionPane.showConfirmDialog(this, "�Ƿ񱣴�?","��ʾ:", JOptionPane.YES_NO_CANCEL_OPTION);
		if (RN == JOptionPane.OK_OPTION){
			if (ResFile.getSrcFile() == null){
				SaveAs();
			}
			else{
				try {
					ResFile.SaveRlb(ResFile.getSrcFile());
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			return 0;
		}
		else if (RN == JOptionPane.NO_OPTION){
			return 1;
		}
		else
			return 2;
	}
	
	public void LoadRlb(int mode){
		try {
			label.setText("����ͼƬ��");
			//this.repaint();
			if (mode == 0){
				ResFile = new RlbFile(DesFile);
			}
			else{
				ResFile = new RlbFile(DesFile,mode);
			}
			label.setText("��ͼ��");
			canvas.ToRlb(ResFile.GetPic(0));
			//canvas.repaint();
			label.setText("����");
			lblX.setText(ResFile.GetPic(0).GetWidth() + "*" + ResFile.GetPic(0).GetHeight() + " ͼƬ������" + ResFile.GetPicNum());
			RefreshListData();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	public void SaveAs(){
		FileSaveDialog FileOpenDialog2 = new FileSaveDialog(new String[][]{{".rlb","rlb24λͼƬ��Դ��(*.rlb)"}});
		File DesRlbFile = FileOpenDialog2.SelectedFile();
		if (DesRlbFile != null){
			if (!DesRlbFile.getAbsolutePath().toLowerCase().endsWith(".rlb")){
				DesRlbFile = new File(DesRlbFile.getAbsolutePath() + ".rlb");
			}
			try {
				ResFile.SaveRlb(DesRlbFile);
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
	}
	
	private void RefreshListData(){
		String TempStr[] = new String[(int)ResFile.GetPicNum()];
		for (int i = 0;i < ResFile.GetPicNum();i ++){
			String TempN = new String("");
			int k = i + 1;
			TempN = TempN + k;
			//����i�м�λ
			int l = TempN.length();
			
			for (int j = 0;j < 4 - l;j ++){
				TempN = "0" + TempN;
			}
			TempStr[i] =  TempN + ":  " + ResFile.GetPic(i).GetName();
		}
		list.setListData(TempStr);
	}
}
