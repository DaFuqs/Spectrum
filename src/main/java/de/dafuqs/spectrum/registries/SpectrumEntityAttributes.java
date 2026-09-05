package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;
import org.jspecify.annotations.*;

public class SpectrumEntityAttributes {
	
	// TODO: this is not the best place for this
	public static final ResourceLocation CRIT_MODIFIER_ID = SpectrumCommon.locate("crit_modifier");
	public static final ResourceLocation REACH_MODIFIER_ID = SpectrumCommon.locate("reach_modifier");
	
	
	
	private static final DeferredRegister<Attribute> REGISTRAR = DeferredRegister.create(Registries.ATTRIBUTE, SpectrumCommon.MOD_ID);
	
	public static final Holder<Attribute> LOOT_CHANCE_MULTIPLIER = REGISTRAR.register("loot_chance_multiplier", () -> new RangedAttribute("attribute.name.spectrum.loot_chance_multiplier", 1.0, 0, 1024));
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
	public static float modifyLootChance(float originalChance, @Nullable Entity entity) {
		if (entity instanceof Player player) {
			float lootChanceMultiplier = (float) player.getAttributeValue(LOOT_CHANCE_MULTIPLIER);
			return originalChance * lootChanceMultiplier;
		}
		return originalChance;
	}
	
}
