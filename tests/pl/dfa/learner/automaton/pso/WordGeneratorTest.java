/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.dfa.learner.automaton.DFA;
import pl.dfa.learner.automaton.DFAFactory;

/**
 * Test of the word generator 
 *
 */
public class WordGeneratorTest {

	private static Logger logger = LogManager.getLogger(WordGeneratorTest.class); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		DFA dfa;
		try {
			dfa = DFAFactory.parseFromFile(new File("examples\\accepting.dfa")); 
			logger.info(dfa); 
			WordSetGenerator generator = new WordSetGenerator(dfa); 
			WordSet wordSet = generator.generateWordSet(10, 1000); 
			logger.info(wordSet.toString(true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}

}
