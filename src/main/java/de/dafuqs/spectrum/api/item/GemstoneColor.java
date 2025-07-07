package de.dafuqs.spectrum.api.item;

import net.minecraft.util.*;
import net.minecraft.world.item.*;

public interface GemstoneColor extends StringRepresentable {
	
	int getColor();
	
	Item getGemstonePowderItem();

}