package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MoltenRodItem extends SpectrumFishingRodItem {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/equipment/molten_rod");
	
	public MoltenRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(SpectrumFluidTags.MOLTEN_ROD_FISHABLE_IN);
	}
	
	@Override
	public void spawnBobber(PlayerEntity user, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion, boolean foundry) {
		world.spawnEntity(new MoltenFishingBobberEntity(user, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.molten_rod.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.molten_rod.tooltip2").formatted(Formatting.GRAY));
	}
	
}