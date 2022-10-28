package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.ItemWithTooltip;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;

public class IncandescentAmalgamItem extends ItemWithTooltip {

    public IncandescentAmalgamItem(Settings settings, String tooltip) {
        super(settings, tooltip);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        stack = super.finishUsing(stack, world, user);

        user.damage(SpectrumDamageSources.INCANDESCENCE, 500f);
        world.createExplosion(user, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), 12f, true, Explosion.DestructionType.NONE);

        return stack;
    }
}
