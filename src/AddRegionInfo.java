import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageReader;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;
import org.opencv.photo.*;


/**
 * 对相邻人脸块的位置信息进行比较
 */
public class AddRegionInfo {
	HashMap<String, double[]>  _locaiton_info ;
	String _source_path  ;
	
	
	public AddRegionInfo(String imgPath) throws IOException {
		// TODO Auto-generated constructor stub
		Init(imgPath) ;
		
		FileInputStream fileInputStream = new FileInputStream(new File(imgPath)) ;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream)) ;
		double[] loca_info = new double[4] ;
		while(true){
			String line = bufferedReader.readLine() ;
			if(line==null || line.isEmpty())
				break ;
			String[] subLine = line.split(" ") ;
			String imgName = subLine[0] ;
			loca_info[0] = Double.valueOf(subLine[1]) ;
			loca_info[1] = Double.valueOf(subLine[2]) ;
			loca_info[2] = Double.valueOf(subLine[3]) ;
			loca_info[3] = Double.valueOf(subLine[4]) ;
			_locaiton_info.put(imgName, loca_info) ;
		}
		fileInputStream.close(); 
		bufferedReader.close(); 
	}
	private void Init(String sourcePath) {
		// TODO Auto-generated method stub
		_locaiton_info = new HashMap<>() ;
		_source_path = sourcePath ;
	}
	
	//let image1 is larger than image2
	public boolean ifSatisfyRegionConstraint(String img1, String img2){
		double eucl_dis = calculateEuclidean(img1, img2) ;
		return false;
	}
	private double calculateEuclidean(String img1, String img2) {
		// TODO Auto-generated method stub
		double[] img1_info = _locaiton_info.get(img1) ;
		double[] img2_info = _locaiton_info.get(img2) ;
		File sonFile = new File(_source_path) ;
		String parentFile = sonFile.getParent() ;
		Mat mat_img1 = Highgui.imread(parentFile+"/"+img1, Highgui.CV_LOAD_IMAGE_GRAYSCALE) ;
		Mat mat_img2 = Highgui.imread(parentFile+"/"+img2, Highgui.CV_LOAD_IMAGE_GRAYSCALE) ;
		int img1_w = mat_img1.width() ;
		int img1_h = mat_img1.height() ;
		int img2_w = mat_img2.width() ;
		int img2_h = mat_img2.height() ;
		int w_gap = img1_w-img2_w ;
		int h_gap = img1_h-img2_h ;
		for(int i=0; i<w_gap; i++){
			for(int j=0; j<h_gap; j++){
				calculateDistance(mat_img1, mat_img2, i, j, img2_w, img2_h) ;
			}
		}
		return 0;
	}
	private double calculateDistance(Mat mat_img1, Mat mat_img2, int i, int j, int w, int h) {
		// TODO Auto-generated method stub
		double dis = 0 ;
		for(int m=0; m<w; m++){
			for(int n=0; n<h; n++){
				dis += Math.pow((mat_img1.get(n, m)[0]-mat_img2.get(n, m)[0]), 2) ;
			}
		}
		return Math.sqrt(dis) ;
	}
}
