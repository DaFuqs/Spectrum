package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.PigmentItemStackDamageImmunities;
import de.dafuqs.pigment.Support;
import de.dafuqs.pigment.inventories.AutoCompactingInventory;
import de.dafuqs.pigment.inventories.AutoInventory;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import de.dafuqs.pigment.recipe.anvil_crushing.AnvilCrushingRecipe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    @Final
    private static TrackedData<ItemStack> STACK;

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Inject(at=@At("HEAD"), method="Lnet/minecraft/entity/ItemEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
    public void doAnvilCrafting(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(DamageSource.ANVIL.equals(source)) {
            ItemEntity thisEntity = (ItemEntity) (Object) this;
            ItemStack thisItemStack = thisEntity.getStack();
            World world = thisEntity.getEntityWorld();

            AutoCompactingInventory autoCompactingInventory = new AutoCompactingInventory();
            autoCompactingInventory.setCompacting(AutoCompactingInventory.AutoCraftingMode.OnexOne, thisItemStack);

            Optional<AnvilCrushingRecipe> optionalAnvilCrushingRecipe = world.getServer().getRecipeManager().getFirstMatch(PigmentRecipeTypes.ANVIL_CRUSHING, autoCompactingInventory, world);
            if(optionalAnvilCrushingRecipe.isPresent()) {
                // Item can be crafted via anvil. Do anvil crafting
                AnvilCrushingRecipe recipe = optionalAnvilCrushingRecipe.get();

                int itemStackAmount = thisEntity.getStack().getCount();
                int crushingInputAmount = Math.min (itemStackAmount, (int) (recipe.getCrushedItemsPerPointOfDamage() * amount));

                if(crushingInputAmount > 0) {
                    ItemStack crushingOutput = recipe.getOutputItemStack();
                    crushingOutput.setCount(crushingOutput.getCount() * crushingInputAmount);

                    // Remove the input amount from the source stack
                    // Or the source stack altogether if it would be empty
                    int remainingItemStackAmount = itemStackAmount - crushingInputAmount;
                    if (remainingItemStackAmount > 0) {
                        thisItemStack.setCount(remainingItemStackAmount);
                    } else {
                        thisEntity.remove(Entity.RemovalReason.DISCARDED);
                    }

                    // Spawn the resulting item stack in the world
                    Vec3d position = thisEntity.getPos();
                    ItemEntity craftedEntity = new ItemEntity(world, position.x, position.y, position.z, crushingOutput);
                    world.spawnEntity(craftedEntity);

                    // Spawn XP depending on how much is crafted, but at least 1
                    float craftingXPFloat = recipe.getExperience() * crushingInputAmount;
                    int craftingXP = Support.getWholeIntFromFloatWithChance(craftingXPFloat, world.random);

                    ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, position.x, position.y, position.z, craftingXP);
                    world.spawnEntity(experienceOrbEntity);

                    // TODO: SEND SOUND AND PARTICLES TO CLIENTS
                    // Play sound
                    SoundEvent soundEvent = recipe.getSoundEvent();
                    float randomVolume = 0.9F + world.getRandom().nextFloat() * 0.2F;
                    float randomPitch = 0.9F + world.getRandom().nextFloat() * 0.2F;
                    world.playSound(position.x, position.y, position.z, soundEvent, SoundCategory.BLOCKS, randomVolume, randomPitch, false);

                    // Particle Effect
                    ParticleEffect particleEffect = recipe.getParticleEffect();
                    world.addParticle(particleEffect, position.x, position.y, position.z, 0, 0, 0);
                }

                // prevent the source itemStack taking damage.
                // ItemEntities have a health of 5 and can actually get killed by a falling anvil
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(method="Lnet/minecraft/entity/ItemEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at=@At("HEAD"), cancellable = true)
    private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ItemStack itemStack = ((ItemEntity)(Object) this).getStack();

        if(itemStack != ItemStack.EMPTY) {
            boolean isImmune = PigmentItemStackDamageImmunities.isDamageImmune(itemStack.getItem(), source);
            if(isImmune) {
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }


}
