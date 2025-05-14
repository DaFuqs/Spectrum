package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class WeepingGalaFrondsTipBlock extends WeepingGalaFrondsBlock {
	
	public static final MapCodec<WeepingGalaFrondsTipBlock> CODEC = simpleCodec(WeepingGalaFrondsTipBlock::new);
	public static final EnumProperty<Form> FORM = EnumProperty.create("form", Form.class);
	
	public WeepingGalaFrondsTipBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(FORM, Form.TIP));
	}
	
	@Override
	public MapCodec<? extends WeepingGalaFrondsTipBlock> codec() {
		return CODEC;
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(FORM) != Form.TIP;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.1F) {
			var reference = BlockReference.of(state, pos);
			var form = reference.getProperty(FORM);
			
			if (form == Form.SPRIG) {
				reference.setProperty(FORM, Form.RESIN);
				reference.update(world);
			} else {
				for (ItemStack rareStack : getResinStacks(state, world, pos, ItemStack.EMPTY, SpectrumLootTables.WEEPING_GALA_SPRIG_RESIN)) {
					popResource(world, pos, rareStack);
				}
				world.playSound(null, pos, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, 1, 0.9F + random.nextFloat() * 0.2F);
				reference.setProperty(FORM, Form.SPRIG);
				reference.update(world);
			}
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		var reference = BlockReference.of(state, pos);
		if (reference.getProperty(FORM) == Form.RESIN) {
			if (!world.isClientSide()) {
				for (ItemStack rareStack : getResinStacks(state, (ServerLevel) world, pos, player.getMainHandItem(), SpectrumLootTables.WEEPING_GALA_SPRIG_RESIN)) {
					popResource(world, pos, rareStack);
				}
			}
			world.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1, 0.9F + world.getRandom().nextFloat() * 0.2F);
			reference.setProperty(FORM, Form.SPRIG);
			reference.update(world);
			
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		
		return InteractionResult.PASS;
	}
	
	public static List<ItemStack> getResinStacks(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack, ResourceKey<LootTable> lootTableKey) {
		var builder = (new LootParams.Builder(world))
				.withParameter(LootContextParams.BLOCK_STATE, state)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, stack);
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(lootTableKey);
		return lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FORM);
	}
	
	public enum Form implements StringRepresentable {
		TIP("tip", 0),
		SPRIG("sprig", 11),
		RESIN("resin", 12);
		
		private final String name;
		private final int luminance;
		
		Form(String name, int luminance) {
			this.name = name;
			this.luminance = luminance;
		}
		
		public int getLuminance() {
			return this.luminance;
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
