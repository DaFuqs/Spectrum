package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.helpers.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.inventories.WorkstaffScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MalachiteWorkstaffItem extends MultiToolItem implements AoEBreakingTool {
	
	public enum Variant {
		GLASS,
		MOONSTONE
	}
	
	public enum GUIToggle {
		SELECT_SILK_TOUCH("item.spectrum.workstaff.message.silk_touch"),
		SELECT_FORTUNE("item.spectrum.workstaff.message.fortune"),
		SELECT_RESONANCE("item.spectrum.workstaff.message.resonance"),
		SELECT_1x1("item.spectrum.workstaff.message.1x1"),
		SELECT_3x3("item.spectrum.workstaff.message.3x3"),
		SELECT_5x5("item.spectrum.workstaff.message.5x5"),
		ENABLE_RIGHT_CLICK_ACTIONS("item.spectrum.workstaff.message.enabled_right_click_actions"),
		DISABLE_RIGHT_CLICK_ACTIONS("item.spectrum.workstaff.message.disabled_right_click_actions");
		
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
	
	private final Variant variant;
	
	public MalachiteWorkstaffItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings, Variant variant) {
		super(material, attackDamage, attackSpeed, settings);
		this.variant = variant;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			if (user instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.openHandledScreen(createScreenHandlerFactory(user.getStackInHand(hand)));
			}
			return TypedActionResult.pass(user.getStackInHand(hand));
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
		if(!canTill(stack.getNbt())) {
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.right_click_actions_disabled").formatted(Formatting.DARK_RED));
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
		if(nbt == null || !nbt.contains(RANGE_NBT_STRING, NbtElement.INT_TYPE)) {
			return 0;
		}
		return nbt.getInt(RANGE_NBT_STRING);
	}
	
	public static void applyToggle(PlayerEntity player, ItemStack stack, GUIToggle toggle) {
		NbtCompound nbt = stack.getOrCreateNbt();
		switch (toggle) {
			case SELECT_1x1 -> {
				nbt.remove(RANGE_NBT_STRING);
			}
			case SELECT_3x3 -> {
				nbt.putInt(RANGE_NBT_STRING, 1);
			}
			case SELECT_5x5 -> {
				nbt.putInt(RANGE_NBT_STRING, 2);
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
				} else if(player instanceof ServerPlayerEntity serverPlayerEntity) {
					triggerUnenchantedWorkstaffAdvancement(serverPlayerEntity);
				}
			}
			case ENABLE_RIGHT_CLICK_ACTIONS -> {
				nbt.remove(RIGHT_CLICK_DISABLED_NBT_STRING);
			}
			case DISABLE_RIGHT_CLICK_ACTIONS -> {
				nbt.putBoolean(RIGHT_CLICK_DISABLED_NBT_STRING, true);
			}
		}
		stack.setNbt(nbt);
	}
	
	private static void triggerUnenchantedWorkstaffAdvancement(ServerPlayerEntity player) {
		player.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 0.75F, 1.0F);
		Support.grantAdvancementCriterion(player, "lategame/trigger_unenchanted_workstaff", "code_triggered");
	}
	
}
