package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EnderMan.class)
public abstract class EndermanEntityMixin {
	
	@Unique
	private final BlockState carriedBlockState = SpectrumBlocks.RADIATING_ENDER.defaultBlockState();
	
	@Shadow
	@Nullable
	public abstract BlockState getCarriedBlock();
	
	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(CallbackInfo info) {
		EnderMan endermanEntity = ((EnderMan) (Object) this);
		Level world = endermanEntity.getCommandSenderWorld();
		if (world instanceof ServerLevel) {
			RandomSource random = world.random;
			
			float chance;
			if (world.dimension().equals(Level.END)) {
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
	
	@Inject(at = @At("RETURN"), method = "requiresCustomPersistence", cancellable = true)
	public void cannotDespawn(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && this.getCarriedBlock() != null && this.getCarriedBlock().is(SpectrumBlocks.RADIATING_ENDER)) {
			cir.setReturnValue(false);
		}
	}
	
}
