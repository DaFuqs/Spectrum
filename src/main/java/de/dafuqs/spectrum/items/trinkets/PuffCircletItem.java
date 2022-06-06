package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PuffCircletItem extends AzureDikeTrinketItem {
	
	public static final float PROJECTILE_DEFLECTION_COST = 2;
	public static final float FALL_DAMAGE_NEGATING_COST = 2;
	
	public PuffCircletItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_ashen_circlet"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.puff_circlet.tooltip"));
		tooltip.add(new TranslatableText("item.spectrum.puff_circlet.tooltip2"));
	}
	
	@Override
	public int maxAzureDike(ItemStack stack) {
		return 4;
	}
	
}