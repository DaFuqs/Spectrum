package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.HypeEntity;
import de.dafuqs.spectrum.items.ItemWithTooltip;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HypeItem extends ItemWithTooltip {

    public HypeItem(Settings settings, String tooltip) {
        super(settings, tooltip);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        if (stack.isOf(this)) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClient()) {
                var hype = new HypeEntity(world, user);
                hype.setItem(stack);
                hype.setVelocity(user, user.getPitch(), user.getYaw(), 0, 1.5F, 0F);
                world.spawnEntity(hype);
            }
            if (!user.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return TypedActionResult.success(stack);
    }
}
