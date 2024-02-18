package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class OmniAccelerator extends BundleItem {

	public OmniAccelerator(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 20;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof ServerPlayerEntity player) {
			Optional<ItemStack> shootStackOptional = removeFirstItem(stack);
			if (shootStackOptional.isPresent()) {
				ItemStack shootStack = shootStackOptional.get();

				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
				if (!world.isClient) {
					ItemProjectileEntity itemProjectileEntity = new ItemProjectileEntity(world, user);
					itemProjectileEntity.setItem(shootStack);
					itemProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.0F, 0.5F);
					world.spawnEntity(itemProjectileEntity);
				}
			} else {
				player.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
			}
		}

		return stack;
	}

	private static Optional<ItemStack> removeFirstItem(ItemStack stack) {
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		if (!nbtCompound.contains("Items")) {
			return Optional.empty();
		} else {
			NbtList nbtList = nbtCompound.getList("Items", 10);
			if (nbtList.isEmpty()) {
				return Optional.empty();
			} else {
				NbtCompound stackNbt = nbtList.getCompound(0);
				ItemStack itemStack = ItemStack.fromNbt(stackNbt);

				if (itemStack.getCount() > 1) {
					stackNbt.putByte("Count", (byte) (itemStack.getCount() - 1));
				} else {
					nbtList.remove(0);
					if (nbtList.isEmpty()) {
						stack.removeSubNbt("Items");
					}
				}

				return Optional.of(itemStack);
			}
		}
	}

}
