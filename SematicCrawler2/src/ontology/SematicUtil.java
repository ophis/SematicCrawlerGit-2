package ontology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.core.Lexeme;

import dal.OntologyDAL;



public class SematicUtil {
	//lexemeType常量
    public static final int CN_WORD = 4;

    public static final int CN_CHAR = 64;

    public static final int ENGLISH = 1;

    public static final int CN_QUAN = 48;

    public static final int ARABIC = 2;

    public static final int LETTER = 3;

    
	public HashMap<String, Integer> extractKeyWordsFromText(String text){
		WordSpliter ws = new WordSpliter();
		ArrayList<Lexeme> wordsLexeme = ws.split(text);
		ArrayList<String> wordsArray = new ArrayList<String>();
		for(Lexeme l:wordsLexeme){
//			System.out.println(l.getLexemeText()+":"+l.getLexemeType());
//			System.out.println(l.toString());
			if(l.getLexemeType()!=LETTER){
				wordsArray.add(l.getLexemeText());
			}
		}
		return extractKeyWords(wordsArray);
	}
	
	private static final double KeyWordsRatio=0.2;
	public HashMap<String, Integer> extractKeyWords(ArrayList<String> wordsArray){
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		for(String word : wordsArray){
			if(words.containsKey(word)){
				words.put(word, words.get(word)+1);
			}
			else if(word.length() < word.getBytes().length){
				words.put(word, 1);
			}
		}
		HashMap<String, Integer> keyWords = new HashMap<String, Integer>();
		int keyWordsInt = (int) (words.size()*KeyWordsRatio);
		WordSpliter wSpliter = new WordSpliter();
		List<Map.Entry<String,Integer>> wordFrequency = wSpliter.orderFrequency(words);
		
		int i=0;
		for(Iterator<Map.Entry<String, Integer>> iterator = wordFrequency.iterator();iterator.hasNext() && i<keyWordsInt;i++){
			Map.Entry<String, Integer> entry= iterator.next();
			keyWords.put(entry.getKey(), entry.getValue());
		}
		return keyWords;
	}

    private static final double upperlimit = 1;
    private static final double lowlimit = 0.4;
	public boolean related(String type, HashMap<String, Integer> keyWords){
		double cosin = cos(type, keyWords);
		if(cosin>lowlimit && cosin<upperlimit){
			return true;
		}
		return false;
	}
	
	public double cos(String type, HashMap<String, Integer> keyWords){
		OntologyDAL ontoDal = new OntologyDAL();
		HashMap<String, Integer> wordRights = ontoDal.getRights(type, keyWords.keySet());
		ontoDal.closeConnection();
		double dotProduct = 0;
		for(String word : keyWords.keySet()){
			dotProduct += keyWords.get(word)*wordRights.get(word);
		}
		if(dotProduct==0){
			return 0;
		}
		else 
		{
			double MProduct = M(keyWords.values())*M(wordRights.values());
			return dotProduct/MProduct;
		}
	}
	
	public double M (Collection<Integer> coordinate){
		double product = 0;
		for(Integer i : coordinate){
			product+= (i*i);
		}
		return Math.sqrt(product);
	}
	
	//test
	public static void main(String args[]){
		SematicUtil smu = new SematicUtil();
		HashMap<String, Integer> keyWords = new HashMap<String, Integer>();
		keyWords.put("用药", 3);
		keyWords.put("剩余", 2);
		keyWords.put("dsfa", 8);

		double cosin = smu.cos("电子商务", keyWords);
		System.out.println(cosin);
	}
}
