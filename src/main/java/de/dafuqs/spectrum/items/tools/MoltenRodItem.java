package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.MoltenFishingBobberEntity;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoltenRodItem extends SpectrumFishingRodItem {
	
	public MoltenRodItem(Settings settings) {
		super(settings);
	}
	

	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(SpectrumFluidTags.MOLTEN_ROD_FISHABLE_IN);
	}
	
	@Override
	public boolean shouldAutosmelt(ItemStack itemStack) {
		return true;
	}
	
	@Override
	public void spawnBobber(PlayerEntity user, World world, int luckOfTheSea, int lure) {
		world.spawnEntity(new MoltenFishingBobberEntity(user, world, luckOfTheSea, lure));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.molten_rod.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.molten_rod.tooltip2").formatted(Formatting.GRAY));
	}
	
}