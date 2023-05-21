package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShootingStarItem extends BlockItem {
	
	private final ShootingStarBlock.Type type;
	
	public ShootingStarItem(ShootingStarBlock block, Settings settings) {
		super(block, settings);
		this.type = block.shootingStarType;
	}
	
	public static int getRemainingHits(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound == null || !nbtCompound.contains("remaining_hits", NbtElement.INT_TYPE)) {
			return 5;
		} else {
			return nbtCompound.getInt("remaining_hits");
		}
	}
	
	public static @NotNull ItemStack getWithRemainingHits(@NotNull ShootingStarItem shootingStarItem, int remainingHits, boolean hardened) {
		return getWithRemainingHits(shootingStarItem.getDefaultStack(), remainingHits, hardened);
	}
	
	public static @NotNull ItemStack getWithRemainingHits(@NotNull ItemStack stack, int remainingHits, boolean hardened) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putInt("remaining_hits", remainingHits);
		if (hardened) {
			nbt.putBoolean("Hardened", true);
		}
		return stack;
	}
	
	@Override
	public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
		if (context.getPlayer().isSneaking()) {
			// place as block
			return super.useOnBlock(context);
		} else {
			// place as entity
			World world = context.getWorld();
			
			if (!world.isClient) {
				ItemStack itemStack = context.getStack();
				Vec3d hitPos = context.getHitPos();
				PlayerEntity user = context.getPlayer();
				
				ShootingStarEntity shootingStarEntity = new ShootingStarEntity(context.getWorld(), hitPos.x, hitPos.y, hitPos.z);
				shootingStarEntity.setShootingStarType(this.type, true, isHardened(itemStack));
				shootingStarEntity.setAvailableHits(getRemainingHits(context.getStack()));
				shootingStarEntity.setYaw(user.getYaw());
				if (!world.isSpaceEmpty(shootingStarEntity, shootingStarEntity.getBoundingBox())) {
					return ActionResult.FAIL;
				} else {
					world.spawnEntity(shootingStarEntity);
					world.emitGameEvent(user, GameEvent.ENTITY_PLACE, context.getBlockPos());
					if (!user.getAbilities().creativeMode) {
						itemStack.decrement(1);
					}
					
					user.incrementStat(Stats.USED.getOrCreateStat(this));
				}
			}
			
			return ActionResult.success(world.isClient);
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if (isHardened(stack)) {
			tooltip.add(Text.translatable("item.spectrum.shooting_star.tooltip.hardened").formatted(Formatting.GRAY));
		}
	}
	
	public ShootingStarBlock.Type getType() {
		return this.type;
	}
	
	public static boolean isHardened(ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		return nbtCompound != null && nbtCompound.getBoolean("Hardened");
	}
	
	public static void setHardened(ItemStack itemStack) {
		NbtCompound nbt = itemStack.getOrCreateNbt();
		nbt.putBoolean("Hardened", true);
		itemStack.setNbt(nbt);
	}
	
}
