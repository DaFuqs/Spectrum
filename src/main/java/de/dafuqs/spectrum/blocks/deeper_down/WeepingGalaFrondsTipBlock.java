package de.dafuqs.spectrum.blocks.deeper_down;

import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

import java.util.*;

public class WeepingGalaFrondsTipBlock extends WeepingGalaFrondsBlock {

    public static final EnumProperty<Form> FORM = EnumProperty.of("form", Form.class);

    public WeepingGalaFrondsTipBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FORM, Form.TIP));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(FORM) != Form.TIP;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.1F) {
            var reference = BlockReference.of(state, pos);
            var form = reference.getProperty(FORM);

            if (form == Form.SPRIG) {
                reference.setProperty(FORM, Form.RESIN);
                reference.update(world);
            }
            else {
                for (ItemStack rareStack : getResinStacks(state, world, pos, ItemStack.EMPTY, SpectrumLootTables.WEEPING_GALA_SPRIG_RESIN)) {
                    dropStack(world, pos, rareStack);
                }
                world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_DRIP, SoundCategory.BLOCKS, 1, 0.9F + random.nextFloat() * 0.2F);
                reference.setProperty(FORM, Form.SPRIG);
                reference.update(world);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var reference = BlockReference.of(state, pos);
        if (reference.getProperty(FORM) == Form.RESIN) {
            if (!world.isClient()) {
                for (ItemStack rareStack : getResinStacks(state, (ServerWorld) world, pos, player.getMainHandStack(), SpectrumLootTables.WEEPING_GALA_SPRIG_RESIN)) {
                    dropStack(world, pos, rareStack);
                }
            }
            world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1, 0.9F + world.getRandom().nextFloat() * 0.2F);
            reference.setProperty(FORM, Form.SPRIG);
            reference.update(world);

            return ActionResult.success(world.isClient());
        }

        return ActionResult.PASS;
    }

    public static List<ItemStack> getResinStacks(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, Identifier lootTableIdentifier) {
        var builder = (new LootContextParameterSet.Builder(world))
                .add(LootContextParameters.BLOCK_STATE, state)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                .add(LootContextParameters.TOOL, stack);

        LootTable lootTable = world.getServer().getLootManager().getLootTable(lootTableIdentifier);
        return lootTable.generateLoot(builder.build(LootContextTypes.BLOCK));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FORM);
    }

    public enum Form implements StringIdentifiable {
        TIP("tip"),
        SPRIG("sprig"),
        RESIN("resin");

        private final String name;

        Form(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }
}
