package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.interaction.*;
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

public class OmniAcceleratorItem extends BundleItem {
	
	public OmniAcceleratorItem(Settings settings) {
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
			Optional<ItemStack> shootStackOptional = getFirstStack(stack);
			if (shootStackOptional.isPresent()) {
				ItemStack shootStack = shootStackOptional.get();
				
				if (!world.isClient) {
					OmniAcceleratorProjectile projectile = OmniAcceleratorProjectile.get(shootStack);
					if (projectile.createProjectile(shootStack, user, world) != null) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), projectile.getSoundEffect(), SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
						if (!player.isCreative()) {
							decrementFirstItem(stack);
						}
					}
				}
			} else {
				player.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
			}
		}
		
		return stack;
	}
	
	public static void decrementFirstItem(ItemStack acceleratorStack) {
		NbtCompound nbtCompound = acceleratorStack.getOrCreateNbt();
		if (nbtCompound.contains("Items")) {
			NbtList itemsList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
			if (!itemsList.isEmpty()) {
				NbtCompound stackNbt = itemsList.getCompound(0);
				int count = stackNbt.getByte("Count");
				if (count > 1) {
					stackNbt.putByte("Count", (byte) (count - 1));
				} else {
					itemsList.remove(0);
					if (itemsList.isEmpty()) {
						acceleratorStack.removeSubNbt("Items");
					}
				}
			}
		}
	}
	
	public static Optional<ItemStack> getFirstStack(ItemStack stack) {
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
