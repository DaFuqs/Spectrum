package de.dafuqs.spectrum.entity.ai;

import net.minecraft.entity.ai.control.*;
import net.minecraft.entity.mob.*;

public class EmptyLookControl extends LookControl {
	
	public EmptyLookControl(MobEntity entity) {
		super(entity);
	}
	
	@Override
	public void tick() {
	}
	
}
	