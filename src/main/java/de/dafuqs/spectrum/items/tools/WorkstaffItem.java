package de.dafuqs.spectrum.items.tools;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;

import java.util.*;

public class WorkstaffItem extends MultiToolItem implements AoEBreakingTool, Preenchanted {
	
	protected static final InkCost BASE_COST_PER_AOE_MINING_RANGE_INCREMENT = new InkCost(InkColors.WHITE, 3); // TODO: make pricier once ink networking is in
	
	public enum GUIToggle {
		SELECT_SILK_TOUCH("item.spectrum.workstaff.message.silk_touch"),
		SELECT_FORTUNE("item.spectrum.workstaff.message.fortune"),
		SELECT_RESONANCE("item.spectrum.workstaff.message.resonance"),
		SELECT_1x1("item.spectrum.workstaff.message.1x1"),
		SELECT_3x3("item.spectrum.workstaff.message.3x3"),
		SELECT_5x5("item.spectrum.workstaff.message.5x5"),
		ENABLE_RIGHT_CLICK_ACTIONS("item.spectrum.workstaff.message.enabled_right_click_actions"),
		DISABLE_RIGHT_CLICK_ACTIONS("item.spectrum.workstaff.message.disabled_right_click_actions"),
		ENABLE_PROJECTILES("item.spectrum.workstaff.message.enabled_projectiles"),
		DISABLE_PROJECTILES("item.spectrum.workstaff.message.disabled_projectiles");
		
		private final String triggerText;
		
		GUIToggle(String triggerText) {
			this.triggerText = triggerText;
		}
		
		public Component getTriggerText() {
			return Component.translatable(triggerText);
		}
		
	}
	
	public WorkstaffItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (user.isShiftKeyDown()) {
			if (user instanceof ServerPlayer serverPlayerEntity) {
				serverPlayerEntity.openMenu(createScreenHandlerFactory(user.getItemInHand(hand)));
			}
			return InteractionResultHolder.consume(user.getItemInHand(hand));
		}
		return super.use(world, user, hand);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		int range = getAoERange(stack);
		if (range > 0) {
			int displayedRange = 1 + range + range;
			tooltip.add(Component.translatable("item.spectrum.workstaff.tooltip.mining_range", displayedRange, displayedRange).withStyle(ChatFormatting.GRAY));
		}
	}
	
	@Override
	public boolean canTill(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT).canTill();
	}
	
	public MenuProvider createScreenHandlerFactory(ItemStack itemStack) {
		return new SimpleMenuProvider((syncId, inventory, player) ->
				new WorkstaffScreenHandler(syncId, inventory, itemStack),
				Component.translatable("item.spectrum.workstaff")
		);
	}
	
	@Override
	public boolean canUseAoE(Player player, ItemStack stack) {
		int range = getAoERange(stack);
		if (range <= 0) {
			return true;
		}
		
		int costForRange = (int) Math.pow(BASE_COST_PER_AOE_MINING_RANGE_INCREMENT.cost(), range);
		return InkPowered.tryDrainEnergy(player, BASE_COST_PER_AOE_MINING_RANGE_INCREMENT.color(), costForRange);
	}
	
	public static void applyToggle(Player player, ItemStack stack, GUIToggle toggle) {
		
		switch (toggle) {
			case SELECT_1x1 -> {
				stack.remove(SpectrumDataComponentTypes.AOE);
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
			case SELECT_3x3 -> {
				stack.set(SpectrumDataComponentTypes.AOE, 1);
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
			case SELECT_5x5 -> {
				stack.set(SpectrumDataComponentTypes.AOE, 2);
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
			// Switching to another enchantment
			// fortune handling is a bit special. Its level is preserved,
			// so we can restore the original enchant level when switching back
			case SELECT_FORTUNE -> enchantAndRemoveOthers(player, stack, toggle.getTriggerText(), Enchantments.FORTUNE);
			case SELECT_SILK_TOUCH -> enchantAndRemoveOthers(player, stack, toggle.getTriggerText(), Enchantments.SILK_TOUCH);
			case SELECT_RESONANCE -> enchantAndRemoveOthers(player, stack, toggle.getTriggerText(), SpectrumEnchantmentKeys.RESONANCE);
			case ENABLE_RIGHT_CLICK_ACTIONS -> {
				stack.update(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT, comp -> new WorkstaffComponent(true, comp.canShoot(), comp.fortuneLevel()));
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
			case DISABLE_RIGHT_CLICK_ACTIONS -> {
				stack.update(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT, comp -> new WorkstaffComponent(false, comp.canShoot(), comp.fortuneLevel()));
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
			case ENABLE_PROJECTILES -> {
				stack.update(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT, comp -> new WorkstaffComponent(comp.canTill(), true, comp.fortuneLevel()));
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
			case DISABLE_PROJECTILES -> {
				stack.update(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT, comp -> new WorkstaffComponent(comp.canTill(), false, comp.fortuneLevel()));
				player.displayClientMessage(toggle.getTriggerText(), true);
			}
		}
	}
	
	private static void enchantAndRemoveOthers(Player player, ItemStack stack, Component message, ResourceKey<Enchantment> enchantment) {
		var registryLookup = player.level().registryAccess();
		
		int existingLevel = SpectrumEnchantmentHelper.getLevel(registryLookup, enchantment, stack);
		if (existingLevel > 0) {
			player.displayClientMessage(Component.translatable("item.spectrum.workstaff.message.already_has_the_enchantment"), true);
			return;
		}
		
		int level = 1;
		
		if (enchantment == Enchantments.FORTUNE) {
			level = stack.getOrDefault(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT).fortuneLevel();
		} else {
			int fortuneLevel = SpectrumEnchantmentHelper.getLevel(registryLookup, Enchantments.FORTUNE, stack);
			stack.update(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT, comp -> new WorkstaffComponent(comp.canTill(), comp.canShoot(), Math.max(comp.fortuneLevel(), Math.max(fortuneLevel, 1))));
		}
		
		ItemStack newStack = stack.copy();
		var removeResult = SpectrumEnchantmentHelper.removeEnchantments(registryLookup, newStack, Enchantments.SILK_TOUCH, SpectrumEnchantmentKeys.RESONANCE, Enchantments.FORTUNE);
		if (removeResult.getB() == 0) {
			if (player instanceof ServerPlayer serverPlayerEntity) {
				triggerUnenchantedWorkstaffAdvancement(serverPlayerEntity);
			}
		} else {
			var addResult = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(registryLookup, removeResult.getA(), enchantment, level, false, AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.APPLY_CONFLICTING_ENCHANTMENTS));
			if (addResult.getA()) {
				stack.set(DataComponents.ENCHANTMENTS, addResult.getB().getEnchantments());
				player.displayClientMessage(message, true);
			} else {
				player.displayClientMessage(Component.translatable("item.spectrum.workstaff.message.would_result_in_conflicting_enchantments"), true);
			}
		}
	}
	
	private static void triggerUnenchantedWorkstaffAdvancement(ServerPlayer player) {
		player.playNotifySound(SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 0.75F, 1.0F);
		Support.grantAdvancementCriterion(player, "lategame/trigger_unenchanted_workstaff", "code_triggered");
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.FORTUNE, 4);
	}
	
}
