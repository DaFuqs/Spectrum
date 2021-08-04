package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.interfaces.GravitableItem;
import de.dafuqs.spectrum.inventories.AutoCompactingInventory;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.registries.SpectrumItemStackDamageImmunities;
import de.dafuqs.spectrum.registries.SpectrumPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(at=@At("HEAD"), method= "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
    public void doAnvilCrafting(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(DamageSource.ANVIL.equals(source)) {
            ItemEntity thisEntity = (ItemEntity) (Object) this;
            ItemStack thisItemStack = thisEntity.getStack();
            World world = thisEntity.getEntityWorld();

            AutoCompactingInventory autoCompactingInventory = new AutoCompactingInventory();
            autoCompactingInventory.setCompacting(AutoCompactingInventory.AutoCraftingMode.OnexOne, thisItemStack);

            Optional<AnvilCrushingRecipe> optionalAnvilCrushingRecipe = world.getServer().getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ANVIL_CRUSHING, autoCompactingInventory, world);
            if(optionalAnvilCrushingRecipe.isPresent()) {
                // Item can be crafted via anvil. Do anvil crafting
                AnvilCrushingRecipe recipe = optionalAnvilCrushingRecipe.get();

                int itemStackAmount = thisEntity.getStack().getCount();
                int crushingInputAmount = Math.min (itemStackAmount, (int) (recipe.getCrushedItemsPerPointOfDamage() * amount));

                if(crushingInputAmount > 0) {
                    Vec3d position = thisEntity.getPos();

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
                    ItemEntity craftedEntity = new ItemEntity(world, position.x, position.y, position.z, crushingOutput);
                    world.spawnEntity(craftedEntity);

                    // Spawn XP depending on how much is crafted, but at least 1
                    float craftingXPFloat = recipe.getExperience() * crushingInputAmount;
                    int craftingXP = Support.getWholeIntFromFloatWithChance(craftingXPFloat, world.random);

                    ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, position.x, position.y, position.z, craftingXP);
                    world.spawnEntity(experienceOrbEntity);

                    // Play sound
                    SoundEvent soundEvent = recipe.getSoundEvent();
                    float randomVolume = 1.0F + world.getRandom().nextFloat() * 0.2F;
                    float randomPitch = 0.9F + world.getRandom().nextFloat() * 0.2F;
                    world.playSound(null, position.x, position.y, position.z, soundEvent, SoundCategory.PLAYERS, randomVolume, randomPitch);

                    // Particle Effect
                    ParticleEffect particleEffect = ParticleTypes.EXPLOSION; // recipe.getParticleEffect();
                    world.addParticle(particleEffect, position.x, position.y, position.z, 0, 0, 0);

                    PacketByteBuf buf = PacketByteBufs.create();
                    BlockPos particleBlockPos = new BlockPos(position.x, position.y, position.z);
                    buf.writeBlockPos(particleBlockPos);
                    // Iterate over all players tracking a position in the world and send the packet to each player
                    for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, particleBlockPos)) {
                        ServerPlayNetworking.send(player, SpectrumPackets.PLAY_PARTICLE_PACKET_ID, buf);
                    }

                }

                // prevent the source itemStack taking damage.
                // ItemEntities have a health of 5 and can actually get killed by a falling anvil
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(method= "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at=@At("HEAD"), cancellable = true)
    private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ItemStack itemStack = ((ItemEntity)(Object) this).getStack();

        if(itemStack != ItemStack.EMPTY) {
            boolean isImmune = SpectrumItemStackDamageImmunities.isDamageImmune(itemStack.getItem(), source);
            if(isImmune) {
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }


    @Inject(method= "tick()V", at=@At("TAIL"))
    public void tick(CallbackInfo ci) {
        ItemEntity itemEntity = ((ItemEntity)(Object) this);
        Item item = itemEntity.getStack().getItem();
        if(item instanceof GravitableItem) {
            // if the stack if floating really high => delete it
            if(itemEntity.getPos().getY() > itemEntity.getEntityWorld().getTopY() + 200) {
                itemEntity.discard();
            } else {
                double mod = ((GravitableItem) item).getGravityModForItemEntity();
                itemEntity.addVelocity(0, mod, 0);
            }
        }
    }

}
