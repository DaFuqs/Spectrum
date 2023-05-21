package de.dafuqs.spectrum.blocks.energy;

import java.util.*;

public class InkNetwork {
	protected static List<InkNetwork> networks = new ArrayList<>();
	
	protected final HashSet<InkDuctBlockEntity> inkDucts = new HashSet<>();
	
	public boolean canConnect(InkDuctBlockEntity newDuct) {
		for (InkDuctBlockEntity currentDuct : inkDucts) {
			currentDuct.canSee(newDuct);
		}
		return false;
	}
}
