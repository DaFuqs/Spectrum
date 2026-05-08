package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.animation.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpiritInstillerBlockEntity extends InWorldInteractionBlockEntity implements MultiblockCrafter {
	
	private static final FlowAnimator.Factory<SpiritInstillerBlockEntity> FACTORY;
	private static final KeyFrame<Float> platformPos = (tickDelta, time) -> (float) (Math.sin((time + tickDelta + 15) / 23) + 6F) * 2F;
	protected static final int INVENTORY_SIZE = 3; // 0: instiller stack; 1-2: item bowl stacks
	public static final List<Vec3i> itemBowlOffsetsHorizontal = new ArrayList<>() {{
		add(new Vec3i(0, 0, 2));
		add(new Vec3i(0, 0, -2));
	}};
	
	public static final List<Vec3i> itemBowlOffsetsVertical = new ArrayList<>() {{
		add(new Vec3i(2, 0, 0));
		add(new Vec3i(-2, 0, 0));
	}};
	
	private boolean inventoryChanged;
	private UUID ownerUUID;
	private UpgradeHolder upgrades;
	private Rotation multiblockRotation = Rotation.NONE;
	private RecipeHolder<SpiritInstillerRecipe> currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	private boolean validStructure;
	
	protected FlowAnimator animator;
	protected FlowData<Float> _platformY = FlowData.NULL(), _haloY = FlowData.NULL(),
			_platformSpin = FlowData.NULL(), _haloSpin = FlowData.NULL(),
			_haloAlpha = FlowData.NULL(), _blossomAlpha = FlowData.NULL();
	protected float platform, geode, calcite, innergeode;
	
	public SpiritInstillerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.SPIRIT_INSTILLER.get(), pos, state, INVENTORY_SIZE);
	}
	
	@Override
	public List<Vec3i> getUpgradePosOffsets() {
		return SpiritInstillerBlock.UPGRADE_BLOCK_OFFSETS;
	}
	
	public static void clientTick(Level world, BlockPos blockPos, BlockState blockState, @NotNull SpiritInstillerBlockEntity instiller) {
		if (instiller.animator == null) {
			instiller.animator = FACTORY.create(FlowStates.INIT, instiller);
		} else {
			instiller.updateAnimator();
		}
		
		if (instiller.currentRecipe != null && world.getGameTime() % 43 == 0) {
			instiller.doChimeParticles(world);
		}
	}
	
	private void updateAnimator() {
		animator.tick();
		
		if (!validStructure) {
			animator.swapState(FlowStates.MB_INVALID);
			return;
		}
		
		if (getItem(0).isEmpty()) {
			animator.swapState(FlowStates.INACTIVE);
		} else if (currentRecipe != null) {
			animator.swapState(FlowStates.ACTIVE);
		} else {
			animator.swapState(FlowStates.IDLE);
		}
	}
	
	public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		if (spiritInstillerBlockEntity.upgrades == null) {
			spiritInstillerBlockEntity.calculateUpgrades();
		}
		
		if (spiritInstillerBlockEntity.inventoryChanged) {
			var previousRecipe = spiritInstillerBlockEntity.currentRecipe;
			calculateCurrentRecipe(world, spiritInstillerBlockEntity);
			
			if (spiritInstillerBlockEntity.currentRecipe != previousRecipe) {
				spiritInstillerBlockEntity.craftingTime = 0;
				if (spiritInstillerBlockEntity.currentRecipe == null) {
					PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) world, spiritInstillerBlockEntity.worldPosition);
				} else {
					spiritInstillerBlockEntity.craftingTimeTotal = (int) Math.ceil(spiritInstillerBlockEntity.currentRecipe.value().getCraftingTime() / spiritInstillerBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
				}
				spiritInstillerBlockEntity.updateInClientWorld();
			}
			spiritInstillerBlockEntity.inventoryChanged = false;
		}
		
		if (spiritInstillerBlockEntity.currentRecipe == null) {
			return;
		}
		
		if (spiritInstillerBlockEntity.craftingTime % 60 == 0) {
			if (!checkRecipeRequirements(world, blockPos, spiritInstillerBlockEntity)) {
				spiritInstillerBlockEntity.craftingTime = 0;
				spiritInstillerBlockEntity.setChanged();
				PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) world, spiritInstillerBlockEntity.worldPosition);
				return;
			}
		}
		
		if (spiritInstillerBlockEntity.currentRecipe != null) {
			spiritInstillerBlockEntity.craftingTime++;
			
			if (spiritInstillerBlockEntity.craftingTime == 1) {
				PlayBlockBoundSoundInstancePayload.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING, (ServerLevel) world, spiritInstillerBlockEntity.worldPosition, Integer.MAX_VALUE);
			} else if (spiritInstillerBlockEntity.craftingTime == spiritInstillerBlockEntity.craftingTimeTotal * 0.01
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.25)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.5)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.75)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.83)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.90)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.95)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.98)
					|| spiritInstillerBlockEntity.craftingTime == Math.floor(spiritInstillerBlockEntity.craftingTimeTotal * 0.99)) {
				spiritInstillerBlockEntity.doItemBowlOrbs(world);
			} else if (spiritInstillerBlockEntity.craftingTime == spiritInstillerBlockEntity.craftingTimeTotal) {
				craftSpiritInstillerRecipe(world, spiritInstillerBlockEntity, spiritInstillerBlockEntity.currentRecipe);
			}
			
			spiritInstillerBlockEntity.setChanged();
		}
	}
	
	private static void calculateCurrentRecipe(@NotNull Level world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		// test the cached recipe => faster
		if (spiritInstillerBlockEntity.currentRecipe != null && !spiritInstillerBlockEntity.isEmpty()) {
			if (spiritInstillerBlockEntity.currentRecipe.value().matches(spiritInstillerBlockEntity.getRecipeInput(), world)) {
				return;
			}
		}
		
		// cached recipe did not match => calculate new
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.currentRecipe = null;
		
		ItemStack instillerStack = spiritInstillerBlockEntity.getItem(SpiritInstillerRecipe.CENTER_INGREDIENT);
		if (!instillerStack.isEmpty()) {
			spiritInstillerBlockEntity.setItem(SpiritInstillerRecipe.CENTER_INGREDIENT, instillerStack);
			
			// left item bowl
			if (world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				spiritInstillerBlockEntity.setItem(SpiritInstillerRecipe.FIRST_INGREDIENT, itemBowlBlockEntity.getItem(0));
			} else {
				spiritInstillerBlockEntity.setItem(SpiritInstillerRecipe.FIRST_INGREDIENT, ItemStack.EMPTY);
			}
			// right item bowl
			if (world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				spiritInstillerBlockEntity.setItem(SpiritInstillerRecipe.SECOND_INGREDIENT, itemBowlBlockEntity.getItem(0));
			} else {
				spiritInstillerBlockEntity.setItem(SpiritInstillerRecipe.SECOND_INGREDIENT, ItemStack.EMPTY);
			}
			
			RecipeHolder<SpiritInstillerRecipe> spiritInstillerRecipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.SPIRIT_INSTILLING, spiritInstillerBlockEntity.getRecipeInput(), world).orElse(null);
			if (spiritInstillerRecipe != null) {
				spiritInstillerBlockEntity.currentRecipe = spiritInstillerRecipe;
				spiritInstillerBlockEntity.craftingTimeTotal = (int) Math.ceil(spiritInstillerRecipe.value().getCraftingTime() / spiritInstillerBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
			}
		}
		
		spiritInstillerBlockEntity.updateInClientWorld();
	}
	
	public static BlockPos getItemBowlPos(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, boolean right) {
		BlockPos blockPos = spiritInstillerBlockEntity.worldPosition;
		switch (spiritInstillerBlockEntity.multiblockRotation) {
			case NONE, CLOCKWISE_180 -> {
				if (right) {
					return blockPos.above().east(2);
				} else {
					return blockPos.above().west(2);
				}
			}
			default -> {
				if (right) {
					return blockPos.above().north(2);
				} else {
					return blockPos.above().south(2);
				}
			}
		}
	}
	
	private static boolean checkRecipeRequirements(Level world, BlockPos blockPos, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		Player lastInteractedPlayer = PlayerOwned.getPlayerIfOnline(world, spiritInstillerBlockEntity.ownerUUID);
		if (lastInteractedPlayer == null) {
			return false;
		}
		
		boolean playerCanCraft = true;
		if (spiritInstillerBlockEntity.currentRecipe != null) {
			playerCanCraft = spiritInstillerBlockEntity.currentRecipe.value().canPlayerCraft(lastInteractedPlayer);
		}
		
		boolean structureComplete = SpiritInstillerBlock.verifyStructure(world, blockPos, null, spiritInstillerBlockEntity);
		boolean canCraft = true;
		if (!playerCanCraft || !structureComplete) {
			if (!structureComplete) {
				world.playSound(null, spiritInstillerBlockEntity.getBlockPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
			}
			
			canCraft = false;
		}
		
		if (lastInteractedPlayer instanceof ServerPlayer serverPlayerEntity) {
			testAndUnlockRecipeAdvancements(serverPlayerEntity, spiritInstillerBlockEntity.currentRecipe, canCraft);
		}
		
		return canCraft & spiritInstillerBlockEntity.currentRecipe.value().canPlayerCraft(lastInteractedPlayer) && spiritInstillerBlockEntity.currentRecipe.value().canCraftWithStacks(spiritInstillerBlockEntity.getRecipeInput());
	}
	
	public static void testAndUnlockRecipeAdvancements(ServerPlayer player, RecipeHolder<SpiritInstillerRecipe> spiritInstillerRecipe, boolean canActuallyCraft) {
		// boss memory advancements
		boolean isBossMenory = spiritInstillerRecipe.value().getGroup() != null && spiritInstillerRecipe.value().getGroup().equals("boss_memories");
		if (isBossMenory) {
			if (canActuallyCraft) {
				Support.grantAdvancementCriterion(player, "midgame/craft_blacklisted_memory_success", "succeed_crafting_boss_memory");
			} else {
				Support.grantAdvancementCriterion(player, "midgame/craft_blacklisted_memory_fail", "fail_to_craft_boss_memory");
			}
		}
		// jade vine crossbreeding advancement
		if (spiritInstillerRecipe.id().equals(SpectrumRecipes.JADE_VINE_CROSSBREEDING)) {
			Support.grantAdvancementCriterion(player, "lategame/create_jade_vine", "crossbred_jade_vine_bulb");
		}
	}
	
	public static void craftSpiritInstillerRecipe(Level world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, @NotNull RecipeHolder<SpiritInstillerRecipe> spiritInstillerRecipe) {
		ItemStack resultStack = spiritInstillerRecipe.value().assemble(spiritInstillerBlockEntity.getRecipeInput(), world.registryAccess());
		decrementItemsInInstillerAndBowls(spiritInstillerBlockEntity);
		if (!resultStack.isEmpty()) {
			if (spiritInstillerBlockEntity.getItem(0).isEmpty()) {
				// keep it on the Instiller
				spiritInstillerBlockEntity.setItem(0, resultStack);
			} else {
				// spawn the result stack in world
				MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, spiritInstillerBlockEntity.worldPosition, resultStack, resultStack.getCount(), MultiblockCrafter.RECIPE_STACK_VELOCITY);
			}
		}
		
		playCraftingFinishedEffects(spiritInstillerBlockEntity);
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.inventoryChanged();
	}
	
	public static void decrementItemsInInstillerAndBowls(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		Level world = spiritInstillerBlockEntity.getLevel();
		if (world == null) return;
		var recipe = spiritInstillerBlockEntity.currentRecipe;
		
		double efficiencyModifier = 1.0;
		if (!recipe.value().areYieldAndEfficiencyUpgradesDisabled()) {
			efficiencyModifier = 1.0 / spiritInstillerBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY);
		}
		
		BlockEntity leftBowlBlockEntity = world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false));
		BlockEntity rightBowlBlockEntity = world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true));
		if (leftBowlBlockEntity instanceof ItemBowlBlockEntity leftBowl && rightBowlBlockEntity instanceof ItemBowlBlockEntity rightBowl) {
			// center ingredient
			int centerIngredientCount = recipe.value().getIngredientStacks().get(SpiritInstillerRecipe.CENTER_INGREDIENT).getCount();
			int decreasedAmountAfterEfficiencyMod = recipe.value().copyComponents() ? centerIngredientCount : Support.getIntFromDecimalWithChance(centerIngredientCount * efficiencyModifier, world.random);
			if (decreasedAmountAfterEfficiencyMod > 0) {
				spiritInstillerBlockEntity.getItem(0).shrink(decreasedAmountAfterEfficiencyMod);
			}
			
			List<IngredientStack> ingredientStacks = recipe.value().getIngredientStacks();
			
			// first side ingredient
			int amountAfterEfficiencyModFirst = Support.getIntFromDecimalWithChance(ingredientStacks.get(SpiritInstillerRecipe.FIRST_INGREDIENT).getCount() * efficiencyModifier, world.random);
			int amountAfterEfficiencyModSecond = Support.getIntFromDecimalWithChance(ingredientStacks.get(SpiritInstillerRecipe.SECOND_INGREDIENT).getCount() * efficiencyModifier, world.random);
			boolean leftIsFirstIngredient = ingredientStacks.get(SpiritInstillerRecipe.FIRST_INGREDIENT).test(leftBowl.getItem(0));
			Vec3 particlePos = new Vec3(spiritInstillerBlockEntity.worldPosition.getX() + 0.5, spiritInstillerBlockEntity.worldPosition.getY() + 1, spiritInstillerBlockEntity.worldPosition.getZ() + 0.5);
			if (leftIsFirstIngredient) {
				if (amountAfterEfficiencyModFirst > 0) {
					leftBowl.decrementBowlStack(particlePos, amountAfterEfficiencyModFirst, true);
				}
				if (amountAfterEfficiencyModSecond > 0) {
					rightBowl.decrementBowlStack(particlePos, amountAfterEfficiencyModSecond, true);
				}
			} else {
				if (amountAfterEfficiencyModFirst > 0) {
					rightBowl.decrementBowlStack(particlePos, amountAfterEfficiencyModFirst, true);
				}
				if (amountAfterEfficiencyModSecond > 0) {
					leftBowl.decrementBowlStack(particlePos, amountAfterEfficiencyModSecond, true);
				}
			}
		}
	}
	
	public static void playCraftingFinishedEffects(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		Level world = spiritInstillerBlockEntity.getLevel();
		if (world == null) return;
		world.playSound(null, spiritInstillerBlockEntity.worldPosition, SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING_FINISHED, SoundSource.BLOCKS, 1.0F, 1.0F);
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world,
				new Vec3(spiritInstillerBlockEntity.worldPosition.getX() + 0.5D, spiritInstillerBlockEntity.worldPosition.getY() + 0.5, spiritInstillerBlockEntity.worldPosition.getZ() + 0.5D),
				ColoredCraftingParticleEffect.LIGHT_BLUE, 75, new Vec3(0.5D, 0.5D, 0.5D),
				new Vec3(0.1D, -0.1D, 0.1D));
	}
	
	public void setValidStructure(boolean validStructure) {
		if (!level.isClientSide()) {
			if(validStructure != this.validStructure) {
				setChanged();
				updateInClientWorld();
			}
			this.validStructure = validStructure;
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.inventoryChanged = true;
		this.ownerUUID = PlayerOwnedWithName.readOwnerUUID(nbt);
		this.validStructure = nbt.getBoolean("validStructure");
		if (nbt.contains("MultiblockRotation")) {
			try {
				this.multiblockRotation = Rotation.valueOf(nbt.getString("MultiblockRotation").toUpperCase(Locale.ROOT));
			} catch (Exception e) {
				this.multiblockRotation = Rotation.NONE;
			}
		}
		
		if (nbt.contains("platformSpin"))
			platform = nbt.getFloat("platformSpin");
		
		this.currentRecipe = MultiblockCrafter.getRecipeHolderFromNbt(level, nbt, SpiritInstillerRecipe.class);
		
		if (nbt.contains("Upgrades", Tag.TAG_LIST)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", Tag.TAG_COMPOUND));
		} else {
			this.upgrades = new UpgradeHolder();
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		nbt.putString("MultiblockRotation", this.multiblockRotation.toString());
		nbt.putBoolean("validStructure", this.validStructure);
		if (this.upgrades != null) {
			nbt.put("Upgrades", this.upgrades.toNbt());
		}
		if (platform != 0) {
			nbt.putFloat("platformSpin", platform);
		}
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.id().toString());
		}
	}
	
	
	// Called when the chunk is first loaded to initialize this on the clients
	
	private void doChimeParticles(@NotNull Level world) {
		doChimeInstillingParticles(world, worldPosition.offset(getItemBowlHorizontalPositionOffset(false).above(3)));
		doChimeInstillingParticles(world, worldPosition.offset(getItemBowlHorizontalPositionOffset(true).above(3)));
	}
	
	public void doChimeInstillingParticles(@NotNull Level world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof GemstoneChimeBlock gemstoneChimeBlock) {
			RandomSource random = world.random;
			ParticleOptions particleEffect = gemstoneChimeBlock.getParticleEffect();
			for (int i = 0; i < 12; i++) {
				world.addParticle(particleEffect,
						pos.getX() + 0.25 + random.nextDouble() * 0.5,
						pos.getY() + 0.15 + random.nextDouble() * 0.5,
						pos.getZ() + 0.25 + random.nextDouble() * 0.5,
						0.06 - random.nextDouble() * 0.12,
						-0.1 - random.nextDouble() * 0.05,
						0.06 - random.nextDouble() * 0.12);
			}
		}
	}
	
	private void doItemBowlOrbs(@NotNull Level world) {
		BlockPos itemBowlPos = worldPosition.offset(getItemBowlHorizontalPositionOffset(false).above());
		BlockEntity blockEntity = world.getBlockEntity(itemBowlPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			itemBowlBlockEntity.spawnOrbParticles(new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0 + platformPos.at(0, world.getGameTime()) / 16.0, this.worldPosition.getZ() + 0.5));
		}
		
		itemBowlPos = worldPosition.offset(getItemBowlHorizontalPositionOffset(true).above());
		blockEntity = world.getBlockEntity(itemBowlPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			itemBowlBlockEntity.spawnOrbParticles(new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0 + platformPos.at(0, world.getGameTime()) / 16.0, this.worldPosition.getZ() + 0.5));
		}
	}
	
	public Vec3i getItemBowlHorizontalPositionOffset(boolean right) {
		if (this.multiblockRotation == Rotation.NONE || this.multiblockRotation == Rotation.CLOCKWISE_180) {
			return itemBowlOffsetsVertical.get(right ? 1 : 0);
		} else {
			return itemBowlOffsetsHorizontal.get(right ? 1 : 0);
		}
	}
	
	public InstanceRecipeInput<SpiritInstillerBlockEntity> getRecipeInput() {
		return new InstanceRecipeInput<>(items, this);
	}
	
	// UPGRADEABLE
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.setChanged();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods2(level, worldPosition, multiblockRotation, 4, 1, this.ownerUUID);
		this.setChanged();
	}
	
	@Override
	public UpgradeHolder getUpgradeHolder() {
		return this.upgrades;
	}
	
	// PLAYER OWNED
	// "owned" is not to be taken literally here. The owner
	// is always set to the last player interacted with to trigger advancements
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.setChanged();
	}
	
	public Rotation getMultiblockRotation() {
		return multiblockRotation;
	}
	
	public void setMultiblockRotation(Rotation blockRotation) {
		this.multiblockRotation = blockRotation;
		this.upgrades = null;
		this.setChanged();
	}
	
	@Override
	public void inventoryChanged() {
		this.inventoryChanged = true;
		super.inventoryChanged();
	}
	
	static {
		var builder = new FlowAnimator.Builder<>(SpiritInstillerBlockEntity.class);
		builder.stateInfo(FlowStates.MB_INVALID, 11);
		builder.stateInfo(FlowStates.INACTIVE, 27);
		builder.stateInfo(FlowStates.IDLE, 17);
		builder.stateInfo(FlowStates.ACTIVE, 17);
		
		builder.handle("platformY", FlowHandlers.FLOAT)
				.initial(0F)
				.interpolate(Interpolation.CUBIC_OUT)
				.loopback(FlowStates.MB_INVALID, FlowStates.INACTIVE)
				.forStates((tickDelta, time) -> (float) (Math.sin((time + tickDelta + 15) / 23) + 4F), FlowStates.IDLE)
				.forStates(platformPos, FlowStates.ACTIVE)
				.push();
		builder.handle("haloY", FlowHandlers.FLOAT)
				.initial(0F)
				.interpolate(Interpolation.CUBIC_OUT)
				.startingKeyFrame(((tickDelta, time) -> (float) (Math.sin((time + tickDelta) / 23) + 1)))
				.loopback(FlowStates.MB_INVALID, FlowStates.INACTIVE, FlowStates.IDLE)
				.forStates((tickDelta, time) -> platformPos.at(tickDelta, time) - 34.5F, FlowStates.ACTIVE)
				.push();
		builder.handle("platformSpin", FlowHandlers.FLOAT)
				.initial(0F)
				.loopback(FlowStates.MB_INVALID, FlowStates.INACTIVE)
				.forStates(0.25F, FlowStates.IDLE)
				.forStates(0.825F, FlowStates.ACTIVE)
				.push();
		builder.handle("haloSpin", FlowHandlers.FLOAT)
				.initial(0.15F)
				.loopback(FlowStates.MB_INVALID, FlowStates.INACTIVE)
				.forStates(0.325F, FlowStates.IDLE)
				.forStates(0.825F, FlowStates.ACTIVE)
				.push();
		builder.handle("haloAlpha", FlowHandlers.FLOAT)
				.initial(0F)
				.forStates(1F, FlowStates.INACTIVE, FlowStates.IDLE, FlowStates.ACTIVE)
				.push();
		
		builder.handle("blossomAlpha", FlowHandlers.FLOAT)
				.initial(0F)
				.interpolate(Interpolation.CUBIC_OUT)
				.loopback(FlowStates.ACTIVE)
				.forStates(1F, FlowStates.INACTIVE, FlowStates.IDLE)
				.push();
		
		FACTORY = builder.build();
	}
}
