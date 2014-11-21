package com.dm.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dm.prog.CountWords;


public class CountItemList {

	public static void main(String[] args) {
		
		CountItemList cl=new CountItemList();
		Map<Integer,String> mp = new HashMap<Integer,String>();
		mp.put(1, "How uploaded uploaded file image file mime , php image-processing file-upload upload mime-types");
		mp.put(2, "How  prevent firefox firefox closing press ctrl ,firefox");
		mp.put(3, "Error Invalid list file variable , r matlab machine-learning c#");
		mp.put(4, "How draw barplot Coreplot,core-plot javascript php");
		
		
		
		Map<String,Map<Integer,String>> termMapFinal = new HashMap<String, Map<Integer,String>>();
		
		Set<String>  setOfWords = cl.getTotalWords(mp);
		
		for(String term:setOfWords)
		{
			termMapFinal.put(term, cl.getKey(term, mp));
		}
		
		System.out.println(termMapFinal.toString());
		Map<String,String> termTag= cl.getTagParent(termMapFinal, mp);
	    System.out.println(termTag.toString());
		
		
		for(Entry<String,String> entry:termTag.entrySet()) 
		{
			
			//System.out.println(termMapFinal.get(entry.getKey()));
			//termMapFinal.get(entry.getKey());
			for(Entry<Integer,String> entry1:termMapFinal.get(entry.getKey()).entrySet()) 
			{
				//System.out.println(entry.getKey()+"==="+entry1.getKey()+"=="+entry1.getValue());
				String arr[] = entry1.getValue().split(" ");
				for(String st:arr)
				{
					//if(entry.getKey().equals(st.split(":")[0]))
						//System.out.println("Term :"+st.split(":")[1]);
				}
				
			}
		}
		
		//Map<Integer,Map<String,Integer>> termMap =cl.getKey("How", mp);
		
		//Map <String ,List<Integer>> tagMap = cl.getTag(termMap, mp);
		//System.out.println(tagMap.toString());
		 
		 //Map<String,String> fm =cl.setMapforTerm(termMap,tagMap,"How","php");
		 //System.out.println(fm.toString());
	}
	
	
	
	public Set<String> getTotalWords(Map<Integer,String> mp){
		
		Set<String>  setOfWords = new HashSet<String>();
		
		for(Entry<Integer,String> entry:mp.entrySet()) 
		 {
			 
				String arr[] = entry.getValue().split(",");
				String termArray[] =arr[0].split(" ");
				for(String tr:termArray){
					if(!tr.equals(""))
					setOfWords.add(tr.trim());
				}
		 }
		return setOfWords;
	}
	
	
	public Map<String,String> setMapforTerm(Map<Integer,Map<String,Integer>> termMap , Map <String ,List<Integer>> tagMap,String term , String tag)
	{
		int totalForterm=0;
		int totalTerms=0;
		int totalTags=0;
		List<Integer> ls=null;
		Map<String,Integer> docMap = null;
		Map<String,String> finaltermMap = new HashMap<String, String>();
		
		
		for(Entry<String ,List<Integer>> entry:tagMap.entrySet()) 
		 {
			if(tag.equals(entry.getKey().toString())){
			ls=entry.getValue();	
			}
				
		 }
		
		for(Integer docNumber:ls)
		{
			docMap =termMap.get(docNumber);
			totalTerms+=docMap.get("total");
			for(Entry<String,Integer> entry:docMap.entrySet()) 
			 {
				 
				if(term.equals(entry.getKey()))
					totalForterm+=entry.getValue();
			 }
			totalTags++;
		}
		
		finaltermMap.put(term, tag+","+totalTags+","+totalForterm+","+totalTerms);
		return finaltermMap;
	}
	
	
	
	
	
	
	
	
	public Map<String,String> getTagParent(Map<String,Map<Integer,String>> termMap,Map<Integer,String> mp)
	{
		Map<String,String> tMap =new HashMap<String,String>();
		String term="";
		String finalValue="";
		CountWords cw = new CountWords();
		for(Entry<String,Map<Integer,String>> entry:termMap.entrySet())
		{
			finalValue="";
			term = entry.getKey();
			//System.out.println(term +":"+valueMap.toString());
			for(Entry<Integer,String> entry1:entry.getValue().entrySet())
			{
//				System.out.println(term+"======"+mp.get(entry1.getKey()).split(",")[1]);
				finalValue+=mp.get(entry1.getKey()).split(",")[1]+" ";
			}
			finalValue=finalValue.substring(0, finalValue.length()-1);
			tMap.put(term, cw.returnFrequency(finalValue));
		}
		return tMap;
	}
	
	
	
/*	public Map<String,String> getTagParent(Map<String,Map<Integer,Map<String,Integer>>> termMap,Map<Integer,String> mp)
	{
		Map<String,String> tMap =new HashMap<String,String>();
		String term="";
		String finalValue="";
		CountWords cw = new CountWords();
		for(Entry<String,Map<Integer,Map<String,Integer>>> entry:termMap.entrySet())
		{
			finalValue="";
			term = entry.getKey();
			//System.out.println(term +":"+valueMap.toString());
			for(Entry<Integer,Map<String,Integer>> entry1:entry.getValue().entrySet())
			{
//				System.out.println(term+"======"+mp.get(entry1.getKey()).split(",")[1]);
				finalValue+=mp.get(entry1.getKey()).split(",")[1]+" ";
			}
			finalValue=finalValue.substring(0, finalValue.length()-1);
			tMap.put(term, cw.returnFrequency(finalValue));
		}
		return tMap;
	}*/
	
	
	
