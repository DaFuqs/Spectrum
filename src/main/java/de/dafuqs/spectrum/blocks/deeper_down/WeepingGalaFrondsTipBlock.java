package de.dafuqs.spectrum.blocks.deeper_down;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.deeper_down.flora.WeepingGalaFrondsBlock;
import de.dafuqs.spectrum.helpers.BlockReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class WeepingGalaFrondsTipBlock extends WeepingGalaFrondsBlock {

    public static final Identifier SPRIG_LOOT_TABLE = SpectrumCommon.locate("gameplay/weeping_gala_sprig_resin");

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
        if (random.nextFloat() < 0.25F) {
            var reference = BlockReference.of(state, pos);
            var form = reference.getProperty(FORM);

            if (form == Form.SPRIG) {
                reference.setProperty(FORM, Form.RESIN);
                reference.update(world);
            }
            else {
                for (ItemStack rareStack : getResinStacks(state, world, pos, null, SPRIG_LOOT_TABLE)) {
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
                for (ItemStack rareStack : getResinStacks(state, (ServerWorld) world, pos, null, SPRIG_LOOT_TABLE)) {
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
                .addOptional(LootContextParameters.TOOL, stack);

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
