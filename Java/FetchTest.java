import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FetchTest {

	public static void main(String[] args) {
		FetchTest ft = new FetchTest();
		String url = "http://www.open-open.com/jsoup/";
		
		Map<String,String> map = ft.getLinks(url);
		Set<Entry<String,String>> entrys = map.entrySet();
		for(Entry<String,String> entry : entrys){
			System.out.println("name = " + entry.getKey() + "   value = " + entry.getValue());
		}
	}
	
	public Map<String,String> getLinks(String url){
		Map<String,String> map = new LinkedHashMap<String,String>();
		
		Connection conn = Jsoup.connect(url);
		conn.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
		try {
			Document doc = conn.get();
//			Elements headLinks = doc.select("head link,head script");
//			for(Element link : headLinks){
//				if(link.hasAttr("href") && !"".equals(link.attr("href"))){
//					map.put(link.attr("href"), link.absUrl("href"));
//					downFile(link.absUrl("href"), null);
//				}else if(link.hasAttr("src") && !"".equals(link.attr("src"))){
//					map.put(link.attr("src"), link.absUrl("src"));
//					downFile(link.absUrl("src"), null);
//				}
//			}
			Elements as = doc.select(".toc a");
			for(Element a : as){
				if(a.hasAttr("href") && a.attr("href") != null && !"".equals(a.attr("href"))
						&& a.hasText() && a.text() != null && !"".equals(a.text())){
					map.put(a.text(), a.absUrl("href"));
					
					File file = new File("D:/doc/"+a.text()+".html");
					BufferedWriter out = null;
					try{
						out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
						out.write(Jsoup.connect(a.absUrl("href")).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36").get().toString());
						out.flush();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if(out != null){
							out.close();
							out = null;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void downFile(String url, File outFile){
		if(outFile == null){
			if(url != null && !"".equals(url)){
				String fileName = url.substring(url.lastIndexOf("/"));
				String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
				if("css".equals(fileSuffix)){
					outFile = new File("D:/doc/css/"+fileName);
				}else if("js".equals(fileSuffix)){
					outFile = new File("D:/doc/js/"+fileName);
				}else{
					return;
				}
				BufferedWriter out = null;
				try {
					Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36").get();
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
					out.write(doc.toString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally{
					if(out != null){
						try {
							out.close();
							out = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				return;
			}
			
		}
		
	}
}
