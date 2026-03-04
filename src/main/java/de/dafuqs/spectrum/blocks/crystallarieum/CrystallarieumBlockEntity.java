package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.animation.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.fluids.capability.templates.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumBlockEntity extends InWorldInteractionBlockEntity implements PlayerOwned, InkStorageBlockEntity<IndividualCappedInkStorage> {
	
	private final static FlowAnimator.Factory<CrystallarieumBlockEntity> FACTORY;
	
	protected final static int CATALYST_SLOT_ID = 0;
	protected final static int INK_STORAGE_STACK_SLOT_ID = 1;
	protected final static int INVENTORY_SIZE = 2;
	
	public static final long INK_STORAGE_SIZE = 64 * 64 * 100;
	
	protected IndividualCappedInkStorage inkStorage;
	protected boolean inkDirty;
	
	@Nullable
	protected UUID ownerUUID;
	
	@Nullable
	protected RecipeHolder<CrystallarieumRecipe> currentRecipe;
	protected CrystallarieumCatalyst currentCatalyst = CrystallarieumCatalyst.EMPTY;
	protected FluidTank fluidStorage = new FluidTank(1000);
	
	// for performance reasons, the crystallarieum only processes recipe logic every 20 ticks
	public static final int SECOND = 20;
	protected TickLooper tickLooper = new TickLooper(SECOND);
	
	protected int currentGrowthStageTicks;
	protected boolean canWork;
	float rotation = 0F;
	
	protected FlowAnimator animator;
	@NotNull
	protected FlowData<Float> _alpha = FlowData.NULL(), _speed = FlowData.NULL(), _bounce = FlowData.NULL();
	
	public CrystallarieumBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.CRYSTALLARIEUM.get(), pos, state, INVENTORY_SIZE);
		this.inkStorage = new IndividualCappedInkStorage(INK_STORAGE_SIZE);
		this.canWork = true;
	}
	
	@SuppressWarnings("unused")
	public static void clientTick(@NotNull Level world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if (crystallarieum.animator == null) {
			crystallarieum.animator = FACTORY.create(FlowStates.INIT, crystallarieum);
		} else {
			crystallarieum.updateAnimator();
		}
		
		if (crystallarieum.canWork && crystallarieum.currentRecipe != null) {
			ParticleOptions particleEffect = ColoredSparkleRisingParticleEffect.of(crystallarieum.currentRecipe.value().getInkColor().getColorInt());
			
			int amount = 1 + crystallarieum.currentRecipe.value().getInkPerSecond();
			if (Support.getIntFromDecimalWithChance(amount / 80.0, world.random) > 0) {
				double randomX = world.getRandom().nextDouble() * 0.8;
				double randomZ = world.getRandom().nextDouble() * 0.8;
				world.addAlwaysVisibleParticle(particleEffect, blockPos.getX() + 0.1 + randomX, blockPos.getY() + 1, blockPos.getZ() + 0.1 + randomZ, 0.0D, 0.03D, 0.0D);
			}
		}
	}
	
	public void updateAnimator() {
		if (currentRecipe == null) {
			animator.swapState(FlowStates.INACTIVE);
		} else {
			if (currentRecipe.value().getFluidIngredient().test(fluidStorage.getFluid()) && inkStorage.getEnergy(currentRecipe.value().getInkColor()) > 0) {
				animator.swapState(FlowStates.ACTIVE);
			} else {
				animator.swapState(FlowStates.IDLE);
			}
		}
		
		animator.tick();
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(@NotNull Level world, BlockPos blockPos, BlockState blockState, CrystallarieumBlockEntity crystallarieum) {
		if (crystallarieum.canWork) {
			transferInk(crystallarieum);
			
			var recipe = crystallarieum.currentRecipe;
			if (recipe != null) {
				crystallarieum.tickLooper.tick();
				if (crystallarieum.tickLooper.reachedCap()) {
					tickRecipe(world, blockPos, crystallarieum, recipe);
					crystallarieum.tickLooper.reset();
				}
			}
		}
	}
	
	/**
	 * Progress the recipe
	 * gets called 1/second
	 */
	private static void tickRecipe(@NotNull Level world, BlockPos blockPos, CrystallarieumBlockEntity crystallarieum, @NotNull RecipeHolder<CrystallarieumRecipe> recipe) {
		if (crystallarieum.currentCatalyst == CrystallarieumCatalyst.EMPTY && !recipe.value().growsWithoutCatalyst()) {
			return;
		}
		
		if (crystallarieum.canWork && (!recipe.value().getFluidIngredient().test(crystallarieum.fluidStorage.getFluid()) || crystallarieum.inkStorage.getEnergy(recipe.value().getInkColor()) == 0)) {
			crystallarieum.canWork = false;
			return;
		}
		
		// advance growing
		float consumedInkFloat = (recipe.value().getInkPerSecond() * crystallarieum.currentCatalyst.growthAccelerationMod() * crystallarieum.currentCatalyst.inkConsumptionMod());
		int consumedInt = Support.getIntFromDecimalWithChance(consumedInkFloat, world.random);
		if (crystallarieum.inkStorage.drainEnergy(recipe.value().getInkColor(), consumedInt) < consumedInt) {
			crystallarieum.canWork = false;
			crystallarieum.setInkDirty();
			crystallarieum.updateInClientWorld();
			return;
		}
		
		crystallarieum.setInkDirty();
		crystallarieum.currentGrowthStageTicks += (int) (SECOND * crystallarieum.currentCatalyst.growthAccelerationMod());
		
		// check if a catalyst should get used up
		if (world.random.nextFloat() < crystallarieum.currentCatalyst.consumeChancePerSecond()) {
			ItemStack catalystStack = crystallarieum.getItem(CATALYST_SLOT_ID);
			catalystStack.shrink(1);
			crystallarieum.updateInClientWorld();
			if (catalystStack.isEmpty()) {
				crystallarieum.currentCatalyst = CrystallarieumCatalyst.EMPTY;
				if (!recipe.value().growsWithoutCatalyst()) {
					crystallarieum.canWork = false;
				}
			}
		}
		
		// advanced enough? grow!
		if (crystallarieum.currentGrowthStageTicks >= recipe.value().getSecondsPerGrowthStage() * SECOND) {
			BlockPos topPos = blockPos.above();
			BlockState topState = world.getBlockState(topPos);
			Optional<BlockState> nextState = recipe.value().getNextState(recipe, topState);
			if (nextState.isPresent()) {
				world.setBlockAndUpdate(topPos, nextState.get());
				ServerPlayer owner = (ServerPlayer) crystallarieum.getOwnerIfOnline();
				if (owner != null) {
					SpectrumAdvancementCriteria.CRYSTALLARIEUM_GROWING.trigger(owner, (ServerLevel) world, topPos, crystallarieum.getItem(CATALYST_SLOT_ID));
				}
			} else {
				crystallarieum.canWork = false;
			}
			crystallarieum.currentGrowthStageTicks = 0;
		}
	}
	
	private static void transferInk(CrystallarieumBlockEntity crystallarieum) {
		ItemStack inkStorageStack = crystallarieum.getItem(INK_STORAGE_STACK_SLOT_ID);
		if (inkStorageStack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			InkStorage itemInkStorage = inkStorageItem.getEnergyStorage(inkStorageStack);
			long transferredAmount = InkStorage.transferInk(itemInkStorage, crystallarieum.inkStorage);
			if (transferredAmount > 0) {
				inkStorageItem.setEnergyStorage(inkStorageStack, itemInkStorage);
			}
		}
	}
	
	@Override
	public void inventoryChanged() {
		if (this.currentRecipe == null || level == null) {
			this.currentCatalyst = CrystallarieumCatalyst.EMPTY;
			this.canWork = false;
		} else {
			this.currentCatalyst = this.currentRecipe.value().getCatalyst(getItem(CATALYST_SLOT_ID));
			BlockState topState = this.level.getBlockState(this.worldPosition.above());
			this.canWork = this.currentRecipe.value().getNextState(this.currentRecipe, topState).isPresent()
					&& (this.currentRecipe.value().growsWithoutCatalyst() || this.currentCatalyst != CrystallarieumCatalyst.EMPTY);
		}
		super.inventoryChanged();
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		CodecHelper.fromNbt(InkStorageComponent.CODEC, nbt.get("InkStorage")).ifPresent(storage ->
				this.inkStorage = new IndividualCappedInkStorage(storage.maxPerColor(), storage.storedEnergy()));
		if (nbt.contains("Looper", Tag.TAG_COMPOUND)) {
			this.tickLooper = TickLooper.readNbt(nbt.getCompound("Looper"));
		}
		
		this.fluidStorage.readFromNBT(registryLookup, nbt);
		this.canWork = nbt.getBoolean("CanWork");
		this.ownerUUID = PlayerOwnedWithName.readOwnerUUID(nbt);
		this.currentCatalyst = CrystallarieumCatalyst.EMPTY;
		this.currentRecipe = MultiblockCrafter.getRecipeHolderFromNbt(level, nbt, CrystallarieumRecipe.class);
		this.currentGrowthStageTicks = nbt.getInt("CurrentGrowthStageDuration");
		if (this.currentRecipe != null) {
			this.currentCatalyst = this.currentRecipe.value().getCatalyst(getItem(CATALYST_SLOT_ID));
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		
		CodecHelper.writeNbt(nbt, "InkStorage", InkStorageComponent.CODEC, new InkStorageComponent(this.inkStorage));
		nbt.put("Looper", this.tickLooper.toNbt());
		
		this.fluidStorage.writeToNBT(registryLookup, nbt);
		nbt.putBoolean("CanWork", this.canWork);
		nbt.putInt("CurrentGrowthStageDuration", this.currentGrowthStageTicks);
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.id().toString());
		}
	}
	
	@Override
	public @Nullable UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
	/**
	 * Searches recipes for a valid one using itemStack and plants the first block of that recipe on top
	 *
	 * @param itemStack stack that is tried to plant on top, if a valid recipe
	 */
	public void acceptStack(ItemStack itemStack, boolean creative, @Nullable UUID player) {
		boolean changed = false;
		
		if (level == null) return;
		if (itemStack.getItem() instanceof InkStorageItem<?> inkStorageItem && inkStorageItem.getDrainability().canDrain(false)) {
			ItemStack currentInkStorageStack = getItem(INK_STORAGE_STACK_SLOT_ID);
			if (currentInkStorageStack.isEmpty()) {
				setItem(INK_STORAGE_STACK_SLOT_ID, itemStack.copy());
				if (!creative) {
					itemStack.setCount(0);
				}
				changed = true;
			}
		} else if (level.getBlockState(worldPosition.above()).isAir()) {
			var recipe = level.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.CRYSTALLARIEUM, new SingleRecipeInput(itemStack), level);
			if (recipe.isPresent()) {
				if (!creative) {
					itemStack.shrink(1);
				}
				BlockState placedState = recipe.get().value().getGrowthStages().getFirst();
				level.setBlockAndUpdate(worldPosition.above(), placedState);
				onTopBlockChange(placedState, recipe.get());
				changed = true;
			}
		} else if (this.currentRecipe != null) {
			ItemStack currentCatalystStack = getItem(CATALYST_SLOT_ID);
			if (currentCatalystStack.isEmpty()) {
				CrystallarieumCatalyst catalyst = this.currentRecipe.value().getCatalyst(itemStack);
				if (catalyst != CrystallarieumCatalyst.EMPTY) {
					setItem(CATALYST_SLOT_ID, itemStack.copy());
					if (!creative) {
						itemStack.setCount(0);
					}
					this.currentCatalyst = catalyst;
					changed = true;
				}
			} else if (ItemStack.isSameItemSameComponents(currentCatalystStack, itemStack)) {
				InventoryHelper.combineStacks(currentCatalystStack, itemStack);
				changed = true;
			}
		}
		
		if (changed) {
			level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.6F);
			if (player != null) {
				this.ownerUUID = player;
			}
			inventoryChanged();
		}
	}
	
	public FluidTank getFluidStorage() {
		return fluidStorage;
	}
	
	/**
	 * Triggered when the block on top of the crystallarieum has changed
	 * Sets the new recipe matching that block state
	 *
	 * @param newState the new block state on top
	 * @param recipe   optionally the matching CrystallarieumRecipe. If null is passed it will be calculated
	 */
	public void onTopBlockChange(BlockState newState, @Nullable RecipeHolder<CrystallarieumRecipe> recipe) {
		if (level == null) return;
		if (newState.isAir()) { // fast fail
			this.currentRecipe = null;
			this.canWork = false;
			setChanged();
			updateInClientWorld();
		} else {
			this.currentRecipe = recipe == null ? CrystallarieumRecipe.getRecipeForState(level, newState) : recipe;
			if (this.currentRecipe != null) {
				ItemStack catalystStack = getItem(CATALYST_SLOT_ID);
				if (!catalystStack.isEmpty()) {
					this.currentCatalyst = this.currentRecipe.value().getCatalyst(catalystStack);
					if (this.currentCatalyst == CrystallarieumCatalyst.EMPTY) {
						ItemEntity itemEntity = new ItemEntity(level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 0.5, catalystStack);
						this.setItem(CATALYST_SLOT_ID, ItemStack.EMPTY);
						level.addFreshEntity(itemEntity);
					}
				}
			}
			
			inventoryChanged();
		}
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage() {
		return this.inkStorage;
	}
	
	@Override
	public void setInkDirty() {
		this.inkDirty = true;
	}
	
	@Override
	public boolean getInkDirty() {
		return this.inkDirty;
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == INK_STORAGE_STACK_SLOT_ID) {
			return stack.getItem() instanceof InkStorageItem;
		} else if (this.currentRecipe != null) {
			return this.currentRecipe.value().getCatalyst(stack) != CrystallarieumCatalyst.EMPTY;
		}
		return false;
	}
	
	static {
		var builder = new FlowAnimator.Builder<>(CrystallarieumBlockEntity.class);
		
		builder.stateInfo(FlowStates.INACTIVE, 30);
		builder.stateInfo(FlowStates.ACTIVE, 15);
		builder.stateInfo(FlowStates.IDLE, 13);
		
		builder.handle("alpha", FlowHandlers.FLOAT)
				.initial(0.1F)
				.forStates(0.1F, FlowStates.INACTIVE)
				.forStates(0.4F, FlowStates.IDLE)
				.forStates(0.8F, FlowStates.ACTIVE)
				.push();
		
		builder.handle("speed", FlowHandlers.FLOAT)
				.initial(0F)
				.forStates(-0.5F, FlowStates.INACTIVE)
				.forStates(1F, FlowStates.IDLE)
				.forStates(2.5F, FlowStates.ACTIVE)
				.push();
		
		builder.handle("bounce", FlowHandlers.FLOAT)
				.initial(0F)
				.forStates(2F, FlowStates.INACTIVE)
				.forStates(1F, FlowStates.IDLE)
				.forStates(4F, FlowStates.ACTIVE)
				.push();
		
		FACTORY = builder.build();
	}
}
