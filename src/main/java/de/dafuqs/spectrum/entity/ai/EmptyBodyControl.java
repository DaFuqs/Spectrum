package de.dafuqs.spectrum.entity.ai;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.*;

public class EmptyBodyControl extends BodyRotationControl {
	
	protected final Mob entity;
	
	public EmptyBodyControl(Mob entity) {
		super(entity);
		this.entity = entity;
	}
	
	@Override
	public void clientTick() {
		entity.yHeadRot = entity.yBodyRot;
		entity.yBodyRot = entity.getYRot();
	}
	
}