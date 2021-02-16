package de.dafuqs.pigment.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    @Final
    private static TrackedData<ItemStack> STACK;

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Inject(at=@At("HEAD"), method="Lnet/minecraft/entity/ItemEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    public void doAnvilCrafting(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(DamageSource.ANVIL.equals(source)) {
            ItemEntity thisEntity = (ItemEntity) (Object) this;
            ItemStack thisItemStack = thisEntity.getStack();
            Item thisItem = thisItemStack.getItem();

            RecipeType anvilCraftingRecipeType = Registry.RECIPE_TYPE.get(Identifier.tryParse("pigment_anvil_crushing"));
            // TODO: crafting
            /*if (thisItem instanceof AnvilCrushable) {
                // Item can be crafted via anvil. Do anvil crafting

                int itemStackAmount = thisEntity.getStack().getCount();
                int craftingInputAmount = Math.min(itemStackAmount, ((AnvilCrushable) thisItem).getAnvilCrushingAmount(amount));
                int craftingResultAmount = ((AnvilCrushable) thisItem).getAnvilCraftingResultAmount(craftingInputAmount);

                if(craftingResultAmount > 0) {
                    // Remove the input amount from the source stack
                    int remainingItemStackAmount = thisItemStack.getCount() - craftingInputAmount;
                    if (remainingItemStackAmount > 0) {
                        thisItemStack.setCount(thisItemStack.getCount() - craftingInputAmount);
                    } else {
                        thisEntity.remove(Entity.RemovalReason.DISCARDED);
                    }

                    // Spawn the resulting item stack in the world
                    World world = thisEntity.getEntityWorld();
                    Vec3d position = thisEntity.getPos();
                    Item craftedItem = ((AnvilCrushable) thisItem).getAnvilCrushingItem();
                    ItemStack craftedItemStack = new ItemStack(craftedItem, craftingResultAmount);
                    ItemEntity craftedEntity = new ItemEntity(world, position.x, position.y, position.z, craftedItemStack);
                    world.spawnEntity(craftedEntity);

                    // TODO: SEND SOUND AND PARTICLES TO CLIENTS
                    // Play sound
                    SoundEvent soundEvent = ((AnvilCrushable) thisItem).getCraftingSoundEvent();
                    float randomVolume = 0.9F + world.getRandom().nextFloat() * 0.2F;
                    float randomPitch = 0.9F + world.getRandom().nextFloat() * 0.2F;
                    world.playSound(position.x, position.y, position.z, soundEvent, SoundCategory.BLOCKS, randomVolume, randomPitch, false);

                    // Particle Effect
                    ParticleEffect particleType = ((AnvilCrushable) thisItem).getCraftingParticleType();
                    world.addParticle(particleType, position.x, position.y, position.z, 0.2, 0, 0);
                    world.addParticle(particleType, position.x, position.y, position.z, 0, 0, 0.2);
                    world.addParticle(particleType, position.x, position.y, position.z, -0.2, 0, 0);
                    world.addParticle(particleType, position.x, position.y, position.z, 0, 0, -0.2);
                }
            }*/
        }
    }


}
