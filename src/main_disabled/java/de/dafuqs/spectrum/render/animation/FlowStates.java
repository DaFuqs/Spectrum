package de.dafuqs.spectrum.render.animation;

public class FlowStates {
	
	// Semantic FlowState, used to indicate an initial state that is never returned to.
	public static final FlowState INIT = new FlowState("init");
	
	// Semantic FlowState, used to indicate that a required structure is missing.
	public static final FlowState MB_INVALID = new FlowState("multiblock_invalid");
	
	// General use
	public static final FlowState OPEN = new FlowState("open");
	public static final FlowState CLOSED = new FlowState("closed");
	public static final FlowState OPEN_ACTIVE = new FlowState("open_active");
	public static final FlowState CLOSED_ACTIVE = new FlowState("closed_active");
	public static final FlowState FULL = new FlowState("full");
	public static final FlowState ACTIVE = new FlowState("active");
	public static final FlowState INACTIVE = new FlowState("inactive");
	public static final FlowState IDLE = new FlowState("idle");
}
