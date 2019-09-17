package algorithm;

public class PatternRecognition {

	private TimePattern pattern;
	
	private static final int UNCERTAINTY = 2;
	
	
	public PatternRecognition(TimePattern pattern) {
		this.pattern = pattern;
	}


	public double recognize(TimePattern patt) {
		pattern.trim(UNCERTAINTY).restartTime(0.0);
		patt.trim(UNCERTAINTY).restartTime(0.0);
		
		
		
		
		return 0.0;
	}


	public TimePattern getPattern() {
		return pattern;
	}


	public void setPattern(TimePattern pattern) {
		this.pattern = pattern;
	}
}
