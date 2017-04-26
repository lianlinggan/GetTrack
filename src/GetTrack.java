import java.io.IOException;
import java.util.HashMap;


public class GetTrack {
	private HashMap<String, double[]> _fea ;
	
	public GetTrack() {
		// TODO Auto-generated constructor stub
		init() ;
	}
	private void init() {
		// TODO Auto-generated method stub
		_fea = new HashMap<String, double[]>() ;	
	}
	public HashMap<String, double[]> getFea(){
		return _fea ;
	}
	/**
	 *@author 连凌淦(Lian Linggan)
	 *@version 2.0
	 *@param Args 输入路径、阈值设定等6个参数
	 */
	static public void main(String Args[]) throws IOException{
	/**
	 *@param Args[0] imgPath输入文件图像文件的路径
	 *@param Args[1] ifNeedLoading是否需要上传图像获取特征
	 *@param Args[2] threshold图像特征域阈值
	 *@param Args[3] ifAddLocationInfo是否加入空间域判断
	 *@param Args[4] location_threshold空间域阈值
	 *@param Args[5] flame_threshold帧距离阈值
	 */
		String imgPath = Args[0] ;
		int ifNeedLoading = Integer.valueOf(Args[1]) ;
		double threshold = Double.valueOf(Args[2]) ;
		int ifAddLocationInfo = Integer.valueOf(Args[3]) ;
		double location_threshold = Double.valueOf(Args[4]) ;
		int flame_threshold = Integer.valueOf(Args[5]) ;
		String ip = Args[6] ;
		int port = Integer.valueOf(Args[7]) ;
		int timeout = Integer.valueOf(Args[8]) ;
		GetImgFeature getImgFeature = new GetImgFeature(imgPath, ifNeedLoading, ip, port, timeout) ;
		HashMap<String, double[]> fea = new HashMap<String, double[]>();
		fea = getImgFeature.GetFeatures() ;
		
		TrackCreate trackCreate = new TrackCreate(fea, imgPath, threshold, flame_threshold, ifAddLocationInfo, location_threshold) ;
	}
}
