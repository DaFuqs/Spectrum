package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.levelgen.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumEntityTypes {
	
	private static final DeferredRegister<EntityType<?>> REGISTRAR = DeferredRegister.create(Registries.ENTITY_TYPE, SpectrumCommon.MOD_ID);
	
	public static final DeferredHolder<EntityType<?>, EntityType<LivingMarkerEntity>> LIVING_MARKER = register("living_marker", EntityType.Builder.of(LivingMarkerEntity::new, MobCategory.MISC).clientTrackingRange(0).updateInterval(2147483647).setShouldReceiveVelocityUpdates(false).sized(0F, 0F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<ShootingStarEntity>> SHOOTING_STAR = register("shooting_star", EntityType.Builder.of((EntityType.EntityFactory<ShootingStarEntity>) ShootingStarEntity::new, MobCategory.MISC).clientTrackingRange(15).updateInterval(20).setShouldReceiveVelocityUpdates(true).sized(0.8F, 0.8F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<SeatEntity>> SEAT = register("seat", EntityType.Builder.of((EntityType.EntityFactory<SeatEntity>) SeatEntity::new, MobCategory.MISC).clientTrackingRange(8).updateInterval(10).setShouldReceiveVelocityUpdates(false).sized(0.01F, 0.01F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<FloatBlockEntity>> FLOAT_BLOCK = register("float_block", EntityType.Builder.of((EntityType.EntityFactory<FloatBlockEntity>) FloatBlockEntity::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(20).sized(0.98F, 0.98F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<PhantomFrameEntity>> PHANTOM_FRAME = register("phantom_frame", EntityType.Builder.of((EntityType.EntityFactory<PhantomFrameEntity>) PhantomFrameEntity::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(2147483647).setShouldReceiveVelocityUpdates(false).sized(0.5F, 0.5F));
	public static final DeferredHolder<EntityType<?>, EntityType<PhantomGlowFrameEntity>> GLOW_PHANTOM_FRAME = register("glow_phantom_frame", EntityType.Builder.of((EntityType.EntityFactory<PhantomGlowFrameEntity>) PhantomGlowFrameEntity::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(2147483647).setShouldReceiveVelocityUpdates(false).sized(0.5F, 0.5F));
	public static final DeferredHolder<EntityType<?>, EntityType<BlockFlooderProjectile>> BLOCK_FLOODER_PROJECTILE = register("block_flooder_projectile", EntityType.Builder.of((EntityType.EntityFactory<BlockFlooderProjectile>) BlockFlooderProjectile::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<InkProjectileEntity>> INK_PROJECTILE = register("ink_projectile", EntityType.Builder.of((EntityType.EntityFactory<InkProjectileEntity>) InkProjectileEntity::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.3F, 0.3F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<LagoonFishingHook>> LAGOON_FISHING_BOBBER = register("lagoon_fishing_bobber", EntityType.Builder.<LagoonFishingHook>of(LagoonFishingHook::new, MobCategory.MISC).noSave().noSummon().fireImmune().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
	public static final DeferredHolder<EntityType<?>, EntityType<MoltenFishingHook>> MOLTEN_FISHING_BOBBER = register("molten_fishing_bobber", EntityType.Builder.<MoltenFishingHook>of(MoltenFishingHook::new, MobCategory.MISC).noSave().noSummon().fireImmune().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
	public static final DeferredHolder<EntityType<?>, EntityType<BedrockFishingHook>> BEDROCK_FISHING_BOBBER = register("bedrock_fishing_bobber", EntityType.Builder.<BedrockFishingHook>of(BedrockFishingHook::new, MobCategory.MISC).noSave().noSummon().fireImmune().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
	public static final DeferredHolder<EntityType<?>, EntityType<FireproofItemEntity>> FIREPROOF_ITEM = register("fireproof_item", EntityType.Builder.of((EntityType.EntityFactory<FireproofItemEntity>) FireproofItemEntity::new, MobCategory.MISC).clientTrackingRange(6).updateInterval(20).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<EggLayingWoolyPigEntity>> EGG_LAYING_WOOLY_PIG = register("egg_laying_wooly_pig", EntityType.Builder.of(EggLayingWoolyPigEntity::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<GlassArrowEntity>> GLASS_ARROW = register("glass_arrow", EntityType.Builder.<GlassArrowEntity>of(GlassArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<MiningProjectileEntity>> MINING_PROJECTILE = register("mining_projectile", EntityType.Builder.of((EntityType.EntityFactory<MiningProjectileEntity>) MiningProjectileEntity::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.3F, 0.3F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<ParametricMiningDeviceEntity>> PARAMETRIC_MINING_DEVICE_ENTITY = register("parametric_mining_device", EntityType.Builder.of((EntityType.EntityFactory<ParametricMiningDeviceEntity>) ParametricMiningDeviceEntity::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F));
	public static final DeferredHolder<EntityType<?>, EntityType<BidentEntity>> BIDENT = register("bident", EntityType.Builder.of((EntityType.EntityFactory<BidentEntity>) BidentEntity::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<BidentMirrorImageEntity>> BIDENT_MIRROR_IMAGE = register("bident_mirror_image", EntityType.Builder.of((EntityType.EntityFactory<BidentMirrorImageEntity>) BidentMirrorImageEntity::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<LightShardEntity>> LIGHT_SHARD = register("light_shard", EntityType.Builder.<LightShardEntity>of(LightShardEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<LightSpearEntity>> LIGHT_SPEAR = register("light_spear", EntityType.Builder.<LightSpearEntity>of(LightSpearEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<LightMineEntity>> LIGHT_MINE = register("light_mine", EntityType.Builder.<LightMineEntity>of(LightMineEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<MonstrosityEntity>> MONSTROSITY = register("monstrosity", EntityType.Builder.of(MonstrosityEntity::new, MobCategory.MISC).fireImmune().canSpawnFarFromPlayer().sized(6.0F, 6.0F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<PreservationTurretEntity>> PRESERVATION_TURRET = register("preservation_turret", EntityType.Builder.of(PreservationTurretEntity::new, MobCategory.MONSTER).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<LizardEntity>> LIZARD = register("lizard", EntityType.Builder.of(LizardEntity::new, MobCategory.AMBIENT).sized(1.0F, 0.7F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<KindlingEntity>> KINDLING = register("kindling", EntityType.Builder.of(KindlingEntity::new, MobCategory.CREATURE).sized(1.0F, 1.0F).passengerAttachments(0.5F).clientTrackingRange(10).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<KindlingCoughEntity>> KINDLING_COUGH = register("kindling_cough", EntityType.Builder.<KindlingCoughEntity>of(KindlingCoughEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<EraserEntity>> ERASER = register("eraser", EntityType.Builder.of(EraserEntity::new, MobCategory.MONSTER).sized(0.3F, 0.3F).clientTrackingRange(10));
	public static final DeferredHolder<EntityType<?>, EntityType<ItemProjectileEntity>> ITEM_PROJECTILE = register("item_projectile", EntityType.Builder.<ItemProjectileEntity>of(ItemProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));
	public static final DeferredHolder<EntityType<?>, EntityType<DragonTalonEntity>> DRAGON_TALON = register("dragon_talon", EntityType.Builder.of((EntityType.EntityFactory<DragonTalonEntity>) DragonTalonEntity::new, MobCategory.MISC).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<DraconicTwinswordEntity>> DRACONIC_TWINSWORD= register("draconic_twinsword", EntityType.Builder.of((EntityType.EntityFactory<DraconicTwinswordEntity>) DraconicTwinswordEntity::new, MobCategory.MISC).clientTrackingRange(6).updateInterval(2).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<Marrow>> MARROW = register("marrow", EntityType.Builder.of(Marrow::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8));
	public static final DeferredHolder<EntityType<?>, EntityType<Splinterspawn>> SPLINTERSPAWN = register("splinterspawn", EntityType.Builder.of(Splinterspawn::new, MobCategory.MONSTER).sized(0.4F, 0.3F).eyeHeight(0.13F).passengerAttachments(0.2375F).clientTrackingRange(8));
	
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(LIVING_MARKER.get(),LivingMarkerEntity.createLivingAttributes().build());
		event.put(EGG_LAYING_WOOLY_PIG.get(), EggLayingWoolyPigEntity.createEggLayingWoolyPigAttributes().build());
		event.put(MONSTROSITY.get(), MonstrosityEntity.createMonstrosityAttributes().build());
		event.put(PRESERVATION_TURRET.get(), PreservationTurretEntity.createGuardianTurretAttributes().build());
		event.put(LIZARD.get(), LizardEntity.createLizardAttributes().build());
		event.put(KINDLING.get(), KindlingEntity.createKindlingAttributes().build());
		event.put(ERASER.get(), EraserEntity.createEraserAttributes().build());
		event.put(MARROW.get(), Marrow.createMarrowAttributes().build());
		event.put(SPLINTERSPAWN.get(), Splinterspawn.createSplinterSpawnAttributes().build());
	}
	
	public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(ERASER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(MARROW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
	}
	
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
		return REGISTRAR.register(name, () -> builder.build(SpectrumCommon.MOD_ID + "." + name));
	}
	
	public static void register(IEventBus modBus) {
		modBus.addListener(SpectrumEntityTypes::registerAttributes);
		modBus.addListener(SpectrumEntityTypes::registerSpawnPlacements);
		REGISTRAR.register(modBus);
	}
	
}
