package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.color.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
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

public class FusionShrineBlockEntity extends InWorldInteractionBlockEntity implements PlayerOwned, Upgradeable {

    protected static final int INVENTORY_SIZE = 7;

    private UUID ownerUUID;
    private UpgradeHolder upgrades;
    private FusionShrineRecipe currentRecipe;
    private int craftingTime;
    private int craftingTimeTotal;

    private boolean inventoryChanged = true;

    public SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
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
            setLightForFluid(world, pos, this.variant.getFluid());
            inventoryChanged();
            markDirty();
        }
    };

    public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.FUSION_SHRINE, pos, state, INVENTORY_SIZE);
    }

    public static void clientTick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
        if (!fusionShrineBlockEntity.isEmpty()) {
            int randomSlot = world.getRandom().nextInt(fusionShrineBlockEntity.size());
            ItemStack randomStack = fusionShrineBlockEntity.getStack(randomSlot);
            if (!randomStack.isEmpty()) {
				Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(randomStack.getItem());
				if (optionalItemColor.isPresent()) {
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
	}
	
	public void spawnCraftingParticles() {
		BlockPos blockPos = getPos();
		FusionShrineRecipe recipe = this.currentRecipe;
		if (recipe != null) {
			Fluid fluid = this.getFluidVariant().getFluid();
			Optional<DyeColor> optionalFluidColor = ColorRegistry.FLUID_COLORS.getMapping(fluid);
			if (optionalFluidColor.isPresent()) {
				ParticleEffect particleEffect = SpectrumParticleTypes.getFluidRisingParticle(optionalFluidColor.get());
				
				float randomX = 0.1F + world.getRandom().nextFloat() * 0.8F;
				float randomZ = 0.1F + world.getRandom().nextFloat() * 0.8F;
				world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY() + 1, blockPos.getZ() + randomZ, 0.0D, 0.1D, 0.0D);
			}
		}
	}
	
	public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if (fusionShrineBlockEntity.upgrades == null) {
			fusionShrineBlockEntity.calculateUpgrades();
		}
		
		if (fusionShrineBlockEntity.inventoryChanged) {
			FusionShrineRecipe previousRecipe = fusionShrineBlockEntity.currentRecipe;
			fusionShrineBlockEntity.currentRecipe = calculateRecipe(world, fusionShrineBlockEntity);
			
			if (fusionShrineBlockEntity.currentRecipe != previousRecipe) {
				fusionShrineBlockEntity.craftingTime = 0;
				if (fusionShrineBlockEntity.currentRecipe == null) {
					SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) world, fusionShrineBlockEntity.pos);
				} else {
					fusionShrineBlockEntity.craftingTimeTotal = (int) Math.ceil(fusionShrineBlockEntity.currentRecipe.getCraftingTime() / fusionShrineBlockEntity.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
				}
				
				fusionShrineBlockEntity.updateInClientWorld();
			}
			
			fusionShrineBlockEntity.inventoryChanged = false;
		}
		
		FusionShrineRecipe recipe = fusionShrineBlockEntity.currentRecipe;
		if (recipe == null || !recipe.getFluidInput().equals(fusionShrineBlockEntity.fluidStorage.variant.getFluid())) {
			return;
		}
		
		// check the crafting conditions from time to time
		// good for performance because of the many checks
		if (fusionShrineBlockEntity.craftingTime % 60 == 0) {
			PlayerEntity lastInteractedPlayer = fusionShrineBlockEntity.getOwnerIfOnline();
			
			boolean recipeConditionsMet = recipe.canPlayerCraft(lastInteractedPlayer) && recipe.areConditionMetCurrently((ServerWorld) world, blockPos);
			boolean structureCompleteWithSky = FusionShrineBlock.verifyStructureWithSkyAccess(world, blockPos, null);
			
			if (!recipeConditionsMet || !structureCompleteWithSky) {
				if (!structureCompleteWithSky) {
					SpectrumS2CPacketSender.playParticleWithExactVelocity((ServerWorld) world, Vec3d.ofCenter(blockPos), SpectrumParticleTypes.RED_CRAFTING, 1, new Vec3d(0, -0.5, 0));
					world.playSound(null, fusionShrineBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.9F + world.random.nextFloat() * 0.2F);
					world.playSound(null, fusionShrineBlockEntity.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + world.random.nextFloat() * 0.2F, 0.5F + world.random.nextFloat() * 0.2F);
					FusionShrineBlock.scatterContents(world, blockPos);
					fusionShrineBlockEntity.inventoryChanged();
				}
				fusionShrineBlockEntity.craftingTime = 0;
				return;
			}
		}
		
		// advance crafting
		++fusionShrineBlockEntity.craftingTime;
		
		if (fusionShrineBlockEntity.craftingTime == 1 && fusionShrineBlockEntity.craftingTimeTotal > 1) {
			SpectrumS2CPacketSender.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING, (ServerWorld) world, fusionShrineBlockEntity.getPos(), fusionShrineBlockEntity.craftingTimeTotal - fusionShrineBlockEntity.craftingTime);
		}
		
		// play the current crafting effect
		FusionShrineRecipeWorldEffect effect = recipe.getWorldEffectForTick(fusionShrineBlockEntity.craftingTime, fusionShrineBlockEntity.craftingTimeTotal);
		if (effect != null) {
			effect.trigger((ServerWorld) world, blockPos);
		}
		
		// craft when enough ticks have passed
		if (fusionShrineBlockEntity.craftingTime == fusionShrineBlockEntity.craftingTimeTotal) {
			craft(world, blockPos, fusionShrineBlockEntity, recipe);
			fusionShrineBlockEntity.inventoryChanged();
		} else {
			SpectrumS2CPacketSender.sendPlayFusionCraftingInProgressParticles(world, blockPos);
		}
		fusionShrineBlockEntity.markDirty();
	}
	
	@Nullable
	private static FusionShrineRecipe calculateRecipe(@NotNull World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		if (fusionShrineBlockEntity.currentRecipe != null) {
			if (fusionShrineBlockEntity.currentRecipe.matches(fusionShrineBlockEntity, world)) {
				return fusionShrineBlockEntity.currentRecipe;
			}
		}
		return world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.FUSION_SHRINE, fusionShrineBlockEntity, world).orElse(null);
	}
	
	// calculate the max amount of items that will be crafted
	// note that we only check each ingredient once, if a match was found
	// custom recipes therefore should not use items / tags that match multiple items
	// at once, since we can not rely on positions in a grid like vanilla does
	// in its crafting table
	private static void craft(World world, BlockPos blockPos, FusionShrineBlockEntity fusionShrineBlockEntity, FusionShrineRecipe recipe) {
		recipe.craft(world, fusionShrineBlockEntity);
		
		if (recipe.shouldPlayCraftingFinishedEffects()) {
			SpectrumS2CPacketSender.sendPlayFusionCraftingFinishedParticles(world, blockPos, recipe.getOutput());
			fusionShrineBlockEntity.playSound(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING_FINISHED, 1.4F);
		}
		
		scatterContents(world, blockPos.up(), fusionShrineBlockEntity); // drop remaining items
		
		fusionShrineBlockEntity.fluidStorage.variant = FluidVariant.blank();
		fusionShrineBlockEntity.fluidStorage.amount = 0;
		world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, 0), 3);
		
	}

	@Override
	public UpgradeHolder getUpgradeHolder() {
		return upgrades;
	}

	public static void scatterContents(World world, BlockPos pos, FusionShrineBlockEntity blockEntity) {
		ItemScatterer.spawn(world, pos, blockEntity.getItems());
		world.updateComparators(pos, world.getBlockState(pos).getBlock());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound("FluidVariant"));
        this.fluidStorage.amount = nbt.getLong("FluidAmount");

        this.craftingTime = nbt.getShort("CraftingTime");
        this.craftingTimeTotal = nbt.getShort("CraftingTimeTotal");
        if (nbt.contains("OwnerUUID")) {
            this.ownerUUID = nbt.getUuid("OwnerUUID");
        } else {
            this.ownerUUID = null;
        }

        this.currentRecipe = null;
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty() && SpectrumCommon.minecraftServer != null) {
				Optional<? extends Recipe> optionalRecipe = SpectrumCommon.minecraftServer.getRecipeManager().get(new Identifier(recipeString));
				if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof FusionShrineRecipe optionalFusionRecipe) {
					this.currentRecipe = optionalFusionRecipe;
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
        nbt.put("FluidVariant", this.fluidStorage.variant.toNbt());
        nbt.putLong("FluidAmount", this.fluidStorage.amount);
        nbt.putShort("CraftingTime", (short) this.craftingTime);
        nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
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
	
	public void playSound(SoundEvent soundEvent, float volume) {
		Random random = world.random;
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, volume, 0.9F + random.nextFloat() * 0.15F);
    }

    public void grantPlayerFusionCraftingAdvancement(FusionShrineRecipe recipe, int experience) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) getOwnerIfOnline();
        if (serverPlayerEntity != null) {
            SpectrumAdvancementCriteria.FUSION_SHRINE_CRAFTING.trigger(serverPlayerEntity, recipe.getOutput(), experience);
        }
    }

    public @NotNull FluidVariant getFluidVariant() {
        if (this.fluidStorage.amount > 0) {
            return this.fluidStorage.variant;
        } else {
            return FluidVariant.blank();
        }
    }

    private void setLightForFluid(World world, BlockPos blockPos, Fluid fluid) {
        if (SpectrumCommon.fluidLuminance.containsKey(fluid)) {
            int light = SpectrumCommon.fluidLuminance.get(fluid);
            world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, light), 3);
        } else {
            world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, 0), 3);
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
		this.markDirty();
	}
	
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods4(world, pos, 2, 0, this.ownerUUID);
		this.markDirty();
	}
	
	public void inventoryChanged() {
		super.inventoryChanged();
		this.inventoryChanged = true;
		this.craftingTime = 0;
	}
	
}