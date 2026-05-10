package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.boom.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.function.*;

@Mixin(BlockBehaviour.class)
public class BlockBehaviorMixin {
	
	@Unique
	private static @Nullable Explosion spectrum$explosion = null;
	
	@Inject(at = @At("HEAD"), method = "onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V")
	protected void spectrum$captureExplosion(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo ci) {
		spectrum$explosion = explosion;
	}
	
	@ModifyArg(method = "onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withParameter(Lnet/minecraft/world/level/storage/loot/parameters/LootContextParam;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;"))
	public Object spectrum$applyBoomStack(Object original) {
		if(original instanceof ItemStack && spectrum$explosion instanceof ExplosionWithStack explosionWithStack) {
			return explosionWithStack.getStack();
		}
		return original;
	}
	
	@ModifyArg(method = "onExplosionHit(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"))
	public ItemStack spectrum$applyBoomStackAfterBreak(ItemStack original) {
		if(spectrum$explosion instanceof ExplosionWithStack explosionWithStack) {
			return explosionWithStack.getStack();
		}
		return original;
	}
	
}
