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
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
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

public class PaintbrushItem extends Item implements SignChangingItem {
	
	public static final int COOLDOWN_DURATION_TICKS = 10;
	public static final int BLOCK_COLOR_COST = 25;
	public static final int INK_SLING_COST = 100;
	
	public static final String COLOR_NBT_STRING = "Color";
	
	public PaintbrushItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		if (world != null && world.isClient) {
			appendClientTooltips(stack, tooltip);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private static void appendClientTooltips(ItemStack stack, List<Text> tooltip) {
		boolean unlockedColoring = AdvancementHelper.hasAdvancementClient(SpectrumAdvancements.PAINTBRUSH_COLORING);
		boolean unlockedSlinging = AdvancementHelper.hasAdvancementClient(SpectrumAdvancements.PAINTBRUSH_INK_SLINGING);
		if (unlockedColoring || unlockedSlinging) {
			Optional<InkColor> color = getColor(stack);
			if (color.isEmpty()) {
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
		return AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.PAINTBRUSH_COLORING);
	}
	
	public static boolean canInkSling(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.PAINTBRUSH_INK_SLINGING);
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
				new PaintbrushScreenHandler(syncId, inventory, itemStack),
				Text.translatable("item.spectrum.paintbrush")
		);
	}
	
	@Override
	public Text getName(ItemStack stack) {
		Text name = Text.translatable(this.getTranslationKey(stack));
		
		Optional<InkColor> color = getColor(stack);
		if (color.isPresent()) {
			InkColor inkColor = color.get();
			name = inkColor.getColoredName().append(" ").append(name);
		}
		
		return name;
	}
	
	public static void setColor(ItemStack stack, @Nullable InkColor color) {
		NbtCompound compound = stack.getOrCreateNbt();
		if (color == null) {
			compound.remove(COLOR_NBT_STRING);
		} else {
			compound.putString(COLOR_NBT_STRING, color.getID().toString());
		}
		stack.setNbt(compound);
	}
	
	public static Optional<InkColor> getColor(ItemStack stack) {
		NbtCompound compound = stack.getNbt();
		if (compound != null && compound.contains(COLOR_NBT_STRING)) {
			return InkColor.ofIdString(compound.getString(COLOR_NBT_STRING));
		}
		return Optional.empty();
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (canColor(context.getPlayer()) && tryColorBlock(context)) {
			return ActionResult.success(world.isClient);
		}
		return super.useOnBlock(context);
	}
	
	private boolean tryColorBlock(ItemUsageContext context) {
		Optional<InkColor> inkColor = getColor(context.getStack());
		if (inkColor.isEmpty()) {
			return false;
		}
		DyeColor dyeColor = inkColor.get().getDyeColor();
		
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof ColorableBlock colorableBlock) {
			if (!colorableBlock.isColor(state, dyeColor)) {
				if (payBlockColorCost(context.getPlayer(), inkColor.get()) && colorableBlock.color(world, pos, dyeColor)) {
					context.getWorld().playSound(null, context.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				} else {
					context.getWorld().playSound(null, context.getBlockPos(), SpectrumSoundEvents.USE_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			return false;
		}
		
		return cursedColor(context);
	}
	
	private boolean cursedColor(ItemUsageContext context) {
		World world = context.getWorld();
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
			if (!world.isClient) {
				world.setBlockState(context.getBlockPos(), newBlockState);
				world.playSound(null, context.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return true;
		} else {
			if (world.isClient) {
				context.getPlayer().playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
		return false;
	}
	
	private boolean payBlockColorCost(PlayerEntity player, InkColor inkColor) {
		if (player == null) {
			return false;
		}
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
				if (user.isCreative() || InkPowered.tryDrainEnergy(user, inkColor, INK_SLING_COST)) {
					user.getItemCooldownManager().set(this, COOLDOWN_DURATION_TICKS);
					
					if (!world.isClient) {
						InkProjectileEntity.shoot(world, user, inkColor);
					}
					// cause the slightest bit of knockback (more if Red)
					if (!user.isCreative()) {
						if(inkColor == InkColors.RED)
						{
							causeKnockback(user, user.getYaw(), user.getPitch(), 0.1F, 0.5F);
						}
						else{
							causeKnockback(user, user.getYaw(), user.getPitch(), 0, 0.3F);
						}
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
		World world = user.getWorld();
		if (canColor(user) && GenericClaimModsCompat.canInteract(entity.getWorld(), entity, user)) {
			Optional<InkColor> color = getColor(stack);
			
			if (color.isPresent()
					&& payBlockColorCost(user, color.get())
					&& EntityColorProcessorRegistry.colorEntity(entity, color.get().getDyeColor())) {
				
				entity.getWorld().playSoundFromEntity(null, entity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				return ActionResult.success(world.isClient);
			}
			
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public boolean useOnSign(World world, SignBlockEntity signBlockEntity, boolean front, PlayerEntity player) {
		if (tryUseOnSign(world, signBlockEntity, front, player, player.getStackInHand(Hand.MAIN_HAND))) return true;
		if (tryUseOnSign(world, signBlockEntity, front, player, player.getStackInHand(Hand.OFF_HAND))) return true;

		player.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
		return false;
	}

	private boolean tryUseOnSign(World world, SignBlockEntity signBlockEntity, boolean front, PlayerEntity player, ItemStack stack) {
		if (stack.isOf(SpectrumItems.PAINTBRUSH)) {
			Optional<InkColor> color = getColor(stack);
			if (color.isPresent()) {
				InkColor inkColor = color.get();
				DyeColor dyeColor = inkColor.getDyeColor();

				if (canColor(player) && payBlockColorCost(player, inkColor)) {
					if (signBlockEntity.changeText((text) -> text.withColor(dyeColor), front)) {
						world.playSound(null, signBlockEntity.getPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.BLOCKS, 1.0F, 1.0F);
						return true;
					}
				}
			}
		}

		return false;
	}
}
