package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.entity.entity.InkProjectileEntity;
import de.dafuqs.spectrum.helpers.BlockVariantHelper;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.inventories.PaintbrushScreenHandler;
import de.dafuqs.spectrum.items.PigmentItem;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PaintBrushItem extends Item {
	
	public static final Identifier UNLOCK_COLORING_ADVANCEMENT_ID = SpectrumCommon.locate("collect_pigment");
	public static final Identifier UNLOCK_INK_SLINGING_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/fill_ink_container");
	
	public static final int COOLDOWN_DURATION_TICKS = 10;
	public static final int BLOCK_COLOR_COST = 25;
	public static final int INK_FLING_COST = 100;
	
	public static final String COLOR_NBT_STRING = "Color";
	private static final Text GUI_TITLE = new TranslatableText("item.spectrum.paintbrush");
	
	public PaintBrushItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		Optional<InkColor> color = getColor(stack);
		boolean unlockedColoring = AdvancementHelper.hasAdvancementClient(UNLOCK_COLORING_ADVANCEMENT_ID);
		boolean unlockedSlinging = AdvancementHelper.hasAdvancementClient(UNLOCK_INK_SLINGING_ADVANCEMENT_ID);
		
		if(unlockedColoring || unlockedSlinging) {
			if (color.isPresent()) {
				tooltip.add(new TranslatableText("spectrum.ink.color." + color.get()));
			} else {
				tooltip.add(new TranslatableText("item.spectrum.paintbrush.tooltip.select_color"));
			}
		}
		
		tooltip.add(new TranslatableText("item.spectrum.paintbrush.ability.header").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.paintbrush.ability.pedestal_triggering").formatted(Formatting.GRAY));
		if(unlockedColoring) {
			tooltip.add(new TranslatableText("item.spectrum.paintbrush.ability.block_coloring").formatted(Formatting.GRAY));
		}
		if(unlockedSlinging) {
			tooltip.add(new TranslatableText("item.spectrum.paintbrush.ability.ink_slinging").formatted(Formatting.GRAY));
		}
	}
	
	public static boolean canColor(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, UNLOCK_COLORING_ADVANCEMENT_ID);
	}
	
	public static boolean canInkSling(PlayerEntity player) {
		return AdvancementHelper.hasAdvancement(player, UNLOCK_INK_SLINGING_ADVANCEMENT_ID);
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(World world, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new PaintbrushScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, serverPlayerEntity.getBlockPos()), itemStack), GUI_TITLE);
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
		
		if (context.getPlayer().isCreative()
				|| InkPowered.tryDrainEnergy(context.getPlayer(), inkColor, BLOCK_COLOR_COST)
				|| InventoryHelper.removeFromInventoryWithRemainders(context.getPlayer(), PigmentItem.byColor(dyeColor).getDefaultStack())) {
			
			// TODO: Use Jellos API to support all of jellos block colors
			// https://modrinth.com/mod/jello
			Block newBlock = BlockVariantHelper.getCursedBlockColorVariant(context.getWorld(), context.getBlockPos(), dyeColor);
			if (newBlock == Blocks.AIR) {
				return false;
			}
			
			if (!context.getWorld().isClient) {
				context.getWorld().setBlockState(context.getBlockPos(), newBlock.getDefaultState());
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
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			if (user instanceof ServerPlayerEntity serverPlayerEntity) {
				if (canColor(serverPlayerEntity)) {
					serverPlayerEntity.openHandledScreen(createScreenHandlerFactory(world, serverPlayerEntity, user.getStackInHand(hand)));
				}
			}
			return TypedActionResult.pass(user.getStackInHand(hand));
		} else if(canInkSling(user)){
			Optional<InkColor> optionalInkColor = getColor(user.getStackInHand(hand));
			if (optionalInkColor.isPresent()) {
				
				InkColor inkColor = optionalInkColor.get();
				if (user.isCreative() || InkPowered.tryDrainEnergy(user, inkColor, INK_FLING_COST)) {
					
					user.getItemCooldownManager().set(this, COOLDOWN_DURATION_TICKS);
					
					if (!world.isClient) {
						// spawn projectile
						InkProjectileEntity paintProjectile = new InkProjectileEntity(world, user);
						paintProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.0F, 1.0F);
						paintProjectile.setColor(inkColor);
						world.spawnEntity(paintProjectile);
						
					}
					
					// cause the slightest bit of knockback
					if(!user.isCreative()) {
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
		return super.useOnEntity(stack, user, entity, hand);
	}
	
}
