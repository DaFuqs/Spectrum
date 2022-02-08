package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import dev.emi.trinkets.api.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AzureDikeBeltItem extends SpectrumTrinketItem implements AzureDikeProvider {
	
	private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/spectrum_midgame");
	
	public AzureDikeBeltItem(Settings settings) {
		super(settings);
	}
	
	@Override
	protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.azure_dike_belt.tooltip"));
	}
	
	@Override
	public int maxAzureDike() {
		return 4;
	}
	
	@Override
	public float azureDikeChargeBonus() {
		return 0;
	}
	
}