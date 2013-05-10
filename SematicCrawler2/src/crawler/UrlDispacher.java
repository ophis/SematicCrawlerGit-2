package crawler;

import java.util.HashMap;

import Jama.*;

public class UrlDispacher {
	public HashMap<Integer, String> dispach(String url) {
		HashMap<Integer, String> result = new HashMap<Integer, String>();

		return result;
	}

	public Matrix getUrlMatrix(String urls) {
		String urlArray[] = urls.split(";");
		int size = urlArray.length;
		double vals[][] = new double[size][4];
		for(int i=0;i<urlArray.length;i++){
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
				urldouble[2] = Hash(s2[0]);//Path
				if (s2.length > 1) {
					urldouble[3] = Hash(s2[1]);//Query+Fragment
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

	public static void main(String args[]) {
		UrlDispacher urlDispacher = new UrlDispacher();
		urlDispacher.getUrlMatrix("http://www.taobao.com/over/there?asdfasdf;http://www.taobao.com/over/there?asdfasdf;http://www.taobao.com/over/there?asdfasdf");

	}
}
