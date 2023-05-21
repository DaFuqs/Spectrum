package de.dafuqs.spectrum.spells;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public abstract class InkSpellEffect {
	
	final InkColor color;
	
	public InkSpellEffect(InkColor color) {
		this.color = color;
	}
	
	public abstract void playEffects(World world, Vec3d origin, float potency);
	
	abstract void affectEntity(Entity entity, Vec3d origin, float potency);
	
	abstract void affectArea(World world, BlockPos origin, float potency);
	
	public static void trigger(InkColor inkColor, World world, Vec3d position, float potency) {
		InkSpellEffect effect = InkSpellEffects.getEffect(inkColor);
		if (effect != null) {
			if (world instanceof ServerWorld) {
				SpectrumS2CPacketSender.playInkEffectParticles((ServerWorld) world, inkColor, position, potency);
			} else {
				effect.playEffects(world, position, potency);
			}
			List<Entity> entities = world.getNonSpectatingEntities(Entity.class, Box.of(position, potency / 2, potency / 2, potency / 2));
			for (Entity entity : entities) {
				effect.affectEntity(entity, position, potency);
			}
			effect.affectArea(world, new BlockPos(position.x, position.y, position.z), potency);
		}
	}
	
}
