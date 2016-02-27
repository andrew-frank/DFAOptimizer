package pl.dfa.learner.automaton.pso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Particle swarm optimisation (PSO) for learning the deterministic 
 * finite automata based on the learning set of inputs divided into 
 * accepted and unaccepted. 
 * 
 * @see https://en.wikipedia.org/wiki/Particle_swarm_optimization 
 *
 */
public class PSO {

	/**
	 * PSO parameters 
	 */
	private PSOParams params; 
	
	/**
	 * Evaluator used to evaluate solutions found by the swarm 
	 */
	private Evaluator evaluator; 
	
	/**
	 * The swarm 
	 */
	private List<Particle> particles; 
	
	/**
	 * Best solution found so far 
	 */
	private Solution bestSoFar; 
	
	/**
	 * Best solutions so far for different state number 
	 */
	private Map<Integer, Solution> bestPerStateNum; 
	
	/**
	 * Listeners for integration with other components (most specifically, the GUI): 
	 */
	private List<PSOEventListener> listeners; 
	
	/**
	 * Logger 
	 */
	private static Logger logger = LogManager.getLogger(PSO.class); 
	
	
	/**
	 * Initialises the algorithm based on the parameters 
	 * @param params algorithm parameters
	 */
	public PSO(PSOParams params) { 
		this.params = params; 
		this.particles = new ArrayList<Particle>(params.getParticlesCount()); 
		this.bestPerStateNum = new HashMap<Integer, Solution>(); 
		this.listeners = new ArrayList<PSOEventListener>(); 
	} 
	
	
	/**
	 * Executes the PSO optimisation metaheuristic. 
	 * 
	 * @param inputs allowed DFA inputs 
	 * @param wordSet training set 
	 * @return search results for this run of the optimisation 
	 */
	public Results search(Set<Integer> inputs, WordSet wordSet) { 
		for(PSOEventListener listener: this.listeners) { 
			listener.searchStarted(); 
		}
		long startTime = System.currentTimeMillis(); 
		Results results = new Results(5); 
		this.evaluator = new Evaluator(wordSet); 
		initParticles(inputs); 
		evaluateParticles(results); 
		logger.info(getStatistics(this.particles));
		
		// search loop 
		int iterations = this.params.getMaxIterations(); 
		
		for(int i = 0; i < iterations; i++) { 
			if(i%50 == 0) { 
//				logger.info("Iteration "+i); 
				for(PSOEventListener listener: this.listeners) { 
					listener.searchUpdate(i, 
							MathUtil.getAverageEvaluation(this.particles), 
							MathUtil.getBestEval(this.particles), 
							MathUtil.getWorstEval(this.particles), 
							this.bestSoFar.getEvaluation());
				}
			}
			updateSpeeds(); 
			moveParticles(); 
			evaluateParticles(results); 
			if(i%50 == 0) { 
				logger.info(getStatistics(this.particles)); 
			} 
			
			if(this.bestSoFar.getEvaluation() == 0) { 
				break; 
			} 
			if(System.currentTimeMillis() - startTime > this.params.getAllowedTimeMillis()) { 
				break; 
			}
		} 
		long finishTime = System.currentTimeMillis(); 
		logger.info("Processing time: "+((double)finishTime - (double)startTime)/1000+" s. "); 
		for(PSOEventListener listener: this.listeners) { 
			listener.searchFinished(results, wordSet); 
		}
		return results; 
	} 


	/**
	 * Performs movements of all particles in the swarm 
	 */
	private void moveParticles() {
		for(Particle particle: this.particles) { 
			particle.performMovement(); 
		}
	}


	/**
	 * Updates speeds for all particles in the swarm. 
	 */
	private void updateSpeeds() {
		for(Particle particle: this.particles) { 
			logger.trace("Velocity "+particle.getVelocity());
			particle.updateVelocity(this.params, this.bestPerStateNum.get(particle.getStateNum())); 
			logger.trace("Velocity updated to: "+particle.getVelocity()); 
		} 
	} 


	/**
	 * Evaluates all the particles in the swarm. In case of improvements in 
	 * the best found so far, the new best solution is added to the results. 
	 * @param results results to update in case of the improved solution being found 
	 */
	private void evaluateParticles(Results results) { 
		double best = Double.MAX_VALUE; 
		if(this.bestSoFar != null && 
				this.bestSoFar.getEvaluation() < best) { 
			best = this.bestSoFar.getEvaluation(); 
		} 
		for(Particle particle: this.particles) { 
			double evaluation = particle.evaluate(evaluator); 
			if(evaluation < best) { 
				logger.info("Found new best "+evaluation); 
				best = evaluation; 
				this.bestSoFar = particle.getSolution(); 
				results.addResult(particle.getSolution());
				for(PSOEventListener listener: this.listeners) { 
					listener.foundNewBest(best); 
				}
			} 
			Integer stateNum = particle.getStateNum(); 
			if(this.bestPerStateNum.get(stateNum) == null) { 
				this.bestPerStateNum.put(stateNum, particle.getSolution()); 
			} else { 
				if(evaluation < this.bestPerStateNum.get(stateNum).getEvaluation()) { 
					this.bestPerStateNum.put(stateNum, particle.getSolution()); 
				}
			}
		} 
	} 
	
	
	/**
	 * Adds listener to this PSO object. 
	 * @param listener PSOListener 
	 */
	public void addListener(PSOEventListener listener) { 
		this.listeners.add(listener); 
	}
	
	
	/**
	 * Gets statistics of the swarm in an easy to read form. 
	 * 
	 * @param toCheck swarm to calculate statistics 
	 * @return statistics of the swarm 
	 */
	private String getStatistics(List<Particle> toCheck) {
		double average = MathUtil.getAverageEvaluation(toCheck); 
		double min = MathUtil.getBestEval(toCheck); 
		double max = MathUtil.getWorstEval(toCheck); 
		return "Average "+average+", min "+min+", max "+max+", best so far "+this.bestSoFar.getEvaluation(); 
	} 


	/**
	 * Initialises the swarm to random locations (solutions). 
	 * @param inputs set of allowed inputs to the DFA 
	 */
	private void initParticles(Set<Integer> inputs) { 
		this.particles.clear(); 
		for(int i = 0; i < this.params.getParticlesCount(); i++) { 
			Solution solution = new Solution(20, inputs); 
			solution.randomiseNonRounded(); 
			this.particles.add(new Particle(solution)); 
		}
		
	}
	
}
