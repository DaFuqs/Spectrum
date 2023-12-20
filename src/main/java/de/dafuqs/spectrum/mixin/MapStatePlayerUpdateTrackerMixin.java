package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.map.ArtisansAtlasState;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;

@Mixin(MapState.PlayerUpdateTracker.class)
public class MapStatePlayerUpdateTrackerMixin {

    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(
            method = "getPacket",
            at = @At(value = "NEW", target = "(IBZLjava/util/Collection;Lnet/minecraft/item/map/MapState$UpdateData;)Lnet/minecraft/network/packet/s2c/play/MapUpdateS2CPacket;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void spectrum$getArtisansAtlasPacket(int mapId, CallbackInfoReturnable<Packet<?>> cir, MapState.UpdateData updateData, Collection<MapIcon> icons) {
        World world = player.getWorld();
        if (world != null) {
            String mapStr = FilledMapItem.getMapName(mapId);
            MapState state = world.getMapState(mapStr);
            if (state instanceof ArtisansAtlasState artisansAtlasState) {
                MapUpdateS2CPacket mapUpdateS2CPacket = new MapUpdateS2CPacket(mapId, state.scale, state.locked, icons, updateData);
                PacketByteBuf buf = PacketByteBufs.create();

                Identifier targetId = artisansAtlasState.getTargetId();
                if (targetId == null) {
                    buf.writeString("");
                } else {
                    buf.writeString(targetId.toString());
                }

                mapUpdateS2CPacket.write(buf);

                Packet<?> packet = ServerPlayNetworking.createS2CPacket(SpectrumS2CPackets.SYNC_ARTISANS_ATLAS, buf);
                cir.setReturnValue(packet);
            }
        }
    }
}
