package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.util.*;
import net.minecraft.world.entity.ai.village.poi.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

// funny little creatures always out for trouble
public class LizardEntity extends TamableAnimal implements PackEntity<LizardEntity>, POIMemorized {
	
	protected static final EntityDataAccessor<Holder<LizardFrillVariant>> FRILL_VARIANT = SynchedEntityData.defineId(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_FRILL_VARIANT);
	protected static final EntityDataAccessor<Holder<LizardHornVariant>> HORN_VARIANT = SynchedEntityData.defineId(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_HORN_VARIANT);
	protected static final EntityDataAccessor<InkColor> COLOR = SynchedEntityData.defineId(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.INK_COLOR);

	protected @Nullable LizardEntity leader;
	protected int groupSize = 1;

	protected int ticksLeftToFindPOI;
	protected @Nullable BlockPos poiPos;
	
	public LizardEntity(EntityType<? extends LizardEntity> entityType, Level world) {
		super(entityType, world);
		this.xpReward = 4;
	}
	
	public static AttributeSupplier.Builder createLizardAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.ATTACK_DAMAGE, 16.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.ARMOR, 6.0D)
				.add(Attributes.ARMOR_TOUGHNESS, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D)
				.add(Attributes.FOLLOW_RANGE, 12.0D);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new OcelotAttackGoal(this));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.2D));
		this.goalSelector.addGoal(4, new FollowClanLeaderGoal<>(this));
		this.goalSelector.addGoal(5, new FindPOIGoal(PoiTypes.LODESTONE, 32));
		this.goalSelector.addGoal(6, new ClanLeaderWanderAroundGoal(this, 0.8, 20, 8, 4));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, target -> !LizardEntity.this.isOwnedBy(target)));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, // different clans attacking each other
				target -> {
					if (target instanceof LizardEntity other) {
						return isDifferentPack(other);
					}
					return !target.isBaby();
				}));
	}

	@Override
	public float getLightLevelDependentMagicValue() {
		return 1.0F;
	}

	@Override
	public boolean isOwnedBy(LivingEntity entity) {
		return entity == this.getOwner() || this.leader != null && entity == this.leader.getOwner();
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (this.tickCount % 1200 == 0) {
			this.heal(1.0F);
		}
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(COLOR, InkColors.MAGENTA);
		
		RegistryAccess registryAccess = this.registryAccess();
		Registry<LizardFrillVariant> lizardFrillVariantRegistry = registryAccess.registryOrThrow(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT);
		Optional<Holder.Reference<LizardFrillVariant>> frillHolder = lizardFrillVariantRegistry.getHolder(LizardFrillVariant.SIMPLE);
		builder.define(FRILL_VARIANT, frillHolder.or(lizardFrillVariantRegistry::getAny).orElseThrow());
		
		Registry<LizardHornVariant> kindlingHornRegistry = registryAccess.registryOrThrow(SpectrumRegistryKeys.LIZARD_HORN_VARIANT);
		Optional<Holder.Reference<LizardHornVariant>> hornHolder = kindlingHornRegistry.getHolder(LizardHornVariant.HORNY);
		builder.define(HORN_VARIANT, hornHolder.or(kindlingHornRegistry::getAny).orElseThrow());
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
		RandomSource random = world.getRandom();
		this.setFrills(world.registryAccess().registry(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT).get().getRandom(world.getRandom()).get());
		this.setHorns(world.registryAccess().registry(SpectrumRegistryKeys.LIZARD_HORN_VARIANT).get().getRandom(world.getRandom()).get());
		
		List<InkColor> elementals = InkColors.elementals();
		this.setColor(elementals.get(random.nextInt(elementals.size())));
		
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putString("color", this.getColor().getID().toString());
		this.getFrills().unwrapKey().ifPresent((resourceKey) -> {
			nbt.putString("frills", resourceKey.location().toString());
		});
		this.getHorns().unwrapKey().ifPresent((resourceKey) -> {
			nbt.putString("horns", resourceKey.location().toString());
		});
		writePOIPosToNbt(nbt);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		
		InkColor color = SpectrumRegistries.INK_COLOR.get(ResourceLocation.tryParse(nbt.getString("color")));
		this.setColor(color == null ? SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.INK_COLOR, InkColorTags.ELEMENTAL_COLORS, this.getRandom(), InkColors.CYAN) : color);
		
		Optional.ofNullable(ResourceLocation.tryParse(nbt.getString("frills")))
				.map((resourceLocation) -> ResourceKey.create(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, resourceLocation))
				.flatMap((resourceKey) -> this.registryAccess().registryOrThrow(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT).getHolder(resourceKey))
				.ifPresent(this::setFrills);
		
		Optional.ofNullable(ResourceLocation.tryParse(nbt.getString("horns")))
				.map((resourceLocation) -> ResourceKey.create(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, resourceLocation))
				.flatMap((resourceKey) -> this.registryAccess().registryOrThrow(SpectrumRegistryKeys.LIZARD_HORN_VARIANT).getHolder(resourceKey))
				.ifPresent(this::setHorns);
		
		readPOIPosFromNbt(nbt);
	}

	@Override
	public void aiStep() {
		Level world = this.level();
		super.aiStep();
		if (!world.isClientSide() && this.ticksLeftToFindPOI > 0) {
			--this.ticksLeftToFindPOI;
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		Level world = this.level();
		ItemStack itemStack = player.getItemInHand(hand);
		if (this.isFood(itemStack)) {
			int i = this.getAge();
			if (!world.isClientSide() && i == 0 && this.canFallInLove() && this.getRandom().nextInt(5) == 0) {
				// yes, this also overrides the existing owner
				// there is no god besides the new god
				this.usePlayerItem(player, hand, itemStack);
				this.tame(player);
				this.setInLove(player);
				return InteractionResult.SUCCESS;
			}

			if (this.isBaby()) {
				this.usePlayerItem(player, hand, itemStack);
				this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
				return InteractionResult.sidedSuccess(world.isClientSide());
			}
			
			if (world.isClientSide()) {
				return InteractionResult.CONSUME;
			}
		}
		
		return InteractionResult.PASS;
	}

	@Override
	public boolean canFallInLove() {
		return super.canFallInLove() || getOwner() != null;
	}
	
	public InkColor getColor() {
		return this.entityData.get(COLOR);
	}
	
	public void setColor(InkColor color) {
		this.entityData.set(COLOR, color);
	}
	
	public Holder<LizardFrillVariant> getFrills() {
		return this.entityData.get(FRILL_VARIANT);
	}
	
	public void setFrills(Holder<LizardFrillVariant> variant) {
		this.entityData.set(FRILL_VARIANT, variant);
	}
	
	public Holder<LizardHornVariant> getHorns() {
		return this.entityData.get(HORN_VARIANT);
	}
	
	public void setHorns(Holder<LizardHornVariant> variant) {
		this.entityData.set(HORN_VARIANT, variant);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SpectrumSoundEvents.ENTITY_LIZARD_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_LIZARD_DEATH;
	}

	// Breeding

	@Override
	public boolean isFood(ItemStack stack) {
		if (stack.is(SpectrumItems.LIZARD_MEAT)) {
			return false;
		}
		return stack.is(ItemTags.MEAT);
	}

	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		LizardEntity other = (LizardEntity) entity;
		LizardEntity child = SpectrumEntityTypes.LIZARD.create(world);
		if (child != null) {
			child.setColor(getChildColor(this, other));
			child.setFrills(getChildFrills(this, other));
			child.setHorns(getChildHorns(this, other));
		}
		return child;
	}
	
	private InkColor getChildColor(LizardEntity firstParent, LizardEntity secondParent) {
		Level world = firstParent.level();
		InkColor color1 = firstParent.getColor();
		InkColor color2 = secondParent.getColor();
		
		return InkColorMixes.getRandomMixedColor(color1, color2, world.getRandom());
	}
	
	private Holder<LizardFrillVariant> getChildFrills(LizardEntity firstParent, LizardEntity secondParent) {
		Level world = this.level();
		return world.getRandom().nextBoolean() ? firstParent.getFrills() : secondParent.getFrills();
	}
	
	private Holder<LizardHornVariant> getChildHorns(LizardEntity firstParent, LizardEntity secondParent) {
		Level world = this.level();
		return world.getRandom().nextBoolean() ? firstParent.getHorns() : secondParent.getHorns();
	}

	// PackEntity

	@Override
	public boolean hasOthersInGroup() {
		return this.groupSize > 1;
	}

	@Override
	public @Nullable LizardEntity getLeader() {
		return this.leader;
	}

	@Override
	public boolean isCloseEnoughToLeader() {
		return this.distanceToSqr(this.leader) <= 121.0;
	}

	@Override
	public void leaveGroup() {
		this.leader.decreaseGroupSize();
		this.leader = null;
	}

	@Override
	public void moveTowardLeader() {
		if (this.hasLeader()) {
			this.getNavigation().moveTo(this.leader, 1.0);
		}
	}

	@Override
	public int getMaxGroupSize() {
		return super.getMaxSpawnClusterSize();
	}

	@Override
	public void joinGroupOf(LizardEntity groupLeader) {
		this.leader = groupLeader;
		groupLeader.increaseGroupSize();
	}

	@Override
	public int getGroupSize() {
		return this.groupSize;
	}

	protected void increaseGroupSize() {
		++this.groupSize;
	}

	protected void decreaseGroupSize() {
		--this.groupSize;
	}

	// POIMemorized
	@Override
	public TagKey<PoiType> getPOITag() {
		return SpectrumPointOfInterestTypeTags.LIZARD_DENS;
	}

	@Override
	public @Nullable BlockPos getPOIPos() {
		return this.poiPos;
	}

	@Override
	public void setPOIPos(@Nullable BlockPos blockPos) {
		this.poiPos = blockPos;
	}

	// Goals
	protected class ClanLeaderWanderAroundGoal extends RandomStrollGoal {

		int chanceToNavigateToPOI;
		int maxDistanceFromPOI;
		
		public ClanLeaderWanderAroundGoal(PathfinderMob mob, double speed, int chance, int chanceToNavigateToPOI, int maxDistanceFromPOI) {
			super(mob, speed, chance);
			this.chanceToNavigateToPOI = chanceToNavigateToPOI;
			this.maxDistanceFromPOI = maxDistanceFromPOI;
		}

		@Override
		public boolean canUse() {
			return !LizardEntity.this.hasLeader() && super.canUse();
		}

		@Override
		protected @Nullable Vec3 getPosition() {
			// when we are away from our poi (their den) there is a chance they navigate back to it, so they always stay near
			if (random.nextFloat() < this.chanceToNavigateToPOI
					&& LizardEntity.this.isPOIValid((ServerLevel) LizardEntity.this.level())
					&& !LizardEntity.this.blockPosition().closerThan(LizardEntity.this.poiPos, this.maxDistanceFromPOI)) {
				
				return Vec3.atCenterOf(LizardEntity.this.poiPos);
			}
			
			return DefaultRandomPos.getPos(LizardEntity.this, 8, 7);
		}

	}

	private class FindPOIGoal extends Goal {
		
		FindPOIGoal(ResourceKey<PoiType> poiType, int maxDistance) {
			super();
		}

		@Override
		public boolean canUse() {
			return LizardEntity.this.hasOthersInGroup()
					&& LizardEntity.this.ticksLeftToFindPOI == 0
					&& !LizardEntity.this.isPOIValid((ServerLevel) LizardEntity.this.level());
		}

		@Override
		public void start() {
			LizardEntity.this.ticksLeftToFindPOI = 200;
			LizardEntity.this.poiPos = LizardEntity.this.findNearestPOI((ServerLevel) LizardEntity.this.level(), LizardEntity.this.blockPosition(), 40);
		}

	}

}
