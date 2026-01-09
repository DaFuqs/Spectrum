package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.projectile.*;

public class SpectrumEntityTypes {
	
	public static final EntityType<LivingMarkerEntity> LIVING_MARKER = register("living_marker", 0, 2147483647, false, EntityDimensions.scalable(0F, 0F), true, LivingMarkerEntity::new);
	public static final EntityType<ShootingStarEntity> SHOOTING_STAR = register("shooting_star", 15, 20, true, EntityDimensions.scalable(0.8F, 0.8F), true, ShootingStarEntity::new);
	public static final EntityType<SeatEntity> SEAT = register("seat", 8, 10, false, EntityDimensions.scalable(0.01F, 0.01F), true, SeatEntity::new);
	public static final EntityType<FloatBlockEntity> FLOAT_BLOCK = register("float_block", 10, 20, true, EntityDimensions.scalable(0.98F, 0.98F), true, FloatBlockEntity::new);
	public static final EntityType<PhantomFrameEntity> PHANTOM_FRAME = register("phantom_frame", 10, 2147483647, false, EntityDimensions.scalable(0.5F, 0.5F), false, PhantomFrameEntity::new);
	public static final EntityType<PhantomGlowFrameEntity> GLOW_PHANTOM_FRAME = register("glow_phantom_frame", 10, 2147483647, false, EntityDimensions.scalable(0.5F, 0.5F), false, PhantomGlowFrameEntity::new);
	public static final EntityType<? extends ThrowableItemProjectile> BLOCK_FLOODER_PROJECTILE = register("block_flooder_projectile", 4, 10, true, EntityDimensions.scalable(0.25F, 0.25F), true, BlockFlooderProjectile::new);
	public static final EntityType<InkProjectileEntity> INK_PROJECTILE = register("ink_projectile", 4, 10, true, EntityDimensions.scalable(0.3F, 0.3F), true, InkProjectileEntity::new);
	public static final EntityType<LagoonFishingBobberEntity> LAGOON_FISHING_BOBBER = register("lagoon_fishing_bobber", EntityType.Builder.<LagoonFishingBobberEntity>of(LagoonFishingBobberEntity::new, MobCategory.MISC).noSave().noSummon().fireImmune().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
	public static final EntityType<MoltenFishingBobberEntity> MOLTEN_FISHING_BOBBER = register("molten_fishing_bobber", EntityType.Builder.<MoltenFishingBobberEntity>of(MoltenFishingBobberEntity::new, MobCategory.MISC).noSave().noSummon().fireImmune().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
	public static final EntityType<BedrockFishingBobberEntity> BEDROCK_FISHING_BOBBER = register("bedrock_fishing_bobber", EntityType.Builder.<BedrockFishingBobberEntity>of(BedrockFishingBobberEntity::new, MobCategory.MISC).noSave().noSummon().fireImmune().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
	public static final EntityType<? extends ItemEntity> FIREPROOF_ITEM = register("fireproof_item", 6, 20, true, EntityDimensions.scalable(0.25F, 0.25F), true, FireproofItemEntity::new);
	public static final EntityType<EggLayingWoolyPigEntity> EGG_LAYING_WOOLY_PIG = register("egg_laying_wooly_pig", EntityType.Builder.of(EggLayingWoolyPigEntity::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10));
	public static final EntityType<GlassArrowEntity> GLASS_ARROW = register("glass_arrow", EntityType.Builder.<GlassArrowEntity>of(GlassArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<MiningProjectileEntity> MINING_PROJECTILE = register("mining_projectile", 4, 10, true, EntityDimensions.scalable(0.3F, 0.3F), true, MiningProjectileEntity::new);
	public static final EntityType<ParametricMiningDeviceEntity> PARAMETRIC_MINING_DEVICE_ENTITY = register("parametric_mining_device", 4, 10, true, EntityDimensions.scalable(0.25F, 0.25F), false, ParametricMiningDeviceEntity::new);
	public static final EntityType<BidentEntity> BIDENT = register("bident", 4, 10, true, EntityDimensions.scalable(0.5F, 0.5F), true, BidentEntity::new);
	public static final EntityType<BidentMirrorImageEntity> BIDENT_MIRROR_IMAGE = register("bident_mirror_image", 4, 10, true, EntityDimensions.scalable(0.5F, 0.5F), true, BidentMirrorImageEntity::new);
	public static final EntityType<LightShardEntity> LIGHT_SHARD = register("light_shard", EntityType.Builder.<LightShardEntity>of(LightShardEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<LightSpearEntity> LIGHT_SPEAR = register("light_spear", EntityType.Builder.<LightSpearEntity>of(LightSpearEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<LightMineEntity> LIGHT_MINE = register("light_mine", EntityType.Builder.<LightMineEntity>of(LightMineEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<MonstrosityEntity> MONSTROSITY = register("monstrosity", EntityType.Builder.of(MonstrosityEntity::new, MobCategory.MISC).fireImmune().canSpawnFarFromPlayer().sized(4.0F, 4.0F).eyeHeight(2.0F).clientTrackingRange(10));
	public static final EntityType<PreservationTurretEntity> PRESERVATION_TURRET = register("preservation_turret", EntityType.Builder.of(PreservationTurretEntity::new, MobCategory.MONSTER).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10));
	public static final EntityType<LizardEntity> LIZARD = register("lizard", EntityType.Builder.of(LizardEntity::new, MobCategory.MONSTER).sized(1.0F, 0.7F).clientTrackingRange(10));
	public static final EntityType<KindlingEntity> KINDLING = register("kindling", EntityType.Builder.of(KindlingEntity::new, MobCategory.CREATURE).sized(1.0F, 1.0F).passengerAttachments(0.5F).clientTrackingRange(10).fireImmune());
	public static final EntityType<KindlingCoughEntity> KINDLING_COUGH = register("kindling_cough", EntityType.Builder.<KindlingCoughEntity>of(KindlingCoughEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).fireImmune());
	public static final EntityType<EraserEntity> ERASER = register("eraser", EntityType.Builder.of(EraserEntity::new, MobCategory.MONSTER).sized(0.4F, 0.4F).clientTrackingRange(10));
	public static final EntityType<ItemProjectileEntity> ITEM_PROJECTILE = register("item_projectile", EntityType.Builder.<ItemProjectileEntity>of(ItemProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));
	public static final EntityType<DragonTalonEntity> DRAGON_TALON = register("dragon_talon", 4, 10, true, EntityDimensions.scalable(0.5F, 0.5F), true, DragonTalonEntity::new);
	public static final EntityType<DraconicTwinswordEntity> DRACONIC_TWINSWORD = register("draconic_twinsword", 6, 2, true, EntityDimensions.scalable(0.5F, 0.5F), true, DraconicTwinswordEntity::new);
	public static final EntityType<Marrow> MARROW = register("marrow", EntityType.Builder.of(Marrow::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8));
	public static final EntityType<Splinterspawn> SPLINTERSPAWN = register("splinterspawn", EntityType.Builder.of(Splinterspawn::new, MobCategory.MONSTER).sized(0.4F, 0.3F).eyeHeight(0.13F).passengerAttachments(0.2375F).clientTrackingRange(8));
	
	public static void register() {
		FabricDefaultAttributeRegistry.register(EGG_LAYING_WOOLY_PIG, EggLayingWoolyPigEntity.createEggLayingWoolyPigAttributes());
		FabricDefaultAttributeRegistry.register(MONSTROSITY, MonstrosityEntity.createMonstrosityAttributes());
		FabricDefaultAttributeRegistry.register(PRESERVATION_TURRET, PreservationTurretEntity.createGuardianTurretAttributes());
		FabricDefaultAttributeRegistry.register(LIZARD, LizardEntity.createLizardAttributes());
		FabricDefaultAttributeRegistry.register(KINDLING, KindlingEntity.createKindlingAttributes());
		FabricDefaultAttributeRegistry.register(ERASER, EraserEntity.createEraserAttributes());
		FabricDefaultAttributeRegistry.register(MARROW, Marrow.createMarrowAttributes());
		FabricDefaultAttributeRegistry.register(SPLINTERSPAWN, Splinterspawn.createSplinterSpawnAttributes());
	}
	
	// TODO: migrate to FabricEntityTypeBuilder, so the "No data fixer registered for xxxx" errors go away
	public static <X extends Entity> EntityType<X> register(String name, int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity, EntityDimensions size, boolean fireImmune, EntityType.EntityFactory<X> factory) {
		EntityType.Builder<X> builder = EntityType.Builder.of(factory, MobCategory.MISC).clientTrackingRange(trackingDistance).updateInterval(updateIntervalTicks).alwaysUpdateVelocity(alwaysUpdateVelocity).sized(size.width(), size.height());
		if (fireImmune) {
			builder.fireImmune();
		}
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, SpectrumCommon.locate(name), builder.build());
	}
	
	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, SpectrumCommon.locate(id), type.build(id));
	}
	
	static {
	}
	
}
