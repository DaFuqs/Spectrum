package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;

public class ExplosionModifierType {
	
	protected ExplosionArchetype applicableArchetype;
	protected int maxModifiersForType;
	
	public ExplosionModifierType(ExplosionArchetype applicableArchetype, int maxModifiersForType) {
		this.applicableArchetype = applicableArchetype;
		this.maxModifiersForType = maxModifiersForType;
	}
	
	public boolean acceptsArchetype(ExplosionArchetype archetype) {
		return switch (this.applicableArchetype) {
			case ALL, COSMETIC -> true;
			case DESTROY_BLOCKS -> archetype.affectsBlocks;
			case DAMAGE_ENTITIES -> archetype.affectsEntities;
		};
	}
	
	public int getMaxModifiersForType() {
		return maxModifiersForType;
	}
	
	public ResourceLocation getId() {
		return SpectrumRegistries.EXPLOSION_MODIFIER_TYPE.getKey(this);
	}
	
}
