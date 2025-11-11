package de.dafuqs.spectrum.blocks.fusion_shrine;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FusionShrineBlockEntity extends InWorldInteractionBlockEntity implements PlayerOwned, Upgradeable {
	
	protected static final int INVENTORY_SIZE = 7;
	
	private UUID ownerUUID;
	private UpgradeHolder upgrades;
	private RecipeHolder<FusionShrineRecipe> currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	
	private boolean inventoryChanged = true;
	
	public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
		@Override
		protected FluidVariant getBlankVariant() {
			return FluidVariant.blank();
		}
		
		@Override
		protected long getCapacity(FluidVariant variant) {
			return FluidConstants.BUCKET;
		}
		
		@Override
		protected void onFinalCommit() {
			super.onFinalCommit();
			setLightForFluid(worldPosition, this.variant.getFluid());
			inventoryChanged();
			setChanged();
		}
	};
	
	public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.FUSION_SHRINE, pos, state, INVENTORY_SIZE);
	}
	
	@SuppressWarnings("unused")
	public static void clientTick(@NotNull Level world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if (!fusionShrineBlockEntity.isEmpty()) {
			int randomSlot = world.getRandom().nextInt(fusionShrineBlockEntity.getContainerSize());
			ItemStack randomStack = fusionShrineBlockEntity.getItem(randomSlot);
			if (!randomStack.isEmpty()) {
				Optional<InkColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(randomStack.getItem());
				if (optionalItemColor.isPresent()) {
					ParticleOptions particleEffect = ColoredCraftingParticleEffect.of(optionalItemColor.get().getColorInt());
					
					int particleAmount = (int) StrictMath.ceil(randomStack.getCount() / 8.0F);
					for (int i = 0; i < particleAmount; i++) {
						float randomX = 3.0F - world.getRandom().nextFloat() * 7;
						float randomZ = 3.0F - world.getRandom().nextFloat() * 7;
						world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		}
	}
	
	public void spawnCraftingParticles() {
		BlockPos blockPos = getBlockPos();
		var recipe = this.currentRecipe;
		if (recipe != null && level != null) {
			Fluid fluid = this.getFluidVariant().getFluid();
			Optional<InkColor> optionalFluidColor = ColorRegistry.FLUID_COLORS.getMapping(fluid);
			if (optionalFluidColor.isPresent()) {
				ParticleOptions particleEffect = ColoredFluidRisingParticleEffect.of(optionalFluidColor.get().getColorInt());
				
				float randomX = 0.1F + level.getRandom().nextFloat() * 0.8F;
				float randomZ = 0.1F + level.getRandom().nextFloat() * 0.8F;
				level.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY() + 1, blockPos.getZ() + randomZ, 0.0D, 0.1D, 0.0D);
			}
		}
	}
	
	public void scatterContents(@NotNull Level world) {
		PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity((ServerLevel) world, Vec3.atCenterOf(this.getBlockPos()), ColoredCraftingParticleEffect.RED, 1, new Vec3(0, -0.5, 0));
		world.playSound(null, this.getBlockPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
		world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.5F + world.random.nextFloat() * 0.2F);
		FusionShrineBlock.scatterContents(world, this.getBlockPos());
		this.inventoryChanged();
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(@NotNull Level world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if (fusionShrineBlockEntity.upgrades == null) {
			fusionShrineBlockEntity.calculateUpgrades();
		}
		
		if (fusionShrineBlockEntity.inventoryChanged) {
			var previousRecipe = fusionShrineBlockEntity.currentRecipe;
			fusionShrineBlockEntity.currentRecipe = calculateRecipe(world, fusionShrineBlockEntity);
			
			if (!Objects.equals(fusionShrineBlockEntity.currentRecipe, previousRecipe)) {
				fusionShrineBlockEntity.craftingTime = 0;
				if (fusionShrineBlockEntity.currentRecipe == null) {
					PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) world, fusionShrineBlockEntity.worldPosition);
				} else {
					fusionShrineBlockEntity.craftingTimeTotal = (int) Math.ceil(fusionShrineBlockEntity.currentRecipe.value().getCraftingTime() / fusionShrineBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
				}
				
				fusionShrineBlockEntity.updateInClientWorld();
			}
			
			fusionShrineBlockEntity.inventoryChanged = false;
		}
		
		var recipe = fusionShrineBlockEntity.currentRecipe;
		if (recipe == null) {
			return;
		}
		
		// check the crafting conditions from time to time
		// good for performance because of the many checks
		if (fusionShrineBlockEntity.craftingTime % 60 == 0) {
			Player lastInteractedPlayer = fusionShrineBlockEntity.getOwnerIfOnline();
			
			boolean recipeConditionsMet = recipe.value().canPlayerCraft(lastInteractedPlayer) && recipe.value().areConditionMetCurrently((ServerLevel) world, blockPos);
			boolean structureComplete = FusionShrineBlock.verifyStructure(world, blockPos, null);
			boolean structureCompleteWithSky = FusionShrineBlock.verifySkyAccess((ServerLevel) world, blockPos) && structureComplete;
			
			if (!recipeConditionsMet || !structureCompleteWithSky) {
				if (!structureCompleteWithSky) {
					fusionShrineBlockEntity.scatterContents(world);
				}
				fusionShrineBlockEntity.craftingTime = 0;
				return;
			}
		}
		
		// advance crafting
		++fusionShrineBlockEntity.craftingTime;
		
		if (fusionShrineBlockEntity.craftingTime == 1 && fusionShrineBlockEntity.craftingTimeTotal > 1) {
			PlayBlockBoundSoundInstancePayload.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING, (ServerLevel) world, fusionShrineBlockEntity.getBlockPos(), fusionShrineBlockEntity.craftingTimeTotal - fusionShrineBlockEntity.craftingTime);
		}
		
		// craft when enough ticks have passed
		FusionShrineRecipeWorldEffect effect = recipe.value().getWorldEffectForTick(fusionShrineBlockEntity.craftingTime, fusionShrineBlockEntity.craftingTimeTotal);
		if (fusionShrineBlockEntity.craftingTime == fusionShrineBlockEntity.craftingTimeTotal) {
			craft(world, blockPos, fusionShrineBlockEntity, recipe);
			fusionShrineBlockEntity.inventoryChanged();
		} else {
			PlayFusionCraftingInProgressParticlePayload.sendPlayFusionCraftingInProgressParticles((ServerLevel) world, blockPos);
		}
		
		// play the current crafting effect
		if (effect != null) {
			effect.trigger((ServerLevel) world, blockPos);
		}
		
		fusionShrineBlockEntity.setChanged();
	}
	
	@Nullable
	private static RecipeHolder<FusionShrineRecipe> calculateRecipe(@NotNull Level world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if (fusionShrineBlockEntity.currentRecipe != null) {
			if (fusionShrineBlockEntity.currentRecipe.value().matches(fusionShrineBlockEntity.getRecipeInput(), world)) {
				return fusionShrineBlockEntity.currentRecipe;
			}
		}
		return world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.FUSION_SHRINE, fusionShrineBlockEntity.getRecipeInput(), world).orElse(null);
	}
	
	private static void craft(Level world, BlockPos blockPos, FusionShrineBlockEntity fusionShrineBlockEntity, RecipeHolder<FusionShrineRecipe> recipe) {
		recipe.value().craft(world, fusionShrineBlockEntity);
		
		if (recipe.value().shouldPlayCraftingFinishedEffects()) {
			PlayFusionCraftingFinishedParticlePayload.sendPlayFusionCraftingFinishedParticles(world, blockPos, recipe.value().assemble(fusionShrineBlockEntity.getRecipeInput(), world.registryAccess()));
			fusionShrineBlockEntity.playSound(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING_FINISHED, 1.4F);
		}
		
		scatterContents(world, blockPos.above(), fusionShrineBlockEntity); // drop remaining items
		
		fusionShrineBlockEntity.fluidStorage.variant = FluidVariant.blank();
		fusionShrineBlockEntity.fluidStorage.amount = 0;
		world.setBlock(blockPos, world.getBlockState(blockPos).setValue(FusionShrineBlock.LIGHT_LEVEL, 0), 3);
	}
	
	@Override
	public UpgradeHolder getUpgradeHolder() {
		return upgrades;
	}
	
	@Override
	public List<Vec3i> getUpgradePosOffsets() {
		return FusionShrineBlock.UPGRADE_BLOCK_OFFSETS;
	}
	
	public static void scatterContents(Level world, BlockPos pos, FusionShrineBlockEntity blockEntity) {
		Containers.dropContents(world, pos, blockEntity.getItems());
		world.updateNeighbourForOutputSignal(pos, world.getBlockState(pos).getBlock());
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.fluidStorage.variant = FluidVariant.CODEC.decode(NbtOps.INSTANCE, nbt.getCompound("FluidVariant")).result().map(Pair::getFirst).orElse(FluidVariant.blank());
		this.fluidStorage.amount = nbt.getLong("FluidAmount");
		
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		
		this.currentRecipe = null;
		this.currentRecipe = MultiblockCrafter.getRecipeHolderFromNbt(level, nbt, FusionShrineRecipe.class);
		
		if (nbt.contains("Upgrades", Tag.TAG_LIST)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", Tag.TAG_COMPOUND));
		} else {
			this.upgrades = new UpgradeHolder();
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.fluidStorage.variant).result().ifPresent(v -> nbt.put("FluidVariant", v));
		nbt.putLong("FluidAmount", this.fluidStorage.amount);
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		if (this.upgrades != null) {
			nbt.put("Upgrades", this.upgrades.toNbt());
		}
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.id().toString());
		}
	}
	
	public void playSound(SoundEvent soundEvent, float volume) {
		if (level != null) {
			RandomSource random = level.random;
			level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), soundEvent, SoundSource.BLOCKS, volume, 0.9F + random.nextFloat() * 0.15F);
		}
	}
	
	public void grantPlayerFusionCraftingAdvancement(ItemStack stack, int experience) {
		ServerPlayer serverPlayerEntity = (ServerPlayer) getOwnerIfOnline();
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.FUSION_SHRINE_CRAFTING.trigger(serverPlayerEntity, stack, experience);
		}
	}
	
	public @NotNull FluidVariant getFluidVariant() {
		if (this.fluidStorage.amount > 0) {
			return this.fluidStorage.variant;
		} else {
			return FluidVariant.blank();
		}
	}
	
	public @NotNull SingleVariantStorage<FluidVariant> getFluidStorage() {
		return this.fluidStorage;
	}
	
	private void setLightForFluid(BlockPos blockPos, Fluid fluid) {
		if (level == null) return;
		int fluidLight = SpectrumEventListeners.getFluidLuminance(fluid);
		level.setBlock(blockPos, level.getBlockState(blockPos).setValue(FusionShrineBlock.LIGHT_LEVEL, fluidLight), Block.UPDATE_ALL);
	}
	
	public StorageRecipeInput<SingleVariantStorage<FluidVariant>> getRecipeInput() {
		return new StorageRecipeInput<>(items, fluidStorage);
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
		setChanged();
	}
	
	// UPGRADEABLE
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.setChanged();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods4(level, worldPosition, 2, 0, this.ownerUUID);
		this.setChanged();
	}
	
	@Override
	public void inventoryChanged() {
		super.inventoryChanged();
		this.inventoryChanged = true;
		this.craftingTime = 0;
	}
	
}
