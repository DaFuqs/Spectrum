package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.*;
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
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
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
	
	private static final Identifier JADE_VINE_CROSSBREEDING = SpectrumCommon.locate("spirit_instiller/secret/germinated_jade_vine_crossbreeding"); // TODO: Move to advancements class
	
	private boolean inventoryChanged;
	private UUID ownerUUID;
	private UpgradeHolder upgrades;
	private BlockRotation multiblockRotation = BlockRotation.NONE;
	private RecipeEntry<SpiritInstillerRecipe> currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	private boolean validStructure;
	
	protected FlowAnimator animator;
	protected FlowData<Float> _platformY = FlowData.NULL(), _haloY = FlowData.NULL(),
			_platformSpin = FlowData.NULL(), _haloSpin = FlowData.NULL(),
			_haloAlpha = FlowData.NULL(), _blossomAlpha = FlowData.NULL();
	protected float platform, geode, calcite, innergeode;
	
	public SpiritInstillerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.SPIRIT_INSTILLER, pos, state, INVENTORY_SIZE);
	}
	
	public static void clientTick(World world, BlockPos blockPos, BlockState blockState, @NotNull SpiritInstillerBlockEntity instiller) {
		if (instiller.animator == null) {
			instiller.animator = FACTORY.create(FlowStates.INIT, instiller);
		}
		else {
			instiller.updateAnimator();
		}
		
		if (instiller.currentRecipe != null && world.getTime() % 43 == 0) {
			instiller.doChimeParticles(world);
		}
	}
	
	private void updateAnimator() {
		animator.tick();
		
		if (!validStructure) {
			animator.swapState(FlowStates.MB_INVALID);
			return;
		}
		
		if (getStack(0).isEmpty()) {
			animator.swapState(FlowStates.INACTIVE);
		}
		else if (currentRecipe != null) {
			animator.swapState(FlowStates.ACTIVE);
		}
		else {
			animator.swapState(FlowStates.IDLE);
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		if (spiritInstillerBlockEntity.upgrades == null) {
			spiritInstillerBlockEntity.calculateUpgrades();
		}
		
		if (spiritInstillerBlockEntity.inventoryChanged) {
			var previousRecipe = spiritInstillerBlockEntity.currentRecipe;
			calculateCurrentRecipe(world, spiritInstillerBlockEntity);
			
			if (spiritInstillerBlockEntity.currentRecipe != previousRecipe) {
				spiritInstillerBlockEntity.craftingTime = 0;
				if (spiritInstillerBlockEntity.currentRecipe == null) {
					PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerWorld) world, spiritInstillerBlockEntity.pos);
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
				spiritInstillerBlockEntity.markDirty();
				PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerWorld) world, spiritInstillerBlockEntity.pos);
				return;
			}
		}
		
		if (spiritInstillerBlockEntity.currentRecipe != null) {
			spiritInstillerBlockEntity.craftingTime++;
			
			if (spiritInstillerBlockEntity.craftingTime == 1) {
				PlayBlockBoundSoundInstancePayload.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING, (ServerWorld) world, spiritInstillerBlockEntity.pos, Integer.MAX_VALUE);
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
			
			spiritInstillerBlockEntity.markDirty();
		}
	}
	
	private static void calculateCurrentRecipe(@NotNull World world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		// test the cached recipe => faster
		if (spiritInstillerBlockEntity.currentRecipe != null && !spiritInstillerBlockEntity.isEmpty()) {
			if (spiritInstillerBlockEntity.currentRecipe.value().matches(spiritInstillerBlockEntity.getRecipeInput(), world)) {
				return;
			}
		}
		
		// cached recipe did not match => calculate new
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.currentRecipe = null;
		
		ItemStack instillerStack = spiritInstillerBlockEntity.getStack(SpiritInstillerRecipe.CENTER_INGREDIENT);
		if (!instillerStack.isEmpty()) {
			spiritInstillerBlockEntity.setStack(SpiritInstillerRecipe.CENTER_INGREDIENT, instillerStack);
			
			// left item bowl
			if (world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				spiritInstillerBlockEntity.setStack(SpiritInstillerRecipe.FIRST_INGREDIENT, itemBowlBlockEntity.getStack(0));
			} else {
				spiritInstillerBlockEntity.setStack(SpiritInstillerRecipe.FIRST_INGREDIENT, ItemStack.EMPTY);
			}
			// right item bowl
			if (world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				spiritInstillerBlockEntity.setStack(SpiritInstillerRecipe.SECOND_INGREDIENT, itemBowlBlockEntity.getStack(0));
			} else {
				spiritInstillerBlockEntity.setStack(SpiritInstillerRecipe.SECOND_INGREDIENT, ItemStack.EMPTY);
			}
			
			RecipeEntry<SpiritInstillerRecipe> spiritInstillerRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.SPIRIT_INSTILLING, spiritInstillerBlockEntity.getRecipeInput(), world).orElse(null);
			if (spiritInstillerRecipe != null) {
				spiritInstillerBlockEntity.currentRecipe = spiritInstillerRecipe;
				spiritInstillerBlockEntity.craftingTimeTotal = (int) Math.ceil(spiritInstillerRecipe.value().getCraftingTime() / spiritInstillerBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
			}
		}
		
		spiritInstillerBlockEntity.updateInClientWorld();
	}
	
	public static BlockPos getItemBowlPos(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, boolean right) {
		BlockPos blockPos = spiritInstillerBlockEntity.pos;
		switch (spiritInstillerBlockEntity.multiblockRotation) {
			case NONE, CLOCKWISE_180 -> {
				if (right) {
					return blockPos.up().east(2);
				} else {
					return blockPos.up().west(2);
				}
			}
			default -> {
				if (right) {
					return blockPos.up().north(2);
				} else {
					return blockPos.up().south(2);
				}
			}
		}
	}
	
	private static boolean checkRecipeRequirements(World world, BlockPos blockPos, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(spiritInstillerBlockEntity.ownerUUID);
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
				world.playSound(null, spiritInstillerBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
			}
			
			canCraft = false;
		}
		
		if (lastInteractedPlayer instanceof ServerPlayerEntity serverPlayerEntity) {
			testAndUnlockRecipeAdvancements(serverPlayerEntity, spiritInstillerBlockEntity.currentRecipe, canCraft);
		}
		
		return canCraft & spiritInstillerBlockEntity.currentRecipe.value().canPlayerCraft(lastInteractedPlayer) && spiritInstillerBlockEntity.currentRecipe.value().canCraftWithStacks(spiritInstillerBlockEntity.getRecipeInput());
	}
	
	public static void testAndUnlockRecipeAdvancements(ServerPlayerEntity player, RecipeEntry<SpiritInstillerRecipe> spiritInstillerRecipe, boolean canActuallyCraft) {
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
		if (spiritInstillerRecipe.id().equals(JADE_VINE_CROSSBREEDING)) {
			Support.grantAdvancementCriterion(player, "lategame/create_jade_vine", "crossbred_jade_vine_bulb");
		}
	}
	
	public static void craftSpiritInstillerRecipe(World world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, @NotNull RecipeEntry<SpiritInstillerRecipe> spiritInstillerRecipe) {
		ItemStack resultStack = spiritInstillerRecipe.value().craft(spiritInstillerBlockEntity.getRecipeInput(), world.getRegistryManager());
		decrementItemsInInstillerAndBowls(spiritInstillerBlockEntity);
		if (!resultStack.isEmpty()) {
			if (spiritInstillerBlockEntity.getStack(0).isEmpty()) {
				// keep it on the Instiller
				spiritInstillerBlockEntity.setStack(0, resultStack);
			} else {
				// spawn the result stack in world
				MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, spiritInstillerBlockEntity.pos, resultStack, resultStack.getCount(), MultiblockCrafter.RECIPE_STACK_VELOCITY);
			}
		}
		
		playCraftingFinishedEffects(spiritInstillerBlockEntity);
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.inventoryChanged();
	}
	
	public static void decrementItemsInInstillerAndBowls(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		World world = spiritInstillerBlockEntity.getWorld();
		if (world == null) return;
		var recipe = spiritInstillerBlockEntity.currentRecipe;
		
		double efficiencyModifier = 1.0;
		if (!recipe.value().areYieldAndEfficiencyUpgradesDisabled() && spiritInstillerBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY) != 1.0) {
			efficiencyModifier = 1.0 / spiritInstillerBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY);
		}
		
		BlockEntity leftBowlBlockEntity = world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false));
		BlockEntity rightBowlBlockEntity = world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true));
		if (leftBowlBlockEntity instanceof ItemBowlBlockEntity leftBowl && rightBowlBlockEntity instanceof ItemBowlBlockEntity rightBowl) {
			// center ingredient
			int decreasedAmountAfterEfficiencyMod = Support.getIntFromDecimalWithChance(recipe.value().getIngredientStacks().get(SpiritInstillerRecipe.CENTER_INGREDIENT).getCount() * efficiencyModifier, world.random);
			if (decreasedAmountAfterEfficiencyMod > 0) {
				spiritInstillerBlockEntity.getStack(0).decrement(decreasedAmountAfterEfficiencyMod);
			}
			
			List<IngredientStack> ingredientStacks = recipe.value().getIngredientStacks();
			
			// first side ingredient
			int amountAfterEfficiencyModFirst = Support.getIntFromDecimalWithChance(ingredientStacks.get(SpiritInstillerRecipe.FIRST_INGREDIENT).getCount() * efficiencyModifier, world.random);
			int amountAfterEfficiencyModSecond = Support.getIntFromDecimalWithChance(ingredientStacks.get(SpiritInstillerRecipe.SECOND_INGREDIENT).getCount() * efficiencyModifier, world.random);
			boolean leftIsFirstIngredient = ingredientStacks.get(SpiritInstillerRecipe.FIRST_INGREDIENT).test(leftBowl.getStack(0));
			Vec3d particlePos = new Vec3d(spiritInstillerBlockEntity.pos.getX() + 0.5, spiritInstillerBlockEntity.pos.getY() + 1, spiritInstillerBlockEntity.pos.getZ() + 0.5);
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
		World world = spiritInstillerBlockEntity.getWorld();
		if (world == null) return;
		world.playSound(null, spiritInstillerBlockEntity.pos, SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING_FINISHED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
				new Vec3d(spiritInstillerBlockEntity.pos.getX() + 0.5D, spiritInstillerBlockEntity.pos.getY() + 0.5, spiritInstillerBlockEntity.pos.getZ() + 0.5D),
				ColoredCraftingParticleEffect.LIGHT_BLUE, 75, new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(0.1D, -0.1D, 0.1D));
	}
	
	public void setValidStructure(boolean validStructure) {
		if (!world.isClient()) {
			this.validStructure = validStructure;
			markDirty();
			updateInClientWorld();
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.inventoryChanged = true;
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		this.validStructure = nbt.getBoolean("validStructure");
		if (nbt.contains("MultiblockRotation")) {
			try {
				this.multiblockRotation = BlockRotation.valueOf(nbt.getString("MultiblockRotation").toUpperCase(Locale.ROOT));
			} catch (Exception e) {
				this.multiblockRotation = BlockRotation.NONE;
			}
		}
		
		if (nbt.contains("platformSpin"))
			platform = nbt.getFloat("platformSpin");
		
		this.currentRecipe = MultiblockCrafter.getRecipeEntryFromNbt(world, nbt, SpiritInstillerRecipe.class);
		
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		} else {
			this.upgrades = new UpgradeHolder();
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
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
	
	private void doChimeParticles(@NotNull World world) {
		doChimeInstillingParticles(world, pos.add(getItemBowlHorizontalPositionOffset(false).up(3)));
		doChimeInstillingParticles(world, pos.add(getItemBowlHorizontalPositionOffset(true).up(3)));
	}
	
	public void doChimeInstillingParticles(@NotNull World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof GemstoneChimeBlock gemstoneChimeBlock) {
			Random random = world.random;
			ParticleEffect particleEffect = gemstoneChimeBlock.getParticleEffect();
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
	
	private void doItemBowlOrbs(@NotNull World world) {
		BlockPos itemBowlPos = pos.add(getItemBowlHorizontalPositionOffset(false).up());
		BlockEntity blockEntity = world.getBlockEntity(itemBowlPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			itemBowlBlockEntity.spawnOrbParticles(new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 1.0 + platformPos.at(0, world.getTime()) / 16.0, this.pos.getZ() + 0.5));
		}
		
		itemBowlPos = pos.add(getItemBowlHorizontalPositionOffset(true).up());
		blockEntity = world.getBlockEntity(itemBowlPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			itemBowlBlockEntity.spawnOrbParticles(new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 1.0 + platformPos.at(0, world.getTime()) / 16.0, this.pos.getZ() + 0.5));
		}
	}
	
	public Vec3i getItemBowlHorizontalPositionOffset(boolean right) {
		if (this.multiblockRotation == BlockRotation.NONE || this.multiblockRotation == BlockRotation.CLOCKWISE_180) {
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
		this.markDirty();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods2(world, pos, multiblockRotation, 4, 1, this.ownerUUID);
		this.markDirty();
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
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.markDirty();
	}
	
	public BlockRotation getMultiblockRotation() {
		return multiblockRotation;
	}
	
	public void setMultiblockRotation(BlockRotation blockRotation) {
		this.multiblockRotation = blockRotation;
		this.upgrades = null;
		this.markDirty();
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
				.interpolate(Interpolation.EASE_OUT)
				.loopback(FlowStates.MB_INVALID, FlowStates.INACTIVE)
				.forStates((tickDelta, time) -> (float) (Math.sin((time + tickDelta + 15) / 23) + 4F), FlowStates.IDLE)
				.forStates(platformPos, FlowStates.ACTIVE)
				.push();
		builder.handle("haloY", FlowHandlers.FLOAT)
				.initial(0F)
				.interpolate(Interpolation.EASE_OUT)
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
				.interpolate(Interpolation.EASE_OUT)
				.loopback(FlowStates.ACTIVE)
				.forStates(1F, FlowStates.INACTIVE, FlowStates.IDLE)
				.push();
		
		FACTORY = builder.build();
	}
}
