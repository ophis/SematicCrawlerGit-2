package crawler;

import java.util.ArrayList;
import java.util.HashMap;

import Jama.*;

public class UrlDispacher {
	public HashMap<Integer, String> dispach(String url) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();

		return result;
	}

	public Matrix getUrlMatrix(String urlArray[]) {
		int size = urlArray.length;
		double vals[][] = new double[size][4];
		for (int i = 0; i < urlArray.length; i++) {
			vals[i] = getDouble(urlArray[i]);
		}
		return new Matrix(vals);
	}

	public double[] getDouble(String url) {
		double[] urldouble = new double[4];
		String[] s1 = url.split("://");
		urldouble[0] = Hash(s1[0]);// Shceme
		if (s1.length > 1) {
			int slashP = s1[1].indexOf("/");
			String Domain = s1[1].substring(0, slashP);
			String Remain = s1[1].substring(slashP + 1);
			urldouble[1] = Hash(Domain);// Domain
			if (Remain.length() > 1) {
				String[] s2 = Remain.split("\\?");
				urldouble[2] = Hash(s2[0]);// Path
				if (s2.length > 1) {
					urldouble[3] = Hash(s2[1]);// Query+Fragment
				}
			}
		}
		return urldouble;
	}

	public double Hash(String s) {
		double r = 0;
		for (int i = 0; i < s.length(); i++) {
			r += s.charAt(i);
		}
		return r;
	}

	public ArrayList<ArrayList<String>> Clustering(String urls) {
		String urlArray[] = urls.split(";");
		Matrix originM = getUrlMatrix(urlArray);
		originM.print(0, 1);
		// standardization matrix
		int col = originM.getColumnDimension();
		int row = originM.getRowDimension();
		double max[] = new double[col];
		for (int i = 0; i < col; i++) {
			double maxE = 0;
			for (int j = 0; j < row; j++) {
				maxE = maxE > originM.get(j, i) ? maxE : originM.get(j, i);
			}
			max[i] = maxE;
		}
		double min[] = new double[col];
		for (int i = 0; i < col; i++) {
			double minE = originM.get(0, i);
			for (int j = 0; j < row; j++) {
				minE = minE > originM.get(j, i) ? originM.get(j, i) : minE;
			}
			min[i] = minE;
		}
		double maxval[][] = new double[row][col];
		double minval[][] = new double[row][col];
		for (int i = 0; i < row; i++) {
			minval[i] = min;
			maxval[i] = max;
		}
		Matrix maxMatrix = new Matrix(maxval);
		Matrix minMatrix = new Matrix(minval);
		originM.minusEquals(minMatrix);
		Matrix tempMatrix = maxMatrix.minus(minMatrix);
		originM.arrayRightDivideEquals(tempMatrix);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (Double.isNaN(originM.get(i, j)))
					originM.set(i, j, 1);
			}
		}
		//TODO 
		originM.print(0, 3);
		double weight[] = { 0.1, 0.4, 0.35, 0.15 };
		double symmetryVal[][] = new double[row][row];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < row; j++) {
				for (int m = 0; m < weight.length; m++) {
					double temp = weight[m]
							* (Math.min(originM.get(i, m), originM.get(j, m)) / Math
									.max(originM.get(i, m), originM.get(j, m)));
					symmetryVal[i][j] += Double.isNaN(temp) ? weight[m] : temp;	
				}
//				symmetryVal[i][j] = symmetryVal[i][j] > 0.7 ? 1 : 0;
			}
		}
		//TODO
		Matrix matrix = new Matrix(symmetryVal);
		matrix.print(0, 3);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < row; j++) {
				symmetryVal[i][j] = symmetryVal[i][j] > 0.7 ? 1 : 0;
			}
		}
		//TODO
		matrix.print(0, 3);
		boolean flag[] = new boolean[row];
		for (int i = 0; i < flag.length; i++) {
			flag[i] = false;
		}

		ArrayList<ArrayList<String>> urlGroup = new ArrayList<ArrayList<String>>();
		double var = 1;
		for (int i = 0; i < row; i++) {
			if (!flag[i]) {
				ArrayList<String> urlsArrayList = new ArrayList<String>();
				for (int j = 0; j < row; j++) {
					if (symmetryVal[j][i] == var) {
						urlsArrayList.add(urlArray[j]);
						flag[j]=true;
					}
				}
				if (urlsArrayList.size() > 0)
					urlGroup.add(urlsArrayList);
			}	
		}

		return urlGroup;
	}

	public static void main(String args[]) {
		UrlDispacher urlDispacher = new UrlDispacher();
		//ArrayList<ArrayList<String>> urlGroup = urlDispacher.Clustering("https://www.taasdfbao.co/over/thaaaa222ere?asfasdf;hdttp://wadsfw.taoabao.co/ovdfasder/tfffffffhere?asfaffeeeeqdf;http://www.taobao.ssco/ovffffefasdfasdfasdfasdffffr/there?asfasdf");
		String url = "http://detail.tmall.com/item.htm?id=12456408701;http://secure.verycd.com/account/realid?continue=http%3A%2F%2Fsecure.verycd.com;http://www.njzwfw.gov.cn/webpage/xwdt_info.jsp?doc_id=11287;https://detail.tmall.com/item.htm?id=12456408703";
		//double d[] = urlDispacher.getDouble(url);
		urlDispacher.Clustering(url);
	}
}
