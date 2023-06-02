package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GlassCrestWorkstaffItem extends WorkstaffItem {
    
    public static final int COOLDOWN_DURATION_TICKS = 10;
    public static final InkCost PROJECTILE_COST = new InkCost(InkColors.WHITE, 50); // TODO: make pricier once ink networking is in
    
    public GlassCrestWorkstaffItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
    
    public static boolean canShoot(NbtCompound nbt) {
        return nbt == null || !nbt.getBoolean(WorkstaffItem.PROJECTILES_DISABLED_NBT_STRING);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<ItemStack> result = super.use(world, user, hand);
        if (!result.getResult().isAccepted()) {
            ItemStack stack = user.getStackInHand(hand);
            if (canShoot(stack.getNbt()) && InkPowered.tryDrainEnergy(user, PROJECTILE_COST)) {
                user.getItemCooldownManager().set(this, COOLDOWN_DURATION_TICKS);
                if (!world.isClient) {
                    MiningProjectileEntity.shoot(world, user, user.getStackInHand(hand));
                }
                stack.damage(2, user, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
        return result;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (canShoot(stack.getNbt())) {
            tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.projectile").formatted(Formatting.GRAY));
        } else {
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.projectiles_disabled").formatted(Formatting.DARK_RED));
        }
    }

}
