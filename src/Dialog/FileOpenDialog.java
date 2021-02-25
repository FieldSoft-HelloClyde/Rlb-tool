package Dialog;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileOpenDialog {

	static File defaultOpenFolderFile = new File(".");
	File ReturnFile;
	File[] ReturnFiles;

	public FileOpenDialog(Component parent,String PathName,String[][] fileENames,boolean MultiSelectEnabled){

		// 创建文件选择器
		JFileChooser fileChooser = new JFileChooser();
		
		// 设置当前目录
		fileChooser.setCurrentDirectory(new File(PathName));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(MultiSelectEnabled);
	
		// 显示所有文件
		fileChooser.addChoosableFileFilter(new FileFilter() {
		
			public boolean accept(File file) {
				return true;
			}
			
			public String getDescription() {
				return "所有文件(*.*)";
			}
			
		});
			  
		// 循环添加需要显示的文件
		for (final String[] fileEName : fileENames) {
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				
				public boolean accept(File file) {
					if (file.getName().toLowerCase().endsWith(fileEName[0]) || file.isDirectory()) {
						return true;
					}
					return false;
				}
			 
				public String getDescription() {
					return fileEName[1];
				}
			 
			});
		}
			  
		if (fileChooser.showDialog(parent, null) == JFileChooser.CANCEL_OPTION){
			ReturnFile = null;
			ReturnFiles = null;
		}
		else{
			defaultOpenFolderFile = fileChooser.getCurrentDirectory();
			ReturnFile = fileChooser.getSelectedFile();
			ReturnFiles = fileChooser.getSelectedFiles();
		}
		
	}
	
	public FileOpenDialog(Component parent,String PathName,String[][] fileENames){
		this(parent,PathName,fileENames,false);
	}

	
	public FileOpenDialog(){
		this(null,defaultOpenFolderFile.getAbsolutePath(),new String[][]{{".java","Java文件(*.java)"}});
	}
	
	public FileOpenDialog(String[][] s){
		this(null,defaultOpenFolderFile.getAbsolutePath(),s);
	}
	
	public File SelectedFile(){
		return ReturnFile;
	}
	
	public File[] SelectedFiles(){
		return ReturnFiles;
	}

}
