package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IncandescentAmalgamItem extends BlockItem implements DamageAwareItem {
    
    public IncandescentAmalgamItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        stack = super.finishUsing(stack, world, user);

        user.damage(SpectrumDamageSources.INCANDESCENCE, 500.0F);
        world.createExplosion(user, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), 2.0F, false, Explosion.DestructionType.DESTROY);
        world.createExplosion(user, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), 10.0F, true, Explosion.DestructionType.NONE);

        if(user.isAlive() && user instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.isCreative()) {
            Support.grantAdvancementCriterion(serverPlayerEntity, "midgame/survive_drinking_incandescent_amalgam", "survived_drinking_incandescent_amalgam");
        }
        
        return stack;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip"));
    }
    
    @Override
    public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
        // remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
        itemEntity.remove(Entity.RemovalReason.KILLED);
        
        int stackCount = itemEntity.getStack().getCount();
        itemEntity.world.createExplosion(itemEntity, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1.0F + stackCount / 16F, false, Explosion.DestructionType.DESTROY);
        itemEntity.world.createExplosion(itemEntity, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 8.0F + stackCount / 8F, true, Explosion.DestructionType.NONE);
    }
    
}
