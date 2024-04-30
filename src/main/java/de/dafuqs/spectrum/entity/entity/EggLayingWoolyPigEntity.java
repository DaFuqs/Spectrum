package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class EggLayingWoolyPigEntity extends AnimalEntity implements Shearable {
	
	private static final Ingredient FOOD = Ingredient.ofItems(SpectrumBlocks.AMARANTH_BUSHEL);
	
	private static final int MAX_GRASS_TIMER = 40;
	private static final TrackedData<Byte> COLOR_AND_SHEARED = DataTracker.registerData(EggLayingWoolyPigEntity.class, TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Boolean> HATLESS = DataTracker.registerData(EggLayingWoolyPigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final Map<DyeColor, float[]> COLORS = new EnumMap<>(ColorHelper.VANILLA_DYE_COLORS.stream().collect(Collectors.toMap(Function.identity(), EggLayingWoolyPigEntity::getDyedColor)));
	private static final Identifier SHEARING_LOOT_TABLE_ID = SpectrumCommon.locate("entities/egg_laying_wooly_pig_shearing");
	
	private int eatGrassTimer;
	private EatGrassGoal eatGrassGoal;
	public int eggLayTime;
	
	public EggLayingWoolyPigEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
		this.eggLayTime = this.random.nextInt(12000) + 12000;
	}
	
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		World world = this.getWorld();
		ItemStack handStack = player.getStackInHand(hand);

		if (handStack.getItem() instanceof DyeItem dyeItem && isAlive() && getColor() != dyeItem.getColor()) {
			world.playSoundFromEntity(player, this, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			if (!world.isClient) {
				setColor(dyeItem.getColor());
				handStack.decrement(1);
			}
			return ActionResult.success(world.isClient);
		} else if (handStack.isOf(Items.BUCKET) && !this.isBaby()) {
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
			ItemStack itemStack2 = ItemUsage.exchangeStack(handStack, player, Items.MILK_BUCKET.getDefaultStack());
			player.setStackInHand(hand, itemStack2);
			return ActionResult.success(world.isClient());
		} else if (handStack.isIn(ConventionalItemTags.SHEARS)) {
			if (!world.isClient() && this.isShearable()) {
				this.sheared(SoundCategory.PLAYERS);
				this.emitGameEvent(GameEvent.SHEAR, player);
				handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.CONSUME;
			}
		} else {
			return super.interactMob(player, hand);
		}
	}
	
	public static DefaultAttributeContainer.Builder createEggLayingWoolyPigAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Override
	protected void initGoals() {
		this.eatGrassGoal = new EatGrassGoal(this);
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(3, new TemptGoal(this, 1.1D, FOOD, false));
		this.goalSelector.add(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(5, this.eatGrassGoal);
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
	}
	
	@Override
	public void handleStatus(byte status) {
		if (status == 10) {
			this.eatGrassTimer = MAX_GRASS_TIMER;
		} else {
			super.handleStatus(status);
		}
	}
	
	@Override
	protected void mobTick() {
		this.eatGrassTimer = this.eatGrassGoal.getTimer();
		super.mobTick();
	}
	
	@Override
	public void tickMovement() {
		if (this.getWorld().isClient()) {
			this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
		}
		
		if (!this.getWorld().isClient() && this.isAlive() && !this.isBaby() && --this.eggLayTime <= 0) {
			this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Items.EGG);
			this.eggLayTime = this.random.nextInt(6000) + 6000;
		}
		
		super.tickMovement();
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(COLOR_AND_SHEARED, (byte) 0);
		this.dataTracker.startTracking(HATLESS, false);
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		EggLayingWoolyPigEntity other = (EggLayingWoolyPigEntity) entity;
		EggLayingWoolyPigEntity child = SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG.create(world);
		if (child != null) {
			child.setColor(this.getChildColor(this, other));
			if (world.random.nextInt(50) == 0) {
				child.setHatless(true);
			}
		}
		return child;
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("Sheared", this.isSheared());
		nbt.putBoolean("Hatless", this.isHatless());
		nbt.putByte("Color", (byte) this.getColor().getId());
		nbt.putInt("EggLayTime", this.eggLayTime);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setSheared(nbt.getBoolean("Sheared"));
		this.setHatless(nbt.getBoolean("Hatless"));
		this.setColor(DyeColor.byId(nbt.getByte("Color")));
		if (nbt.contains("EggLayTime")) {
			this.eggLayTime = nbt.getInt("EggLayTime");
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_PIG_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
	}
	
	@Override
	public void onEatingGrass() {
		this.setSheared(false);
		if (this.isBaby()) {
			this.growUp(60);
		}
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.95F * dimensions.height;
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
			return 0.62831855F + 0.21991149F * MathHelper.sin(f * 28.7F);
		} else {
			return this.eatGrassTimer > 0 ? 0.62831855F : this.getPitch() * 0.017453292F;
		}
	}
	
	@Override
	public void sheared(SoundCategory shearedSoundCategory) {
		var world = this.getWorld();
		world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
		this.setSheared(true);
		
		for (ItemStack droppedStack : getShearedStacks((ServerWorld) world)) {
			ItemEntity itemEntity = this.dropStack(droppedStack, 1);
			if (itemEntity != null) {
				itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
			}
		}
	}
	
	public List<ItemStack> getShearedStacks(ServerWorld world) {
		var builder = (new LootContextParameterSet.Builder(world))
				.add(LootContextParameters.THIS_ENTITY, this)
				.add(LootContextParameters.ORIGIN, this.getPos());
		
		LootTable lootTable = world.getServer().getLootManager().getLootTable(SHEARING_LOOT_TABLE_ID);
		return lootTable.generateLoot(builder.build(LootContextTypes.GIFT));
	}
	
	@Override
	public boolean isShearable() {
		return this.isAlive() && !this.isSheared() && !this.isBaby();
	}
	
	public boolean isSheared() {
		return (this.dataTracker.get(COLOR_AND_SHEARED) & 16) != 0;
	}
	
	public void setSheared(boolean sheared) {
		byte color = this.dataTracker.get(COLOR_AND_SHEARED);
		if (sheared) {
			this.dataTracker.set(COLOR_AND_SHEARED, (byte) (color | 16));
		} else {
			this.dataTracker.set(COLOR_AND_SHEARED, (byte) (color & -17));
		}
	}
	
	public boolean isHatless() {
		return this.dataTracker.get(HATLESS);
	}
	
	public void setHatless(boolean hatless) {
		this.dataTracker.set(HATLESS, hatless);
	}
	
	// COLORING
	public static float[] getRgbColor(DyeColor dyeColor) {
		return COLORS.get(dyeColor);
	}
	
	private static float[] getDyedColor(DyeColor color) {
		if (color == DyeColor.WHITE) {
			return new float[]{1.0F, 1.0F, 1.0F};
		} else {
			float[] fs = color.getColorComponents();
			return new float[]{fs[0], fs[1], fs[2]};
		}
	}
	
	public DyeColor getColor() {
		return DyeColor.byId(this.dataTracker.get(COLOR_AND_SHEARED) & 15);
	}
	
	public void setColor(DyeColor color) {
		byte b = this.dataTracker.get(COLOR_AND_SHEARED);
		this.dataTracker.set(COLOR_AND_SHEARED, (byte) (b & 240 | color.getId() & 15));
	}
	
	private DyeColor getChildColor(AnimalEntity firstParent, AnimalEntity secondParent) {
		World world = this.getWorld();
		DyeColor dyeColor = ((EggLayingWoolyPigEntity) firstParent).getColor();
		DyeColor dyeColor2 = ((EggLayingWoolyPigEntity) secondParent).getColor();
		CraftingInventory craftingInventory = createDyeMixingCraftingInventory(dyeColor, dyeColor2);
		Optional<Item> optionalItem = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingInventory, world).map((recipe) -> recipe.craft(craftingInventory, world.getRegistryManager())).map(ItemStack::getItem);
		
		if (optionalItem.isPresent() && optionalItem.get() instanceof DyeItem dyeItem) {
			return dyeItem.getColor();
		}
		return world.random.nextBoolean() ? dyeColor : dyeColor2;
	}
	
	private static CraftingInventory createDyeMixingCraftingInventory(DyeColor firstColor, DyeColor secondColor) {
		CraftingInventory craftingInventory = new CraftingInventory(new ScreenHandler(null, -1) {
			@Override
			public ItemStack quickMove(PlayerEntity player, int index) {
				return ItemStack.EMPTY;
			}
			
			@Override
			public boolean canUse(PlayerEntity player) {
				return false;
			}
		}, 2, 1);
		craftingInventory.setStack(0, new ItemStack(DyeItem.byColor(firstColor)));
		craftingInventory.setStack(1, new ItemStack(DyeItem.byColor(secondColor)));
		return craftingInventory;
	}
	
}
