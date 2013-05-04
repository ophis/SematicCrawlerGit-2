package crawler;

import dal.OntologyDAL;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import ontology.SematicUtil;

import org.apache.http.Header;


public class Crawler extends WebCrawler{
	//filt none html links
	private static final Pattern filters = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	public static String OntologyType;
	
	@Override
	public boolean shouldVisit(WebURL url){
		String href = url.getURL().toLowerCase();
		if (filters.matcher(href).matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page){
		Header[] responseHeaders = page.getFetchResponseHeaders();
		System.out.println(page.getContentType());
		String charset = "utf-8";
		if (responseHeaders != null) {
			System.out.println("Response headers:");
			for (Header header : responseHeaders) {
//				System.out.println("\t" + header.getName() + ": " + header.getValue());
				if(header.getName().equals("Content-Type")){
					System.out.println("\t" + header.getName() + ": " + header.getValue());
					String contentType = header.getValue();
					int charsetIndex = contentType.indexOf("charset=");
					if(charsetIndex>0){
						charset = contentType.substring(charsetIndex+"charset=".length(), contentType.length());
						charset = charset.equals("gb2312") ? "GBK" : "utf-8";
						System.out.println("\tcharset: " + charset);
					}
				}
			}
		}
		
//		int docid = page.getWebURL().getDocid();
//		String url = page.getWebURL().getURL();
//		String domain = page.getWebURL().getDomain();
//		String path = page.getWebURL().getPath();
//		String subDomain = page.getWebURL().getSubDomain();
//		String parentUrl = page.getWebURL().getParentUrl();
		String text=null;
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}
		
		try {
				text = new String(text.getBytes(),charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println("Docid: " + docid);
//		System.out.println("URL: " + url);
//		System.out.println("Domain: '" + domain + "'");
//		System.out.println("Sub-domain: '" + subDomain + "'");
//		System.out.println("Path: '" + path + "'");
//		System.out.println("Parent page: " + parentUrl);
//		System.out.println("Text: " + text);
//		String url = page.getWebURL().getURL();
//		String hashedName2 = Cryptography.MD5(url) + ".txt";
//		IO.writeBytesToFile(text.getBytes(), "/Volumes/Mac Storage/crawlerTest/"+hashedName2);
		SematicUtil smu	= new SematicUtil();
		HashMap<String, Integer> keyWords = smu.extractKeyWordsFromText(text);
		OntologyDAL oDal = new OntologyDAL();
		oDal.addAll2Ontology(OntologyType, keyWords);
		System.out.println("=============");
	}
}
