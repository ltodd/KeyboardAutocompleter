/**
 * 
 */
package Autocompleter;

/**
 * @author leest_000
 *
 * A word to be suggested for the autocompletion of an unfinished word.
 * Contains {@link String} word and int confidence.
 */
public class Candidate {
	private String word;
	
	/**
	 * Confidence represents how likely this word is a suitable autocompletion 
	 * for a given word fragment.
	 * 
	 * Higher confidence means the word is more likely correct.
	 */
	private int confidence;
	
	public Candidate(String word, int confidence) {
		this.word = word;
		this.confidence = confidence;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getConfidence() {
		return confidence;
	}
	
	public String toString() {
		return String.format("\"%s\" (%d)", word, confidence);
	}
}
