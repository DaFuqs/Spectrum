package de.dafuqs.spectrum.items;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.*;

public class MysteriousCompassItem extends StructureCompassItem implements SlotBackgroundEffectProvider {
	
	public MysteriousCompassItem(Properties settings) {
		super(settings, SpectrumStructureTags.MYSTERIOUS_COMPASS_LOCATED);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide() && world.getGameTime() % 200 == 0 && entity instanceof Player player)
			if (AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.MYSTERIOUS_LOCKET_SOCKETING)) {
				locateStructure(stack, world, entity);
			} else {
				removeStructurePos(stack);
			}
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return SlotEffect.FULL_PACKAGE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return 0xFFFFFF;
	}
}
