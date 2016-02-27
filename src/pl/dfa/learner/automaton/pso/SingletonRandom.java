/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.Random;


/**
 * Singleton random number generator, to ensure all the objects 
 * share the same object (for randomisation quality enhancement): 
 * 
 * 
 *
 */
public class SingletonRandom {

	private static Random random; 
	
	private SingletonRandom() { 
		random = new Random(); 
	} 
	
	
	/**
	 * Gets the random number generator
	 * @return
	 */
	public static Random getRandom() { 
		if(random == null) { 
			random = new Random(); 
		} 
		return random; 
	}
}
