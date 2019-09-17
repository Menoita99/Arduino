package algorithm;

import java.util.ArrayList;
import java.util.List;

public class TimePattern {


	private List<Integer> valuesPattern;
	private List<Double> timePattern;
	private double referenceValue;
	

	
	public TimePattern(List<Integer> valuesPattern, List<Double> timePattern, double referenceValue) {
		this.valuesPattern = valuesPattern;
		this.timePattern = timePattern;
		this.referenceValue = referenceValue;
	}
	
	
	
	public TimePattern trim(double uncertainty) {
		int start = 0;
		int end = valuesPattern.size()-1;
		
		while(Math.abs(referenceValue-valuesPattern.get(start))<= uncertainty)
			start++;
		while(Math.abs(referenceValue-valuesPattern.get(end))<= uncertainty)
			end--;
		
		List<Integer> newValues = new ArrayList<Integer>();
		List<Double> newTime = new ArrayList<Double>();
		
		for(int i = start; i <= end ; i++) {
			newValues.add(valuesPattern.get(i));
			newTime.add(timePattern.get(i));
		}
			
		valuesPattern = newValues;
		timePattern = newTime;
		
		return this;
	}
	
	

	public List<Double> getTimePattern() {
		return timePattern;
	}

	public void setTimePattern(List<Double> timePattern) {
		this.timePattern = timePattern;
	}

	public List<Integer> getValuesPattern() {
		return valuesPattern;
	}

	public void setValuesPattern(List<Integer> valuesPattern) {
		this.valuesPattern = valuesPattern;
	}



	public double getReferenceValue() {
		return referenceValue;
	}



	public void setReferenceValue(double referenceValue) {
		this.referenceValue = referenceValue;
	}



	public TimePattern restartTime(double d) {
		for (int i = timePattern.size()-1; i >= 0 ; i--) 
			timePattern.set(i, timePattern.get(i)-timePattern.get(0)+d);
		return this;
	}
	
	
	
}
