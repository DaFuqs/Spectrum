package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MidnightAberrationItem extends CloakedItem implements FabricItem {
	
	private static final Identifier MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/crumble_midnight_aberration");
	private static final String MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_CRITERION = "have_midnight_aberration_crumble";
	
	// Aberrations crumble in the player's inventory (or any inventory that ticks)
	// but only after a short grace period, to give them a chance to actually look at it / use it
	private static final int CRUMBLING_GRACE_PERIOD_TICKS = 40;
	private static final String FIRST_INVENTORY_TICK_NBT = "first_inventory_tick";
	
	public MidnightAberrationItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if (!world.isClient && world.getTime() % 20 == 0 && entity instanceof ServerPlayerEntity player) {
			NbtCompound compound = stack.getNbt();
			if (compound != null && compound.getBoolean("Stable")) {
				return;
			}
			
			NbtCompound nbtCompound = stack.getOrCreateNbt();
			if (!nbtCompound.contains(FIRST_INVENTORY_TICK_NBT)) {
				nbtCompound.putLong(FIRST_INVENTORY_TICK_NBT, world.getTime());
				return;
			}
			long firstInventoryTick = nbtCompound.getLong(FIRST_INVENTORY_TICK_NBT);
			if (world.getTime() < firstInventoryTick + CRUMBLING_GRACE_PERIOD_TICKS) {
				return;
			}
			
			// check if it's a real stack in the player's inventory or just a proxy item (like a Bottomless Bundle)
			if (world.random.nextFloat() < 0.2F) {
				stack.decrement(1);
				player.getInventory().offerOrDrop(Items.GUNPOWDER.getDefaultStack());
				world.playSoundFromEntity(null, player, SpectrumSoundEvents.MIDNIGHT_ABERRATION_CRUMBLING, SoundCategory.PLAYERS, 0.5F, 1.0F);
				
				Support.grantAdvancementCriterion(player, MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_ID, MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_CRITERION);
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		NbtCompound compound = stack.getNbt();
		if (compound != null && compound.getBoolean("Stable")) {
			tooltip.add(Text.translatable("item.spectrum.midnight_aberration.tooltip.stable"));
		}
	}
	
	@Override
	public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		// do not play the hand update animation when it starts crumbling
		NbtCompound oldNbt = oldStack.getNbt();
		NbtCompound newNbt = newStack.getNbt();
		
		if (newNbt == null) {
			return super.allowNbtUpdateAnimation(player, hand, oldStack, newStack);
		}
		if (oldNbt != null && oldNbt.contains(FIRST_INVENTORY_TICK_NBT)) {
			return super.allowNbtUpdateAnimation(player, hand, oldStack, newStack);
		}
		if (!newNbt.contains(FIRST_INVENTORY_TICK_NBT)) {
			return super.allowNbtUpdateAnimation(player, hand, oldStack, newStack);
		}
		
		return false;
	}
	
	public ItemStack getStableStack() {
		ItemStack stack = getDefaultStack();
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putBoolean("Stable", true);
		stack.setNbt(compound);
		return stack;
	}
	
	@Override
	public @Nullable Pair<Item, MutableText> getCloakedItemTranslation() {
		return new Pair<>(this, Text.translatable("item.spectrum.midnight_aberration.cloaked"));
	}
	
}
