package de.dafuqs.spectrum.api.energy;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface InkPowered {
	
	/**
	 * The advancement the player needs to have in order to use ink powered tools
	 */
	ResourceLocation REQUIRED_ADVANCEMENT = SpectrumCommon.locate("milestones/unlock_ink_use");
	
	static boolean canUseClient() {
		Minecraft client = Minecraft.getInstance();
		return canUse(client.player);
	}
	
	static boolean canUse(Player playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, InkPowered.REQUIRED_ADVANCEMENT);
	}
	
	/**
	 * The colors that the object requires for working.
	 * These are added as the player facing tooltip
	 **/
	List<InkColor> getUsedColors();
	
	/**
	 * The colors that the object requires for working.
	 * These are added as the player facing tooltip
	 **/
	default void addInkPoweredTooltip(List<Component> tooltip) {
		if (getUsedColors().size() > 1) {
			tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.prefix").withStyle(ChatFormatting.GRAY));
			for (InkColor color : getUsedColors()) {
				tooltip.add(color.getColoredInkName().withStyle(ChatFormatting.GRAY));
			}
		} else {
			tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.consume", getUsedColors().get(0).getColoredInkName()).withStyle(ChatFormatting.GRAY));
		}
	}
	
	private static long tryDrainEnergy(@NotNull ItemStack stack, InkColor color, long amount, @Nullable Player player) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			if (!inkStorageItem.getDrainability().canDrain(player != null)) {
				return 0;
			}
			
			InkStorage inkStorage = inkStorageItem.getEnergyStorage(stack);
			long drained = inkStorage.drainEnergy(color, amount);
			
			if (drained > 0) {
				if (player instanceof ServerPlayer serverPlayerEntity) {
					SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayerEntity, stack, inkStorage, color, -amount);
				}
				inkStorageItem.setEnergyStorage(stack, inkStorage);
			}
			
			return drained;
		}
		return 0;
	}
	
	private static long tryGetEnergy(@NotNull ItemStack stack, InkColor color) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			return inkStorageItem.getEnergyStorage(stack).getEnergy(color);
		}
		return 0;
	}
	
	static boolean tryDrainEnergy(@NotNull Container inventory, InkColor color, long amount) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack currentStack = inventory.getItem(i);
			if (!currentStack.isEmpty()) {
				amount -= tryDrainEnergy(currentStack, color, amount, null);
				if (amount <= 0) return true;
			}
		}
		return false;
	}
	
	static boolean tryDrainEnergy(@NotNull Player player, @NotNull InkColor color, long amount) {
		if (player.isCreative()) return true;
		
		for (ItemStack itemStack : player.getInventory().items) {
			amount -= tryDrainEnergy(itemStack, color, amount, player);
			if (amount <= 0) return true;
		}
		
		return false;
	}
	
}
