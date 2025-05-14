package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
	
	@Inject(method = "getCustomSoundId", at = @At("HEAD"), cancellable = true)
	protected void spectrum$customNoteBlockSound(Level world, BlockPos pos, CallbackInfoReturnable<ResourceLocation> cir) {
		BlockState state = world.getBlockState(pos.above());
		if (state.getBlock() instanceof SpectrumSkullBlock spectrumSkullBlock) {
			cir.setReturnValue(spectrumSkullBlock.getType().getNoteBlockSound());
		}
	}
	
}