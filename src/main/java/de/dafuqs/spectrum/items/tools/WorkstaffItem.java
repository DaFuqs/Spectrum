package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
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
	
	protected static final InkCost BASE_COST_PER_AOE_MINING_RANGE_INCREMENT = new InkCost(InkColors.WHITE, 10);
	
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
		int range = getRange(stack);
		if(range > 0) {
			int displayedRange = 1 + range + range;
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.mining_range", displayedRange, displayedRange).formatted(Formatting.GRAY));
		}
	}
	
	/**
	 * Invoke shovel, axe and hoe right click actions (in this order)
	 * Like stripping logs, tilling grass paths etc.
	 * To get tilled earth it has to converted to path and then tilled again
	 */
	public ActionResult useOnBlock(ItemUsageContext context) {
		NbtCompound nbt = context.getStack().getNbt();
		if(canTill(nbt)) {
			super.useOnBlock(context);
		}
		return ActionResult.PASS;
	}
	
	public static boolean canTill(NbtCompound nbt) {
		return nbt == null || !nbt.getBoolean(RIGHT_CLICK_DISABLED_NBT_STRING);
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
				new WorkstaffScreenHandler(syncId, inventory, itemStack),
				Text.translatable("item.spectrum.workstaff")
		);
	}
	
	@Override
	public int getRange(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains(RANGE_NBT_STRING, NbtElement.INT_TYPE)) {
			return 0;
		}
		return nbt.getInt(RANGE_NBT_STRING);
	}
	
	@Override
	public boolean canMineAtRange(PlayerEntity player, ItemStack stack) {
		int range = getRange(stack);
		if (range <= 0) {
			return true;
		}
		
		int costForRange = (int) Math.pow(BASE_COST_PER_AOE_MINING_RANGE_INCREMENT.getCost(), range);
		return InkPowered.tryDrainEnergy(player, BASE_COST_PER_AOE_MINING_RANGE_INCREMENT.getColor(), costForRange);
	}
	
	public static void applyToggle(PlayerEntity player, ItemStack stack, GUIToggle toggle) {
		NbtCompound nbt = stack.getOrCreateNbt();
		switch (toggle) {
			case SELECT_1x1 -> {
				nbt.remove(RANGE_NBT_STRING);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case SELECT_3x3 -> {
				nbt.putInt(RANGE_NBT_STRING, 1);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case SELECT_5x5 -> {
				nbt.putInt(RANGE_NBT_STRING, 2);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			// switching to another enchantment
			// fortune is handled a bit special. Its level is preserved in NBT, should the player enchant a different/higher tier
			// of fortune onto the tool, so that level can be restored later
			case SELECT_FORTUNE -> {
				boolean couldRemoveOtherEnchantment = SpectrumEnchantmentHelper.removeEnchantment(stack, Enchantments.SILK_TOUCH);
				couldRemoveOtherEnchantment |= SpectrumEnchantmentHelper.removeEnchantment(stack, SpectrumEnchantments.RESONANCE);
				int fortuneLevel = 3;
				if(nbt.contains("FortuneLevel", NbtElement.INT_TYPE)) {
					fortuneLevel = nbt.getInt("FortuneLevel");
					nbt.remove("FortuneLevel");
				}
				if(couldRemoveOtherEnchantment) {
					SpectrumEnchantmentHelper.addOrExchangeEnchantment(stack, Enchantments.FORTUNE, fortuneLevel, true, true);
					player.sendMessage(toggle.getTriggerText(), true);
				} else if(player instanceof ServerPlayerEntity serverPlayerEntity) {
					triggerUnenchantedWorkstaffAdvancement(serverPlayerEntity);
				}
			}
			case SELECT_SILK_TOUCH -> {
				int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack);
				boolean couldRemoveOtherEnchantment = SpectrumEnchantmentHelper.removeEnchantment(stack, Enchantments.FORTUNE);
				if(couldRemoveOtherEnchantment) {
					nbt.putInt("FortuneLevel", fortuneLevel);
				}
				couldRemoveOtherEnchantment |= SpectrumEnchantmentHelper.removeEnchantment(stack, SpectrumEnchantments.RESONANCE);
				if(couldRemoveOtherEnchantment) {
					SpectrumEnchantmentHelper.addOrExchangeEnchantment(stack, Enchantments.SILK_TOUCH, 1, true, true);
					player.sendMessage(toggle.getTriggerText(), true);
				} else if(player instanceof ServerPlayerEntity serverPlayerEntity) {
					triggerUnenchantedWorkstaffAdvancement(serverPlayerEntity);
				}
			}
			case SELECT_RESONANCE -> {
				int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack);
				boolean couldRemoveOtherEnchantment = SpectrumEnchantmentHelper.removeEnchantment(stack, Enchantments.FORTUNE);
				if(couldRemoveOtherEnchantment) {
					nbt.putInt("FortuneLevel", fortuneLevel);
				}
				couldRemoveOtherEnchantment |= SpectrumEnchantmentHelper.removeEnchantment(stack, Enchantments.SILK_TOUCH);
				if(couldRemoveOtherEnchantment) {
					SpectrumEnchantmentHelper.addOrExchangeEnchantment(stack, SpectrumEnchantments.RESONANCE, 1, true, true);
					player.sendMessage(toggle.getTriggerText(), true);
				} else if (player instanceof ServerPlayerEntity serverPlayerEntity) {
					triggerUnenchantedWorkstaffAdvancement(serverPlayerEntity);
				}
			}
			case ENABLE_RIGHT_CLICK_ACTIONS -> {
				nbt.remove(RIGHT_CLICK_DISABLED_NBT_STRING);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case DISABLE_RIGHT_CLICK_ACTIONS -> {
				nbt.putBoolean(RIGHT_CLICK_DISABLED_NBT_STRING, true);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case ENABLE_PROJECTILES -> {
				nbt.remove(PROJECTILES_DISABLED_NBT_STRING);
				player.sendMessage(toggle.getTriggerText(), true);
			}
			case DISABLE_PROJECTILES -> {
				nbt.putBoolean(PROJECTILES_DISABLED_NBT_STRING, true);
				player.sendMessage(toggle.getTriggerText(), true);
			}
		}
		stack.setNbt(nbt);
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
