package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class FusionShrineBlockEntity extends BlockEntity implements RecipeInputProvider, PlayerOwned, Upgradeable {

	private UUID ownerUUID;
	private Map<Upgradeable.UpgradeType, Double> upgrades;
	
	protected int INVENTORY_SIZE = 8;
	protected SimpleInventory inventory;
	protected @NotNull Fluid storedFluid;
	
	private FusionShrineRecipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;

	public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.FUSION_SHRINE, pos, state);
		
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.storedFluid = Fluids.EMPTY;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		this.storedFluid = Registry.FLUID.get(Identifier.tryParse(nbt.getString("fluid")));
		this.craftingTime = nbt.getShort("CraftingTime");
		this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
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

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
		nbt.putString("fluid", Registry.FLUID.getId(this.storedFluid).toString());
		nbt.putShort("CraftingTime", (short)this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short)this.craftingTimeTotal);
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
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
		Inventory inventory = fusionShrineBlockEntity.getInventory();
		if(!inventory.isEmpty()) {
			int randomSlot = fusionShrineBlockEntity.world.getRandom().nextInt(inventory.size());
			ItemStack randomStack = inventory.getStack(randomSlot);
			if(!randomStack.isEmpty()) {
				Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(randomStack.getItem());
				if(optionalItemColor.isPresent()) {
					ParticleEffect particleEffect = SpectrumParticleTypes.getCraftingParticle(optionalItemColor.get());
					
					int particleAmount = (int) StrictMath.ceil(randomStack.getCount() / 8.0F);
					for (int i = 0; i < particleAmount; i++) {
						float randomX = 3.0F - world.getRandom().nextFloat() * 7;
						float randomZ = 3.0F - world.getRandom().nextFloat() * 7;
						world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		}
		FusionShrineRecipe recipe = fusionShrineBlockEntity.currentRecipe;
		if(recipe != null) {
			Fluid fluid = fusionShrineBlockEntity.getFluid();
			if (fluid != Fluids.EMPTY && recipe.areConditionMetCurrently(world)) {
				Optional<DyeColor> optionalFluidColor = ColorRegistry.FLUID_COLORS.getMapping(fluid);
				if (optionalFluidColor.isPresent()) {
					ParticleEffect particleEffect = SpectrumParticleTypes.getFluidRisingParticle(optionalFluidColor.get());
					
					float randomX = 0.25F + world.getRandom().nextFloat() * 0.5F;
					float randomZ = 0.25F + world.getRandom().nextFloat() * 0.5F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY() + 1, blockPos.getZ() + randomZ, 0.0D, 0.1D, 0.0D);
				}
			}
		}
	}

	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if(fusionShrineBlockEntity.upgrades == null) {
			fusionShrineBlockEntity.calculateUpgrades();
		}
		
		FusionShrineRecipe recipe = getCurrentRecipe(world, fusionShrineBlockEntity);
		if(recipe != null && recipe.getFluidInput().equals(fusionShrineBlockEntity.storedFluid)) {
			// check the crafting conditions from time to time
			//  good for performance because of the many checks
			if(fusionShrineBlockEntity.craftingTime % 60 == 0) {
				PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(world, fusionShrineBlockEntity.ownerUUID);
				
				boolean recipeConditionsMet = Support.hasAdvancement(lastInteractedPlayer, recipe.getRequiredAdvancementIdentifier()) && recipe.areConditionMetCurrently(world);
				boolean structureCompleteWithSky = world.getBlockState(blockPos.up()).isAir() && world.isSkyVisible(blockPos) && FusionShrineBlock.verifyStructure(world, blockPos, null);
				
				if(!recipeConditionsMet || !structureCompleteWithSky) {
					if(!structureCompleteWithSky) {
						world.playSound(null, fusionShrineBlockEntity.getPos(), SpectrumSoundEvents.FUSION_SHRINE_CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + fusionShrineBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + fusionShrineBlockEntity.world.random.nextFloat() * 0.2F);
						world.playSound(null, fusionShrineBlockEntity.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + fusionShrineBlockEntity.world.random.nextFloat() * 0.2F, 0.5F + fusionShrineBlockEntity.world.random.nextFloat() * 0.2F);
						FusionShrineBlock.scatterContents(world, blockPos);
					}
					fusionShrineBlockEntity.craftingTime = 0;
					return;
				}
			}

			// advance crafting
			++fusionShrineBlockEntity.craftingTime;
			fusionShrineBlockEntity.markDirty();
			
			if(fusionShrineBlockEntity.craftingTime == 1 && fusionShrineBlockEntity.craftingTimeTotal > 1) {
				SpectrumS2CPackets.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING, (ServerWorld) fusionShrineBlockEntity.world, fusionShrineBlockEntity.getPos(), fusionShrineBlockEntity.craftingTimeTotal - fusionShrineBlockEntity.craftingTime);
			}

			// play the current crafting effect
			FusionShrineRecipeWorldEffect effect = recipe.getWorldEffectForTick(fusionShrineBlockEntity.craftingTime);
			if(effect != null) {
				effect.doEffect((ServerWorld) world, blockPos);
			}

			// craft when enough ticks have passed
			if(fusionShrineBlockEntity.craftingTime == fusionShrineBlockEntity.craftingTimeTotal) {
				craft(world, blockPos, fusionShrineBlockEntity, recipe);
			}
			fusionShrineBlockEntity.markDirty();
		} else {
			if(fusionShrineBlockEntity.craftingTime > 0) {
				fusionShrineBlockEntity.craftingTime = 0;
				fusionShrineBlockEntity.markDirty();
			}
		}
	}
	
	private static FusionShrineRecipe getCurrentRecipe(@NotNull World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if(fusionShrineBlockEntity.currentRecipe != null) {
			if(fusionShrineBlockEntity.currentRecipe.matches(fusionShrineBlockEntity.inventory, world)) {
				return fusionShrineBlockEntity.currentRecipe;
			}
		}
		
		FusionShrineRecipe previousRecipe = fusionShrineBlockEntity.currentRecipe;
		FusionShrineRecipe recipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.FUSION_SHRINE, fusionShrineBlockEntity.inventory, world).orElse(null);
		fusionShrineBlockEntity.craftingTime = 0;
		fusionShrineBlockEntity.currentRecipe = recipe;
		if(recipe == null) {
			SpectrumS2CPackets.sendCancelBlockBoundSoundInstance((ServerWorld) fusionShrineBlockEntity.world, fusionShrineBlockEntity.pos);
		} else {
			fusionShrineBlockEntity.craftingTimeTotal = (int) Math.ceil(recipe.getCraftingTime() / fusionShrineBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
		}
		
		if(fusionShrineBlockEntity.currentRecipe != previousRecipe) {
			fusionShrineBlockEntity.updateInClientWorld();
		}
		return recipe;
	}
	
	// calculate the max amount of items that will be crafted
	// note that we only check each ingredient once, if a match was found
	// custom recipes therefore should not use items / tags that match multiple items
	// at once, since we can not rely on positions in a grid like vanilla does
	// in its crafting table
	private static void craft(World world, BlockPos blockPos, FusionShrineBlockEntity fusionShrineBlockEntity, FusionShrineRecipe recipe) {
		int maxAmount = recipe.getOutput().getMaxCount();
		for(Ingredient ingredient : recipe.getIngredients()) {
			for(int i = 0; i < fusionShrineBlockEntity.INVENTORY_SIZE; i++) {
				ItemStack currentStack = fusionShrineBlockEntity.inventory.getStack(i);
				if(ingredient.test(currentStack)) {
					maxAmount = Math.min(maxAmount, currentStack.getCount());
					break;
				}
			}
		}
		
		double efficiencyModifier = fusionShrineBlockEntity.upgrades.get(UpgradeType.EFFICIENCY);
		if(maxAmount > 0) {
			for(Ingredient ingredient : recipe.getIngredients()) {
				for(int i = 0; i < fusionShrineBlockEntity.INVENTORY_SIZE; i++) {
					ItemStack currentStack = fusionShrineBlockEntity.inventory.getStack(i);
					if(ingredient.test(currentStack)) {
						int reducedAmount = Support.getIntFromDecimalWithChance(maxAmount / efficiencyModifier, fusionShrineBlockEntity.world.random);
						if(currentStack.getCount() - reducedAmount < 1) {
							fusionShrineBlockEntity.inventory.setStack(i, ItemStack.EMPTY);
						} else {
							currentStack.decrement(reducedAmount);
						}
						break;
					}
				}
			}

			fusionShrineBlockEntity.setFluid(Fluids.EMPTY); // empty the shrine
			spawnCraftingResultAndXP(world, fusionShrineBlockEntity, recipe, maxAmount); // spawn results
			scatterContents(world, blockPos.up(), fusionShrineBlockEntity); // drop remaining items
			
			SpectrumS2CPackets.sendPlayFusionCraftingFinishedParticles(world, blockPos, recipe.getOutput());
			fusionShrineBlockEntity.playSound(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING_FINISHED, 1.4F);

			//only triggered on server side. Therefore, has to be sent to client via S2C packet
			fusionShrineBlockEntity.grantPlayerFusionCraftingAdvancement(recipe);
		}
	}
	
	public void playSound(SoundEvent soundEvent, float volume) {
		Random random = world.random;
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, volume, 0.9F + random.nextFloat() * 0.15F);
	}
	
	private void grantPlayerFusionCraftingAdvancement(FusionShrineRecipe recipe) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(this.world, this.ownerUUID);
		if(serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.FUSION_SHRINE_CRAFTING.trigger(serverPlayerEntity, recipe.getOutput());
		}
	}

	public static void spawnCraftingResultAndXP(World world, FusionShrineBlockEntity blockEntity, FusionShrineRecipe recipe, int amount) {
		int resultAmountBeforeMod = amount * recipe.getOutput().getCount();
		double yieldModifier =  recipe.areYieldUpgradesDisabled() ? 1.0 : blockEntity.upgrades.get(UpgradeType.YIELD);
		int resultAmountAfterMod = Support.getIntFromDecimalWithChance(resultAmountBeforeMod * yieldModifier, blockEntity.world.random);
		
		while(resultAmountAfterMod > 0) {
			int currentAmount = Math.min(amount, recipe.getOutput().getMaxCount());

			ItemEntity itemEntity = new ItemEntity(world, blockEntity.pos.getX() + 0.5, blockEntity.pos.getY() + 1, blockEntity.pos.getZ() + 0.5, new ItemStack(recipe.getOutput().getItem(), currentAmount));
			itemEntity.setVelocity(0, 0.3, 0);
			world.spawnEntity(itemEntity);
			
			resultAmountAfterMod -= currentAmount;
		}

		if (recipe.getExperience() > 0) {
			double experienceModifier = blockEntity.upgrades.get(UpgradeType.EXPERIENCE);
			float recipeExperienceBeforeMod = recipe.getExperience();
			int spawnedXPAmount = Support.getIntFromDecimalWithChance(recipeExperienceBeforeMod * experienceModifier, blockEntity.world.random);
			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, blockEntity.pos.getX() + 0.5, blockEntity.pos.getY() + 1, blockEntity.pos.getZ() + 0.5, spawnedXPAmount);
			world.spawnEntity(experienceOrbEntity);
		}
	}

	public static void scatterContents(World world, BlockPos pos, FusionShrineBlockEntity blockEntity) {
		ItemScatterer.spawn(world, pos, blockEntity.inventory);
		world.updateComparators(pos, world.getBlockState(pos).getBlock());
	}

	public @NotNull Fluid getFluid() {
		return this.storedFluid;
	}

	public void setFluid(@NotNull Fluid fluid) {
		this.storedFluid = fluid;
		setLightForFluid(world, pos, fluid);
		this.markDirty();
		updateInClientWorld();
	}

	public void setLightForFluid(World world, BlockPos blockPos, Fluid fluid) {
		if(SpectrumCommon.fluidLuminance.containsKey(fluid)) {
			int light = SpectrumCommon.fluidLuminance.get(fluid);
			world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, light), 3);
		}
	}

	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}

	public Inventory getInventory() {
		return this.inventory;
	}
	
	// RECIPE INPUT PROVIDER
	@Override
	public void provideRecipeInputs(RecipeMatcher finder) {

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
	
	public void updateInClientWorld() {
		// update tile entity
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
		// update fluid (light updates automatically)
		//fusionShrineBlockEntity.world.updateListeners(fusionShrineBlockEntity.pos, fusionShrineBlockEntity.world.getBlockState(fusionShrineBlockEntity.pos), fusionShrineBlockEntity.world.getBlockState(fusionShrineBlockEntity.pos), Block.NO_REDRAW);
		//fusionShrineBlockEntity.world.addSyncedBlockEvent(fusionShrineBlockEntity.pos, fusionShrineBlockEntity.world.getBlockState(fusionShrineBlockEntity.pos).getBlock(), 1, 0);
	}
	
	// UPGRADEABLE
	public void resetUpgrades() {
		this.upgrades = null;
	}
	
	public void calculateUpgrades() {
		Pair<Integer, Map<Upgradeable.UpgradeType, Double>> upgrades = Upgradeable.checkUpgradeMods(world, pos, 2, 0);
		this.upgrades = upgrades.getRight();
	}
	
}