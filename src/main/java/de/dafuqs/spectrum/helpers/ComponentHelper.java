package de.dafuqs.spectrum.helpers;

import net.minecraft.core.component.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

import java.util.function.*;

// TODO: remove
public class ComponentHelper {
	
	public static <T> void setOrRemove(ItemStack stack, Supplier<DataComponentType<T>> type, T value, boolean set) {
		if (set)
			stack.set(type, value);
		else
			stack.remove(type);
	}
	
	public static void setOrRemove(ItemStack stack, Supplier<DataComponentType<Unit>> type, boolean set) {
		setOrRemove(stack, type, Unit.INSTANCE, set);
	}
	
}
