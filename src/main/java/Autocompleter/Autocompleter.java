package Autocompleter;

import java.util.List;

/**
 * @author leest_000
 *
 * A module that will suggest words given a word fragment.
 * To improve suggestions, this module can be trained by providing 
 * passages.
 */
public interface Autocompleter {
	/**
	 * Returns a List of possible words that
	 * could be used to autocomplete fragment based
	 * on passages used to train this instance of {@link Autocompleter}.
	 */
	public List<Candidate> getWords(String fragment);
	
	
	/**
	 * Updates the backing vocabulary by adding words from
	 * passage, increasing the
	 * confidence of commonly used words.
	 */
	public void train(String... passage);
	
}
