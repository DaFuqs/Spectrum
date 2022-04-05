package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlock;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.inventories.AutoCraftingInventory;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpiritInstillerBlockEntity extends BlockEntity implements MultiblockCrafter {
	
	private UUID ownerUUID;
	private Map<UpgradeType, Double> upgrades;
	
	protected int INVENTORY_SIZE = 1;
	protected SimpleInventory inventory;
	
	private BlockRotation multiblockRotation = BlockRotation.NONE;
	protected boolean inventoryChanged;
	
	private final AutoCraftingInventory autoCraftingInventory;
	private SpiritInstillerRecipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	
	public SpiritInstillerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.SPIRIT_INSTILLER, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.autoCraftingInventory = new AutoCraftingInventory(3, 1);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.inventoryChanged = nbt.getBoolean("InventoryChanged");
		if(nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if(nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if(!recipeString.isEmpty()) {
				Optional<? extends Recipe> optionalRecipe = Optional.empty();
				if (world != null) {
					optionalRecipe = world.getRecipeManager().get(new Identifier(recipeString));
				}
				if(optionalRecipe.isPresent() && optionalRecipe.get() instanceof SpiritInstillerRecipe spiritInstillerRecipe) {
					this.currentRecipe = spiritInstillerRecipe;
				} else {
					this.currentRecipe = null;
				}
			} else {
				this.currentRecipe = null;
			}
		} else {
			this.currentRecipe = null;
		}
		if(nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = Upgradeable.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
		nbt.putShort("CraftingTime", (short)this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short)this.craftingTimeTotal);
		nbt.putBoolean("InventoryChanged", this.inventoryChanged);
		if(this.upgrades != null) {
			nbt.put("Upgrades", Upgradeable.toNbt(this.upgrades));
		}
		if(this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if(this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
	}
	
	// TODO: test
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		if(spiritInstillerBlockEntity.upgrades == null) {
			spiritInstillerBlockEntity.calculateUpgrades();
		}
		
		if(spiritInstillerBlockEntity.inventoryChanged) {
			calculateCurrentRecipe(world, spiritInstillerBlockEntity);
			spiritInstillerBlockEntity.inventoryChanged = false;
		}
		
		boolean craftingSuccess = false;
		
		if(spiritInstillerBlockEntity.currentRecipe != null) {
			if(spiritInstillerBlockEntity.craftingTime % 60 == 1) {
				if (!checkRecipeRequirements(world, blockPos, spiritInstillerBlockEntity)) {
					spiritInstillerBlockEntity.craftingTime = 0;
					SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) spiritInstillerBlockEntity.world, spiritInstillerBlockEntity.pos);
					return;
				}
			}
			if(spiritInstillerBlockEntity.craftingTime == 1) {
				SpectrumS2CPacketSender.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING, (ServerWorld) spiritInstillerBlockEntity.world, spiritInstillerBlockEntity.pos, Integer.MAX_VALUE);
			}
			
			if (spiritInstillerBlockEntity.currentRecipe != null) {
				spiritInstillerBlockEntity.craftingTime++;
				
				// looks cooler this way
				if (spiritInstillerBlockEntity.craftingTime == spiritInstillerBlockEntity.craftingTimeTotal - 10) {
					playCraftingFinishedEffects(spiritInstillerBlockEntity);
				}
				if (spiritInstillerBlockEntity.craftingTime == spiritInstillerBlockEntity.craftingTimeTotal) {
					craftSpiritInstillerRecipe(world, spiritInstillerBlockEntity, spiritInstillerBlockEntity.currentRecipe);
					craftingSuccess = true;
				}
				spiritInstillerBlockEntity.markDirty();
			}
			
			if (craftingSuccess) {
				spiritInstillerBlockEntity.craftingTime = 0;
				spiritInstillerBlockEntity.inventoryChanged();
			}
		} else {
			SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) spiritInstillerBlockEntity.world, spiritInstillerBlockEntity.pos);
		}
	}
	
	private static boolean calculateCurrentRecipe(@NotNull World world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		if(spiritInstillerBlockEntity.currentRecipe != null) {
			if(spiritInstillerBlockEntity.currentRecipe.matches(spiritInstillerBlockEntity.autoCraftingInventory, world)) {
				return true;
			}
		}
		
		spiritInstillerBlockEntity.craftingTime = 0;
		spiritInstillerBlockEntity.currentRecipe = null;
		
		spiritInstillerBlockEntity.autoCraftingInventory.setStack(0, spiritInstillerBlockEntity.inventory.getStack(0));
		if(world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, false)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			spiritInstillerBlockEntity.autoCraftingInventory.setStack(1, itemBowlBlockEntity.getInventory().getStack(0));
		} else {
			spiritInstillerBlockEntity.autoCraftingInventory.setStack(1, ItemStack.EMPTY);
		}
		if(world.getBlockEntity(getItemBowlPos(spiritInstillerBlockEntity, true)) instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			spiritInstillerBlockEntity.autoCraftingInventory.setStack(2, itemBowlBlockEntity.getInventory().getStack(0));
		} else {
			spiritInstillerBlockEntity.autoCraftingInventory.setStack(2, ItemStack.EMPTY);
		}
		
		SpiritInstillerRecipe spiritInstillerRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE, spiritInstillerBlockEntity.autoCraftingInventory, world).orElse(null);
		if (spiritInstillerRecipe != null) {
			spiritInstillerBlockEntity.currentRecipe = spiritInstillerRecipe;
			spiritInstillerBlockEntity.craftingTimeTotal = (int) Math.ceil(spiritInstillerRecipe.getCraftingTime() / spiritInstillerBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
		}
		
		return false;
	}
	
	public static BlockPos getItemBowlPos(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, boolean right) {
		BlockPos blockPos = spiritInstillerBlockEntity.pos;
		switch (spiritInstillerBlockEntity.multiblockRotation) {
			case NONE, CLOCKWISE_180 -> {
				if(right) {
					return blockPos.up().north(2);
				} else {
					return blockPos.up().south(2);
				}
			}
			default -> {
				if(right) {
					return blockPos.up().east(2);
				} else {
					return blockPos.up().west(2);
				}
			}
		}
	}
	
	private static boolean checkRecipeRequirements(World world, BlockPos blockPos, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(world, spiritInstillerBlockEntity.ownerUUID);
		
		if(lastInteractedPlayer == null) {
			return false;
		}
		
		boolean playerCanCraft = true;
		if (spiritInstillerBlockEntity.currentRecipe != null) {
			playerCanCraft = spiritInstillerBlockEntity.currentRecipe.canPlayerCraft(lastInteractedPlayer);
		}
		boolean structureComplete = EnchanterBlock.verifyStructure(world, blockPos, null);
		
		if (!playerCanCraft || !structureComplete) {
			if (!structureComplete) {
				world.playSound(null, spiritInstillerBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + spiritInstillerBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + spiritInstillerBlockEntity.world.random.nextFloat() * 0.2F);
				world.playSound(null, spiritInstillerBlockEntity.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + spiritInstillerBlockEntity.world.random.nextFloat() * 0.2F, 0.5F + spiritInstillerBlockEntity.world.random.nextFloat() * 0.2F);
				EnchanterBlock.scatterContents(world, blockPos);
			}
			return false;
		}
		return true;
	}
	
	public static void craftSpiritInstillerRecipe(World world, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity, @NotNull SpiritInstillerRecipe spiritInstillerRecipe) {
		if(decrementItems(spiritInstillerBlockEntity)) {
			// if there is room: place the output on the instiller
			// otherwise: pop it off
			ItemStack resultStack = spiritInstillerRecipe.getOutput().copy();
			
			if (!spiritInstillerRecipe.areYieldAndEfficiencyUpgradesDisabled() && spiritInstillerBlockEntity.upgrades.get(UpgradeType.YIELD) != 1.0) {
				int resultCountMod = Support.getIntFromDecimalWithChance(resultStack.getCount() * spiritInstillerBlockEntity.upgrades.get(UpgradeType.YIELD), world.random);
				resultStack.setCount(resultCountMod);
			}
			
			if (spiritInstillerBlockEntity.getInventory().getStack(0).isEmpty()) {
				EnchanterBlockEntity.spawnItemStackAsEntitySplitViaMaxCount(world, spiritInstillerBlockEntity.pos, resultStack, resultStack.getCount());
			} else {
				spiritInstillerBlockEntity.getInventory().setStack(0, resultStack);
			}
			
			int awardedExperience = Support.getIntFromDecimalWithChance(spiritInstillerRecipe.getExperience(), spiritInstillerBlockEntity.world.random);
			MultiblockCrafter.spawnExperience(spiritInstillerBlockEntity.world, spiritInstillerBlockEntity.pos, awardedExperience);
			
			grantPlayerSpiritInstillingAdvancementCriterion(world, spiritInstillerBlockEntity.ownerUUID, resultStack, awardedExperience);
		}
	}
	
	public static boolean decrementItems(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		SpiritInstillerRecipe spiritInstillerRecipe = spiritInstillerBlockEntity.currentRecipe;
		boolean success = true;

		int resultAmountAfterEfficiencyMod = 1;
		for(int i = 0; i < 3; i++) {
			if(!spiritInstillerRecipe.areYieldAndEfficiencyUpgradesDisabled() && spiritInstillerBlockEntity.upgrades.get(UpgradeType.EFFICIENCY) != 1.0) {
				double efficiencyModifier = 1.0 / spiritInstillerBlockEntity.upgrades.get(UpgradeType.EFFICIENCY);
				resultAmountAfterEfficiencyMod = Support.getIntFromDecimalWithChance(efficiencyModifier, spiritInstillerBlockEntity.world.random);
			}
			
			if(resultAmountAfterEfficiencyMod > 0) {
				if(i == 0) {
					spiritInstillerBlockEntity.inventory.getStack(0).decrement(resultAmountAfterEfficiencyMod);
				} else {
					BlockPos itemBowlPos = spiritInstillerBlockEntity.pos.add(getItemBowlPos(spiritInstillerBlockEntity, i == 1));
					BlockEntity blockEntity = spiritInstillerBlockEntity.world.getBlockEntity(itemBowlPos);
					if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
						itemBowlBlockEntity.decrementBowlStack(spiritInstillerBlockEntity.pos, resultAmountAfterEfficiencyMod);
						itemBowlBlockEntity.updateInClientWorld();
					} else {
						success = false;
					}
				}
			}
		}
		
		return success;
	}
	
	private static void grantPlayerSpiritInstillingAdvancementCriterion(World world, UUID playerUUID, ItemStack resultStack, int experience) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(world, playerUUID);
		if(serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.SPIRIT_INSTILLER_CRAFTING.trigger(serverPlayerEntity, resultStack, experience);
		}
	}
	
	public static void playCraftingFinishedEffects(@NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		spiritInstillerBlockEntity.world.playSound(null, spiritInstillerBlockEntity.pos, SpectrumSoundEvents.SPIRIT_INSTILLER_CRAFTING_FINISHED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) spiritInstillerBlockEntity.world,
				new Vec3d(spiritInstillerBlockEntity.pos.getX() + 0.5D, spiritInstillerBlockEntity.pos.getY() + 0.5, spiritInstillerBlockEntity.pos.getZ() + 0.5D),
				SpectrumParticleTypes.LIGHT_BLUE_CRAFTING, 75, new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(0.1D, -0.1D, 0.1D));
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}
	
	// UPGRADEABLE
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.markDirty();
	}
	
	@Override
	public void calculateUpgrades() {
		Pair<Integer, Map<UpgradeType, Double>> upgrades = Upgradeable.checkUpgradeMods2(world, pos, multiblockRotation, 4, 1);
		this.upgrades = upgrades.getRight();
		this.markDirty();
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
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher finder) {
	
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public BlockRotation getMultiblockRotation() {
		return multiblockRotation;
	}
	
	public void setMultiblockRotation(BlockRotation blockRotation) {
		this.multiblockRotation = blockRotation;
		this.upgrades = null;
		this.markDirty();
	}
	
	public void inventoryChanged() {
		markDirty();
		inventory.markDirty();
		updateInClientWorld();
	}
	
}
