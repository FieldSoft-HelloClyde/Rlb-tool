package Dialog;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileSaveDialog {
	File ReturnFile;

	public FileSaveDialog(Component parent,String PathName,String[][] fileENames){

		// �����ļ�ѡ����
		JFileChooser fileChooser = new JFileChooser();
		
		// ���õ�ǰĿ¼
		fileChooser.setCurrentDirectory(new File(PathName));
		fileChooser.setAcceptAllFileFilterUsed(false);
	
		// ��ʾ�����ļ�
		fileChooser.addChoosableFileFilter(new FileFilter() {
		
			public boolean accept(File file) {
				return true;
			}
			
			public String getDescription() {
				return "�����ļ�(*.*)";
			}
			
		});
			  
		// ѭ�������Ҫ��ʾ���ļ�
		for (final String[] fileEName : fileENames) {
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				
				public boolean accept(File file) {
					if (file.getName().endsWith(fileEName[0]) || file.isDirectory()) {
						return true;
					}
					return false;
				}
			 
				public String getDescription() {
					return fileEName[1];
				}
			 
			});
		}
			  
		if (fileChooser.showSaveDialog(null) == JFileChooser.CANCEL_OPTION){
			ReturnFile = null;
		}
		else{
			ReturnFile = fileChooser.getSelectedFile();
		}
	}
	
	public FileSaveDialog(){
		this(null,".",new String[][]{{".java","Java�ļ�(*.java)"}});
	}
	
	public FileSaveDialog(String[][] s){
		this(null,".",s);
	}
	
	public File SelectedFile(){
		return ReturnFile;
	}
}
