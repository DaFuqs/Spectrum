package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.items.map.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.common.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.saveddata.maps.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(MapItemSavedData.HoldingPlayer.class)
public class MapStatePlayerUpdateTrackerMixin {

    @Shadow
    @Final
	public Player player;
	
	@Inject(method = "nextUpdatePacket", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/saveddata/maps/MapId;BZLjava/util/Collection;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;"), cancellable = true)
	private void spectrum$getArtisansAtlasPacket(MapId mapId, CallbackInfoReturnable<Packet<?>> cir, @Local MapItemSavedData.MapPatch updateData, @Local Collection<MapDecoration> icons) {
		Level world = player.level();
		if (world != null && world.getMapData(mapId) instanceof ArtisansAtlasState state) {
			var mapPacket = new ClientboundMapItemDataPacket(mapId, state.scale, state.locked, icons, updateData);
			var payload = new SyncArtisansAtlasPayload(Optional.ofNullable(state.getTargetId()), mapPacket);
			var customPacket = new ClientboundCustomPayloadPacket(payload);
			cir.setReturnValue(customPacket);
		}
    }
}
