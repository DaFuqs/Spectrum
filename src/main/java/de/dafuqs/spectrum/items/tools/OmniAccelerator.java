package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.api.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
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
			Optional<ItemStack> shootStackOptional = decrementFirstItem(stack);
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

	private static Optional<ItemStack> decrementFirstItem(ItemStack stack) {
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		if (!nbtCompound.contains("Items")) {
			return Optional.empty();
		} else {
			NbtList itemsList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
			if (itemsList.isEmpty()) {
				return Optional.empty();
			} else {
				NbtCompound stackNbt = itemsList.getCompound(0);
				ItemStack itemStack = ItemStack.fromNbt(stackNbt);

				if (itemStack.getCount() > 1) {
					stackNbt.putByte("Count", (byte) (itemStack.getCount() - 1));
				} else {
					itemsList.remove(0);
					if (itemsList.isEmpty()) {
						stack.removeSubNbt("Items");
					}
				}

				return Optional.of(itemStack);
			}
		}
	}
	
	private static Optional<ItemStack> getFirstStack(ItemStack stack) {
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		if (!nbtCompound.contains("Items")) {
			return Optional.empty();
		} else {
			NbtList itemsList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
			if (itemsList.isEmpty()) {
				return Optional.empty();
			} else {
				NbtCompound stackNbt = itemsList.getCompound(0);
				ItemStack itemStack = ItemStack.fromNbt(stackNbt);
				return Optional.of(itemStack);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class Renderer implements DynamicItemRenderer {
		public Renderer() {}
		@Override
		public void render(ItemRenderer renderer, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
			renderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
			if(mode != ModelTransformationMode.GUI) return;
			
			Optional<ItemStack> optionalStack = getFirstStack(stack);
			if(optionalStack.isEmpty()) {
				return;
			}
			ItemStack bundledStack = optionalStack.get();
			
			MinecraftClient client = MinecraftClient.getInstance();
			BakedModel bundledModel = renderer.getModel(bundledStack, client.world, client.player, 0);
			
			matrices.push();
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0.5F, 0.5F, 0.5F);
			renderer.renderItem(bundledStack, mode, leftHanded, matrices, vertexConsumers, light, overlay, bundledModel);
			matrices.pop();
		}
	}

}
