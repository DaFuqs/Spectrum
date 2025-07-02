package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(DropExperienceBlock.class)
public interface ExperienceDroppingBlockAccessor {

    @Accessor
	IntProvider getXpRange();

}
