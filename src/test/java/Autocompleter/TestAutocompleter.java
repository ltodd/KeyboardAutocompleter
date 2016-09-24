package Autocompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Assert;
import org.junit.Test;

public class TestAutocompleter {

	@Test
	public void test() {
		List<String> passages = new ArrayList<String>();
		List<Candidate> candidates;
		
		Autocompleter autocompleter = new WordCountAutocompleter();
		
		//test untrained
		candidates = autocompleter.getWords("thi");
		Assert.assertTrue(candidates.isEmpty());
		Assert.assertTrue(checkAllPassages(passages, candidates));
		
		
		//test single arg
		passages.add("The third thing that I need to tell you is that this thing does not think thoroughly.");
		autocompleter.train(passages.get(0));
		candidates = autocompleter.getWords("th");
		Assert.assertTrue(checkAllPassages(passages, candidates));
		Assert.assertTrue(checkConfidence(candidates, "the", 1));
		Assert.assertTrue(checkConfidence(candidates, "that", 2));
		Assert.assertTrue(checkConfidence(candidates, "thoroughly", 1));
		Assert.assertTrue(!checkCandidacy(candidates, "thoroughly."));
		
		//test multi args
		passages.add("Hello, my name is... Hi, my name is.\n Who? My\n name is (chicka chicka) Slim Shady!\n Here's a contraction");
		autocompleter = new WordCountAutocompleter();
		autocompleter.train(passages.toArray(new String[passages.size()]));
		candidates = autocompleter.getWords("i");
		Assert.assertTrue(checkAllPassages(passages, candidates));
		Assert.assertTrue(checkConfidence(candidates, "is", 4));
		Assert.assertTrue(!checkCandidacy(candidates, "is."));
		
		//test contraction
		candidates = autocompleter.getWords("he");
		Assert.assertTrue(checkConfidence(candidates, "here's", 1));
		
		//test update
		String h = "Hi HI hi, hI hi-hi.";
		passages.add(h);
		autocompleter.train(h);
		candidates = autocompleter.getWords("h");
		Assert.assertTrue(checkAllPassages(passages, candidates));
		Assert.assertTrue(checkConfidence(candidates, "hi", 5));
		Assert.assertTrue(checkConfidence(candidates, "hi-hi", 1));
		

		candidates = autocompleter.getWords("z");
		Assert.assertTrue(checkAllPassages(passages, candidates));
		Assert.assertTrue(candidates.isEmpty());
		
		candidates = autocompleter.getWords("");
		Assert.assertTrue(checkAllPassages(passages, candidates));
		Assert.assertTrue(!checkCandidacy(candidates, ""));
		
		//test my treemap range
		autocompleter = new WordCountAutocompleter();
		autocompleter.train("th ti");
		candidates = autocompleter.getWords("th");
		Assert.assertTrue(!checkCandidacy(candidates, "ti"));
		
	}

	/**
	 * Check the confidences of all the candidates to see if they match the word counts 
	 */
	private boolean checkAllPassages(List<String> passages, List<Candidate> candidates) {
		String epic = String.join(" ", passages);
		
		int count;
		for(Candidate c : candidates) {
			count = 0;
			StringTokenizer st = new StringTokenizer(epic, " \t\n\r\f.,;");
			while(st.hasMoreTokens()) {
				if(c.getWord().toLowerCase().equals(st.nextToken().toLowerCase())) {
					count++;
				}
			}
			if(c.getConfidence() != count) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * check the confidence of a given word
	 */
	private boolean checkConfidence(List<Candidate> candidates, String word, int confidence) {
		for(Candidate c : candidates) {
			if(c != null && word.equals(c.getWord()) && c.getConfidence() == confidence){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check if the word is a candidate
	 */
	private boolean checkCandidacy(List<Candidate> candidates, String word) {
		for(Candidate c : candidates) {
			if(c != null && word.equals(c.getWord())){
				return true;
			}
		}
		return false;
	}
}
