package de.dafuqs.spectrum.items.tools;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

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

		public Text getTriggerText() {
			return Text.translatable(triggerText);
		}
		
	}
	
	public static final String RANGE_NBT_STRING = "Range";
	public static final String RIGHT_CLICK_DISABLED_NBT_STRING = "RightClickDisabled";
	public static final String PROJECTILES_DISABLED_NBT_STRING = "ProjectilesDisabled";

    public WorkstaffItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			if (user instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.openHandledScreen(createScreenHandlerFactory(user.getStackInHand(hand)));
			}
			return TypedActionResult.consume(user.getStackInHand(hand));
		}
		return super.use(world, user, hand);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		int range = getAoERange(stack);
		if(range > 0) {
			int displayedRange = 1 + range + range;
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.mining_range", displayedRange, displayedRange).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public boolean canTill(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		return nbt == null || !nbt.getBoolean(RIGHT_CLICK_DISABLED_NBT_STRING);
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
				new WorkstaffScreenHandler(syncId, inventory, itemStack),
				Text.translatable("item.spectrum.workstaff")
		);
	}
	
	@Override
	public int getAoERange(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains(RANGE_NBT_STRING, NbtElement.NUMBER_TYPE)) {
			return 0;
		}
		return nbt.getInt(RANGE_NBT_STRING);
	}
	
	@Override
	public boolean canUseAoE(PlayerEntity player, ItemStack stack) {
		int range = getAoERange(stack);
		if (range <= 0) {
			return true;
		}
		
		int costForRange = (int) Math.pow(BASE_COST_PER_AOE_MINING_RANGE_INCREMENT.getCost(), range);
		return InkPowered.tryDrainEnergy(player, BASE_COST_PER_AOE_MINING_RANGE_INCREMENT.getColor(), costForRange);
	}
	
	public static void applyToggle(PlayerEntity player, ItemStack stack, GUIToggle toggle) {
		
		switch (toggle) {
			case SELECT_1x1 -> {
				stack.getOrCreateNbt().remove(RANGE_NBT_STRING);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case SELECT_3x3 -> {
				stack.getOrCreateNbt().putInt(RANGE_NBT_STRING, 1);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case SELECT_5x5 -> {
				stack.getOrCreateNbt().putInt(RANGE_NBT_STRING, 2);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			// switching to another enchantment
			// fortune handling is a bit special. Its level is preserved in NBT,
			// to restore the original enchant level when switching back
			case SELECT_FORTUNE -> {
				enchantAndRemoveOthers(player, stack, toggle.getTriggerText(), Enchantments.FORTUNE);
			}
			case SELECT_SILK_TOUCH -> {
				enchantAndRemoveOthers(player, stack, toggle.getTriggerText(), Enchantments.SILK_TOUCH);
			}
			case SELECT_RESONANCE -> {
				enchantAndRemoveOthers(player, stack, toggle.getTriggerText(), SpectrumEnchantments.RESONANCE);
			}
			case ENABLE_RIGHT_CLICK_ACTIONS -> {
				stack.getOrCreateNbt().remove(RIGHT_CLICK_DISABLED_NBT_STRING);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case DISABLE_RIGHT_CLICK_ACTIONS -> {
				stack.getOrCreateNbt().putBoolean(RIGHT_CLICK_DISABLED_NBT_STRING, true);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case ENABLE_PROJECTILES -> {
				stack.getOrCreateNbt().remove(PROJECTILES_DISABLED_NBT_STRING);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case DISABLE_PROJECTILES -> {
				stack.getOrCreateNbt().putBoolean(PROJECTILES_DISABLED_NBT_STRING, true);
				player.sendMessage(toggle.getTriggerText(), true);
			}
		}
	}
	
	private static void enchantAndRemoveOthers(PlayerEntity player, ItemStack stack, Text message, Enchantment enchantment) {
		int existingLevel = EnchantmentHelper.getLevel(enchantment, stack);
		if (existingLevel > 0) {
			player.sendMessage(Text.translatable("item.spectrum.workstaff.message.already_has_the_enchantment"), true);
			return;
		}
		
		int level = 1;
		
		NbtCompound nbt = stack.getOrCreateNbt();
		if (enchantment == Enchantments.FORTUNE) {
			if (nbt.contains("FortuneLevel", NbtElement.NUMBER_TYPE)) {
				level = nbt.getInt("FortuneLevel");
				nbt.remove("FortuneLevel");
			}
		} else {
			int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack);
			if (fortuneLevel > 0) {
				nbt.putInt("FortuneLevel", fortuneLevel);
			}
		}
		
		ItemStack newStack = stack.copy();
		var result = SpectrumEnchantmentHelper.removeEnchantments(newStack, Enchantments.SILK_TOUCH, SpectrumEnchantments.RESONANCE, Enchantments.FORTUNE);
		if (result.getRight() == 0) {
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				triggerUnenchantedWorkstaffAdvancement(serverPlayerEntity);
			}
		} else {
			var result2 = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(result.getLeft(), enchantment, level, false, AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.APPLY_CONFLICTING_ENCHANTMENTS));
			if (result2.getLeft()) {
				stack.setNbt(result2.getRight().getNbt());
				player.sendMessage(message, true);
			} else {
				player.sendMessage(Text.translatable("item.spectrum.workstaff.message.would_result_in_conflicting_enchantments"), true);
			}
		}
	}
	
	private static void triggerUnenchantedWorkstaffAdvancement(ServerPlayerEntity player) {
		player.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 0.75F, 1.0F);
		Support.grantAdvancementCriterion(player, "lategame/trigger_unenchanted_workstaff", "code_triggered");
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.FORTUNE, 4);
	}

	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}

}
