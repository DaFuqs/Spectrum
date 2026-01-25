package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
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
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class EggLayingWoolyPigEntity extends Animal implements Shearable {
	
	private static final Ingredient FOOD = Ingredient.of(SpectrumBlocks.AMARANTH_BUSHEL);
	
	private static final int MAX_GRASS_TIMER = 40;
	private static final EntityDataAccessor<Byte> COLOR_AND_SHEARED = SynchedEntityData.defineId(EggLayingWoolyPigEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> HATLESS = SynchedEntityData.defineId(EggLayingWoolyPigEntity.class, EntityDataSerializers.BOOLEAN);
	private static final Map<DyeColor, Integer> COLORS = new EnumMap<>(SpectrumColorHelper.VANILLA_DYE_COLORS.stream().collect(Collectors.toMap(Function.identity(), EggLayingWoolyPigEntity::getDyedColor)));
	
	private int eatGrassTimer;
	private EatBlockGoal eatGrassGoal;
	public int eggLayTime;
	
	public EggLayingWoolyPigEntity(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
		this.eggLayTime = this.random.nextInt(12000) + 12000;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		Level world = this.level();
		ItemStack handStack = player.getItemInHand(hand);
		
		if (handStack.getItem() instanceof DyeItem dyeItem && isAlive() && getColor() != dyeItem.getDyeColor()) {
			world.playSound(player, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				setColor(dyeItem.getDyeColor());
				handStack.shrink(1);
			}
			return InteractionResult.sidedSuccess(world.isClientSide);
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
		this.eatGrassGoal = new EatBlockGoal(this);
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
			this.eatGrassTimer = MAX_GRASS_TIMER;
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
			this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			this.spawnAtLocation(Items.EGG);
			this.eggLayTime = this.random.nextInt(6000) + 6000;
		}
		
		super.aiStep();
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(COLOR_AND_SHEARED, (byte) 0);
		builder.define(HATLESS, false);
	}
	
	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		EggLayingWoolyPigEntity other = (EggLayingWoolyPigEntity) entity;
		EggLayingWoolyPigEntity child = SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG.get().create(world);
		if (child != null) {
			child.setColor(this.getChildColor(this, other));
			if (world.random.nextInt(50) == 0) {
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
		nbt.putByte("Color", (byte) this.getColor().getId());
		nbt.putInt("EggLayTime", this.eggLayTime);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.setSheared(nbt.getBoolean("Sheared"));
		this.setHatless(nbt.getBoolean("Hatless"));
		this.setColor(DyeColor.byId(nbt.getByte("Color")));
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
			return this.eatGrassTimer < 4 ? ((float) this.eatGrassTimer - delta) / 4.0F : -((float) (this.eatGrassTimer - MAX_GRASS_TIMER) - delta) / 4.0F;
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
				itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
			}
		}
	}
	
	public List<ItemStack> getShearedStacks(ServerLevel world) {
		var builder = (new LootParams.Builder(world))
				.withParameter(LootContextParams.THIS_ENTITY, this)
				.withParameter(LootContextParams.ORIGIN, this.position());
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(SpectrumLootTables.EGG_LAYING_WOOLY_PIG_SHEARING);
		return lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
	}
	
	@Override
	public boolean readyForShearing() {
		return this.isAlive() && !this.isSheared() && !this.isBaby();
	}
	
	public boolean isSheared() {
		return (this.entityData.get(COLOR_AND_SHEARED) & 16) != 0;
	}
	
	public void setSheared(boolean sheared) {
		byte color = this.entityData.get(COLOR_AND_SHEARED);
		if (sheared) {
			this.entityData.set(COLOR_AND_SHEARED, (byte) (color | 16));
		} else {
			this.entityData.set(COLOR_AND_SHEARED, (byte) (color & -17));
		}
	}
	
	public boolean isHatless() {
		return this.entityData.get(HATLESS);
	}
	
	public void setHatless(boolean hatless) {
		this.entityData.set(HATLESS, hatless);
	}
	
	// COLORING
	public static int getRgbColor(DyeColor dyeColor) {
		return COLORS.get(dyeColor);
	}
	
	private static int getDyedColor(DyeColor color) {
		if (color == DyeColor.WHITE) {
			return -1644826;
		} else {
			int i = color.getTextureDiffuseColor();
			return net.minecraft.util.FastColor.ARGB32.color(255,
					Mth.floor((float) net.minecraft.util.FastColor.ARGB32.red(i) * 0.75F),
					Mth.floor((float) net.minecraft.util.FastColor.ARGB32.green(i) * 0.75F),
					Mth.floor((float) net.minecraft.util.FastColor.ARGB32.blue(i) * 0.75F));
		}
	}
	
	public DyeColor getColor() {
		return DyeColor.byId(this.entityData.get(COLOR_AND_SHEARED) & 15);
	}
	
	public void setColor(DyeColor color) {
		byte b = this.entityData.get(COLOR_AND_SHEARED);
		this.entityData.set(COLOR_AND_SHEARED, (byte) (b & 240 | color.getId() & 15));
	}
	
	private DyeColor getChildColor(Animal firstParent, Animal secondParent) {
		Level world = this.level();
		DyeColor dyeColor = ((EggLayingWoolyPigEntity) firstParent).getColor();
		DyeColor dyeColor2 = ((EggLayingWoolyPigEntity) secondParent).getColor();
		CraftingInput craftingRecipeInput = createChildColorRecipeInput(dyeColor, dyeColor2);
		Optional<Item> optionalItem = this.level().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingRecipeInput, this.level()).map((recipe) -> recipe.value().assemble(craftingRecipeInput, this.level().registryAccess())).map(ItemStack::getItem);
		
		if (optionalItem.isPresent() && optionalItem.get() instanceof DyeItem dyeItem) {
			return dyeItem.getDyeColor();
		}
		return world.random.nextBoolean() ? dyeColor : dyeColor2;
	}
	
	private static CraftingInput createChildColorRecipeInput(DyeColor firstColor, DyeColor secondColor) {
		return CraftingInput.of(2, 1, List.of(new ItemStack(DyeItem.byColor(firstColor)), new ItemStack(DyeItem.byColor(secondColor))));
	}
	
	private static TransientCraftingContainer createDyeMixingCraftingInventory(DyeColor firstColor, DyeColor secondColor) {
		TransientCraftingContainer craftingInventory = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
			@Override
			public ItemStack quickMoveStack(Player player, int index) {
				return ItemStack.EMPTY;
			}
			
			@Override
			public boolean stillValid(Player player) {
				return false;
			}
		}, 2, 1);
		craftingInventory.setItem(0, new ItemStack(DyeItem.byColor(firstColor)));
		craftingInventory.setItem(1, new ItemStack(DyeItem.byColor(secondColor)));
		return craftingInventory;
	}
	
}
