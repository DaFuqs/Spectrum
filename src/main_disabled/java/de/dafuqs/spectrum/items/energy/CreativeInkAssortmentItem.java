package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CreativeInkAssortmentItem extends Item implements InkStorageItem<CreativeInkStorage>, CreativeOnlyItem, SlotBackgroundEffectProvider {
	
	public CreativeInkAssortmentItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (!world.isClientSide) {
			BlockEntity blockEntity = world.getBlockEntity(context.getClickedPos());
			if (blockEntity instanceof InkStorageBlockEntity<?> inkStorageBlockEntity) {
				inkStorageBlockEntity.getEnergyStorage().fillCompletely();
				inkStorageBlockEntity.setInkDirty();
				blockEntity.setChanged();
			}
		}
		return super.useOn(context);
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.ALWAYS;
	}
	
	@Override
	public CreativeInkStorage getEnergyStorage(ItemStack itemStack) {
		return new CreativeInkStorage();
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultInstance() {
		return super.getDefaultInstance();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		CreativeOnlyItem.appendTooltip(tooltip);
		getEnergyStorage(stack).addTooltip(tooltip);
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float delta) {
		var colors = new ArrayList<InkColor>();
		
		if (player == null)
			return 0;
		
		var time = player.level().getGameTime() % 864000;
		
		for (InkColor inkColor : SpectrumRegistries.INK_COLOR) {
			colors.add(inkColor);
		}
		
		if (colors.size() == 1) {
			var color = colors.getFirst();
			return SpectrumColorHelper.colorVecToRGB(color.getColorVec());
		}
		
		var curColor = colors.get((int) (time % (30L * colors.size()) / 30));
		var nextColor = colors.get((int) ((time % (30L * colors.size()) / 30 + 1) % colors.size()));
		var blendFactor = (((float) time + delta) % 30) / 30F;
		
		return SpectrumColorHelper.interpolate(curColor.getTextColorVec(), nextColor.getTextColorVec(), blendFactor);
	}
}
