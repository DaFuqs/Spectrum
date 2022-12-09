package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.InWorldInteractionBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FusionShrineBlockEntity extends InWorldInteractionBlockEntity implements PlayerOwned, Upgradeable {
	
	protected static final int INVENTORY_SIZE = 7;
	
	protected @NotNull Fluid storedFluid;
	private UUID ownerUUID;
	private Map<Upgradeable.UpgradeType, Float> upgrades;
	private FusionShrineRecipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	
	private boolean inventoryChanged = true;
	
	public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.FUSION_SHRINE, pos, state, INVENTORY_SIZE);
		this.storedFluid = Fluids.EMPTY;
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
		FusionShrineRecipe recipe = fusionShrineBlockEntity.currentRecipe;
		if (recipe != null) {
			Fluid fluid = fusionShrineBlockEntity.getFluid();
			if (recipe.getFluidInput().equals(fluid) && recipe.areConditionMetCurrently(world)) {
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
					fusionShrineBlockEntity.craftingTimeTotal = (int) Math.ceil(fusionShrineBlockEntity.currentRecipe.getCraftingTime() / fusionShrineBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
				}
				
				fusionShrineBlockEntity.updateInClientWorld();
			}
			
			fusionShrineBlockEntity.inventoryChanged = false;
		}
		
		FusionShrineRecipe recipe = fusionShrineBlockEntity.currentRecipe;
		if (recipe != null && recipe.getFluidInput().equals(fusionShrineBlockEntity.storedFluid)) {
			// check the crafting conditions from time to time
			// good for performance because of the many checks
			if (fusionShrineBlockEntity.craftingTime % 60 == 0) {
				PlayerEntity lastInteractedPlayer = fusionShrineBlockEntity.getOwnerIfOnline();
				
				boolean recipeConditionsMet = recipe.canPlayerCraft(lastInteractedPlayer) && recipe.areConditionMetCurrently(world);
				boolean structureCompleteWithSky = FusionShrineBlock.verifyStructureWithSkyAccess(world, blockPos, null);
				
				if (!recipeConditionsMet || !structureCompleteWithSky) {
					if (!structureCompleteWithSky) {
						SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity((ServerWorld) world, Vec3d.ofCenter(blockPos), SpectrumParticleTypes.RED_CRAFTING, 1, Vec3d.ZERO, new Vec3d(0, -0.5, 0));
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
				effect.doEffect((ServerWorld) world, blockPos);
			}
			
			// craft when enough ticks have passed
			if (fusionShrineBlockEntity.craftingTime == fusionShrineBlockEntity.craftingTimeTotal) {
				craft(world, blockPos, fusionShrineBlockEntity, recipe);
				fusionShrineBlockEntity.inventoryChanged();
			}
			fusionShrineBlockEntity.markDirty();
		}
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
		fusionShrineBlockEntity.setFluid(Fluids.EMPTY); // empty the shrine
		scatterContents(world, blockPos.up(), fusionShrineBlockEntity); // drop remaining items
		
		SpectrumS2CPacketSender.sendPlayFusionCraftingFinishedParticles(world, blockPos, recipe.getOutput());
		fusionShrineBlockEntity.playSound(SpectrumSoundEvents.FUSION_SHRINE_CRAFTING_FINISHED, 1.4F);
	}
	
	public Map<UpgradeType, Float> getUpgrades() {
		return this.upgrades;
	}
	
	public static void scatterContents(World world, BlockPos pos, FusionShrineBlockEntity blockEntity) {
		ItemScatterer.spawn(world, pos, blockEntity.getItems());
		world.updateComparators(pos, world.getBlockState(pos).getBlock());
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.storedFluid = Registry.FLUID.get(Identifier.tryParse(nbt.getString("fluid")));
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
			this.upgrades = Upgradeable.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putString("fluid", Registry.FLUID.getId(this.storedFluid).toString());
		nbt.putShort("CraftingTime", (short) this.craftingTime);
		nbt.putShort("CraftingTimeTotal", (short) this.craftingTimeTotal);
		if (this.upgrades != null) {
			nbt.put("Upgrades", Upgradeable.toNbt(this.upgrades));
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
	
	public @NotNull Fluid getFluid() {
		return this.storedFluid;
	}
	
	public void setFluid(@NotNull Fluid fluid) {
		this.storedFluid = fluid;
		setLightForFluid(world, pos, fluid);
		inventoryChanged();
	}
	
	public void setLightForFluid(World world, BlockPos blockPos, Fluid fluid) {
		if (SpectrumCommon.fluidLuminance.containsKey(fluid)) {
			int light = SpectrumCommon.fluidLuminance.get(fluid);
			world.setBlockState(blockPos, world.getBlockState(blockPos).with(FusionShrineBlock.LIGHT_LEVEL, light), 3);
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
	
	@Override
	public float getUpgradeValue(UpgradeType upgradeType) {
		return this.upgrades.get(upgradeType);
	}
	
	public void inventoryChanged() {
		super.inventoryChanged();
		this.inventoryChanged = true;
		this.craftingTime = 0;
	}
	
}