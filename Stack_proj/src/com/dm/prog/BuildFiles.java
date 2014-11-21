package com.dm.prog;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.dm.prog.RemoveStopWords;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class BuildFiles {
	private static CellProcessor[] getTrainingProcessors() {
        
        
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), // ID
                new NotNull(), // Title
                new NotNull(), // Body
                new NotNull(), // Tags
                // initialising
        };
        
        return processors;
	}
	
	private static CellProcessor[] getTestProcessors() {
        
        
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), // ID
                new NotNull(), // Title
                new NotNull(), // Body
        };
        
        return processors;
	}
	public Map<Integer,String> createTrainingMap(String fileName,List<String> tagList)
	{
		Map<Integer,String> map = new HashMap<Integer,String>();
		 ICsvBeanReader beanReader = null;
	        try {
	                beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
	                
	                // the header elements are used to map the values to the bean (names must match)
	                final String[] header = beanReader.getHeader(true);
	                final CellProcessor[] processors = getTrainingProcessors();
	                
	                TrainingBean td;
	                while( (td = beanReader.read(TrainingBean.class, header, processors)) != null ) {
	                	 //map.put(Integer.parseInt(td.getId().trim()), RemoveStopWords.removeStopWords(td.getTitle().trim())+","+ td.getTags().trim());
	                	//System.out.println(StemmingTerms.Stem(RemoveStopWords.removeStopWords(td.getTitle().trim())));
	                	map.put(Integer.parseInt(td.getId().trim()), Stemmer.Stem(RemoveStopWords.removeStopWords(td.getTitle().trim()))+","+ td.getTags().trim());
	                	tagList.add(td.getTags().trim());
	                }
	                
	        }catch(FileNotFoundException fe)
	        {
	        	fe.printStackTrace();
	        }catch(IOException ie)
	        {
	        	ie.printStackTrace();
	        }
	        finally {
	                if( beanReader != null ) {
	                        try {
								beanReader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
	                }
	        }
        return map;
	}
	
	
	public Map<Integer,String> createTestMap(String fileName)
	{
		Map<Integer,String> map = new HashMap<Integer,String>();
		ICsvBeanReader beanReader = null;
        try {
                beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
                
                // the header elements are used to map the values to the bean (names must match)
                final String[] header = beanReader.getHeader(true);
                final CellProcessor[] processors = getTestProcessors();
                
                TestDataBean tdb;
                while( (tdb = beanReader.read(TestDataBean.class, header, processors)) != null ) {
                	 map.put(Integer.parseInt(tdb.getId()), Stemmer.Stem(RemoveStopWords.removeStopWords(tdb.getTitle())));
                }
                
        }catch(FileNotFoundException fe)
        {
        	fe.printStackTrace();
        }catch(IOException ie)
        {
        	ie.printStackTrace();
        }
        finally {
                if( beanReader != null ) {
                        try {
							beanReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
                }
        }
        return map;
	}
	
	
	
	public static Set<String> StringToSet(String str)
	{
		Set <String> termSet = new HashSet<String>();
		
		String termArray[] =str.split(" ");
		for(String term:termArray){
			if(!term.equals(""))
				termSet.add(term.trim());
		}
		
		return termSet;
	}
	
	public static String getTags(Map<String,Double> map)
	{
		String tagString = "";
		List<Double> list = new ArrayList<Double>();
		for(Entry<String,Double> entry:map.entrySet()) 
		 {
			list.add(entry.getValue());
		 }
		Collections.sort(list);
		
		if(list.size()>5)
		list= (list.subList(list.size() -3, list.size()));
		
		for(double probability:list)
		{
			for (Entry< String,Double> entry : map.entrySet()) {
	            if (entry.getValue() == probability) {
	               
	            	String strArr[] = entry.getKey().split(",");
	            	tagString+=strArr[1]+",";
	            }
	        }
		}
		return tagString;
	}
	
	
	public static void writeToFile(int id,String tags)
	{

		CSVWriter writer=null;
		BufferedWriter out=null;
		try{
			    out = new BufferedWriter(new FileWriter("e:\\final.csv"));
		        writer = new CSVWriter(out);
		        String[] values = {"Id","tags"};
		        writer.writeNext(values);
		        values = new String[]{id+"",tags};
			    writer.writeNext(values);
			     
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try
			{
				writer.close();
				out.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	public static Map<String,Integer> returnFrequency(String str)
	{
		List<String> stringLst = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		Map<String,Integer> mp = new HashMap<String,Integer>();
	 
		String arr[] = str.split(" ");
		for(String a:arr)
		{
			if(!a.equals(""))
				stringLst.add(a.trim());
		}
		
		set.addAll(stringLst);
		
		for(String s:set)
		{
			if(!s.equals("") && !s.equals(null))
			{
				mp.put(s, Collections.frequency(stringLst, s));
			}
		}
		
		return mp;
	}
	
	
}
