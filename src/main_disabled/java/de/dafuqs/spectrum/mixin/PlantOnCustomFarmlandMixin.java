package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.farming.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({CropBlock.class, StemBlock.class, AttachedStemBlock.class, PitcherCropBlock.class})
public abstract class PlantOnCustomFarmlandMixin {
	
	@ModifyReturnValue(method = "mayPlaceOn", at = @At("RETURN"))
	public boolean spectrum$canPlantOnTopOfCustomFarmland(boolean original, @NotNull BlockState floor, BlockGetter world, BlockPos pos) {
		return original || floor.getBlock() instanceof SpectrumFarmlandBlock;
	}
	
}
