package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlock;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnchanterBlockEntity extends BlockEntity implements PlayerOwned, Upgradeable {
	
	public static final int INVENTORY_SIZE = 2; // 0: any itemstack, 1: Knowledge Drop;
	
	private UUID ownerUUID;
	protected SimpleInventory inventory;
	protected boolean inventoryChanged;
	private Map<Upgradeable.UpgradeType, Double> upgrades;
	
	private Recipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	
	@Nullable
	private Direction itemFacing; // for rendering the item on the enchanter only
	
	public EnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.ENCHANTER, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		if(nbt.contains("item_facing", NbtElement.STRING_TYPE)) {
			this.itemFacing = Direction.valueOf(nbt.getString("item_facing").toUpperCase(Locale.ROOT));
		}
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
				if(optionalRecipe.isPresent() && optionalRecipe.get() instanceof FusionShrineRecipe optionalFusionRecipe) {
					this.currentRecipe = optionalFusionRecipe;
				} else {
					this.currentRecipe = null;
				}
			} else {
				this.currentRecipe = null;
			}
		} else {
			this.currentRecipe = null;
		}
		if(nbt.contains("Upgrades", NbtElement.COMPOUND_TYPE)) {
			this.upgrades = Upgradeable.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		}
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
		if(this.itemFacing != null) {
			nbt.putString("item_facing", this.itemFacing.toString().toUpperCase(Locale.ROOT));
		}
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
	
	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void setItemFacingDirection(Direction facingDirection) {
		this.itemFacing = facingDirection;
	}
	
	public Direction getItemFacingDirection() {
		// if placed via pipe or other sources
		return Objects.requireNonNullElse(this.itemFacing, Direction.NORTH);
	}
	
	public static void clientTick(World world, BlockPos blockPos, BlockState blockState, EnchanterBlockEntity enchanterBlockEntity) {
		ItemStack experienceStack = enchanterBlockEntity.getInventory().getStack(1);
		if(!experienceStack.isEmpty() && experienceStack.getItem() instanceof ExperienceStorageItem) {
			int experience = ((ExperienceStorageItem) experienceStack.getItem()).getStoredExperience(experienceStack);
			int amount = Support.getExperienceOrbSizeForExperience(experience);
			
			if(world.random.nextInt(10) < amount) {
				float randomX = 0.2F + world.getRandom().nextFloat() * 0.6F;
				float randomZ = 0.2F + world.getRandom().nextFloat() * 0.6F;
				float randomY = -0.1F + world.getRandom().nextFloat() * 0.4F;
				world.addParticle(SpectrumParticleTypes.LIME_SPARKLE_RISING, blockPos.getX() + randomX, blockPos.getY() + 2.5 + randomY, blockPos.getZ() + randomZ, 0.0D, -0.1D, 0.0D);
			}
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, EnchanterBlockEntity enchanterBlockEntity) {
		if(enchanterBlockEntity.upgrades == null) {
			enchanterBlockEntity.calculateUpgrades();
		}
		
		if(enchanterBlockEntity.inventoryChanged) {
			calculateCurrentRecipe(world, enchanterBlockEntity);
		}
		
		boolean craftingSuccess = false;
		
		// if player d
		if(enchanterBlockEntity.currentRecipe != null && enchanterBlockEntity.craftingTime % 60 == 1) {
			PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(world, enchanterBlockEntity.ownerUUID);
			
			boolean playerHasAdvancement = true;
			if(enchanterBlockEntity.currentRecipe instanceof EnchanterRecipe enchanterRecipe) {
				playerHasAdvancement = Support.hasAdvancement(lastInteractedPlayer, enchanterRecipe.getRequiredAdvancementIdentifier());
			} else if(enchanterBlockEntity.currentRecipe instanceof EnchantmentUpgradeRecipe enchantmentUpgradeRecipe) {
				playerHasAdvancement = Support.hasAdvancement(lastInteractedPlayer, enchantmentUpgradeRecipe.getRequiredAdvancementIdentifier());
			}
			boolean structureComplete = EnchanterBlock.verifyStructure(world, blockPos, null);
			
			if(!playerHasAdvancement || !structureComplete) {
				if(!structureComplete) {
					world.playSound(null, enchanterBlockEntity.getPos(), SpectrumSoundEvents.FUSION_SHRINE_CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + enchanterBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + enchanterBlockEntity.world.random.nextFloat() * 0.2F);
					world.playSound(null, enchanterBlockEntity.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + enchanterBlockEntity.world.random.nextFloat() * 0.2F, 0.5F + enchanterBlockEntity.world.random.nextFloat() * 0.2F);
					EnchanterBlock.scatterContents(world, blockPos);
				}
				enchanterBlockEntity.craftingTime = 0;
				return;
			}
		}
		
		if(enchanterBlockEntity.currentRecipe instanceof EnchanterRecipe enchanterRecipe) {
		
		} else if(enchanterBlockEntity.currentRecipe instanceof EnchantmentUpgradeRecipe enchantmentUpgradeRecipe) {
		
		} else {
			// in-code recipe for enchanting items
		}
		
		if(craftingSuccess) {
			enchanterBlockEntity.inventoryChanged();
		}
	}
	
	/**
	 * Calculates and sets a new recipe for the enchanter based on it's inventory
	 * @param world Wthe Enchanter World
	 * @param enchanterBlockEntity The Enchanter Block Entity
	 * @return Wether or not the previous recipe was still valid. False means it was changed
	 */
	private static boolean calculateCurrentRecipe(@NotNull World world, EnchanterBlockEntity enchanterBlockEntity) {
		if(enchanterBlockEntity.currentRecipe != null) {
			if(enchanterBlockEntity.currentRecipe.matches(enchanterBlockEntity.inventory, world)) {
				return true;
			}
		}
		
		enchanterBlockEntity.craftingTime = 0;
		
		Recipe previousRecipe = enchanterBlockEntity.currentRecipe;
		EnchanterRecipe enchanterRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ENCHANTER, enchanterBlockEntity.inventory, world).orElse(null);
		if(enchanterRecipe == null) {
			EnchantmentUpgradeRecipe enchantmentUpgradeRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, enchanterBlockEntity.inventory, world).orElse(null);
			if(enchantmentUpgradeRecipe == null) {
				enchanterBlockEntity.currentRecipe = null;
			} else {
				enchanterBlockEntity.currentRecipe = enchantmentUpgradeRecipe;
				enchanterBlockEntity.craftingTimeTotal = enchantmentUpgradeRecipe.getRequiredItemCount();
			}
		} else {
			enchanterBlockEntity.currentRecipe = enchanterRecipe;
			enchanterBlockEntity.craftingTimeTotal = (int) Math.ceil(enchanterRecipe.getCraftingTime() / enchanterBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
		}
		
		if(enchanterBlockEntity.currentRecipe != previousRecipe) {
			enchanterBlockEntity.updateInClientWorld();
			//SpectrumS2CPackets.sendCancelBlockBoundSoundInstance((ServerWorld) enchanterBlockEntity.world, enchanterBlockEntity.pos);
		}
		
		return false;
	}
	
	public void inventoryChanged() {
		this.inventory.markDirty();
		this.inventoryChanged = true;
	}
	
	public void playSound(SoundEvent soundEvent, float volume) {
		Random random = world.random;
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, volume, 0.9F + random.nextFloat() * 0.15F);
	}
	
	private void grantPlayerEnchantingAdvancement(ServerPlayerEntity player, ItemStack resultStack, int levels) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(this.world, this.ownerUUID);
		if(serverPlayerEntity != null) {
			player.incrementStat(Stats.ENCHANT_ITEM);
			Criteria.ENCHANTED_ITEM.trigger(player, resultStack, levels);
		}
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
	
	// UPGRADEABLE
	public void resetUpgrades() {
		this.upgrades = null;
	}
	
	public void calculateUpgrades() {
		Pair<Integer, Map<UpgradeType, Double>> upgrades = Upgradeable.checkUpgradeMods(world, pos, 2, 0);
		this.upgrades = upgrades.getRight();
	}
	
}
