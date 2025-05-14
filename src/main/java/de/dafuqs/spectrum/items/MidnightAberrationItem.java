package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MidnightAberrationItem extends CloakedItem implements FabricItem {
	
	private static final ResourceLocation MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/crumble_midnight_aberration");
	private static final String MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_CRITERION = "have_midnight_aberration_crumble";
	
	// Aberrations crumble in the player's inventory (or any inventory that ticks)
	// but only after a short grace period, to give them a chance to actually look at it / use it
	private static final int CRUMBLING_GRACE_PERIOD_TICKS = 40;
	private static final String FIRST_INVENTORY_TICK_NBT = "first_inventory_tick";
	
	public MidnightAberrationItem(Item.Properties settings, ResourceLocation cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if (!world.isClientSide && world.getGameTime() % 20 == 0 && entity instanceof ServerPlayer player) {
			if (stack.has(SpectrumDataComponentTypes.STABLE))
				return;
			
			if (!stack.has(SpectrumDataComponentTypes.TIMESTAMP)) {
				stack.set(SpectrumDataComponentTypes.TIMESTAMP, world.getGameTime());
				return;
			}
			
			long firstInventoryTick = stack.get(SpectrumDataComponentTypes.TIMESTAMP);
			if (world.getGameTime() < firstInventoryTick + CRUMBLING_GRACE_PERIOD_TICKS) {
				return;
			}
			
			// check if it's a real stack in the player's inventory or just a proxy item (like a Bottomless Bundle)
			if (world.random.nextFloat() < 0.2F) {
				stack.shrink(1);
				player.getInventory().placeItemBackInInventory(Items.GUNPOWDER.getDefaultInstance());
				world.playSound(null, player, SpectrumSoundEvents.MIDNIGHT_ABERRATION_CRUMBLING, SoundSource.PLAYERS, 0.5F, 1.0F);
				
				Support.grantAdvancementCriterion(player, MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_ID, MIDNIGHT_ABERRATION_CRUMBLING_ADVANCEMENT_CRITERION);
			}
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		if (stack.has(SpectrumDataComponentTypes.STABLE))
			tooltip.add(Component.translatable("item.spectrum.midnight_aberration.tooltip.stable"));
	}
	
	@Override
	public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		// do not play the hand update animation when it starts crumbling
		return oldStack.has(SpectrumDataComponentTypes.TIMESTAMP) != newStack.has(SpectrumDataComponentTypes.TIMESTAMP);
	}
	
	public ItemStack getStableStack() {
		ItemStack stack = getDefaultInstance();
		stack.set(SpectrumDataComponentTypes.STABLE, Unit.INSTANCE);
		return stack;
	}
	
	@Override
	public @Nullable Tuple<Item, MutableComponent> getCloakedItemTranslation() {
		return new Tuple<>(this, Component.translatable("item.spectrum.midnight_aberration.cloaked"));
	}
	
}
