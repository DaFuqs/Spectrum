package de.dafuqs.pigment.worldgen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class WaterDependentRandomPatchFeatureConfig implements FeatureConfig {

    public static final Codec<WaterDependentRandomPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockStateProvider.TYPE_CODEC.fieldOf("out_of_water_state_provider").forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.outOfWaterStateProvider;
        }), BlockStateProvider.TYPE_CODEC.fieldOf("in_water_state_provider").forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.inWaterStateProvider;
        }), BlockPlacer.TYPE_CODEC.fieldOf("block_placer").forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.blockPlacer;
        }), BlockState.CODEC.listOf().fieldOf("whitelist").forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.whitelist.stream().map(Block::getDefaultState).collect(Collectors.toList());
        }), BlockState.CODEC.listOf().fieldOf("blacklist").forGetter((randomPatchFeatureConfig) -> {
            return ImmutableList.copyOf(randomPatchFeatureConfig.blacklist);
        }), Codecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.tries;
        }), Codecs.NONNEGATIVE_INT.fieldOf("xspread").orElse(7).forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.spreadX;
        }), Codecs.NONNEGATIVE_INT.fieldOf("yspread").orElse(3).forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.spreadY;
        }), Codecs.NONNEGATIVE_INT.fieldOf("zspread").orElse(7).forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.spreadZ;
        }), Codec.BOOL.fieldOf("can_replace").orElse(false).forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.canReplace;
        }), Codec.BOOL.fieldOf("project").orElse(true).forGetter((randomPatchFeatureConfig) -> {
            return randomPatchFeatureConfig.project;
        })).apply(instance, WaterDependentRandomPatchFeatureConfig::new);
    });

    public final BlockStateProvider outOfWaterStateProvider;
    public final BlockStateProvider inWaterStateProvider;
    public final BlockPlacer blockPlacer;
    public final Set<Block> whitelist;
    public final Set<BlockState> blacklist;
    public final int tries;
    public final int spreadX;
    public final int spreadY;
    public final int spreadZ;
    public final boolean canReplace;
    public final boolean project;

    private WaterDependentRandomPatchFeatureConfig(BlockStateProvider outOfWaterStateProvider, BlockStateProvider inWaterStateProvider, BlockPlacer blockPlacer, List<BlockState> whitelist, List<BlockState> blacklist, int tries, int spreadX, int spreadY, int spreadZ, boolean canReplace, boolean project) {
        this(outOfWaterStateProvider, inWaterStateProvider, blockPlacer, (whitelist.stream().map(AbstractBlockState::getBlock).collect(Collectors.toSet())), ImmutableSet.copyOf(blacklist), tries, spreadX, spreadY, spreadZ, canReplace, project);
    }

    WaterDependentRandomPatchFeatureConfig(BlockStateProvider outOfWaterStateProvider, BlockStateProvider inWaterStateProvider, BlockPlacer blockPlacer, Set<Block> whitelist, Set<BlockState> blacklist, int tries, int spreadX, int spreadY, int spreadZ, boolean canReplace, boolean project) {
        this.outOfWaterStateProvider = outOfWaterStateProvider;
        this.inWaterStateProvider = inWaterStateProvider;
        this.blockPlacer = blockPlacer;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
        this.tries = tries;
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        this.canReplace = canReplace;
        this.project = project;
    }

    public static class Builder {
        private final BlockStateProvider outOfWaterStateProvider;
        private final BlockStateProvider inWaterStateProvider;
        private final BlockPlacer blockPlacer;
        private Set<Block> whitelist = ImmutableSet.of();
        private Set<BlockState> blacklist = ImmutableSet.of();
        private int tries = 64;
        private int spreadX = 7;
        private int spreadY = 3;
        private int spreadZ = 7;
        private boolean canReplace;
        private boolean project = true;

        public Builder(BlockStateProvider outOfWaterStateProvider, BlockStateProvider inWaterStateProvider, BlockPlacer blockPlacer) {
            this.outOfWaterStateProvider = outOfWaterStateProvider;
            this.inWaterStateProvider = inWaterStateProvider;
            this.blockPlacer = blockPlacer;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder whitelist(Set<Block> whitelist) {
            this.whitelist = whitelist;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder blacklist(Set<BlockState> blacklist) {
            this.blacklist = blacklist;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder tries(int tries) {
            this.tries = tries;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder spreadX(int spreadX) {
            this.spreadX = spreadX;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder spreadY(int spreadY) {
            this.spreadY = spreadY;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder spreadZ(int spreadZ) {
            this.spreadZ = spreadZ;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder canReplace() {
            this.canReplace = true;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig.Builder cannotProject() {
            this.project = false;
            return this;
        }

        public WaterDependentRandomPatchFeatureConfig build() {
            return new WaterDependentRandomPatchFeatureConfig(this.outOfWaterStateProvider, this.inWaterStateProvider, this.blockPlacer, this.whitelist, this.blacklist, this.tries, this.spreadX, this.spreadY, this.spreadZ, this.canReplace, this.project);
        }
    }
}