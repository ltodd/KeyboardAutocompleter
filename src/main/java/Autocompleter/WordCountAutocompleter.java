/**
 * 
 */
package Autocompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leest_000
 *
 * {@link Autocompleter} implementation based on word counts from training
 * passages.
 * 
 * An instance of {@link WordCountAutocompleter} will recommend autocompletion
 * {@link Candidate}s with a confidences based on their number of occurrences in
 * every passage used to train it.
 * 
 */
public class WordCountAutocompleter implements Autocompleter {

	/**
	 * A lexicographically sorted map of all the words ever used to train this
	 * instance of {@link WordCountAutocompleter}.
	 */
	TreeMap<String, Integer> words = new TreeMap<String, Integer>();
	
	/**
	 * Returns a List of possible words that
	 * could be used to autocomplete fragment based on 
	 * the word count of passages used to train this 
	 * instance of {@link WordCountAutocompleter}.
	 * 
	 * Words are all treated as lowercase
	 * 
	 * Candidates are sorted by confidence (highest to lowest)
	 */
	public List<Candidate> getWords(String fragment) {
		List<Candidate> candidates = new ArrayList<Candidate>();
		
		// If there's something wrong, we'll just return an empty list of candidates
		if(fragment != null && fragment.length() > 0 && words.size() > 0) {
			// I don't care about casing
			fragment = fragment.toLowerCase();
			
			// This is pretty fun here.  I'm going to increment the last char
			// of the fragment by 1 to get the first fragment lexicographically
			// after ALL potiential words based on the given fragment
			int endIndex = fragment.length() -1;
			char nextChar = (char) ((byte) fragment.charAt(endIndex) + 1);
			String nextFrag = fragment.substring(0, endIndex) + nextChar;
			
			// Now, I'll use fragment and nextFrag as a range to get all 
			// possible words based on the given fragment
			Stream<Candidate> candidateStream = words.subMap(fragment, nextFrag)
					.entrySet().stream().map( e -> {return new Candidate(e.getKey(), e.getValue());});
			
			// sort by confidence (highest first)
			candidates = candidateStream.sorted(
					(c1, c2) ->	Integer.compare(c2.getConfidence(), c1.getConfidence()))
					.collect(Collectors.toList());
		}
		
		return candidates;
	}


	/**
	 * Updates the backing vocabulary by adding words from
	 * passage, increasing the
	 * confidence of commonly used words.
	 * 
	 * A single instance of a word increases it's confidence
	 * by 1.
	 */
	public void train(String... passage) {
		String joinedPassage = String.join(" ", passage);
		StringTokenizer st = new StringTokenizer(joinedPassage, " \t\n\r\f.,;");
		String word;
		int count;
		
		while(st.hasMoreTokens()) {
			word = st.nextToken().toLowerCase();
			count = 1;
			if(words.containsKey(word)){
				count += words.get(word);
			}
			
			words.put(word, count);
		}
	}
	
	
	
	public static void main(String[] args) {
		ArrayList<String> passages = new ArrayList();
		Autocompleter autocompleter = new WordCountAutocompleter();

    	System.out.println("Welcome.  Please choose a command:");
	    Scanner scanner = new Scanner(System.in);
		while(true) {
		    System.out.println("train or auto?");
		    String command = scanner.nextLine();
		    if(command.toLowerCase().equals("train")) {
		    	System.out.println("What should I learn?");
		    	autocompleter.train(scanner.nextLine());
		    } else if(command.toLowerCase().equals("auto")) {
		    	System.out.println("Enter fragment:");
				List<Candidate> candidates = autocompleter.getWords(scanner.next());
				scanner.nextLine();
				System.out.println(candidates);
		    } else {
		    	System.out.println("That is not an exceptable command.  Try \"train\" or \"auto\"");
		    }
		}
	}
}
