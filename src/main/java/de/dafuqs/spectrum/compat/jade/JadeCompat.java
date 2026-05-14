package de.dafuqs.spectrum.compat.jade;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import snownee.jade.api.*;
import snownee.jade.api.callback.*;

@WailaPlugin
public class JadeCompat implements IWailaPlugin, JadeRayTraceCallback {
	
	@Nullable
	private IWailaClientRegistration registration;
	
	@Override
	public void registerClient(IWailaClientRegistration wailaRegistration) {
		registration = wailaRegistration;
		registration.addRayTraceCallback(1000, this);
	}
	
	@Override
	public @Nullable Accessor<?> onRayTrace(HitResult hitResult, @Nullable Accessor<?> currentAccessor, @Nullable Accessor<?> originalAccessor) {
		if (registration == null) {
			return currentAccessor;
		}
		if (currentAccessor instanceof BlockAccessor blockAccessor
				&& blockAccessor.getBlock() instanceof RevelationAware revelationAware) {
			if (revelationAware.isVisibleTo(currentAccessor.getPlayer())) {
				return currentAccessor;
			}
			return registration.blockAccessor()
					.from(blockAccessor)
					.blockState(revelationAware.getBlockStateCloaks().get(blockAccessor.getBlockState()))
					.build();
		}
		return currentAccessor;
	}
}
