package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Teste {

	public static void main(String[] args) {
		testePatternRecognition();
	}


	private static void testePatternRecognition() {
		List<String> strings = getData();
		List<Integer> values = new ArrayList<Integer>();
		List<Double> time = new ArrayList<Double>();

		for (int i = 0; i < strings.size(); i++) {
			if(i%2==0) values.add(Integer.parseInt(strings.get(i).trim()));
			else       time.add(Double.parseDouble(strings.get(i).trim()));
		}

		TimePattern t = new TimePattern(values, time, 517);
		PatternRecognition pr = new PatternRecognition(t);	
		pr.recognize(t);
		
		setData(t.trim(2));
	}

	
	

	private static List<String> getData() {
		List<String> strings = new ArrayList<String>();

		try(Scanner s = new Scanner(new File("C:\\Users\\Rui Menoita\\Desktop\\data2.txt"))){
			while(s.hasNextLine()) {
				String[] splited = s.nextLine().split(":");
				for (int i = 0; i < splited.length; i++) 
					if(!splited[i].trim().matches(""))
						strings.add(splited[i].trim());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return strings;
	}
	
	
	private static void setData(TimePattern tp) {

		try(PrintWriter pw = new PrintWriter(new File("C:\\Users\\Rui Menoita\\Desktop\\save.txt"))){
			for (int i = 0; i < tp.getValuesPattern().size(); i++) 
				pw.append(tp.getValuesPattern().get(i)+ " : "+tp.getTimePattern().get(i)+"\n");
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}



}
