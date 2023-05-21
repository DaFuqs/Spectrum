package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.mob.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin {
	
	final BlockState carriedBlockState = SpectrumBlocks.RADIATING_ENDER.getDefaultState();
	
	@Shadow
	@Nullable
	public abstract BlockState getCarriedBlock();
	
	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(CallbackInfo info) {
		EndermanEntity endermanEntity = ((EndermanEntity) (Object) this);
		if (endermanEntity.getEntityWorld() != null && endermanEntity.getEntityWorld() instanceof ServerWorld) {
			Random random = endermanEntity.getEntityWorld().random;
			
			float chance;
			if (endermanEntity.getEntityWorld().getRegistryKey().equals(World.END)) {
				chance = SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureInEndChance;
			} else {
				chance = SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureChance;
			}
			
			if (random.nextFloat() < chance) {
				if (endermanEntity.getCarriedBlock() == null) {
					endermanEntity.setCarriedBlock(carriedBlockState);
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "cannotDespawn()Z", cancellable = true)
	public void cannotDespawn(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && this.getCarriedBlock() != null && this.getCarriedBlock().isOf(SpectrumBlocks.RADIATING_ENDER)) {
			cir.setReturnValue(false);
		}
	}
	
}
