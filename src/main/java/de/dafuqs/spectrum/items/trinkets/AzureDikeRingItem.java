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

public class AzureDikeRingItem extends SpectrumTrinketItem implements AzureDikeProvider {
	
	private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_azure_dike_ring");
	
	public AzureDikeRingItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		// Only a single trinket of that type may be equipped at once
		if(SpectrumTrinketItem.hasEquipped(entity, this)) {
			return false;
		}
		
		return super.canEquip(stack, slot, entity);
	}
	
	@Override
	protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.azure_dike_ring.tooltip"));
	}
	
	@Override
	public int maxAzureDike() {
		return 3;
	}
	
	@Override
	public float azureDikeChargeBonus() {
		return 0;
	}
	
}