package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AzureDikeBeltItem extends AzureDikeTrinketItem {
	
	public AzureDikeBeltItem(Settings settings) {
		super(settings, SpectrumCommon.locate("progression/unlock_azure_dike_ring"));
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.azure_dike_belt.tooltip"));
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		return 6;
	}
	
	@Override
	public float rechargeBonusAfterDamageTicks(ItemStack stack) {
		return 100;
	}
	
}