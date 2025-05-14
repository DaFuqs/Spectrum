package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkDrainTrinketItem extends SpectrumTrinketItem implements InkStorageItem<FixedSingleInkStorage>, ExtendedItemBarProvider, SlotBackgroundEffectProvider {
	
	/**
	 * TODO: set to the original value again, once ink networking is in. Currently the original max value cannot be achieved.
	 * Players WILL grind out that amount of pigment in some way and will then complain
	 * <p>
	 * lmao trueee ~ Azzyypaaras.
	 */
	public static final int MAX_INK = 3276800; // 1677721600;
	public final InkColor inkColor;
	
	public InkDrainTrinketItem(Properties settings, ResourceLocation unlockIdentifier, InkColor inkColor) {
		super(settings, unlockIdentifier);
		this.inkColor = inkColor;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		
		if (storedInk >= MAX_INK) {
			tooltip.add(Component.translatable("spectrum.tooltip.ink_drain.tooltip.maxed_out").withStyle(ChatFormatting.GRAY));
		} else {
			long nextStepInk;
			int pow = 0;
			do {
				nextStepInk = (long) (100 * Math.pow(8, pow));
				pow++;
			} while (storedInk >= nextStepInk);
			
			tooltip.add(Component.translatable("spectrum.tooltip.ink_drain.tooltip.ink_for_next_step", storedInk, inkStorage.getStoredColor().getColoredInkName(), Support.getShortenedNumberString(nextStepInk - storedInk)).withStyle(ChatFormatting.GRAY));
		}
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return isMaxedOut(stack);
	}
	
	private boolean isMaxedOut(ItemStack stack) {
		return getEnergyStorage(stack).isFull();
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultInstance() {
		return super.getDefaultInstance();
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.NEVER;
	}
	
	@Override
	public FixedSingleInkStorage getEnergyStorage(ItemStack itemStack) {
		var storage = itemStack.get(SpectrumDataComponentTypes.INK_STORAGE);
		if (storage != null)
			for (var entry : storage.storedEnergy().entrySet())
				return new FixedSingleInkStorage(storage.maxEnergyTotal(), entry.getKey(), entry.getValue());
		return new FixedSingleInkStorage(MAX_INK, inkColor);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		itemStack.set(SpectrumDataComponentTypes.INK_STORAGE, new InkStorageComponent(storage));
		itemStack.set(DataComponents.RARITY, storage.isFull() ? Rarity.EPIC : super.getDefaultInstance().get(DataComponents.RARITY));
	}
	
	@Override
	public ItemStack getFullStack() {
		return InkStorageItem.super.getFullStack();
	}
	
	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean allowVanillaDurabilityBarRendering(@Nullable Player player, ItemStack stack) {
		return false;
	}
	
	@Override
	public BarSignature getSignature(@Nullable Player player, @NotNull ItemStack stack, int index) {
		var inkTank = getEnergyStorage(stack);
		var progress = (int) Math.round(Mth.clampedLerp(0, 14, Math.log(inkTank.getEnergy(inkColor) / 100.0f) / Math.log(8) / 5.0F));
		
		if (progress == 0 || progress == 14)
			return PASS;
		
		return new BarSignature(1, 13, 14, progress, 1, inkColor.getTextColorInt(), 2, ExtendedItemBarProvider.DEFAULT_BACKGROUND_COLOR);
	}
	
	@Override
	public float getEffectOpacity(@Nullable Player player, ItemStack stack, float tickDelta) {
		var inkTank = getEnergyStorage(stack);
		return (float) (Math.log(inkTank.getEnergy(inkColor) / 100.0f) / Math.log(8) / 5.0F);
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var inkTank = getEnergyStorage(stack);
		return inkTank.isFull() ? SlotEffect.PULSE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return inkColor.getColorInt();
	}
}
