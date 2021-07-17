package de.dafuqs.pigment.entity;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.entity.entity.GravityBlockEntity;
import de.dafuqs.pigment.entity.entity.InvisibleGlowItemFrameEntity;
import de.dafuqs.pigment.entity.entity.InvisibleItemFrameEntity;
import de.dafuqs.pigment.entity.entity.ShootingStarEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentEntityTypes {

    public static final Identifier SPAWN_PACKET_ID = new Identifier(PigmentCommon.MOD_ID, "spawn_packet");

    public static EntityType<ShootingStarEntity> SHOOTING_STAR;
    public static EntityType<GravityBlockEntity> GRAVITY_BLOCK;
    public static EntityType<InvisibleItemFrameEntity> INVISIBLE_ITEM_FRAME;
    public static EntityType<InvisibleGlowItemFrameEntity> INVISIBLE_GLOW_ITEM_FRAME;

    public static void registerClient() {
        SHOOTING_STAR = register("shooting_star", 240, 20, true, EntityDimensions.changing(0.98F, 0.98F), ShootingStarEntity::new);
        GRAVITY_BLOCK = register("gravity_block", 160, 20, true, EntityDimensions.changing(0.98F, 0.98F), GravityBlockEntity::new);
        INVISIBLE_ITEM_FRAME = register("invisible_item_frame", 10, 2147483647, true, EntityDimensions.changing(0.5F, 0.5F), InvisibleItemFrameEntity::new);
        INVISIBLE_GLOW_ITEM_FRAME = register("invisible_glow_item_frame", 10, 2147483647, true, EntityDimensions.changing(0.5F, 0.5F), InvisibleGlowItemFrameEntity::new);
    }

    public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(PigmentCommon.MOD_ID, name), FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).trackable(trackingDistance, updateIntervalTicks, alwaysUpdateVelocity).dimensions(size).disableSaving().build());
    }

    public static <X extends Entity> EntityType<X> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(PigmentCommon.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(size).disableSaving().build());
    }

}