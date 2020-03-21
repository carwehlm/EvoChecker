//==============================================================================
//	
 //	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evochecker.evolvable.Evolvable;
import evochecker.evolvable.EvolvableDistribution;
import evochecker.evolvable.EvolvableDouble;
import evochecker.evolvable.EvolvableInteger;
import evochecker.evolvable.EvolvableModule;
import evochecker.evolvable.EvolvableModuleAlternative;
import evochecker.evolvable.EvolvableOption;
import evochecker.evolvable.EvolvableRange;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

/**
 * Factory constructing the genotype of a proble
 * @author sgerasimou
 *
 */
public class GenotypeFactory{
	
	/** Map between a gene and an evolvable element*/
	private static Map<AbstractGene,Evolvable> elementsMap = new HashMap<AbstractGene, Evolvable>();

	/**
	 * Create a list chromosome (or individual) as a sequence of genes
	 */
	public static List<AbstractGene> createChromosome(List<Evolvable> evolvableList) throws EvoCheckerException{
		List<AbstractGene> genes = new ArrayList<AbstractGene> ();		
		for (Evolvable evolvable : evolvableList){
			AbstractGene gene = initialiseGene(evolvable);
			genes.add(gene);
			elementsMap.put(gene, evolvable);
		}
			return genes;		
	}
		
		
	/**
	 * Initialise this gene 
	 */
	private static AbstractGene initialiseGene (Evolvable evolvable) throws EvoCheckerException{
		String name 	= evolvable.getName();
		
		if (evolvable instanceof EvolvableDouble){
			Number minValue	= ((EvolvableRange)evolvable).getMinValue();
			Number maxValue = ((EvolvableRange)evolvable).getMaxValue();
			return new DoubleGene(name, (double)minValue, (double)maxValue);
		}
		else if (evolvable instanceof EvolvableInteger){
			Number minValue	= ((EvolvableRange)evolvable).getMinValue();
			Number maxValue = ((EvolvableRange)evolvable).getMaxValue();
			return new IntegerGene(name, (int)minValue, (int)maxValue);
		}
		else if (evolvable instanceof EvolvableDistribution){
			//TODO We do not consider specific bounds for distributions yet
			int numberOfOutcomes = ((EvolvableDistribution)evolvable).getCardinality();
			return new DistributionGene(name, numberOfOutcomes);
		}
		else if (evolvable instanceof EvolvableModuleAlternative){
			int numberOfAlternatives = (int) ((EvolvableModuleAlternative)evolvable).getMaxValue();
			return new AlternativeModuleGene(name, numberOfAlternatives);
		}
		//TODO here we will modules that can be replicated - not supported at the moment **/
		else if (evolvable instanceof EvolvableModule){
			throw new EvoCheckerException("EvolvableModule not yet supported");
		}
		//TODO here we will modules that can be replicated - not supported at the moment **/
		else if (evolvable instanceof EvolvableOption){
			throw new EvoCheckerException("EvolvableOption not yet supported");
		}

		throw new EvoCheckerException ("Error in Genotype Factory");
	}

	
	/**
	 * Get the generated pair of genes and evolvable elements
	 * @return
	 */
	public static Map<AbstractGene, Evolvable> getMapping(){
		return elementsMap;
	}	
}
