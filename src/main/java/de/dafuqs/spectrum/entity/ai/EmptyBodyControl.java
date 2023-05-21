package de.dafuqs.spectrum.entity.ai;

import net.minecraft.entity.ai.control.*;
import net.minecraft.entity.mob.*;

public class EmptyBodyControl extends BodyControl {
	
	protected final MobEntity entity;
	
	public EmptyBodyControl(MobEntity entity) {
		super(entity);
		this.entity = entity;
	}
	
	@Override
	public void tick() {
		entity.headYaw = entity.bodyYaw;
		entity.bodyYaw = entity.getYaw();
	}
	
}