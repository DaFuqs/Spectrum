package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.blocks.boom.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;
import java.util.function.*;

@Mixin(BlockBehaviour.class)
public class BlockBehaviorMixin {
	
	@Inject(at = @At("HEAD"), method = "onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V")
	protected void spectrum$captureExplosion(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo ci) {
		ExplosionWithStack.spectrum$explosion = explosion;
	}
	
	@ModifyArg(method = "onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withParameter(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;"))
	public Object spectrum$applyBoomStack(Object original) {
		if(original instanceof ItemStack && ExplosionWithStack.spectrum$explosion instanceof ExplosionWithStack explosionWithStack) {
			return explosionWithStack.getStack();
		}
		return original;
	}
	
	@ModifyArg(method = "onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"))
	public ItemStack spectrum$applyBoomStackAfterBreak(ItemStack original) {
		if(ExplosionWithStack.spectrum$explosion instanceof ExplosionWithStack explosionWithStack) {
			return explosionWithStack.getStack();
		}
		return original;
	}
	
	@ModifyReturnValue(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> spectrum$getDroppedStacks(List<ItemStack> original, BlockState state, LootParams.Builder params) {
		ServerLevel level = params.getLevel();
		ItemStack stack = params.getOptionalParameter(LootContextParams.TOOL);
		Vec3 origin = params.getOptionalParameter(LootContextParams.ORIGIN);
		
		if(stack == null  || origin == null) {
			// funky af getDrops() call
			return original;
		}
		
		BlockPos pos = BlockPos.containing(origin);
		Entity entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
		BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		
		// Voiding curse: no drops
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.NO_BLOCK_DROPS)) {
			level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.05);
			original.clear();
			return original;
		}
		
		// Resonance: drop self or modify drops for some items
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			original = ResonanceProcessor.applyResonance(level.registryAccess(), state, blockEntity, original);
		}
		
		if (!original.isEmpty()) {
			// Foundry enchant: try smelting recipe for each stack
			if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.SMELTS_MORE_LOOT)) {
				original = FoundryHelper.applyFoundry(level, original);
			}
			
			// Inventory insertion enchant: add it to the player's inventory if there is room
			if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.INVENTORY_INSERTION_EFFECT)) {
				List<ItemStack> leftoverReturnStacks = new ArrayList<>();
				
				if (entity instanceof Player player) {
					boolean anyAdded = false;
					for (ItemStack itemStack : original) {
						Item item = itemStack.getItem();
						int count = itemStack.getCount();
						
						if (player.getInventory().add(itemStack)) {
							anyAdded = true;
							if (itemStack.isEmpty()) {
								itemStack.setCount(count);
							}
							player.awardStat(Stats.ITEM_PICKED_UP.get(item), count);
						} else {
							leftoverReturnStacks.add(itemStack);
						}
					}
					if (anyAdded) {
						level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
					}
				}
				original = leftoverReturnStacks;
			}
		}
		
		return original;
	}
	
}
