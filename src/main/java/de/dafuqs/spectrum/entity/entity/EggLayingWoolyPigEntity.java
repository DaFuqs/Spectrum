package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.event.*;
import org.jspecify.annotations.*;

import java.util.*;

public class EggLayingWoolyPigEntity extends Animal implements Shearable {
	
	private static final Ingredient FOOD = Ingredient.of(SpectrumBlocks.AMARANTH_BUSHEL);
	
	public static final int EAT_ANIMATION_TICKS = 40;
	private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(EggLayingWoolyPigEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HATLESS = SynchedEntityData.defineId(EggLayingWoolyPigEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> EATEN_COLORED_GRASS = SynchedEntityData.defineId(EggLayingWoolyPigEntity.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Optional<InkColor>> COLOR = SynchedEntityData.defineId(EggLayingWoolyPigEntity.class, SpectrumEntityDataSerializers.OPTIONAL_INK_COLOR);
	public static final int DEFAULT_COLOR = -1644826;
	
	private int eatGrassTimer;
	private EatGrassOrColoredGrassGoal eatGrassGoal;
	protected int eggLayTime;
	
	public EggLayingWoolyPigEntity(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
		this.eggLayTime = this.getRandom().nextInt(12000) + 12000;
	}
	
	public int getRenderedColor() {
		if (getColor().isEmpty()) {
			return DEFAULT_COLOR;
		}
		return getColor().get().getColorRGB();
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		Level world = this.level();
		ItemStack handStack = player.getItemInHand(hand);
		
		if (handStack.getItem() instanceof DyeItem dyeItem && isAlive() && getColor().isPresent() && getColor().get().getDyeColor().get() != dyeItem.getDyeColor()) {
			InkColor dyeInkColor = InkColor.ofDyeColor(dyeItem.getDyeColor());
			if (!world.isClientSide() && dyeInkColor != null) {
				setColor(Optional.of(dyeInkColor));
				world.playSound(player, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
				handStack.shrink(1);
			}
			return InteractionResult.sidedSuccess(world.isClientSide());
		} else if (handStack.is(Items.BUCKET) && !this.isBaby()) {
			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
			ItemStack itemStack2 = ItemUtils.createFilledResult(handStack, player, Items.MILK_BUCKET.getDefaultInstance());
			player.setItemInHand(hand, itemStack2);
			return InteractionResult.sidedSuccess(world.isClientSide());
		} else if (handStack.is(Tags.Items.TOOLS_SHEAR)) {
			if (!world.isClientSide() && this.readyForShearing()) {
				this.shear(SoundSource.PLAYERS);
				this.gameEvent(GameEvent.SHEAR, player);
				handStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		} else {
			return super.mobInteract(player, hand);
		}
	}
	
	public static AttributeSupplier.Builder createEggLayingWoolyPigAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 12.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D);
	}
	
	@Override
	public boolean isFood(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Override
	protected void registerGoals() {
		this.eatGrassGoal = new EatGrassOrColoredGrassGoal(this);
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, FOOD, false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(5, this.eatGrassGoal);
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}
	
	@Override
	public void handleEntityEvent(byte status) {
		if (status == 10) {
			this.eatGrassTimer = EAT_ANIMATION_TICKS;
		} else {
			super.handleEntityEvent(status);
		}
	}
	
	@Override
	protected void customServerAiStep() {
		this.eatGrassTimer = this.eatGrassGoal.getEatAnimationTick();
		super.customServerAiStep();
	}
	
	@Override
	public void aiStep() {
		if (this.level().isClientSide()) {
			this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
		}
		
		if (!this.level().isClientSide() && this.isAlive() && !this.isBaby() && --this.eggLayTime <= 0) {
			this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);
			
			if(hasEatenColoredGrass() && this.getColor().isPresent()) {
				this.spawnAtLocation(PigmentItem.byColor(getColor().get()));
			} else {
				this.spawnAtLocation(Items.EGG);
			}
			
			this.eggLayTime = this.getRandom().nextInt(6000) + 6000;
			this.setEatenColoredGrass(false);
		}
		
		super.aiStep();
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SHEARED, false);
		builder.define(HATLESS, false);
		builder.define(EATEN_COLORED_GRASS, false);
		builder.define(COLOR, Optional.empty());
	}

	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		EggLayingWoolyPigEntity other = (EggLayingWoolyPigEntity) entity;
		EggLayingWoolyPigEntity child = SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG.get().create(world);
		if (child != null) {
			child.setColor(this.getChildColor(this, other));
			if (world.getRandom().nextInt(50) == 0) {
				child.setHatless(true);
			}
		}
		return child;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("Sheared", this.isSheared());
		nbt.putBoolean("Hatless", this.isHatless());
		nbt.putBoolean("eaten_colored_grass", this.hasEatenColoredGrass());
		if(getColor().isPresent()) {
			nbt.putString("color", this.getColor().get().getID().toString());
		}
		nbt.putInt("EggLayTime", this.eggLayTime);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.setSheared(nbt.getBoolean("Sheared"));
		this.setHatless(nbt.getBoolean("Hatless"));
		this.setEatenColoredGrass(nbt.getBoolean("eaten_colored_grass"));
		
		if (nbt.contains("color")) {
			this.setColor(SpectrumRegistries.INK_COLOR.getOptional(ResourceLocation.tryParse(nbt.getString("color"))));
			
		}
		
		if (nbt.contains("EggLayTime")) {
			this.eggLayTime = nbt.getInt("EggLayTime");
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_EGG_LAYING_WOOLY_PIG_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SpectrumSoundEvents.ENTITY_EGG_LAYING_WOOLY_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_EGG_LAYING_WOOLY_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SpectrumSoundEvents.ENTITY_EGG_LAYING_WOOLY_STEP, 0.15F, 1.0F);
	}
	
	@Override
	public void ate() {
		this.setSheared(false);
		if (this.isBaby()) {
			this.ageUp(60);
		}
	}
	
	public float getNeckAngle(float delta) {
		if (this.eatGrassTimer <= 0) {
			return 0.0F;
		} else if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
			return 1.0F;
		} else {
			return this.eatGrassTimer < 4 ? ((float) this.eatGrassTimer - delta) / 4.0F : -((float) (this.eatGrassTimer - EAT_ANIMATION_TICKS) - delta) / 4.0F;
		}
	}
	
