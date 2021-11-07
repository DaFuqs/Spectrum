package de.dafuqs.spectrum.blocks.fusion_shrine;

import com.glisco.owo.Owo;
import com.glisco.owo.client.OwoClient;
import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import de.dafuqs.spectrum.registries.color.ItemColorRegistry;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.recipe.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class FusionShrineBlockEntity extends BlockEntity implements RecipeInputProvider, PlayerOwned, BlockEntityClientSerializable {

	private UUID ownerUUID;
	
	protected int INVENTORY_SIZE = 8;
	protected SimpleInventory inventory;
	protected @NotNull Fluid storedFluid;
	
	private FusionShrineRecipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;

	public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.FUSION_SHRINE, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		storedFluid = Fluids.EMPTY;
	}

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
				Optional<? extends Recipe> optionalRecipe;
				if (SpectrumClient.minecraftClient != null) {
					optionalRecipe = MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(recipeString));
				} else {
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
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
		nbt.putString("fluid", Registry.FLUID.getId(this.storedFluid).toString());
		nbt.putShort("CraftingTime", (short)this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short)this.craftingTimeTotal);
		if(this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if(this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
		return nbt;
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
						world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.01D, 0.0D);
					}
				}
			}
		}
		Recipe recipe = fusionShrineBlockEntity.currentRecipe;
		if(recipe != null) {
			Fluid fluid = fusionShrineBlockEntity.getFluid();
			if (fluid != Fluids.EMPTY) {
				Optional<DyeColor> optionalFluidColor = ColorRegistry.FLUID_COLORS.getMapping(fluid);
				if (optionalFluidColor.isPresent()) {
					ParticleEffect particleEffect = SpectrumParticleTypes.getRisingParticle(optionalFluidColor.get());
					
					float randomX = 0.25F + world.getRandom().nextFloat() * 0.5F;
					float randomZ = 0.25F + world.getRandom().nextFloat() * 0.5F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY() + 1, blockPos.getZ() + randomZ, 0.0D, 0.00D, 0.0D);
				}
			}
		}
	}

	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
		FusionShrineRecipe recipe = getCurrentRecipe(world, fusionShrineBlockEntity);
		if(recipe != null && recipe.getFluidInput().equals(fusionShrineBlockEntity.storedFluid)) {
			// check the crafting conditions from time to time
			// not that nice that crafting may start a whole
			// second later, but good for performance
			// because of the many checks
			if(fusionShrineBlockEntity.craftingTime % 20 == 0) {
				PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(world, fusionShrineBlockEntity.ownerUUID);
				if(!(lastInteractedPlayer != null
						&& (recipe.getRequiredAdvancementIdentifier() == null || Support.hasAdvancement(lastInteractedPlayer, recipe.getRequiredAdvancementIdentifier()))
						&& world.getBlockState(blockPos.up()).isAir()
						&& world.isSkyVisible(blockPos)
						&& recipe.areConditionMetCurrently((ServerWorld) world))) {

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

		FusionShrineRecipe recipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.FUSION_SHRINE, fusionShrineBlockEntity.inventory, world).orElse(null);
		fusionShrineBlockEntity.craftingTime = 0;
		fusionShrineBlockEntity.currentRecipe = recipe;
		if(recipe == null) {
			SpectrumS2CPackets.sendCancelBlockBoundSoundInstance((ServerWorld) fusionShrineBlockEntity.world, fusionShrineBlockEntity.pos);
		} else {
			fusionShrineBlockEntity.craftingTimeTotal = recipe.getCraftingTime();
		}
		
		fusionShrineBlockEntity.sync();
		return recipe;
	}

	private static void craft(World world, BlockPos blockPos, FusionShrineBlockEntity fusionShrineBlockEntity, FusionShrineRecipe recipe) {
		// calculate the max amount of items that will be crafted
		// note that we only check each ingredient once, if a match was found
		// custom recipes therefore should not use items / tags that match multiple items
		// at once, since we can not rely on positions in a grid like vanilla does
		// in it's crafting table
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

		if(maxAmount > 0) {
			for(Ingredient ingredient : recipe.getIngredients()) {
				for(int i = 0; i < fusionShrineBlockEntity.INVENTORY_SIZE; i++) {
					ItemStack currentStack = fusionShrineBlockEntity.inventory.getStack(i);
					if(ingredient.test(currentStack)) {
						if(currentStack.getCount() - maxAmount < 1) {
							fusionShrineBlockEntity.inventory.setStack(i, ItemStack.EMPTY);
						} else {
							currentStack.decrement(maxAmount);
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
		int resultAmount = amount * recipe.getOutput().getCount();
		while(resultAmount > 0) {
			int currentAmount = Math.min(amount, recipe.getOutput().getMaxCount());

			ItemEntity itemEntity = new ItemEntity(world, blockEntity.pos.getX() + 0.5, blockEntity.pos.getY() + 1, blockEntity.pos.getZ() + 0.5, new ItemStack(recipe.getOutput().getItem(), currentAmount));
			itemEntity.setVelocity(0, 0.3, 0);
			world.spawnEntity(itemEntity);

			resultAmount -= currentAmount;
		}

		if (recipe.getExperience() > 0) {
			int spawnedXPAmount = Support.getWholeIntFromFloatWithChance(recipe.getExperience(), blockEntity.world.random);
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

	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NOTIFY_LISTENERS);
	}

	public void setLightForFluid(World world, BlockPos blockPos, Fluid fluid) {
		if(SpectrumCommon.fluidLuminance.containsKey(fluid)) {
			int light = SpectrumCommon.fluidLuminance.get(fluid);
			world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, light), 3);
		}
	}

	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		return this.writeNbt(new NbtCompound());
	}

	// when marked dirty this is called to send updates to clients
	// see also MobSpawnerBlockEntity for a vanilla version of this
	@Nullable
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		SpectrumS2CPackets.sendBlockEntityUpdate(this, SpectrumS2CPackets.BlockEntityUpdatePacketID.FUSION_SHRINE);
		return null;
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
	// is always set to the last player interacting with
	// the fusion shrine to trigger advancements

	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
	}
	
	// BLOCKENTITYCLIENTSERIALIZABLE
	@Override
	public void fromClientTag(NbtCompound tag) {
		this.readNbt(tag);
	}
	
	@Override
	public NbtCompound toClientTag(NbtCompound tag) {
		return this.toInitialChunkDataNbt();
	}
	
}