/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;


/**
 * Velocity of a PSO particle 
 * @see https://en.wikipedia.org/wiki/Particle_swarm_optimization
 *
 */
public class Velocity {

	/**
	 * Transition velocity vector 
	 */
	private Map<Integer, DoubleTransitionTable> transitions; 
	
	/**
	 * Accepted states velocity vector 
	 */
	private Map<Integer, Double> acceptedStates; 
	
	/**
	 * Initial transition velocity boundary
	 */
	private double initialStateMaxAbs; 
	
	/**
	 * Initial acceptance velocity boundary 
	 */
	private double initialAcceptedMaxAbs; 
	
	/**
	 * Acceptance boundary
	 */
	private double acceptedMax; 
	
	/**
	 * Set of states 
	 */
	private Set<Integer> states; 
	
	/**
	 * Set of inputs 
	 */
	private Set<Integer> inputs; 
	
	/**
	 * Random values generator 
	 */
	private Random random;
	
	
	/**
	 * Initialises a new velocity object for the PSO metaheuristic particle 
	 * 
	 * @param states set of states 
	 * @param inputs set of inputs 
	 * @param initialStateMaxAbs initial transition velocity boundary 
	 * @param initialAcceptedMaxAbs initial acceptance velocity boundary 
	 * @param acceptedMax acceptance velocity boundary 
	 */
	public Velocity(Set<Integer> states, Set<Integer> inputs, 
			double initialStateMaxAbs, double initialAcceptedMaxAbs, double acceptedMax) { 
		this.initialStateMaxAbs = initialStateMaxAbs; 
		this.initialAcceptedMaxAbs = initialAcceptedMaxAbs; 
		this.transitions = new TreeMap<Integer, DoubleTransitionTable>(); 
		this.acceptedStates = new TreeMap<Integer, Double>(); 
		this.states = states; 
		this.inputs = inputs; 
		this.random = SingletonRandom.getRandom(); 
		this.acceptedMax = acceptedMax; 
		initTransitions(); 
		initAcceptedStates(); 
	}
	
	
	
	/**
	 * Initialises the transitions velocity vector 
	 */
	private void initTransitions() { 
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = new DoubleTransitionTable(
					states, "Transition table for input "+input, -2, 2); 
			this.transitions.put(input, table); 
		}
	} 
	
	
	/**
	 * Initialises the acceptance velocity vectors 
	 */
	private void initAcceptedStates() { 
		for(Integer state: this.states) { 
			this.acceptedStates.put(state, Double.valueOf(-1)); 
		} 
	} 
	
	
	/**
	 * Randomises all the elements of the velocity vector 
	 * 
	 */
	public void randomise() { 
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = this.transitions.get(input); 
			table.randomiseNonRounded(this.initialStateMaxAbs); 
		} 
		for(Integer state: this.states) { 
			double newAccepted = random.nextDouble() * this.initialAcceptedMaxAbs; 
			if(random.nextBoolean()) {
				newAccepted = -newAccepted; 
			}
			this.acceptedStates.put(state, newAccepted); 
		}
	} 
	
	
	@Override 
	public String toString() { 
		StringBuilder builder = new StringBuilder(); 
		builder.append("Velocity "); 
		for(Integer input: this.inputs) { 
			builder.append(this.transitions.get(input)); 
			builder.append("\n"); 
		} 
		builder.append("\nAccepted: \n"); 
		for(Integer state: this.states) { 
			builder.append(state); 
			builder.append(":\t"); 
			builder.append(this.acceptedStates.get(state)); 
			builder.append("\n"); 
		} 
		return builder.toString(); 
	}



	/**
	 * Updates the velocity 
	 * 
	 * @param params PSO parameters 
	 * @param current current solution 
	 * @param currentBest best solution found by the current particle 
	 * @param bestSoFar best solution found so far by all particles 
	 */
	public void update(PSOParams params, Solution current, Solution currentBest, Solution bestSoFar) {
		updateTransitions(params, current, currentBest, bestSoFar); 
		updateAccepted(params, current, currentBest, bestSoFar); 
	}


	/**
	 * Moves the acceptance vectors position 
	 *
	 * @param params PSO parameters 
	 * @param current current solution 
	 * @param currentBest best solution found by the current particle 
	 * @param bestSoFar best solution found so far by all particles 
	 */
	private void updateAccepted(PSOParams params, Solution current, 
			Solution currentBest, Solution bestSoFar) {
		for(Integer state: this.states) { 
			double currentAccepted = current.getAccepted(state); 
			double currentVelocity = this.acceptedStates.get(state); 
			double personalBest = currentBest.getAccepted(state); 
			double globalBest = bestSoFar.getAccepted(state); 
			
			double personalBestVector = personalBest - currentAccepted; 
			double globalBestVector = globalBest - currentAccepted; 
			
			double newVelocity = random.nextDouble() * params.getVelWeight() * currentVelocity + 
					random.nextDouble() * params.getPersonalWeight() * personalBestVector + 
					random.nextDouble() * params.getGlobalWeight() * globalBestVector; 
			if(Math.abs(newVelocity) > this.acceptedMax) { 
				if(newVelocity < 0) { 
					newVelocity = -this.acceptedMax; 
				} else { 
					newVelocity = this.acceptedMax; 
				}
			} 
			this.acceptedStates.put(state, newVelocity); 
		}
	}



	/**
	 * Moves the transition vector 
	 * 
	 * @param params PSO parameters 
	 * @param current current solution 
	 * @param currentBest best solution found by the current particle 
	 * @param bestSoFar best solution found so far by all particles 
	 */
	private void updateTransitions(PSOParams params, Solution current, Solution currentBest, Solution bestSoFar) {
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = this.transitions.get(input); 
			for(Integer state: this.states) { 
				double currentSolution = current.getNextValue(state, input); 
				double currentVelocity = table.getNext(state); 
				double personalBest = currentBest.getNextValue(state, input); 
				double globalBest = bestSoFar.getNextValue(state, input); 
				
				double personalBestVector = personalBest - currentSolution; 
				double globalBestVector = globalBest - currentSolution; 
				
				double newVelocity = random.nextDouble() * params.getVelWeight() * currentVelocity + 
						random.nextDouble() * params.getPersonalWeight() * personalBestVector + 
						random.nextDouble() * params.getGlobalWeight() * globalBestVector; 
				table.set(state, newVelocity);
			} 
		} 
	}


	/**
	 * Gets the next state for this transitions vector 
	 * 
	 * @param state current state 
	 * @param input current input 
	 * @return next state, encoded as a <code>double</code> 
	 */
	public double getNextValue(Integer state, Integer input) {
		double next = this.transitions.get(input).getNext(state); 
		return next;
	}


	/**
	 * Gets the acceptance of the current state 
	 * 
	 * @param state current state 
	 * @return acceptance of the current state, encoded as a double 
	 */
	public double getAccepted(Integer state) {
		double accepted = this.acceptedStates.get(state); 
		return accepted; 
	}

}
