package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.farming.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({CropBlock.class, StemBlock.class, AttachedStemBlock.class, PitcherCropBlock.class})
public abstract class PlantOnCustomFarmlandMixin {
	
	@ModifyReturnValue(method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("RETURN"))
	public boolean spectrum$canPlantOnTopOfCustomFarmland(boolean original, @NotNull BlockState floor, BlockView world, BlockPos pos) {
		return original || floor.getBlock() instanceof SpectrumFarmlandBlock;
	}
	
}
