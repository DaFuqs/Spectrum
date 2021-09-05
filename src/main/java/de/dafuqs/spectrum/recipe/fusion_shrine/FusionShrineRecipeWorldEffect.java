package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

/**
 * Effects that are played when crafting with the fusion shrine
 */
public enum FusionShrineRecipeWorldEffect {
    NOTHING,
    WEATHER_CLEAR,
    WEATHER_RAIN,
    WEATHER_THUNDER,
    LIGHTNING_ON_SHRINE,
    LIGHTNING_AROUND_SHRINE,
    VISUAL_EXPLOSIONS_ON_SHRINE,
    SINGLE_VISUAL_EXPLOSION_ON_SHRINE;

    public void doEffect(ServerWorld world, BlockPos shrinePos) {
        switch (this) {
            case WEATHER_CLEAR: {
                world.setWeather(6000, 0, false, false);
                break;
            }
            case WEATHER_RAIN: {
                world.setWeather(0, 6000, true, false);
                break;
            }
            case WEATHER_THUNDER: {
                world.setWeather(0, 6000, true, true);
                break;
            }
            case LIGHTNING_ON_SHRINE: {
                LightningEntity lightningEntity =  EntityType.LIGHTNING_BOLT.create(world);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(shrinePos));
                lightningEntity.setCosmetic(true);
                world.spawnEntity(lightningEntity);break;

            }
            case LIGHTNING_AROUND_SHRINE: {
                if(world.getRandom().nextFloat() < 0.05F) {
                    int randomX = shrinePos.getX() + 12 - world.getRandom().nextInt(24);
                    int randomZ = shrinePos.getZ() + 12 - world.getRandom().nextInt(24);

                    BlockPos randomPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomPos));
                    lightningEntity.setCosmetic(false);
                    world.spawnEntity(lightningEntity);
                }
                break;
            }
            case VISUAL_EXPLOSIONS_ON_SHRINE: {
                if(world.getRandom().nextFloat() < 0.1) {
                    // TODO: dedub from ItemEntityMixin
                    // Particle Effect
                    PacketByteBuf buf = PacketByteBufs.create();
                    BlockPos particleBlockPos = new BlockPos(shrinePos.getX(), shrinePos.getY() + 1, shrinePos.getZ());
                    buf.writeBlockPos(particleBlockPos);
                    // Iterate over all players tracking a position in the world and send the packet to each player
                    for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, particleBlockPos)) {
                        ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_ANVIL_CRAFTING_PARTICLE_PACKET_ID, buf);
                    }
                }
                break;
            }
            case SINGLE_VISUAL_EXPLOSION_ON_SHRINE: {
                // TODO: dedub from ItemEntityMixin
                // Particle Effect
                PacketByteBuf buf = PacketByteBufs.create();
                BlockPos particleBlockPos = new BlockPos(shrinePos.getX(), shrinePos.getY(), shrinePos.getZ());
                buf.writeBlockPos(particleBlockPos);
                // Iterate over all players tracking a position in the world and send the packet to each player
                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, particleBlockPos)) {
                    ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_ANVIL_CRAFTING_PARTICLE_PACKET_ID, buf);
                }
                break;
            }
        }
    }

    /**
     * True for all effects that should just play once.
     * Otherwise, it will be triggered each tick of the recipe
     */
    public boolean isOneTimeEffect(FusionShrineRecipeWorldEffect effect) {
        return effect == LIGHTNING_ON_SHRINE || effect == SINGLE_VISUAL_EXPLOSION_ON_SHRINE || effect == WEATHER_CLEAR || effect == WEATHER_RAIN || effect == WEATHER_THUNDER;
    }


}
