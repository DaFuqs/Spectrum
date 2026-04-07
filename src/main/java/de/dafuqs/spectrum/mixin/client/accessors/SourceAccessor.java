package de.dafuqs.spectrum.mixin.client.accessors;

import com.mojang.blaze3d.audio.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Channel.class)
public interface SourceAccessor {
	
	@Accessor
	int getSource();
	
}