package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PaintbrushItem extends Item implements SignApplicator {
	
	public static final int COOLDOWN_DURATION_TICKS = 10;
	public static final int BLOCK_COLOR_COST = 25;
	public static final int INK_SLING_COST = 100;
	
	public PaintbrushItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			appendClientTooltips(stack, tooltip);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private static void appendClientTooltips(ItemStack stack, List<Component> tooltip) {
		boolean unlockedColoring = AdvancementHelper.hasAdvancementClient(SpectrumAdvancements.PAINTBRUSH_COLORING);
		boolean unlockedSlinging = AdvancementHelper.hasAdvancementClient(SpectrumAdvancements.PAINTBRUSH_INK_SLINGING);
		if (unlockedColoring || unlockedSlinging) {
			Optional<InkColor> color = getColor(stack);
			if (color.isEmpty()) {
				tooltip.add(Component.translatable("item.spectrum.paintbrush.tooltip.select_color"));
			}
		}
		
		tooltip.add(Component.translatable("item.spectrum.paintbrush.ability.header").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.paintbrush.ability.pedestal_triggering").withStyle(ChatFormatting.GRAY));
		if (unlockedColoring) {
			tooltip.add(Component.translatable("item.spectrum.paintbrush.ability.block_coloring").withStyle(ChatFormatting.GRAY));
		}
		if (unlockedSlinging) {
			tooltip.add(Component.translatable("item.spectrum.paintbrush.ability.ink_slinging").withStyle(ChatFormatting.GRAY));
		}
	}
	
	public static boolean canColor(Player player) {
		return AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.PAINTBRUSH_COLORING);
	}
	
	public static boolean canInkSling(Player player) {
		return AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.PAINTBRUSH_INK_SLINGING);
	}
	
	public MenuProvider createScreenHandlerFactory(ItemStack itemStack) {
		return new SimpleMenuProvider((syncId, inventory, player) ->
				new PaintbrushScreenHandler(syncId, inventory, itemStack),
				Component.translatable("item.spectrum.paintbrush")
		);
	}
	
	@Override
	public Component getName(ItemStack stack) {
		Component name = Component.translatable(this.getDescriptionId(stack));
		
		Optional<InkColor> color = getColor(stack);
		if (color.isPresent()) {
			InkColor inkColor = color.get();
			name = inkColor.getColoredName().append(" ").append(name);
		}
		
		return name;
	}
	
	public static void setColor(ItemStack stack, @Nullable InkColor color) {
		stack.set(SpectrumDataComponentTypes.INK_COLOR, color);
	}
	
	public static Optional<InkColor> getColor(ItemStack stack) {
		return Optional.ofNullable(stack.get(SpectrumDataComponentTypes.INK_COLOR));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (canColor(context.getPlayer()) && tryColorBlock(context)) {
			return InteractionResult.sidedSuccess(world.isClientSide);
		}
		return super.useOn(context);
	}
	
	private boolean tryColorBlock(UseOnContext context) {
		Optional<InkColor> inkColor = getColor(context.getItemInHand());
		if (inkColor.isEmpty()) {
			return false;
		}
		Optional<DyeColor> dyeColor = inkColor.get().getDyeColor();
		
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof ColorableBlock colorableBlock) {
			if (!colorableBlock.isColor(world, pos, dyeColor)) {
				if (payBlockColorCost(context.getPlayer(), inkColor.get()) && colorableBlock.color(world, pos, dyeColor, context.getPlayer())) {
					context.getLevel().playSound(null, context.getClickedPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					context.getLevel().playSound(null, context.getClickedPos(), SpectrumSoundEvents.USE_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
			return false;
		}
		
		return cursedColor(context);
	}
	
	private boolean cursedColor(UseOnContext context) {
		Level world = context.getLevel();
		if (context.getPlayer() == null) {
			return false;
		}
		
		Optional<InkColor> optionalInkColor = getColor(context.getItemInHand());
		if (optionalInkColor.isEmpty()) {
			return false;
		}
		
		InkColor inkColor = optionalInkColor.get();
		Optional<DyeColor> optionalDyeColor = inkColor.getDyeColor();
		if (optionalDyeColor.isEmpty()) {
			return false;
		}
		DyeColor dyeColor = optionalDyeColor.get();
		
		BlockState newBlockState = BlockVariantHelper.getCursedBlockColorVariant(context.getLevel(), context.getClickedPos(), dyeColor);
		if (newBlockState.isAir()) {
			return false;
		}

		if (payBlockColorCost(context.getPlayer(), inkColor)) {
			if (!world.isClientSide) {
				world.setBlockAndUpdate(context.getClickedPos(), newBlockState);
				world.playSound(null, context.getClickedPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
			return true;
		} else {
			if (world.isClientSide) {
				context.getPlayer().playSound(SpectrumSoundEvents.USE_FAIL, 1.0F, 1.0F);
			}
		}
		return false;
	}
	
	private boolean payBlockColorCost(Player player, InkColor inkColor) {
		if (player == null) {
			return false;
		}
		if (player.isCreative()) {
			return true;
		}
		if (InkPowered.tryDrainEnergy(player, inkColor, BLOCK_COLOR_COST)) {
			return true;
		}
		Optional<DyeColor> dyeColor = inkColor.getDyeColor();
		if (dyeColor.isEmpty()) {
			return false;
		}
		return InventoryHelper.removeFromInventoryWithRemainders(player, PigmentItem.byColor(inkColor).getDefaultInstance());
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (user.isShiftKeyDown()) {
			if (user instanceof ServerPlayer serverPlayerEntity) {
				if (canColor(serverPlayerEntity)) {
					serverPlayerEntity.openMenu(createScreenHandlerFactory(user.getItemInHand(hand)));
				}
			}
			return InteractionResultHolder.pass(user.getItemInHand(hand));
		} else if (canInkSling(user)) {
			Optional<InkColor> optionalInkColor = getColor(user.getItemInHand(hand));
			if (optionalInkColor.isPresent()) {
				
				InkColor inkColor = optionalInkColor.get();
				if (user.isCreative() || InkPowered.tryDrainEnergy(user, inkColor, INK_SLING_COST)) {
					user.getCooldowns().addCooldown(this, COOLDOWN_DURATION_TICKS);
					
					if (!world.isClientSide) {
						InkProjectileEntity.shoot(world, user, inkColor);
					}
					// cause the slightest bit of knockback (more if Red)
					if (!user.isCreative()) {
						if(inkColor == InkColors.RED)
						{
							causeKnockback(user, user.getYRot(), user.getXRot(), 0.1F, 0.5F);
						}
						else{
							causeKnockback(user, user.getYRot(), user.getXRot(), 0, 0.3F);
						}
					}
				} else {
					if (world.isClientSide) {
						user.playSound(SpectrumSoundEvents.USE_FAIL, 1.0F, 1.0F);
					}
				}
				
				return InteractionResultHolder.pass(user.getItemInHand(hand));
			}
		}
		return super.use(world, user, hand);
	}
	
	private void causeKnockback(Player user, float yaw, float pitch, float roll, float multiplier) {
		float f = Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F) * multiplier;
		float g = Mth.sin((pitch + roll) * 0.017453292F) * multiplier;
		float h = -Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F) * multiplier;
		user.push(f, g, h);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		Level world = user.level();
		if (canColor(user) && GenericClaimModsCompat.canInteract(entity.level(), entity, user)) {
			Optional<InkColor> color = getColor(stack);
			
			if (color.isPresent()
					&& payBlockColorCost(user, color.get())
					&& EntityColorProcessorRegistry.colorEntity(entity, color.get().getDyeColor(), entity instanceof Player player ? player : null)) {
				
				entity.level().playSound(null, entity, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
				return InteractionResult.sidedSuccess(world.isClientSide);
			}
			
		}
		return super.interactLivingEntity(stack, user, entity, hand);
	}

	@Override
	public boolean tryApplyToSign(Level world, SignBlockEntity signBlockEntity, boolean front, Player player) {
		if (tryUseOnSign(world, signBlockEntity, front, player, player.getItemInHand(InteractionHand.MAIN_HAND))) return true;
		if (tryUseOnSign(world, signBlockEntity, front, player, player.getItemInHand(InteractionHand.OFF_HAND))) return true;
		
		player.playSound(SpectrumSoundEvents.USE_FAIL, 1.0F, 1.0F);
		return false;
	}
	
	// TODO: can this be moved to ColorableBlock / as a block color processor?
	private boolean tryUseOnSign(Level world, SignBlockEntity signBlockEntity, boolean front, Player player, ItemStack stack) {
		if (stack.is(SpectrumItems.PAINTBRUSH)) {
			Optional<InkColor> color = getColor(stack);
			if (color.isPresent()) {
				InkColor inkColor = color.get();
				Optional<DyeColor> dyeColor = inkColor.getDyeColor();

				if (canColor(player) && payBlockColorCost(player, inkColor)) {
					if (signBlockEntity.updateText(signText -> {
						if (dyeColor.isPresent()) {
							return signText.setColor(dyeColor.get());
						}
						return signText;
					}, front)) {
						world.playSound(null, signBlockEntity.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundSource.BLOCKS, 1.0F, 1.0F);
						return true;
					}
				}
			}
		}

		return false;
	}
}
