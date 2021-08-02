package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.GravityBlockEntity;
import de.dafuqs.spectrum.entity.entity.InvisibleGlowItemFrameEntity;
import de.dafuqs.spectrum.entity.entity.InvisibleItemFrameEntity;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEntityTypes {

    public static EntityType<ShootingStarEntity> SHOOTING_STAR = register("shooting_star", 240, 20, true, EntityDimensions.changing(0.98F, 0.98F), ShootingStarEntity::new);
    public static EntityType<GravityBlockEntity> GRAVITY_BLOCK = register("gravity_block", 160, 20, true, EntityDimensions.changing(0.98F, 0.98F), GravityBlockEntity::new);
    public static EntityType<InvisibleItemFrameEntity> INVISIBLE_ITEM_FRAME = register("invisible_item_frame", 10, 2147483647, true, EntityDimensions.changing(0.5F, 0.5F), InvisibleItemFrameEntity::new);
    public static EntityType<InvisibleGlowItemFrameEntity> INVISIBLE_GLOW_ITEM_FRAME = register("invisible_glow_item_frame", 10, 2147483647, true, EntityDimensions.changing(0.5F, 0.5F), InvisibleGlowItemFrameEntity::new);

    public static void registerClient() {
    }

    public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).trackable(trackingDistance, updateIntervalTicks, alwaysUpdateVelocity).dimensions(size).build());
    }

    public static <X extends Entity> EntityType<X> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(size).build());
    }

}