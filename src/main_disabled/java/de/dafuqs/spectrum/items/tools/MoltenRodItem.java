package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class MoltenRodItem extends SpectrumFishingRodItem {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/equipment/molten_rod");
	
	public MoltenRodItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.is(SpectrumFluidTags.MOLTEN_ROD_FISHABLE_IN);
	}
	
	@Override
	public void spawnBobber(Player user, Level world, int luckBonus, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean shouldSmeltDrops) {
		world.addFreshEntity(new MoltenFishingBobberEntity(user, world, luckBonus, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.molten_rod.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.molten_rod.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
}
