package de.dafuqs.spectrum.items;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class MysteriousCompassItem extends StructureCompassItem implements SlotBackgroundEffectProvider {

	public MysteriousCompassItem(Settings settings) {
		super(settings, SpectrumStructureTags.MYSTERIOUS_COMPASS_LOCATED);
	}
	
	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient && world.getTime() % 200 == 0 && entity instanceof PlayerEntity player)
			if (AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.MYSTERIOUS_LOCKET_SOCKETING)) {
				locateStructure(stack, world, entity);
			} else {
				removeStructurePos(stack);
		}
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		return SlotEffect.FULL_PACKAGE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return 0xFFFFFF;
	}
}
