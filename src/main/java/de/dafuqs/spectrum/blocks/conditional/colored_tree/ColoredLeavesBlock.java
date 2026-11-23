package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import de.dafuqs.spectrum.registries.client.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ColoredLeavesBlock extends FlammableLeavesBlock implements RevelationAware, ColoredTree {
	
	public static final MapCodec<ColoredLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredLeavesBlock::getColor)
	).apply(instance, ColoredLeavesBlock::new));
	
	public static final BooleanProperty FROM_SAPLING = BooleanProperty.create("from_sapling");
	
	private static final Map<InkColor, ColoredLeavesBlock> LEAVES = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	protected static final ResourceLocation DUMMY_ADVANCEMENT_ID = SpectrumCommon.locate("dummy");
	protected final ResourceLocation advancementId;
	protected ResourceLocation currentAdvancementId;
	
	public ColoredLeavesBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		LEAVES.put(color, this);
		advancementId = ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.LEAVES, this.color);
		currentAdvancementId = advancementId;
		RevelationAware.register(this);
		
		this.registerDefaultState(this.stateDefinition.any()
						.setValue(DISTANCE, 7)
						.setValue(PERSISTENT, Boolean.FALSE)
						.setValue(WATERLOGGED, Boolean.FALSE)
						.setValue(FROM_SAPLING, Boolean.FALSE)
		);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FROM_SAPLING);
	}
	
	public static Collection<ColoredLeavesBlock> allLeaves() {
		return LEAVES.values();
	}
	
	@Override
	public MapCodec<? extends ColoredLeavesBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return currentAdvancementId;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		BlockState thisState = this.defaultBlockState();
		BlockState oakState = Blocks.OAK_LEAVES.defaultBlockState();
		
		Map<BlockState, BlockState> map = new Hashtable<>();
		for(boolean persistent : new boolean[]{false, true}) {
			for (boolean waterlogged : new boolean[]{false, true}) {
				for (boolean fromSapling : new boolean[]{false, true}) {
					for (int distance = 1; distance < 8; distance++) {
						map.put(
								thisState.setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, persistent).setValue(WATERLOGGED, waterlogged).setValue(FROM_SAPLING, fromSapling),
								oakState.setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, persistent).setValue(WATERLOGGED, waterlogged)
						);
					}
				}
			}
		}
		return map;
	}
	
	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (this.decaying(state)) {
			if(state.getValue(FROM_SAPLING)) {
				// id the leaf was grown from a sapling, drop as if harvested by a player
				this.currentAdvancementId = DUMMY_ADVANCEMENT_ID;
				dropResources(state, level, pos, null, level.getRandomPlayer(), ItemStack.EMPTY);
				this.currentAdvancementId = advancementId;
			} else {
				dropResources(state, level, pos);
			}
			
			level.removeBlock(pos, false);
		}
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.OAK_LEAVES.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.coloredLeavesBlockColorProvider != null && SpectrumColorProviders.coloredLeavesItemColorProvider != null) {
			SpectrumColorProviders.coloredLeavesBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.coloredLeavesItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.coloredLeavesBlockColorProvider != null && SpectrumColorProviders.coloredLeavesItemColorProvider != null) {
			SpectrumColorProviders.coloredLeavesBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.coloredLeavesItemColorProvider.setShouldApply(true);
		}
	}
	
	@Override
	public InkColor getColor() {
		return this.color;
	}

	public static @Nullable ColoredLeavesBlock byColor(InkColor color) {
		return LEAVES.get(color);
	}
	
	public static Collection<ColoredLeavesBlock> all() {
		return LEAVES.values();
	}
	
}
