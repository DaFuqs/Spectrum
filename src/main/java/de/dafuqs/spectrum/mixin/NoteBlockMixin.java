package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
	
	@Inject(method = "getCustomSound(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	protected void spectrum$customNoteBlockSound(World world, BlockPos pos, CallbackInfoReturnable<Identifier> cir) {
		BlockState state = world.getBlockState(pos.up());
		if (state.getBlock() instanceof SpectrumSkullBlock spectrumSkullBlock) {
			cir.setReturnValue(spectrumSkullBlock.getSkullType().getNoteBlockSound());
		}
	}
	
}