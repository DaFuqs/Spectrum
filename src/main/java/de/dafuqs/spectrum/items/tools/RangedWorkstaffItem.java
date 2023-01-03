package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.energy.InkCost;
import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.entity.entity.MiningProjectileEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RangedWorkstaffItem extends WorkstaffItem {

    public static final int COOLDOWN_DURATION_TICKS = 10;
    public static final InkCost PROJECTILE_COST = new InkCost(InkColors.CYAN, 250);

    public RangedWorkstaffItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public static boolean canShoot(NbtCompound nbt) {
        return nbt == null || !nbt.getBoolean(WorkstaffItem.PROJECTILES_DISABLED_NBT_STRING);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<ItemStack> result = super.use(world, user, hand);
        if (!result.getResult().isAccepted() && canShoot(user.getStackInHand(hand).getNbt()) && InkPowered.tryDrainEnergy(user, PROJECTILE_COST)) {
            user.getItemCooldownManager().set(this, COOLDOWN_DURATION_TICKS);
            if (!world.isClient) {
                MiningProjectileEntity.shoot(world, user, user.getStackInHand(hand));
            }
        }
        return result;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (canShoot(stack.getNbt())) {
            tooltip.add(Text.translatable("item.spectrum.glass_crest_workstaff.tooltip.projectile").formatted(Formatting.GRAY));
        }
    }

}
