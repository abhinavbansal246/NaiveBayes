package com.dm.prog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.dm.prog.CountWords;
import com.dm.prog.RemoveStopWords;


public class DataMine {
	static Map<String,Double> finalMap = new HashMap<String,Double>();
	public static void main(String[] args) {
		try
		{
			System.out.println(new Timestamp(new java.util.Date().getTime()));
			BuildFiles bf = new BuildFiles();
			CountWords cw = new CountWords();
			int totalRows=0;
			
			Map<String,Integer> tagCount =null;
			//Map<String,Double> termTagMap =null;
			Map<Integer,String> testMap = null;
			Map<Integer,String> trainingMap = null;
			List<String> tagList= new ArrayList<String>();
			Set<String> tagSet= new HashSet<String>();
			
			trainingMap=bf.createTrainingMap("E:\\FALL 2013\\Chengkai 5334\\Project1\\rar\\Train\\Train3.csv",tagList);
			//System.out.println(trainingMap.toString());
			totalRows = trainingMap.size();
			tagCount =cw.returnFrequency(tagList);
			tagSet  =BuildModel.getTotaltags(trainingMap);
			int totalWords = BuildModel.getTotalWords(trainingMap);
			for(String str:tagSet)
			{
				BuildModel.probabilityMap(finalMap,totalWords,str,BuildModel.getKey(str,trainingMap));
			}
		
			
			testMap=bf.createTestMap("E:\\FALL 2013\\Chengkai 5334\\Project1\\rar\\Test\\Test3.csv");
			
			FileWriteOutput.createHeader();
			getOutputFile(testMap,tagSet,finalMap,tagCount,totalRows);
			FileWriteOutput.closeReader();
			System.out.println(new Timestamp(new java.util.Date().getTime()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void getOutputFile(Map<Integer,String> documents,Set<String> tags,Map<String,Double> ttMap,Map<String,Integer> tagCount,int totalRows)
	{
		String finalTags="";
		String termTag=""; 
		int documentId=0;
		double termProbability=1;
		double documentProbability=1;
		Map<String,Double> finalMap = new HashMap<String,Double>();
		Map<String,Integer> termListMap =null;
		for(Entry<Integer,String> entry:documents.entrySet()) 
		 {
			
			documentId=entry.getKey();
			Set<String> termSet = BuildFiles.StringToSet(entry.getValue());
			/*
			 * Map of count of each word
			 */
			termListMap = BuildFiles.returnFrequency(entry.getValue());
			
			for(String tag:tags)
			{
				for(String term:termSet)
				{
					termTag = term+","+tag;
					if(ttMap.containsKey(termTag))
					{
						termProbability*=ttMap.get(termTag);
						//System.out.println("term: "+term+"---"+termListMap.get(term));
						termProbability=Math.pow(termProbability, termListMap.get(term));
					}
				}
				documentProbability = ((double)tagCount.get(tag)/(double)totalRows);
				documentProbability*=((documentProbability)*termProbability);
				
				finalMap.put(documentId+","+tag, documentProbability);
				termProbability=1;
				documentProbability=1;
			}
			
			
			finalTags = BuildFiles.getTags(finalMap);
			finalTags = finalTags.substring(0,finalTags.length()-1);
			
			//System.out.println(documentId +" "+finalTags );
			FileWriteOutput.printRows(documentId, finalTags);
			/*
			 * Here call the Build File function to print the output file
			 */
			finalMap.clear();
			
		 }
	}
	
}