	public Map <String ,List<Integer>> getTag(Map<Integer,Map<String,Integer>> termMap,Map<Integer,String> mp)
	{
		
		Set<String>  set=new HashSet<String>();
		Map <String ,List<Integer>> tagDocMap = new HashMap<String, List<Integer>>();
		Map<Integer,String> tagMap = new HashMap<Integer, String>();
		for(Entry<Integer,Map<String,Integer>> entry:termMap.entrySet()) 
		 {
			 Integer counter = entry.getKey();
			 
			 String arr[]= mp.get(counter).split(",");
			 String tagArr[] = arr[1].split(" ");
			 tagMap.put(counter,arr[1]);
			 for(String s:tagArr)
			 {
				 if(!s.equals(""))
					 set.add(s.trim());
			 }
			 
		 }

		
		for(String str:set)
		{
			tagDocMap.put(str, getKeyFrequency(str, tagMap));
		}
		return tagDocMap;
	}
	
	
	
	public List<Integer>  getKeyFrequency(String value, Map<Integer, String> map) {
		  List<Integer> keys = new ArrayList<Integer>();
		  
		  for(Entry<Integer, String> entry:map.entrySet()) {
			  String str=entry.getValue();
			  String arr[] = str.split(",");
			  String titleTags[] = arr[0].split(" ");
			  for(String s: titleTags){
				  if(s.equals(value))
				  {
					  keys.add(entry.getKey());
					  break;
				  }
			 }
		  }
		  
		   return keys;
	}
	
	
	
	
	public Map<Integer,String> getKey(String value, Map<Integer, String> map) {
		  
		  List<Integer> keys = new ArrayList<Integer>();
		  //Map<Integer,Map<String,Integer>> finalMap = new HashMap<Integer,Map<String,Integer>>();
		  Map<Integer,String> freqMap = new HashMap<Integer,String>();
		  CountWords cw = new CountWords();
		  
		  
		  for(Entry<Integer, String> entry:map.entrySet()) {
			  String str=entry.getValue();
			  String arr[] = str.split(",");
			  String titleTags[] = arr[0].split(" ");
			  for(String s: titleTags){
				  if(s.equals(value))
				  {
					  keys.add(entry.getKey());
					  break;
				  }
			 }
		  }
		  
		  for(Integer key:keys){
			  String arr[]=map.get(key).toString().split(",");
			  
			  freqMap.put(key,cw.returnFrequency(arr[0]));
		  }
		  //System.out.println(freqMap.toString());
		 // finalMap = perDocumentFrequency(freqMap);
		 // System.out.println(finalMap.toString());
		  keys.clear();
		  keys=null;
		  
		  /*freqMap.clear();
		  freqMap=null;*/
		  
/*		  map.clear();
		  map =null;
*/		  
		  //return finalMap;
		  return freqMap;
	}
	
	
	
	public Map<Integer,Map<String,Integer>> perDocumentFrequency(Map<Integer,String> map)
	{
		String str="";
		Map<Integer,Map<String,Integer>> finalMap = new HashMap<Integer,Map<String,Integer>>();
		List<String> tempList = new ArrayList<String>();
		CountWords cw = new CountWords();
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        str=(String)pairs.getValue();
	        tempList.add(str.trim());
	        finalMap.put(Integer.parseInt(pairs.getKey().toString()),returnFrequency(tempList));
	        tempList.clear();
	    }
	    
	    map.clear();
	    map=null;
	    
	    tempList.clear();
	    tempList =null;
	    
		return finalMap;
	}
	
	
	public Map<Integer,Map<String,Integer>> perDocumentFrequency(List<String> list)
	{
		int counter=1;
		String str="";
		CountItemList ctl = new CountItemList();
		Map<Integer,String> mp = new HashMap<Integer,String>();
		
		Map<Integer,Map<String,Integer>> finalMap = new HashMap<Integer,Map<String,Integer>>();
		List<String> tempList = new ArrayList<String>();
		for(String st:list)
		{
			mp.put(counter, st);
			counter++;
		}
		counter =1;
		Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        str=(String)pairs.getValue();
	        tempList.add(str.trim());
	        finalMap.put(counter,ctl.returnFrequency(tempList));
	        counter++;
	        tempList.clear();
	    }
	    
		return finalMap;
	}
	
	

	public Map<String,Integer> returnFrequency(List<String> lst)
	{
		
		List<String> stringLst = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		Map<String,Integer> mp = new HashMap<String,Integer>();
		int frequencyTotal=0;
		
		for(String str:lst){
			String arr[] = str.split(" ");
			for(String a:arr)
			stringLst.add(a.trim());
		}
		
		set.addAll(stringLst);
		
		for(String s:set)
		{
			if(!s.equals("") && !s.equals(null)){
			//mp.put(s, Double.parseDouble(df.format((1+Math.log10(Collections.frequency(stringLst, s))))));
			mp.put(s, Collections.frequency(stringLst, s));
			frequencyTotal+=Collections.frequency(stringLst, s);
			}
		}
		mp.put("total", frequencyTotal);
		
		return mp;
	}
	
	
	
}