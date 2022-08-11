package de.dafuqs.spectrum.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InkSpellEffect {

	abstract int baseRange();
	abstract void affectEntity(LivingEntity entity, float potency);
	abstract void affectArea(World world, BlockPos centerPos, float potency);
	
}
