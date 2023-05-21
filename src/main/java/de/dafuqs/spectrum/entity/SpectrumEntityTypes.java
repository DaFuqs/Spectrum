package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.registry.*;

public class SpectrumEntityTypes {
	
	public static final EntityType<LivingMarkerEntity> LIVING_MARKER = register("living_marker", 0, 2147483647, false, EntityDimensions.changing(0F, 0F), true, LivingMarkerEntity::new);
	public static final EntityType<ShootingStarEntity> SHOOTING_STAR = register("shooting_star", 15, 20, true, EntityDimensions.changing(0.8F, 0.8F), true, ShootingStarEntity::new);
	public static final EntityType<FloatBlockEntity> FLOAT_BLOCK = register("gravity_block", 10, 20, true, EntityDimensions.changing(0.98F, 0.98F), true, FloatBlockEntity::new);
	public static final EntityType<PhantomFrameEntity> PHANTOM_FRAME = register("phantom_frame", 10, 2147483647, false, EntityDimensions.changing(0.5F, 0.5F), false, PhantomFrameEntity::new);
	public static final EntityType<PhantomGlowFrameEntity> GLOW_PHANTOM_FRAME = register("glow_phantom_frame", 10, 2147483647, false, EntityDimensions.changing(0.5F, 0.5F), false, PhantomGlowFrameEntity::new);
	public static final EntityType<? extends ThrownItemEntity> BLOCK_FLOODER_PROJECTILE = register("block_flooder_projectile", 4, 10, true, EntityDimensions.changing(0.25F, 0.25F), true, BlockFlooderProjectile::new);
	public static final EntityType<InkProjectileEntity> INK_PROJECTILE = register("ink_projectile", 4, 10, true, EntityDimensions.changing(0.3F, 0.3F), true, InkProjectileEntity::new);
	public static final EntityType LAGOON_FISHING_BOBBER = register("lagoon_fishing_bobber", EntityType.Builder.create(LagoonFishingBobberEntity::new, SpawnGroup.MISC).disableSaving().disableSummon().makeFireImmune().setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(5));
	public static final EntityType MOLTEN_FISHING_BOBBER = register("molten_fishing_bobber", EntityType.Builder.create(MoltenFishingBobberEntity::new, SpawnGroup.MISC).disableSaving().disableSummon().makeFireImmune().setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(5));
	public static final EntityType BEDROCK_FISHING_BOBBER = register("bedrock_fishing_bobber", EntityType.Builder.create(BedrockFishingBobberEntity::new, SpawnGroup.MISC).disableSaving().disableSummon().makeFireImmune().setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(5));
	public static final EntityType<? extends ItemEntity> FIREPROOF_ITEM = register("non_burnable_item_entity", 6, 20, true, EntityDimensions.changing(0.25F, 0.25F), true, FireproofItemEntity::new);
	public static final EntityType<EggLayingWoolyPigEntity> EGG_LAYING_WOOLY_PIG = register("egg_laying_wooly_pig", EntityType.Builder.create(EggLayingWoolyPigEntity::new, SpawnGroup.CREATURE).setDimensions(0.9F, 1.3F).maxTrackingRange(10));
	public static final EntityType GLASS_ARROW = register("glass_arrow", EntityType.Builder.create(GlassArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20));
	public static final EntityType<MiningProjectileEntity> MINING_PROJECTILE = register("mining_projectile", 4, 10, true, EntityDimensions.changing(0.3F, 0.3F), true, MiningProjectileEntity::new);
	public static final EntityType<BidentEntity> BIDENT = register("bident", 4, 10, true, EntityDimensions.changing(0.5F, 0.5F), true, BidentEntity::new);
	public static final EntityType<BidentEntity> BIDENT_MIRROR_IMAGE = register("bident_mirror_image", 4, 10, true, EntityDimensions.changing(0.5F, 0.5F), true, BidentMirrorImageEntity::new);
	public static final EntityType<LightShardEntity> LIGHT_SHARD = register("light_shard", 4, 20, true, EntityDimensions.fixed(0.75F, 0.75F), true, LightShardEntity::new);
	public static final EntityType<LightSpearEntity> LIGHT_SPEAR = register("light_spear", 4, 20, true, EntityDimensions.fixed(0.75F, 0.75F), true, LightSpearEntity::new);
	public static final EntityType<LightMineEntity> LIGHT_MINE = register("light_mine", 4, 20, true, EntityDimensions.fixed(0.75F, 0.75F), true, LightMineEntity::new);
	public static final EntityType<MonstrosityEntity> MONSTROSITY = register("monstrosity", EntityType.Builder.create(MonstrosityEntity::new, SpawnGroup.MISC).makeFireImmune().spawnableFarFromPlayer().setDimensions(10.0F, 10.0F).maxTrackingRange(10));
	public static final EntityType<GuardianTurretEntity> GUARDIAN_TURRET = register("guardian_turret", EntityType.Builder.create(GuardianTurretEntity::new, SpawnGroup.MONSTER).makeFireImmune().spawnableFarFromPlayer().setDimensions(1.0F, 1.0F).maxTrackingRange(10));
	
	public static void register() {
		FabricDefaultAttributeRegistry.register(EGG_LAYING_WOOLY_PIG, EggLayingWoolyPigEntity.createEggLayingWoolyPigAttributes());
		FabricDefaultAttributeRegistry.register(MONSTROSITY, MonstrosityEntity.createMonstrosityAttributes());
		FabricDefaultAttributeRegistry.register(GUARDIAN_TURRET, GuardianTurretEntity.createGuardianTurretAttributes());
	}

	public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, boolean fireImmune, EntityType.EntityFactory<X> factory) {
		FabricEntityTypeBuilder<X> builder = FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).trackRangeChunks(trackingDistance).trackedUpdateRate(updateIntervalTicks).forceTrackedVelocityUpdates(alwaysUpdateVelocity).dimensions(size);
		if (fireImmune) {
			builder.fireImmune();
		}
		return Registry.register(Registries.ENTITY_TYPE, SpectrumCommon.locate(name), builder.build());
	}
	
	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
		return Registry.register(Registries.ENTITY_TYPE, SpectrumCommon.locate(name), type.build(name));
	}
	
	/*public static <X extends Entity> EntityType<X> register(String name, SpawnGroup category, EntityDimensions size, EntityType.EntityFactory<X> factory) {
		return Registry.register(Registry.ENTITY_TYPE, SpectrumCommon.locate(name), FabricEntityTypeBuilder.create(category, factory).dimensions(size).build());
	}*/
	
}