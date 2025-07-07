package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public record InertiaComponent(Block lastMined, long count) {
	
	public static final InertiaComponent DEFAULT = new InertiaComponent(Blocks.AIR, 0);
	
	public static final Codec<InertiaComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("last_mined").forGetter(InertiaComponent::lastMined),
			Codec.LONG.fieldOf("count").forGetter(InertiaComponent::count)
	).apply(i, InertiaComponent::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, InertiaComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.BLOCK),
			InertiaComponent::lastMined,
			ByteBufCodecs.VAR_LONG,
			InertiaComponent::count,
			InertiaComponent::new
	);
	
	public static void onInertiaBlockBreak(Level level, BlockPos pos, BlockState state, ServerPlayer serverPlayerEntity, ItemStack handStack) {
		InertiaComponent inertia = handStack.getOrDefault(SpectrumDataComponentTypes.INERTIA, InertiaComponent.DEFAULT);
		
		// if an instabreak block was broken, do not trigger inertia
		// (does not make sense to have a speed boost on that, perhaps broken on accident
		if (state.getDestroySpeed(level, pos) <= 0) {
			return;
		}
		
		// if a block is not revealed, it does not reset inertia,
		// but it also does not advance it
		if (!RevelationRegistry.isVisibleTo(state, serverPlayerEntity)) {
			return;
		}
		
		long inertiaAmount = state.is(inertia.lastMined()) ? inertia.count() + 1 : 1;
		handStack.set(SpectrumDataComponentTypes.INERTIA, new InertiaComponent(state.getBlock(), inertiaAmount));
		
		// TODO PORT
//		SpectrumAdvancementCriteria.INERTIA_USED.trigger(serverPlayerEntity, state, inertiaAmount);
	}
	
}
