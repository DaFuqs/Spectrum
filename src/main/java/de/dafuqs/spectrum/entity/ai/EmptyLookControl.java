package de.dafuqs.spectrum.entity.ai;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.*;

public class EmptyLookControl extends LookControl {
	
	public EmptyLookControl(Mob entity) {
		super(entity);
	}
	
	@Override
	public void tick() {
	}
	
}
	