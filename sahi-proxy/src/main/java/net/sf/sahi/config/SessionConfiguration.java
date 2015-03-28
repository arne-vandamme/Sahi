package net.sf.sahi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Configuration settings for a single browser/script session.
 *
 * @author Arne Vandamme
 */
@Component
@Scope("prototype")
public class SessionConfiguration
{
	private int timeBetweenSteps = 100;
	private int timeBetweenStepsOnError = 1000;
	private int maxReattemptsOnErrors = 10;
	private int maxCyclesForPageLoad = 10;
	private int stabilityIndex = 5;

	public int getTimeBetweenSteps() {
		return timeBetweenSteps;
	}

	@Autowired
	public void setTimeBetweenSteps( @Value("${script.time_between_steps:100}") int timeBetweenSteps ) {
		this.timeBetweenSteps = timeBetweenSteps;
	}

	public int getTimeBetweenStepsOnError() {
		return timeBetweenStepsOnError;
	}

	@Autowired
	public void setTimeBetweenStepsOnError( @Value("${script.time_between_steps_on_error:1000}") int timeBetweenStepsOnError ) {
		this.timeBetweenStepsOnError = timeBetweenStepsOnError;
	}

	public int getMaxReattemptsOnErrors() {
		return maxReattemptsOnErrors;
	}

	@Autowired
	public void setMaxReattemptsOnErrors( @Value("${script.max_reattempts_on_error:10}") int maxReattemptsOnErrors ) {
		this.maxReattemptsOnErrors = maxReattemptsOnErrors;
	}

	public int getMaxCyclesForPageLoad() {
		return maxCyclesForPageLoad;
	}

	@Autowired
	public void setMaxCyclesForPageLoad( @Value("${script.max_cycles_for_page_load:10}") int maxCyclesForPageLoad ) {
		this.maxCyclesForPageLoad = maxCyclesForPageLoad;
	}

	public int getStabilityIndex() {
		return stabilityIndex;
	}

	@Autowired
	public void setStabilityIndex( @Value("${script.stability_index:5}") int stabilityIndex ) {
		this.stabilityIndex = stabilityIndex;
	}
}
