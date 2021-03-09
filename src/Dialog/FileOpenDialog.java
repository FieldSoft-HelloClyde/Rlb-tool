package Dialog;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileOpenDialog {

    static File defaultOpenFolderFile = new File(".");
    File ReturnFile;
    File[] ReturnFiles;

    public FileOpenDialog(Component parent, String[][] fileENames, boolean MultiSelectEnabled) {

        // �����ļ�ѡ����
        JFileChooser fileChooser = new JFileChooser();

        // ���õ�ǰĿ¼
        fileChooser.setCurrentDirectory(new File(defaultOpenFolderFile.getAbsolutePath()));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(MultiSelectEnabled);

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

        if (fileChooser.showDialog(parent, null) == JFileChooser.CANCEL_OPTION) {
            ReturnFile = null;
            ReturnFiles = null;
        } else {
            defaultOpenFolderFile = fileChooser.getCurrentDirectory();
            ReturnFile = fileChooser.getSelectedFile();
            ReturnFiles = fileChooser.getSelectedFiles();
        }

    }

    public FileOpenDialog(Component parent, String[][] fileENames) {
        this(parent, fileENames, false);
    }


    public FileOpenDialog() {
        this(null, new String[][]{{".java", "Java�ļ�(*.java)"}});
    }

    public FileOpenDialog(String[][] s) {
        this(null, s);
    }

    public File SelectedFile() {
        return ReturnFile;
    }

    public File[] SelectedFiles() {
        return ReturnFiles;
    }

}
