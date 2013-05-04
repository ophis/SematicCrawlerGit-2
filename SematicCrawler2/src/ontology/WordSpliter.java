package ontology;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class WordSpliter {
	
	public ArrayList<Lexeme> split(String text){
		ArrayList<Lexeme> result = new ArrayList<Lexeme>();
		IKSegmenter ikSeg = new IKSegmenter(new StringReader(text), true);
		try {
			Lexeme l = null;
			while( (l = ikSeg.next()) != null){
				result.add(l);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public HashMap<String, Integer> frequency(ArrayList<String> words){
		HashMap<String, Integer> wordFrequency = new HashMap<String,Integer>();
		for(Iterator<String> iterator = words.iterator();iterator.hasNext();){
			String word = iterator.next();
			if(wordFrequency.containsKey(word)){
				wordFrequency.put(word, wordFrequency.get(word)+1);
			}
			else {
				wordFrequency.put(word, 1);
			}
		}
		
		return wordFrequency;
	}
	
	public List<Entry<String, Integer>> orderFrequency(HashMap<String, Integer> frequency) {
		List<Map.Entry<String, Integer>> infoIds =
			    new ArrayList<Map.Entry<String, Integer>>(frequency.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
		        return (o2.getValue() - o1.getValue()); 
		        //return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		});
		return infoIds;
	}
}
