/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representation of a particle in the particle swarm optimisation metaheuristic. 
 * Each particle keeps track of it's current position (solution), best position (solution) 
 * and the current velocity. 
 * 
 * @see https://en.wikipedia.org/wiki/Particle_swarm_optimization
 *
 */
public class Particle {

	/**
	 * Particle velocity 
	 */
	private Velocity velocity; 
	
	/**
	 * Current position (solution) 
	 */
	private Solution current; 
	
	/**
	 * Best position (solution) that this particle ever was in 
	 */
	private Solution bestLocal; 
	
	/**
	 * logger object 
	 */
	private static final Logger logger = LogManager.getLogger(Particle.class); 
	
	/**
	 * New particle based on an initial solution. 
	 * @param solution initial solution 
	 */
	public Particle(Solution solution) { 
		this.current = solution; 
		this.bestLocal = solution; 
		this.velocity = new Velocity(solution.getStates(), solution.getInputs(), 1, 0.2, 0.4); 
		this.velocity.randomise(); 
	} 
	
	
	/**
	 * Creates a new particle with defined speed boundaries 
	 * 
	 * @param solution initial solution 
	 * @param maxTransitionSpeed maximum speed for transition changes 
	 * @param maxAcceptedSpeed maximum speed of acceptable states changes 
	 */
	public Particle(Solution solution, double maxTransitionSpeed, double maxAcceptedSpeed) { 
		this.current = solution; 
		this.bestLocal = solution; 
		this.velocity = new Velocity(solution.getStates(), solution.getInputs(), 
				maxTransitionSpeed, maxAcceptedSpeed, maxAcceptedSpeed); 
		this.velocity.randomise(); 
	} 
	
	
	/**
	 * Gets the current solution for this particle 
	 * @return current solution 
	 */
	public Solution getSolution() { 
		return this.current; 
	}


	/**
	 * Gets the current velocity for this particle 
	 * @return current velocity
	 */
	public Velocity getVelocity() { 
		return this.velocity;
	}


	/**
	 * Gets the best position (solution) that this particle ever was. 
	 * @return best solution for this particle 
	 */
	public Solution getParticleBest() { 		
		return this.bestLocal; 
	}


	/**
	 * Updates the velocity for this solution as defined in the PSO metaheuristic. 
	 * 
	 * @param params velocity change parameters 
	 * @param bestSoFar best solution found so far by the entire swarm 
	 */
	public void updateVelocity(PSOParams params, Solution bestSoFar) { 
		if(bestSoFar == null) { 
			logger.warn("Best so far for "+this.getStateNum()+" is null. "); 
			return; 
		}
		this.velocity.update(params, this.current, this.bestLocal, bestSoFar); 
		
	}


	/**
	 * Updates the position of this particle 
	 */
	public void performMovement() { 
		Solution newSolution = new Solution(this.current); 
		for(Integer input: this.current.getInputs()) { 
			for(Integer state: this.current.getStates()) { 
				double next = this.current.getNextValue(state, input); 
				double speed = this.velocity.getNextValue(state, input); 
				next += 0.25 * speed; 
				newSolution.setTransition(state, input, next);
			} 			
		} 
		for(Integer state: this.current.getStates()) { 
			double accepted = this.current.getAccepted(state); 
			double speed = this.velocity.getAccepted(state); 
			double newAccepted = accepted + (0.75 * speed); 
			newSolution.setAccepted(state, newAccepted); 
		} 
		this.current = newSolution; 
	}


	/**
	 * Evaluates current and best solutions found by this particle 
	 * @param evaluator evaluator to use for evaluations 
	 * @return current solution evaluation 
	 */
	public double evaluate(Evaluator evaluator) {
		evaluator.evaluate(this.current); 
		if(this.current.getEvaluation() < this.bestLocal.getEvaluation()) { 
			this.bestLocal = this.current; 
		}
		return this.current.getEvaluation(); 
	} 
	
	
	public String toString() { 
		return this.current.toString()+"\nvelocity: \n"
				+this.velocity.toString(); 
	}


	/**
	 * Gets the number of states
	 * @return
	 */
	public Integer getStateNum() {
		return this.current.getStateNumber();
	}
	
	
}
