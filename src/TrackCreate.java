import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class TrackCreate {
	private HashMap<String, double[]> _fea ;
	private String _imgPath ;
	//the pre-word '_selected' means pictures that can be recognized by face feature program
	//num is the count of all selected imgs, 
	//name and feature is the map of name and features with same index ;
	//img_flame_index is the related flame number of the image name
	private int _selected_imgs_num ;
	private String[] _selected_imgs_name ;
	private double[][] _selected_imgs_feas ;
	private int[] _selected_img_flame_index ;
	private int _feas_length ;
	
	private int[][] _track_name_lastFrame;
	private int[] _track_img_label ;
	
	public TrackCreate(HashMap<String, double[]> fea, String imgPath, double threshold, int frame_threshold, int ifAddLocationInfo, double location_threshold) throws IOException {
		// TODO Auto-generated constructor stub
		System.out.println(imgPath + "  TrackCreate ..");
		_fea = new HashMap<>() ;
		_imgPath = new String() ;
		

		_fea = fea ;
		Iterator<String> length_iter = _fea.keySet().iterator();
		String key = length_iter.next();
		_feas_length =  _fea.get(key).length ;
		
		_imgPath = imgPath ;
		_selected_imgs_num = _fea.size() ;
		_selected_imgs_name = new String[_selected_imgs_num] ;
		_selected_img_flame_index = new int[_selected_imgs_num] ;
		_selected_imgs_feas = new double[_selected_imgs_num][_feas_length] ;
		
		getSelectedImgInfo() ;
		createTrack(threshold, frame_threshold, ifAddLocationInfo, location_threshold) ;
		moveImageToFolder();
		
	}

	private void moveImageToFolder() {
		// TODO Auto-generated method stub
		System.out.println("moveImageToFolder doing ...");
		File tempFile = new File(_imgPath+"/0.8/") ;
		if(!tempFile.exists())
			tempFile.mkdirs() ;
		for(int i=0; i<_selected_imgs_num; i++){
			System.out.println("Moving image " + _selected_imgs_name[i]);
			String srcFileName =  _imgPath + "/" + _selected_imgs_name[i] ;
			String destFileName = _imgPath + "/0.8/" + _track_img_label[i] + "/" + _selected_imgs_name[i] ;
			CopyFileUtil.copyFile(srcFileName, destFileName, true);
		}
	}

	private void createTrack(double threshold, int flame_threshold, int ifAddLocationInfo, double location_threshold) throws IOException {
		System.out.println("createTrack  doing ... threadhold:" + threshold);
		// TODO Auto-generated method stub
		_track_name_lastFrame = new int[_selected_imgs_num][2] ;
		_track_img_label = new int[_selected_imgs_num] ;
		int track_length = 0 ;
		AddLocationInfo addLocationInfo = new AddLocationInfo(_imgPath) ;
		
		for(int i=0; i<_selected_imgs_num; i++ ){
			System.out.println("");
			System.out.println("classifying  image " + _selected_imgs_name[i]);
			_track_img_label[i] = 0 ;
			
			double img_track_dis = 10000 ;
			int belongTo_track = 0 ;
			for(int track_j=0; track_j<track_length; track_j++){
				int curr_track_index = _track_name_lastFrame[track_j][1] ;
				
				//= this condition asked the distance between image and last track's images
				//  must less than 200, which mean interval must less than 10s
				int frame_dis = Math.abs(_selected_img_flame_index[i]-_selected_img_flame_index[curr_track_index]) ;
				if( frame_dis<flame_threshold ){
					//-- calculate the distance from image to track's last image
					//-- if distance is less than before and satisfied threshold constraint, update
					double temp_dis = calculateImage2TrackDis(i, curr_track_index) ;
					System.out.println(_selected_imgs_name[i] + " M " + _selected_imgs_name[curr_track_index] + " " + temp_dis );
					if(temp_dis<threshold && temp_dis<img_track_dis){
						if(ifAddLocationInfo==1){
							if(addLocationInfo.ifSatisfyLocationConstraint(_selected_imgs_name[i], _selected_imgs_name[curr_track_index], location_threshold)){
								img_track_dis = temp_dis ;
								belongTo_track = track_j ;
								//System.out.println("1==        track_name_lastFrame:  "+_track_name_lastFrame[track_j][0] + "  " + _track_name_lastFrame[track_j][1]);
								_track_img_label[i] = _track_name_lastFrame[track_j][0] ;
								//_track_name_lastFrame[track_j][1] = i ;
								//System.out.println("2===       track_name_lastFrame:  "+_track_name_lastFrame[track_j][0] + "  " + _track_name_lastFrame[track_j][1]);
							}
							else if(frame_dis<8 && addLocationInfo.ifSatisfySoftLocationConstraint(_selected_imgs_name[i], _selected_imgs_name[curr_track_index], location_threshold) ){
								img_track_dis = temp_dis ;
								belongTo_track = track_j ;
								_track_img_label[i] = _track_name_lastFrame[track_j][0] ;
							}
							
						}
						else{
							img_track_dis = temp_dis ;
							belongTo_track = track_j ;
							_track_img_label[i] = _track_name_lastFrame[track_j][0] ;
							//_track_name_lastFrame[track_j][1] = i ;
						}
					}
					//--
					else if(frame_dis<=10){
						//double temp_dis = calculateImage2TrackDis(i, curr_track_index) ;
						//System.out.println(_selected_imgs_name[i] + " M " + _selected_imgs_name[curr_track_index] + " " + temp_dis );
						if(temp_dis<threshold+2 && temp_dis<img_track_dis){
							if(ifAddLocationInfo==1){
								if(addLocationInfo.ifSatisfyLocationConstraint(_selected_imgs_name[i], _selected_imgs_name[curr_track_index], location_threshold)){
									belongTo_track = track_j ;
									img_track_dis = temp_dis ;
									//System.out.println("1==        track_name_lastFrame:  "+_track_name_lastFrame[track_j][0] + "  " + _track_name_lastFrame[track_j][1]);
									_track_img_label[i] = _track_name_lastFrame[track_j][0] ;
									//_track_name_lastFrame[track_j][1] = i ;
									//System.out.println("2===       track_name_lastFrame:  "+_track_name_lastFrame[track_j][0] + "  " + _track_name_lastFrame[track_j][1]);
								}
							}
						}
					}
				}
				//=
				
			}//for end 'track_j'
			
			if(_track_img_label[i]==0){
				_track_name_lastFrame[track_length][0] = track_length+1 ;
				_track_name_lastFrame[track_length][1] = i ;
				track_length++ ;
				//track_last_label = track_length+1 ;
				//_track_img_label[i] = track_last_label ;
				_track_img_label[i] = track_length ;
			}
			else{
				//_track_img_label[i] = _track_name_lastFrame[belongTo_track][0] ;
				_track_name_lastFrame[belongTo_track][1] = i ;
			}
			System.out.println(" -- track " + _track_img_label[i] + "  track dis: " + img_track_dis);
		}
	}

	private double calculateImage2TrackDis(int image, int track_last_image) {
		// TODO Auto-generated method stub
		double distance = 0 ;
		for(int i=0; i<_feas_length; i++){
			distance += Math.pow( (_selected_imgs_feas[image][i]-_selected_imgs_feas[track_last_image][i]),2 ) ;
		}
		distance = Math.sqrt(distance) ;
		return distance ;
	}

	//'getSelectedImgInfo' is to initial all images' information in order
	//flame number, features, and image's name were connect with same index
	private void getSelectedImgInfo() throws IOException {
		System.out.println("getSelectedImgInfo  doing ...");
		// TODO Auto-generated method stub
		String imgNameList = _imgPath+"/img_name_list.txt" ;
		FileInputStream fs=new FileInputStream(new File(imgNameList));
		BufferedReader br=new BufferedReader(new InputStreamReader(fs));
		int i=0 ;
		while(true){
			String line=br.readLine();
			if(line==null||line.isEmpty())
				break;
			int pos=line.indexOf(" ");
			String imgName = line.substring(0, pos) ;
			//System.out.println(imgName);
			if( _fea.get(imgName)!=null){
				_selected_imgs_feas[i] = _fea.get(imgName) ;
				_selected_imgs_name[i] = imgName ;
				_selected_img_flame_index[i] = Integer.parseInt(getFlameIndex(imgName)) ;
				i++ ;
			}
		}
	}
	
	//get flame number of the image from image name
	private String getFlameIndex(String imgName) {
		// TODO Auto-generated method stub
		int begin_pos = imgName.indexOf("_")+1 ;
		int end_pos = imgName.indexOf("_", begin_pos) ;
		return imgName.substring(begin_pos, end_pos) ;
	}
	
	
	
	
	
}
