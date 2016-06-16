package com.test.forfun;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class SpamProbCalculate {

	HashMap<String, Integer> biGramMap;	 // store "(bi-gram, occurrence)"
	
	
	public SpamProbCalculate() {
		super();
		biGramMap = new HashMap<String, Integer>(); 
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// read the file in ...
		String aFile = "SampleData0.txt";
		//String aFile = "ToBe.txt";
		BufferedReader br = null;
		StringBuffer sb = null;

		SpamProbCalculate sbc = new SpamProbCalculate();
			
	    try {			
		    br = new BufferedReader(new FileReader(aFile));

			String line = null;
			sb = new StringBuffer("");
			while ((line = br.readLine()) != null) {
				if (line.length() != 0) {
					sb.append(line);
					sb.append(' ');
				} else {
					// end of a paragraph ... process previous paragraph
					//System.out.println(sb);
					//System.out.println("**para size: "+ sb.length() +" <<new paragraph>>");
					sbc.processParagraph(sb);
					sb = new StringBuffer("");
				}
			}
			
			if (sb.length()!=0) { // could be a one liner, and need to process last paragraph ...
				sbc.processParagraph(sb);
			}
			
			br.close();
			
			// start iterating thru the HashMap to get and print values ... 
			sbc.getProbability();

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	

	// get every bi-gram from a paragraph ...
	public void processParagraph(StringBuffer paragraph) {
		StringTokenizer tokenizer = new StringTokenizer(paragraph.toString(), " \t\n\r\f-,.");
		String previous_str = ""; // empty string
		while (tokenizer.hasMoreTokens()) {
			// need to get next Token to make a two words token
			String current_str = tokenizer.nextToken();
			current_str = current_str.toUpperCase();
			if (previous_str.length() == 0) { // assign the first "previous" string
				previous_str = current_str;
			} else {
				// concatenate 2 strings into one counting token
				String target_str = previous_str + " " + current_str;
				//System.out.println(target_str);
				addToMap(target_str);
				previous_str = current_str;
			}
		}
		
	}
	
	// use HashMap to store the bi-gram per paragraph
	public void addToMap(String aBiGram) {
		HashMap<String, Integer> hm = getBiGramMap();
		
		// use aBiGram as key, find the value and increase count
        if (hm.containsKey(aBiGram)) {  // key exists already ...
        	Integer value = (Integer)hm.get(aBiGram);
        	hm.put(aBiGram, value+1);
        }  else {  // insert new token into map ...
        	hm.put(aBiGram, 1);  // first occurence
        }
	}
	
	// calculate the product and k-value
	public void getProbability() {
		HashMap<String, Integer> hm = getBiGramMap();
		//TreeMap<String, Integer> tm = new TreeMap(hm);
		
		double product = 1;
		double occurence_product = 1;
		double totol_bi_grams = 0;
		double probability = 0;

		for(Object key : hm.keySet()) {
		    Integer occurence = (Integer)hm.get(key);
		    occurence_product = Math.pow(occurence.intValue(), occurence.intValue());  // 2*2, 3*3*3, ...
		    product = product * occurence_product;
		    totol_bi_grams += occurence;
		}
		probability = Math.pow(product, (double) 1 / totol_bi_grams);
		
		System.out.println("Total bi_grams count: " + totol_bi_grams);
		System.out.println("Total p_value: " + product);
		System.out.println("Probability: " + probability);
	}


	/**
	 * @return the biGramMap
	 */
	public HashMap<String, Integer> getBiGramMap() {
		return biGramMap;
	}



	/**
	 * @param biGramMap the biGramMap to set
	 */
	public void setBiGramMap(HashMap<String, Integer> biGramMap) {
		this.biGramMap = biGramMap;
	}

}
