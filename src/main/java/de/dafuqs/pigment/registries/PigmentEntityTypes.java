package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.gravity.GravityBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentEntityTypes {

    public static EntityType<GravityBlockEntity> GRAVITY_BLOCK;

    public static void registerClient() {
        GRAVITY_BLOCK = register("gravity_block", 160, 20, true, EntityDimensions.changing(0.98F, 0.98F), GravityBlockEntity::new);
    }

    public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(PigmentCommon.MOD_ID, name), FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).trackable(trackingDistance, updateIntervalTicks, alwaysUpdateVelocity).dimensions(size).disableSaving().build());
    }

    public static <X extends Entity> EntityType<X> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<X> factory) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(PigmentCommon.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(size).disableSaving().build());
    }

}