package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PaintbrushItem extends Item {
	
	public static final Identifier UNLOCK_COLORING_ADVANCEMENT_ID = SpectrumCommon.locate("collect_pigment");
	public static final Identifier UNLOCK_INK_SLINGING_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/fill_ink_container");
	
	public static final int COOLDOWN_DURATION_TICKS = 10;
	public static final int BLOCK_COLOR_COST = 25;
	public static final int INK_FLING_COST = 100;
	
	public static final String COLOR_NBT_STRING = "Color";
	
	public PaintbrushItem(Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		Optional<InkColor> color = getColor(stack);
		boolean unlockedColoring = AdvancementHelper.hasAdvancementClient(UNLOCK_COLORING_ADVANCEMENT_ID);
		boolean unlockedSlinging = AdvancementHelper.hasAdvancementClient(UNLOCK_INK_SLINGING_ADVANCEMENT_ID);
		
		if (unlockedColoring || unlockedSlinging) {
			if (color.isPresent()) {
				tooltip.add(Text.translatable("spectrum.ink.color." + color.get()));
			} else {
				tooltip.add(Text.translatable("item.spectrum.paintbrush.tooltip.select_color"));
			}
		}
		
		tooltip.add(Text.translatable("item.spectrum.paintbrush.ability.header").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.paintbrush.ability.pedestal_triggering").formatted(Formatting.GRAY));
		if (unlockedColoring) {
			tooltip.add(Text.translatable("item.spectrum.paintbrush.ability.block_coloring").formatted(Formatting.GRAY));
		}
		if (unlockedSlinging) {
			tooltip.add(Text.translatable("item.spectrum.paintbrush.ability.ink_slinging").formatted(Formatting.GRAY));
		}
	}
	
	public static boolean canColor(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, UNLOCK_COLORING_ADVANCEMENT_ID);
	}
	
	public static boolean canInkSling(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, UNLOCK_INK_SLINGING_ADVANCEMENT_ID);
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
				new PaintbrushScreenHandler(syncId, inventory, itemStack),
				Text.translatable("item.spectrum.paintbrush")
		);
	}
	
	public static void setColor(ItemStack stack, @Nullable InkColor color) {
		NbtCompound compound = stack.getOrCreateNbt();
		if (color == null) {
			compound.remove(COLOR_NBT_STRING);
		} else {
			compound.putString(COLOR_NBT_STRING, color.toString());
		}
		stack.setNbt(compound);
	}
	
	public static Optional<InkColor> getColor(ItemStack stack) {
		NbtCompound compound = stack.getNbt();
		if (compound != null && compound.contains(COLOR_NBT_STRING)) {
			return Optional.of(InkColor.of(compound.getString(COLOR_NBT_STRING)));
		}
		return Optional.empty();
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (canColor(context.getPlayer()) && cursedColor(context)) {
			return ActionResult.success(context.getWorld().isClient);
		}
		return super.useOnBlock(context);
	}
	
	private boolean cursedColor(ItemUsageContext context) {
		if (context.getPlayer() == null) {
			return false;
		}

		Optional<InkColor> optionalInkColor = getColor(context.getStack());
		if (optionalInkColor.isEmpty()) {
			return false;
		}

		InkColor inkColor = optionalInkColor.get();
		DyeColor dyeColor = inkColor.getDyeColor();

		BlockState newBlockState = BlockVariantHelper.getCursedBlockColorVariant(context.getWorld(), context.getBlockPos(), dyeColor);
		if (newBlockState.isAir()) {
			return false;
		}

		if (payBlockColorCost(context.getPlayer(), inkColor)) {
			if (!context.getWorld().isClient) {
				context.getWorld().setBlockState(context.getBlockPos(), newBlockState);
				context.getWorld().playSound(null, context.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return true;
		} else {
			if (context.getWorld().isClient) {
				context.getPlayer().playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
		return false;
	}
	
	private boolean payBlockColorCost(PlayerEntity player, InkColor inkColor) {
		return player.isCreative()
				|| InkPowered.tryDrainEnergy(player, inkColor, BLOCK_COLOR_COST)
				|| InventoryHelper.removeFromInventoryWithRemainders(player, PigmentItem.byColor(inkColor.getDyeColor()).getDefaultStack());
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			if (user instanceof ServerPlayerEntity serverPlayerEntity) {
				if (canColor(serverPlayerEntity)) {
					serverPlayerEntity.openHandledScreen(createScreenHandlerFactory(user.getStackInHand(hand)));
				}
			}
			return TypedActionResult.pass(user.getStackInHand(hand));
		} else if (canInkSling(user)) {
			Optional<InkColor> optionalInkColor = getColor(user.getStackInHand(hand));
			if (optionalInkColor.isPresent()) {
				
				InkColor inkColor = optionalInkColor.get();
				if (user.isCreative() || InkPowered.tryDrainEnergy(user, inkColor, INK_FLING_COST)) {
					user.getItemCooldownManager().set(this, COOLDOWN_DURATION_TICKS);
					
					if (!world.isClient) {
						InkProjectileEntity.shoot(world, user, inkColor);
					}
					// cause the slightest bit of knockback
					if (!user.isCreative()) {
						causeKnockback(user, user.getYaw(), user.getPitch(), 0, 0.3F);
					}
				} else {
					if (world.isClient) {
						user.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
					}
				}
				
				return TypedActionResult.pass(user.getStackInHand(hand));
			}
		}
		return super.use(world, user, hand);
	}
	
	private void causeKnockback(PlayerEntity user, float yaw, float pitch, float roll, float multiplier) {
		float f = MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F) * multiplier;
		float g = MathHelper.sin((pitch + roll) * 0.017453292F) * multiplier;
		float h = -MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F) * multiplier;
		user.addVelocity(f, g, h);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (canColor(user)) {
			Optional<InkColor> color = getColor(stack);
			if (color.isPresent() && payBlockColorCost(user, color.get())) {
				boolean colored = ColorHelper.tryColorEntity(user, entity, color.get().getDyeColor());
				if (colored) {
					return ActionResult.success(user.world.isClient);
				}
			}
		}
		return super.useOnEntity(stack, user, entity, hand);
	}
	
}
