package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.display.*;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.*;

public interface GatedRecipeDisplay extends Display {
	
	boolean isUnlocked();
	
	boolean isSecret();
	
	@Nullable
	Component getSecretHintText();
	
}
