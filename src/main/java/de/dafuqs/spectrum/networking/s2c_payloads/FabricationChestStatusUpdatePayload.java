package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record FabricationChestStatusUpdatePayload(BlockPos pos, boolean isFull, boolean hasValidRecipes, List<ItemStack> stacks) implements CustomPacketPayload {
	
	public static final Type<FabricationChestStatusUpdatePayload> ID = SpectrumC2SPackets.makeId("fabrication_chest_status_update");
	public static final StreamCodec<RegistryFriendlyByteBuf, FabricationChestStatusUpdatePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, FabricationChestStatusUpdatePayload::pos,
			ByteBufCodecs.BOOL, FabricationChestStatusUpdatePayload::isFull,
			ByteBufCodecs.BOOL, FabricationChestStatusUpdatePayload::hasValidRecipes,
			ItemStack.LIST_STREAM_CODEC, FabricationChestStatusUpdatePayload::stacks,
			FabricationChestStatusUpdatePayload::new
	);
	
	public static void sendFabricationChestStatusUpdate(FabricationChestBlockEntity chest) {
		BlockPos pos = chest.getBlockPos();
		boolean isFull = chest.isFullServer();
		boolean hasValidRecipes = chest.hasValidRecipes();
		List<ItemStack> stacks = new ArrayList<>(chest.getRecipeOutputs());
		
		PacketDistributor.sendToPlayersTrackingChunk(
				(ServerLevel) chest.getLevel(), new ChunkPos(pos), new FabricationChestStatusUpdatePayload(
						pos, isFull,
						hasValidRecipes,
						stacks
				)
		);
	}
	
	@SuppressWarnings("resource")
	public static void execute(FabricationChestStatusUpdatePayload payload, IPayloadContext context) {
		Optional<FabricationChestBlockEntity> entity = context.player().level().getBlockEntity(payload.pos, SpectrumBlockEntities.FABRICATION_CHEST.get());
		entity.ifPresent(fabricationChestBlockEntity -> fabricationChestBlockEntity.updateState(payload.isFull, payload.hasValidRecipes, payload.stacks));
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}