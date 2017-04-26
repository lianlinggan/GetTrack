import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;

import org.apache.thrift.TException;

import pixtalks.face.thriftservice.ServiceWrapper;

public class GetImgFeature {
	private String _ip;
	private int _port;
	private int _timeout;
	private HashMap<String,double[]> _feas;

    /**
     *通过从服务器端获得人脸特征
     */
	public GetImgFeature(String imgPath, int ifNeedLoading, String ip, int port, int timeout) throws IOException {
		// TODO Auto-generated constructor stub
		_feas = new HashMap<String,double[]>();
		_ip = new String();
		/*
		_ip = "k2.cvforce.com";
		_port = 9090;
		_timeout = 5000;
		*/
		_ip = ip ;
		_port = port ;
		_timeout = timeout ;
		if(ifNeedLoading==1){
			LoadFeature(imgPath);
		}
		else{
			ReadFeature(imgPath);
		}
	};

    /**
     *
     * @param imgPath 人脸图像特征所在路径
     * @throws IOException 如果不存在特征文件，抛出IO异常
     */
	private void ReadFeature(String imgPath) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fs = new FileInputStream(new File(imgPath+"/features.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(fs)) ;
		
		while(true){
			String line=br.readLine();
			if(line==null||line.isEmpty())
				break;
			String[] dataLine = line.split(" ") ;
			String imgName = dataLine[0] ;
			double[] fea = new double[dataLine.length-1] ;
			for(int i=1; i<dataLine.length; i++){
				fea[i-1] = Double.valueOf(dataLine[i]) ;
			}
			_feas.put(imgName, fea) ;
		}
	};

    /**
     * 从服务器获得特征，并且按照一定的存储规则存储特征数据
     * @param imgPath 存储从服务器获得的人脸数据路径
     * @throws IOException
     *
     */
	private void LoadFeature(String imgPath) throws IOException {
		String imgListName = imgPath+"/img_name_list.txt" ;
		FileInputStream fs=new FileInputStream(new File(imgListName));
		BufferedReader br=new BufferedReader(new InputStreamReader(fs));
		
		
		FileOutputStream f_out = new FileOutputStream(new File(imgPath+"/features.txt"));
		PrintStream p_out = new PrintStream(f_out);
		
		while(true){
			String line=br.readLine();
			if(line==null||line.isEmpty())
				break;
			int pos=line.indexOf(" ");
			String imgName = line.substring(0, pos) ;
			String fileName=imgPath+"/"+imgName;
			System.out.println(fileName);
			FileInputStream is = new FileInputStream(new File(fileName));
			byte[] img = new byte[is.available()];
			is.read(img);
			try {
				//load features
				double[] fea = ServiceWrapper.getFeature(img, true, _ip, _port, _timeout);
				if(fea!=null){
					_feas.put(imgName, fea);
					p_out.print(imgName+" ");
					for(int k=0; k<fea.length; k++){
						p_out.print(fea[k]+" ");
					}
					p_out.print("\r");
					//System.out.println(000);
				}
				else{
					System.out.println(line.substring(0,pos)+" cannot be detected");
				}

			} catch (TException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			is.close();
			
		}
		f_out.close();
		p_out.close();
		br.close();
		fs.close();
	};
	
	HashMap<String, double[]> GetFeatures(){
		return _feas ;
	}
		
}
