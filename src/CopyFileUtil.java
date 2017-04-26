import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
  
import javax.swing.JOptionPane;  

public class CopyFileUtil {

    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
  
        if (!srcFile.exists()) {
            System.out.println(srcFile.getName() + " does not exist");
            return false;  
        } else if (!srcFile.isFile()) {
            System.out.println(srcFile.getName() + " is not a File");
            return false;  
        }

        File destFile = new File(destFileName);  
        if (destFile.exists()) {

            if (overlay) {
                new File(destFileName).delete();  
            }  
        } else {

            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    return false;  
                }  
            }  
        }  


        int byteread = 0;
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  


    public static boolean copyDirectory(String srcDirName, String destDirName,  
            boolean overlay) {

        File srcDir = new File(srcDirName);  
        if (!srcDir.exists()) {  
            System.out.println(srcDir.getPath() + " dir does not exist!");
            return false;  
        } else if (!srcDir.isDirectory()) {
            System.out.println(srcDir.getPath() + " is not a directory!");
            return false;  
        }  


        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        File destDir = new File(destDirName);

        if (destDir.exists()) {
            if (overlay) {  
                new File(destDirName).delete();  
            } else {
                System.out.println(destDirName + " is exist and cannot be overlay!");
                return false;  
            }  
        } else {
            System.out.println(destDirName + " is not exit and creating .. ");
            if (!destDir.mkdirs()) {  
                System.out.println("Creat " + destDirName + " false!");
                return false;  
            }  
        }  
  
        boolean flag = true;  
        File[] files = srcDir.listFiles();  
        for (int i = 0; i < files.length; i++) {

            if (files[i].isFile()) {  
                flag = CopyFileUtil.copyFile(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            } else if (files[i].isDirectory()) {  
                flag = CopyFileUtil.copyDirectory(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            }  
        }  
        if (!flag) {
            System.out.println("Copy fail!");
            return false;  
        } else {  
            return true;  
        }  
    }   
}  