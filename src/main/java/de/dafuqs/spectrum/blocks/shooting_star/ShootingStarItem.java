package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
		ItemStack stack = shootingStarItem.getDefaultStack();
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putInt("remaining_hits", remainingHits);
		if(hardened) {
			nbtCompound.putBoolean("Hardened", true);
		}
		stack.setNbt(nbtCompound);
		return stack;
	}
	
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
		if(isHardened(stack)) {
			tooltip.add(new TranslatableText("item.spectrum.shooting_star.tooltip.hardened").formatted(Formatting.GRAY));
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
