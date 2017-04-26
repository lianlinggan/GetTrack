import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AddLocationInfo {
	HashMap<String, double[]> _name_location ;
	public AddLocationInfo(String imgPath) throws IOException {
		// TODO Auto-generated constructor stub
		
		_name_location = new HashMap<String, double[]>() ;
		FileInputStream fStream = new FileInputStream(imgPath+"/img_location_info.txt") ;
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fStream)) ;
		while(true){
			String line = bReader.readLine() ;
			if(line==null || line.isEmpty())
				break ;
			//System.out.println(line);
			String[] splitInfo = line.split(" ") ;
			//System.out.println(splitInfo.length);
			double[] location = new double[4] ;
			location[0] = Double.valueOf(splitInfo[1]) ;
			location[1] = Double.valueOf(splitInfo[2]) ;
			location[2] = Double.valueOf(splitInfo[3]) ;
			location[3] = Double.valueOf(splitInfo[4]) ;
			_name_location.put(splitInfo[0], location) ;
		}
	}
	
	public boolean ifSatisfyLocationConstraint(String img1, String img2, double location_threshold){
		double[] img1_location = _name_location.get(img1) ;
		double[] img2_location = _name_location.get(img2) ;
		double img_avg_h = (img1_location[2]+img2_location[2])/2 ;
		double img_avg_w = (img1_location[3]+img2_location[3])/2 ;
		double h_threshold = img_avg_h*0.6 ;
		double w_threshold = img_avg_w*0.6 ;
		double Y_dis = Math.abs(img1_location[0]-img2_location[0]) ;
		double X_dis = Math.abs(img1_location[1]-img2_location[1]) ;
		if( Y_dis<h_threshold && X_dis<w_threshold){
			System.out.println(img1 + "  vs  " +img2 + "         X: " + X_dis + "  Y:" + Y_dis + "  w: " + img_avg_w + "  h:" + img_avg_h + " x/w" + X_dis/img_avg_w + " y/h" + Y_dis/img_avg_h) ;
			return true ;
		}
		else
			System.out.println(img1 + "  vs  " + img2 + "  not fit location !  " + X_dis + "  " + Y_dis + "  w: " + img_avg_w + "  h:" + img_avg_h + " x/w" + X_dis/img_avg_w + " y/h" + Y_dis/img_avg_h);
			return false ;
	}
	
	public boolean ifSatisfySingleLocationConstraint(String img1, String img2, double location_threshold){
		double[] img1_location = _name_location.get(img1) ;
		double[] img2_location = _name_location.get(img2) ;
		double img_avg_h = (img1_location[2]+img2_location[2])/2 ;
		double img_avg_w = (img1_location[3]+img2_location[3])/2 ;
		double h_threshold = img_avg_h*0.6 ;
		double w_threshold = img_avg_w*0.6 ;
		double Y_dis = Math.abs(img1_location[0]-img2_location[0]) ;
		double X_dis = Math.abs(img1_location[1]-img2_location[1]) ;
		if( Y_dis<h_threshold || X_dis<w_threshold){
			System.out.println(img1 + "  vs  " +img2 + "         X: " + X_dis + "  Y:" + Y_dis + "  w: " + img_avg_w + "  h:" + img_avg_h + " x/w" + X_dis/img_avg_w + " y/h" + Y_dis/img_avg_h) ;
			return true ;
		}
		else
			System.out.println(img1 + "  vs  " + img2 + "  not fit location !  " + X_dis + "  " + Y_dis + "  w: " + img_avg_w + "  h:" + img_avg_h + " x/w" + X_dis/img_avg_w + " y/h" + Y_dis/img_avg_h);
			return false ;
	}
	
	public boolean ifSatisfySoftLocationConstraint(String img1, String img2, double location_threshold){
		double[] img1_location = _name_location.get(img1) ;
		double[] img2_location = _name_location.get(img2) ;
		double img_avg_h = (img1_location[2]+img2_location[2])/2 ;
		double img_avg_w = (img1_location[3]+img2_location[3])/2 ;
		double h_threshold = img_avg_h*0.8 ;
		double w_threshold = img_avg_w*0.8 ;
		double Y_dis = Math.abs(img1_location[0]-img2_location[0]) ;
		double X_dis = Math.abs(img1_location[1]-img2_location[1]) ;
		if( Y_dis<h_threshold && X_dis<w_threshold){
			System.out.println(img1 + "  vs  " +img2 + "         X: " + X_dis + "  Y:" + Y_dis + "  w: " + img_avg_w + "  h:" + img_avg_h + " x/w" + X_dis/img_avg_w + " y/h" + Y_dis/img_avg_h) ;
			return true ;
		}
		else
			System.out.println(img1 + "  vs  " + img2 + "  not fit location !  " + X_dis + "  " + Y_dis + "  w: " + img_avg_w + "  h:" + img_avg_h + " x/w" + X_dis/img_avg_w + " y/h" + Y_dis/img_avg_h);
			return false ;
	}
}