	public float getHeadAngle(float delta) {
		if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
			float f = ((float) (this.eatGrassTimer - 4) - delta) / 32.0F;
			return 0.62831855F + 0.21991149F * Mth.sin(f * 28.7F);
		} else {
			return this.eatGrassTimer > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
		}
	}
	
	@Override
	public void shear(SoundSource shearedSoundSource) {
		var world = this.level();
		world.playSound(null, this, SoundEvents.SHEEP_SHEAR, shearedSoundSource, 1.0F, 1.0F);
		this.setSheared(true);
		
		for (ItemStack droppedStack : getShearedStacks((ServerLevel) world)) {
			ItemEntity itemEntity = this.spawnAtLocation(droppedStack, 1);
			if (itemEntity != null) {
				itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.1F, this.getRandom().nextFloat() * 0.05F, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.1F));
			}
		}
	}
	
	public List<ItemStack> getShearedStacks(ServerLevel world) {
		var builder = (new LootParams.Builder(world))
				.withParameter(LootContextParams.THIS_ENTITY, this)
				.withParameter(LootContextParams.ORIGIN, this.position());
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(SpectrumLootTableKeys.EGG_LAYING_WOOLY_PIG_SHEARING);
		return lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
	}
	
	@Override
	public boolean readyForShearing() {
		return this.isAlive() && !this.isSheared() && !this.isBaby();
	}
	
	public boolean isSheared() {
		return this.entityData.get(SHEARED);
	}
	
	public void setSheared(boolean sheared) {
		this.entityData.set(SHEARED, sheared);
	}
	
	public boolean isHatless() {
		return this.entityData.get(HATLESS);
	}
	
	public void setHatless(boolean hatless) {
		this.entityData.set(HATLESS, hatless);
	}
	
	public Optional<InkColor> getColor() {
		return this.entityData.get(COLOR);
	}
	
	public void setColor(Optional<InkColor> color) {
		this.entityData.set(COLOR, color);
	}
	
	public void setEatenColoredGrass(boolean eatenColoredGrass) {
		this.entityData.set(EATEN_COLORED_GRASS, eatenColoredGrass);
	}
	
	public boolean hasEatenColoredGrass() {
		return this.entityData.get(EATEN_COLORED_GRASS);
	}
	
	private Optional<InkColor> getChildColor(Animal firstParent, Animal secondParent) {
		Optional<InkColor> color = ((EggLayingWoolyPigEntity) firstParent).getColor();
		Optional<InkColor> color2 = ((EggLayingWoolyPigEntity) secondParent).getColor();
		
		if(color.isEmpty() && color2.isEmpty()) {
			return Optional.empty();
		}
		if(color.isEmpty()) {
			return color2;
		}
		if(color2.isEmpty()) {
			return color;
		}
		return Optional.of(InkColorMixes.getRandomMixedColor(color.get(), color2.get(), this.level().getRandom()));
	}
	
	public static class EatGrassOrColoredGrassGoal extends Goal {
		
		private final EggLayingWoolyPigEntity mob;
		private final Level level;
		private int eatAnimationTick;
		
		public EatGrassOrColoredGrassGoal(EggLayingWoolyPigEntity mob) {
			super();
			this.mob = mob;
			this.level = mob.level();
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
		}
		
		public boolean canUse() {
			if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
				return false;
			} else {
				BlockPos pos = this.mob.blockPosition();
				BlockState posState = this.level.getBlockState(pos);
				if(posState.is(Blocks.SHORT_GRASS) || posState.getBlock() instanceof ColoredShortGrassBlock) {
					return true;
				}
				BlockState belowState = this.level.getBlockState(pos.below());
				return belowState.is(Blocks.GRASS_BLOCK) || belowState.getBlock() instanceof ColoredGrassBlock;
			}
		}
		
		public void start() {
			this.eatAnimationTick = this.adjustedTickDelay(EAT_ANIMATION_TICKS);
			this.level.broadcastEntityEvent(this.mob, (byte)10);
			this.mob.getNavigation().stop();
		}
		
		public void stop() {
			this.eatAnimationTick = 0;
		}
		
		public boolean canContinueToUse() {
			return this.eatAnimationTick > 0;
		}
		
		public int getEatAnimationTick() {
			return this.eatAnimationTick;
		}
		
		public void tick() {
			this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
			if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
				BlockPos pos = this.mob.blockPosition();
				BlockState posState = this.level.getBlockState(pos);
				
				if (posState.getBlock() instanceof ColoredShortGrassBlock coloredShortGrassBlock) {
					if (EventHooks.canEntityGrief(this.level, this.mob)) {
						this.level.destroyBlock(pos, false);
						this.mob.setColor(Optional.of(coloredShortGrassBlock.getColor()));
						this.mob.setEatenColoredGrass(true);
						this.mob.heal(1.0F);
					}
					this.mob.ate();
					return;
				}
				
				if (posState.is(Blocks.SHORT_GRASS)) {
					if (EventHooks.canEntityGrief(this.level, this.mob)) {
						this.level.destroyBlock(pos, false);
					}
					this.mob.ate();
					return;
				}
				
				BlockPos belowPos = pos.below();
				BlockState belowState = this.level.getBlockState(belowPos);
				
				if (belowState.getBlock() instanceof ColoredGrassBlock coloredGrassBlock) {
					if (EventHooks.canEntityGrief(this.level, this.mob)) {
						this.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, belowPos, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
						this.level.setBlock(belowPos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
						this.mob.setColor(Optional.of(coloredGrassBlock.getColor()));
						this.mob.setEatenColoredGrass(true);
						this.mob.heal(1.0F);
					}
					this.mob.ate();
					return;
				}
				
				if (belowState.is(Blocks.GRASS_BLOCK)) {
					if (EventHooks.canEntityGrief(this.level, this.mob)) {
						this.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, belowPos, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
						this.level.setBlock(belowPos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
					}
					this.mob.ate();
				}
			}
		}
		
	}
	
}
