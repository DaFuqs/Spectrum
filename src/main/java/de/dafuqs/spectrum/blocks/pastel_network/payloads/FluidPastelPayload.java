package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.client.*;
import de.dafuqs.spectrum.particle.effect.*;
import dev.emi.emi.*;
import dev.emi.emi.api.stack.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import org.jetbrains.annotations.*;

public record FluidPastelPayload(FluidStack fluidStack, ParticleOptions particleEffect) implements PastelPayload {
	
	public static final MapCodec<FluidPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			FluidStack.CODEC.fieldOf("fluid").forGetter(FluidPastelPayload::fluidStack)
	).apply(i, FluidPastelPayload::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FluidPastelPayload> STREAM_CODEC = StreamCodec.composite(
			FluidStack.STREAM_CODEC, FluidPastelPayload::fluidStack,
			FluidPastelPayload::new
	);
	
	public FluidPastelPayload(FluidStack fluidStack) {
		this(fluidStack, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack)));
	}
	
	@Override
	public void tick(Level level, PastelTransmissionParticle particle) {
		if(level.getGameTime() % 2 == 0) {
			Vec3 pos = particle.getPos();
			level.addParticle(particleEffect, pos.x(), pos.y(), pos.z(), 0, 0, 0);
		}
	}
	
	public MapCodec<FluidPastelPayload> codec() {
		return CODEC;
	}
	
	public StreamCodec<RegistryFriendlyByteBuf, FluidPastelPayload> streamCodec() {
		return STREAM_CODEC;
	}
	
	@Override
	public void arriveAtDestination(Level level, BlockPos destination, @Nullable PastelNodeBlockEntity destinationNode) {
		if (destinationNode != null) {
			@Nullable IFluidHandler destinationHandler = FluidPastelPayloadType.getConnectedFluidStorage(destinationNode);
			if (destinationHandler != null) {
				destinationHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
				destinationNode.addUnderway(SpectrumPastelPayloadTypes.FLUID.getKey(), -fluidStack.getAmount());
			}
		}
	}
	
}
