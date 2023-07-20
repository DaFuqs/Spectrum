package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.color.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
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
	
	protected static final int INVENTORY_SIZE = 1;
	public static final List<Vec3i> itemBowlOffsetsHorizontal = new ArrayList<>() {{
		add(new Vec3i(0, 0, 2));
		add(new Vec3i(0, 0, -2));
	}};
	
	public static final List<Vec3i> itemBowlOffsetsVertical = new ArrayList<>() {{
		add(new Vec3i(2, 0, 0));
		add(new Vec3i(-2, 0, 0));
	}};
	
	private final Inventory autoCraftingInventory; // 0: instiller stack; 1-2: item bowl stacks
	private boolean inventoryChanged;
	private UUID ownerUUID;
	private UpgradeHolder upgrades;
	private BlockRotation multiblockRotation = BlockRotation.NONE;
	private SpiritInstillerRecipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	
	public SpiritInstillerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.SPIRIT_INSTILLER, pos, state, INVENTORY_SIZE);
		this.autoCraftingInventory = new SimpleInventory(INVENTORY_SIZE + 2); // 2 item bowls
	}
	
	public static void clientTick(World world, BlockPos blockPos, BlockState blockState, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		if (spiritInstillerBlockEntity.currentRecipe != null) {
			spiritInstillerBlockEntity.doInstillerParticles(world);
			if (world.getTime() % 40 == 0) {
				spiritInstillerBlockEntity.doChimeParticles(world);
			}
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		if (spiritInstillerBlockEntity.upgrades == null) {
			spiritInstillerBlockEntity.calculateUpgrades();
		}
		
		if (spiritInstillerBlockEntity.inventoryChanged) {
			SpiritInstillerRecipe previousRecipe = spiritInstillerBlockEntity.currentRecipe;
			calculateCurrentRecipe(world, spiritInstillerBlockEntity);
			
			if (spiritInstillerBlockEntity.currentRecipe != previousRecipe) {
				spiritInstillerBlockEntity.craftingTime = 0;
				if (spiritInstillerBlockEntity.currentRecipe == null) {
					SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) world, spiritInstillerBlockEntity.pos);
				} else {
					spiritInstillerBlockEntity.craftingTimeTotal = (int) Math.ceil(spiritInstillerBlockEntity.currentRecipe.getCraftingTime() / spiritInstillerBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
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
				return;
			}
		}
		
		if (spiritInstillerBlockEntity.currentRecipe != null) {
			spiritInstillerBlockEntity.craftingTime++;
			
			if (spiritInstillerBlockEntity.craftingTime == 1) {
				SpectrumS2CPacketSender.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING, (ServerWorld) spiritInstillerBlockEntity.world, spiritInstillerBlockEntity.pos, Integer.MAX_VALUE);
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
		if (spiritInstillerBlockEntity.currentRecipe != null && !spiritInstillerBlockEntity.autoCraftingInventory.isEmpty()) {
			if (spiritInstillerBlockEntity.currentRecipe.matches(spiritInstillerBlockEntity.autoCraftingInventory, world)) {
				return;
			}
		}
		
		// cached recipe did not match => calculate new
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.currentRecipe = null;
		
		ItemStack instillerStack = spiritInstillerBlockEntity.getStack(SpiritInstillerRecipe.CENTER_INGREDIENT);
		if (!instillerStack.isEmpty()) {
			spiritInstillerBlockEntity.autoCraftingInventory.setStack(SpiritInstillerRecipe.CENTER_INGREDIENT, instillerStack);
			
			// left item bowl
			if (world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				spiritInstillerBlockEntity.autoCraftingInventory.setStack(SpiritInstillerRecipe.FIRST_INGREDIENT, itemBowlBlockEntity.getStack(0));
			} else {
				spiritInstillerBlockEntity.autoCraftingInventory.setStack(SpiritInstillerRecipe.FIRST_INGREDIENT, ItemStack.EMPTY);
			}
			// right item bowl
			if (world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				spiritInstillerBlockEntity.autoCraftingInventory.setStack(SpiritInstillerRecipe.SECOND_INGREDIENT, itemBowlBlockEntity.getStack(0));
			} else {
				spiritInstillerBlockEntity.autoCraftingInventory.setStack(SpiritInstillerRecipe.SECOND_INGREDIENT, ItemStack.EMPTY);
			}
			
			SpiritInstillerRecipe spiritInstillerRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.SPIRIT_INSTILLING, spiritInstillerBlockEntity.autoCraftingInventory, world).orElse(null);
			if (spiritInstillerRecipe != null) {
				spiritInstillerBlockEntity.currentRecipe = spiritInstillerRecipe;
				spiritInstillerBlockEntity.craftingTimeTotal = (int) Math.ceil(spiritInstillerRecipe.getCraftingTime() / spiritInstillerBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
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
			playerCanCraft = spiritInstillerBlockEntity.currentRecipe.canPlayerCraft(lastInteractedPlayer);
		}
		
		boolean structureComplete = SpiritInstillerBlock.verifyStructure(world, blockPos, null, spiritInstillerBlockEntity);
		boolean canCraft = true;
		if (!playerCanCraft || !structureComplete) {
			if (!structureComplete) {
				world.playSound(null, spiritInstillerBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + spiritInstillerBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + spiritInstillerBlockEntity.world.random.nextFloat() * 0.2F);
			}
			
			canCraft = false;
		}
		
		if (lastInteractedPlayer instanceof ServerPlayerEntity serverPlayerEntity) {
			testAndUnlockUnlockBossMemoryAdvancement(serverPlayerEntity, spiritInstillerBlockEntity.currentRecipe, canCraft);
		}
		
		return canCraft & spiritInstillerBlockEntity.currentRecipe.canPlayerCraft(lastInteractedPlayer) && spiritInstillerBlockEntity.currentRecipe.canCraftWithStacks(spiritInstillerBlockEntity.autoCraftingInventory);
	}
	
	public static void testAndUnlockUnlockBossMemoryAdvancement(ServerPlayerEntity player, SpiritInstillerRecipe spiritInstillerRecipe, boolean canActuallyCraft) {
		boolean isBossMemory = spiritInstillerRecipe.getGroup() != null && spiritInstillerRecipe.getGroup().equals("boss_memories");
		if (isBossMemory) {
			if (canActuallyCraft) {
				Support.grantAdvancementCriterion(player, "midgame/craft_blacklisted_memory_success", "succeed_crafting_boss_memory");
			} else {
				Support.grantAdvancementCriterion(player, "midgame/craft_blacklisted_memory_fail", "fail_to_craft_boss_memory");
			}
		}
	}
	
	public static void craftSpiritInstillerRecipe(World world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, @NotNull SpiritInstillerRecipe spiritInstillerRecipe) {
		ItemStack resultStack = spiritInstillerRecipe.craft(spiritInstillerBlockEntity, world.getRegistryManager());
		decrementItemsInInstillerAndBowls(spiritInstillerBlockEntity);
		if (!resultStack.isEmpty()) {
			// spawn the result stack in world
			if (spiritInstillerBlockEntity.getStack(0).isEmpty()) {
				spiritInstillerBlockEntity.setStack(0, resultStack);
			} else {
				MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, spiritInstillerBlockEntity.pos, resultStack, resultStack.getCount(), MultiblockCrafter.RECIPE_STACK_VELOCITY);
			}
		}
		
		playCraftingFinishedEffects(spiritInstillerBlockEntity);
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.inventoryChanged();
	}
	
	public static void decrementItemsInInstillerAndBowls(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		SpiritInstillerRecipe recipe = spiritInstillerBlockEntity.currentRecipe;
		
		double efficiencyModifier = 1.0;
		if (!recipe.areYieldAndEfficiencyUpgradesDisabled() && spiritInstillerBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY) != 1.0) {
			efficiencyModifier = 1.0 / spiritInstillerBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY);
		}
		
		BlockEntity leftBowlBlockEntity = spiritInstillerBlockEntity.world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false));
		BlockEntity rightBowlBlockEntity = spiritInstillerBlockEntity.world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true));
		if (leftBowlBlockEntity instanceof ItemBowlBlockEntity leftBowl && rightBowlBlockEntity instanceof ItemBowlBlockEntity rightBowl) {
			// center ingredient
			int decreasedAmountAfterEfficiencyMod = Support.getIntFromDecimalWithChance(recipe.getIngredientStacks().get(SpiritInstillerRecipe.CENTER_INGREDIENT).getCount() * efficiencyModifier, spiritInstillerBlockEntity.world.random);
			if (decreasedAmountAfterEfficiencyMod > 0) {
				spiritInstillerBlockEntity.getStack(0).decrement(decreasedAmountAfterEfficiencyMod);
			}
			
			List<IngredientStack> ingredientStacks = recipe.getIngredientStacks();
			
			// first side ingredient
			int amountAfterEfficiencyModFirst = Support.getIntFromDecimalWithChance(ingredientStacks.get(SpiritInstillerRecipe.FIRST_INGREDIENT).getCount() * efficiencyModifier, spiritInstillerBlockEntity.world.random);
			int amountAfterEfficiencyModSecond = Support.getIntFromDecimalWithChance(ingredientStacks.get(SpiritInstillerRecipe.SECOND_INGREDIENT).getCount() * efficiencyModifier, spiritInstillerBlockEntity.world.random);
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
		spiritInstillerBlockEntity.world.playSound(null, spiritInstillerBlockEntity.pos, SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING_FINISHED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) spiritInstillerBlockEntity.world,
				new Vec3d(spiritInstillerBlockEntity.pos.getX() + 0.5D, spiritInstillerBlockEntity.pos.getY() + 0.5, spiritInstillerBlockEntity.pos.getZ() + 0.5D),
				SpectrumParticleTypes.LIGHT_BLUE_CRAFTING, 75, new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(0.1D, -0.1D, 0.1D));
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.inventoryChanged = nbt.getBoolean("InventoryChanged");
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("MultiblockRotation")) {
			try {
				this.multiblockRotation = BlockRotation.valueOf(nbt.getString("MultiblockRotation").toUpperCase(Locale.ROOT));
			} catch (Exception e) {
				this.multiblockRotation = BlockRotation.NONE;
			}
		}
		
		this.currentRecipe = null;
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty() && SpectrumCommon.minecraftServer != null) {
				Optional<? extends Recipe> optionalRecipe = SpectrumCommon.minecraftServer.getRecipeManager().get(new Identifier(recipeString));
				if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof SpiritInstillerRecipe spiritInstillerRecipe) {
					this.currentRecipe = spiritInstillerRecipe;
				}
			}
		}
		
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		} else {
			this.upgrades = new UpgradeHolder();
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		nbt.putBoolean("InventoryChanged", this.inventoryChanged);
		nbt.putString("MultiblockRotation", this.multiblockRotation.toString());
		if (this.upgrades != null) {
			nbt.put("Upgrades", this.upgrades.toNbt());
		}
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
	}
	
	
	// Called when the chunk is first loaded to initialize this on the clients
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		Inventories.writeNbt(nbtCompound, this.getItems());
		nbtCompound.putShort("CraftingTime", (short) this.craftingTime);
		nbtCompound.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		nbtCompound.putString("MultiblockRotation", this.multiblockRotation.toString());
		if (this.currentRecipe != null && checkRecipeRequirements(world, this.pos, this)) {
			nbtCompound.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
		return nbtCompound;
	}
	
	private void doInstillerParticles(@NotNull World world) {
		Optional<DyeColor> stackColor = ItemColors.ITEM_COLORS.getMapping(this.getStack(0).getItem());
		
		if (stackColor.isPresent()) {
			Random random = world.random;
			ParticleEffect particleEffect = SpectrumParticleTypes.getSparkleRisingParticle(stackColor.get());
			world.addParticle(particleEffect,
					pos.getX() + 0.25 + random.nextDouble() * 0.5,
					pos.getY() + 0.75,
					pos.getZ() + 0.25 + random.nextDouble() * 0.5,
					0.02 - random.nextDouble() * 0.04,
					0.01 + random.nextDouble() * 0.05,
					0.02 - random.nextDouble() * 0.04);
		}
	}
	
	private void doChimeParticles(@NotNull World world) {
		doChimeInstillingParticles(world, pos.add(getItemBowlHorizontalPositionOffset(false).up(3)));
		doChimeInstillingParticles(world, pos.add(getItemBowlHorizontalPositionOffset(true).up(3)));
	}
	
	public void doChimeInstillingParticles(@NotNull World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof GemstoneChimeBlock gemstoneChimeBlock) {
			Random random = world.random;
			ParticleEffect particleEffect = gemstoneChimeBlock.getParticleEffect();
			for (int i = 0; i < 16; i++) {
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
			itemBowlBlockEntity.spawnOrbParticles(new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5));
		}
		
		itemBowlPos = pos.add(getItemBowlHorizontalPositionOffset(true).up());
		blockEntity = world.getBlockEntity(itemBowlPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			itemBowlBlockEntity.spawnOrbParticles(new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5));
		}
	}
	
	public Vec3i getItemBowlHorizontalPositionOffset(boolean right) {
		if (this.multiblockRotation == BlockRotation.NONE || this.multiblockRotation == BlockRotation.CLOCKWISE_180) {
			return itemBowlOffsetsVertical.get(right ? 1 : 0);
		} else {
			return itemBowlOffsetsHorizontal.get(right ? 1 : 0);
		}
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
		super.inventoryChanged();
		this.inventoryChanged = true;
		this.autoCraftingInventory.clear();
		markDirty();
	}
	
}
