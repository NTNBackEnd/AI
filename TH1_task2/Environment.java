package TH1_task2; 

public class Environment {
	public static final Action MOVE_LEFT = new DynamicAction("LEFT");
	public static final Action MOVE_RIGHT = new DynamicAction("RIGHT");
	public static final Action MOVE_UP = new DynamicAction("UP");
	public static final Action MOVE_DOWN = new DynamicAction("DOWN");
	public static final Action SUCK_DIRT = new DynamicAction("SUCK");
	public static final String LOCATION_A = "A";
	public static final String LOCATION_B = "B";
	public static final String LOCATION_C = "C";
	public static final String LOCATION_D = "D";
	public int score = 0;

	public enum LocationState {
		CLEAN, DIRTY
	}

	private EnvironmentState envState;
	private boolean isDone = false;// all squares are CLEAN
	private Agent agent = null;

	public Environment(LocationState locAState, LocationState locBState,LocationState locCState,LocationState locDState) {
		envState = new EnvironmentState(locAState, locBState,locCState,locDState);
	}

	// add an agent into the enviroment
	public void addAgent(Agent agent, String location) { //**********
		this.agent = agent;
		envState.setAgentLocation(location);
	}

	public EnvironmentState getCurrentState() {
		return this.envState;
	}

	// Update enviroment state when agent do an action
	public EnvironmentState executeAction(Action action) {//**********
		String agentLocation = envState.getAgentLocation(); 
		if(action == Environment.SUCK_DIRT) {
			score += 500;
			envState.setLocationState(envState.getAgentLocation(), LocationState.CLEAN);
		}else {
			score -= 10;
			if(action.equals(MOVE_RIGHT) && LOCATION_A.equals(agentLocation)) {
				envState.setAgentLocation(LOCATION_B);
			}
			else if(action.equals(MOVE_DOWN) && LOCATION_B.equals(agentLocation)) {
				envState.setAgentLocation(LOCATION_D);
			}
			else if(action.equals(MOVE_LEFT) && LOCATION_D.equals(agentLocation)) {
				envState.setAgentLocation(LOCATION_C);
			}
			else if(action.equals(MOVE_UP) && LOCATION_C.equals(agentLocation)) {
				envState.setAgentLocation(LOCATION_A);
			}
			else if(action.equals(SUCK_DIRT)) {
				envState.setLocationState(agentLocation, LocationState.CLEAN);
			}
			else {
				score -= 90;
			}
		}
		return envState;
	}

	// get percept<AgentLocation, LocationState> at the current location where agent
	// is in.
	public Percept getPerceptSeenBy() {
		String agentLocation = envState.getAgentLocation();
		LocationState state = envState.getLocationState(agentLocation);
		return new Percept(agentLocation, state);
	}

	public void step() {
		envState.display();
		String agentLocation = this.envState.getAgentLocation();
		Action anAction = agent.execute(getPerceptSeenBy());
		EnvironmentState es = executeAction(anAction);

		System.out.println("Agent Loc.: " + agentLocation + "\tAction: " + anAction + "\tScore: " + score);

		if ((es.getLocationState(LOCATION_A) == LocationState.CLEAN)
				&& (es.getLocationState(LOCATION_B) == LocationState.CLEAN)
				&& (es.getLocationState(LOCATION_C) == LocationState.CLEAN)
				&& (es.getLocationState(LOCATION_D) == LocationState.CLEAN)) 
			
			isDone = true;// if both squares are clean, then agent do not need to do any action
		es.display();
	}

	public void step(int n) {
		for (int i = 0; i < n; i++) {
			step();
			System.out.println("-------------------------");
		}
	}

	public void stepUntilDone() {
		int i = 0;

		while (!isDone) {
			System.out.println("step: " + i++);
			step();
		}
	}
}
