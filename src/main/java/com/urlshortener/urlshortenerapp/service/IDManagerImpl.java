package com.urlshortener.urlshortenerapp.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.urlshortener.urlshortenerapp.controller.LinkRestController;

@Service
public class IDManagerImpl implements IDManager {
	
	private static HashMap<String, Integer> adjectiveToIndexTable;
	
	// Base 62 -> Base 10
	private static HashMap<Character, Integer> charToIndexTable;
	
	// Base 10 -> Base 62
	private static List<Character> indexToCharTable;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRestController.class);
	
	public IDManagerImpl() {
//		this.initializeAdjectiveToIndexTable();
		this.initializeCharToIndexTable();
		long t = 123;
		String t2 = "b9";
		System.out.println(convertBase10ToBase62ID(t));
		System.out.println(this.generateUniqueID(t));
		System.out.println(this.getKeyFromID(t2));
	}
	
	private List<String> getTerms(String path) {
		List<String> terms = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF8"));
			String line;
			
			while((line = br.readLine()) != null) {
				// Removes UTF-8 BOM if present in CSV.
				line = line.replace("\uFEFF", "");
				terms.add(line);
			}
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return terms;
	}
	
	private void initializeCharToIndexTable() {
		// 0 -> a, 1 -> b, ..., 61 -> 9
		charToIndexTable = new HashMap<>();
		// < a, b, ..., 9 >
		indexToCharTable = new ArrayList<>();
		
		for (int i = 0; i < 26; i++) {
			char c = 'a';
			c += i;
			charToIndexTable.put(c, i);
			indexToCharTable.add(c);
		}
		
		for (int i = 26; i < 52; i++) {
			char c = 'A';
			c += (i - 26);
			charToIndexTable.put(c, i);
			indexToCharTable.add(c);
		}
		
		for (int i = 52; i < 62; i++) {
			char c = '0';
			c += (i - 52);
			charToIndexTable.put(c, i);
			indexToCharTable.add(c);
		}
	}
	
//	private void initializeAdjectiveToIndexTable() {
//		adjectiveToIndexTable = new HashMap<>();
//		List<String> adjectives = getTerms("data/adjectives.csv");
//		
//		for (int i = 0; i < adjectives.size(); i++) {
//			adjectiveToIndexTable.put(adjectives.get(i), i);
//			System.out.println(i);
//			System.out.println(adjectives.get(i));
//		}
//	}
	
	private List<Integer> convertBase10ToBase62ID(Long id) {
		List<Integer> digits = new ArrayList<>();
		while (id > 0) {
			int remainder = (int)(id % 62);
			digits.add(0, remainder);
			
			id /= 62;
		}
		
		return digits;
	}
	
	private Long convertBase62ToBase10ID(List<Character> ids) {
		long id = 0L;
		int exp = ids.size() - 1;
		for (int i = 0; i < ids.size(); i++, exp--) {
			int base10 = charToIndexTable.get(ids.get(i));
			id += (base10 * Math.pow(62.0, exp));
		}
		
		return id;
	}
	

	@Override
	public String generateUniqueID(Long id) {
		LOGGER.info("id: " + id.toString());
		
		if (id == 0L) {
			return id.toString();
		} else {
			List<Integer> base62 = convertBase10ToBase62ID(id);
			StringBuilder uniqueID = new StringBuilder();
			for (int digit : base62) {
				uniqueID.append(indexToCharTable.get(digit));
			}
			return uniqueID.toString();
		}
		
	}

	@Override
	public Long getKeyFromID(String id) {
		List<Character> characters = new ArrayList<>();
		for (int i = 0; i < id.length(); i++) {
			characters.add(id.charAt(i));
		}
		Long key = convertBase62ToBase10ID(characters);
		return key;
	}

}
