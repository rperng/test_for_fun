package com.test.forfun;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
 	
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
	
 	public class WordCount2 {
 	
 	public static final String theFilename = "SampleData.out/part-00000";
 	
	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	     private final static IntWritable one = new IntWritable(1);
 	     private Text word = new Text();
	
 	     public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
 	       String line = value.toString();
 	       // get it line by line ...
 	       StringTokenizer tokenizer = new StringTokenizer(line, " \t\n\r\f-,." );
 	       String previous_str = ""; // empty string
 	       while (tokenizer.hasMoreTokens()) {
 	    	  // need to get next Token to make a two words token
 	    	 String current_str = tokenizer.nextToken();
 	    	 current_str = current_str.toUpperCase();
 	    	 if (previous_str.length()==0) {   // assign the first "previous" string
 	    		 previous_str = current_str;
 	    	 } else {
 	    	 // contact 2 string into one counting token 
 	    	 String target_str = previous_str + " " + current_str;
 	    	 //System.out.println(target_str);
	         word.set(target_str);
 	         output.collect(word, one);
 	         previous_str = current_str;
 	    	 }
 	       }
 	     }
 	   }
 	
 	   public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
 	     public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
 	       int sum = 0;
	       while (values.hasNext()) {
 	         sum += values.next().get();
	       }
 	       output.collect(key, new IntWritable(sum));
 	     }
 	   }
 	
 	   public static void main(String[] args) throws Exception {
		   
 	     JobConf conf = new JobConf(WordCount.class);
	     conf.setJobName("wordcount");
	     conf.setNumMapTasks(1);   // need to be sequential, I guess ...
	     
 	     conf.setOutputKeyClass(Text.class);
 	     conf.setOutputValueClass(IntWritable.class);
	
 	     conf.setMapperClass(Map.class);
 	     conf.setCombinerClass(Reduce.class);
 	     conf.setReducerClass(Reduce.class);
 	
	     conf.setInputFormat(TextInputFormat.class);
 	     conf.setOutputFormat(TextOutputFormat.class);
 	
 	     FileInputFormat.setInputPaths(conf, new Path(args[0]));
 	     FileOutputFormat.setOutputPath(conf, new Path(args[1]));
 	
 	     JobClient.runJob(conf);
	     
 		 // Run it with
 		 //    hadoop jar indexer.jar index.WordCount2 SampleData.txt SampleData.out  
 	     // The count for bi-gram should be in SampleData.out/part-00000 now,
 	     // get the count for each and start the 
 	     Configuration config = new Configuration();
 	     FileSystem fs = FileSystem.get(config);
 	     Path filenamePath = new Path(fs.getWorkingDirectory()+"/"+theFilename);
 	     //Path filenamePath = new Path(theFilename);

 	     try {
 	     FSDataInputStream fsis = fs.open(filenamePath);
 	     String str = null;
 	     int total_bi_grams = 0;
 	     double p_value=1;
 	     double probability = 0;
 	     double occurence_product = 1;
 	     
 	     while ((str = fsis.readLine())!= null)
 	     {
 	 	   //System.out.println(str);	 
 	       int occu = getOccurence(str);
 	       occurence_product = Math.pow(occu, occu); // 2*2, 3*3*3, ...
 	       //System.out.println(occu + "***" +occurence_product);
 	       p_value = p_value * occurence_product;
 	       total_bi_grams += occu;
 	     }
 	     
 	     probability = Math.pow(p_value, (double)1/total_bi_grams);

 	     System.out.println("total bi-grams tokens = " + total_bi_grams);
 	     System.out.println("total p_value = " + p_value);
 	     System.out.println("probability = " + probability);
         fsis.close();
         } catch (IOException ioe) {
             System.err.println("IOException during operation: " + ioe.toString());
             System.exit(1);
        }

 	     
 	   }
 	   
 	   private static int getOccurence(String a_line) {
 	      // get the last token of the line, it's the occurence ... 		   
 	      String[] strArray;  
  	      String lasttoken = null;  
  	      
  	      String pattern = "[\\s]+";
  	      Pattern splitter = Pattern.compile(pattern);
  	      String[] result = splitter.split(a_line);
          
  	      return Integer.valueOf(result[result.length -1]).intValue();
  	      
 	   }
 	}
