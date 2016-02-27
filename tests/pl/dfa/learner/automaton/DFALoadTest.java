/**
 * 
 */
package pl.dfa.learner.automaton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.dfa.learner.automaton.ComputeResults;
import pl.dfa.learner.automaton.DFAComputer;
import pl.dfa.learner.automaton.DFA;
import pl.dfa.learner.automaton.DFAFactory;

/**
 * DFA loading from file test 
 *
 */
public class DFALoadTest {

	private static Logger logger = LogManager.getLogger(DFALoadTest.class); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try { 
			logger.info("DFA load test. ");
			DFA dfa = DFAFactory.parseFromFile(new File("examples\\accepting.dfa")); 
			DFAComputer computer = new DFAComputer(dfa); 
			logger.info(dfa); 
			logger.info(dfa.toString(true));
			
			List<Integer> inputs = new ArrayList<Integer>(); 
			// test input 12112
			inputs.add(1); 
			inputs.add(2); 
			inputs.add(1);
			inputs.add(1);
			inputs.add(2); 
			ComputeResults results = computer.compute(inputs); 
			// get the results for the word 12112 
			logger.info("Final state: "+results); 
			
			List<Integer> inputs1 = new ArrayList<Integer>(); 
			inputs1.add(2); 
			inputs1.add(1); 
			inputs1.add(1); 
			inputs1.add(2); 
			inputs1.add(1); 
			
			computer.reset(); 
			results = computer.compute(inputs1); 
			// get the results for the word 21121
			logger.info("Final state: "+results); 
			
			// load another DFA 
			DFA nonAccepting = DFAFactory.parseFromFile(new File("examples\\nonAccepting.dfa")); 
			DFAComputer computerNonAccepting = new DFAComputer(nonAccepting); 
			logger.info(nonAccepting);
		
			// test with the same inputs 
			ComputeResults results1 = computerNonAccepting.compute(inputs); 
			logger.info("Final state: "+results1); 
			results = computerNonAccepting.compute(inputs1); 
			logger.info("Final state: "+results); 
			
			// load another DFA 
			DFA twoEven = DFAFactory.parseFromFile(new File("examples\\twoEven.dfa")); 
			DFAComputer computerTwoEven = new DFAComputer(twoEven); 
			logger.info(twoEven); 
			logger.info(twoEven.toString(true));
			
			// test with the same inputs 
			ComputeResults results2 = computerTwoEven.compute(inputs); 
			logger.info("Final state: "+results2); 
			results = computerNonAccepting.compute(inputs1); 
			logger.info("Final state: "+results); 
			
			// load another DFA 
			DFA threeState = DFAFactory.parseFromFile(new File("examples\\threeInputsTest.dfa")); 
			// test the detailed String representation 
			logger.info(threeState.toString(true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
