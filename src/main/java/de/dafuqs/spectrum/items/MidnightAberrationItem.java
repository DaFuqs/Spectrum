package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MidnightAberrationItem extends CloakedItem {
	
	private static final Identifier MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/crumble_midnight_aberration");
	private static final String MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_CRITERION = "have_midnight_aberration_crumble";
	
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
