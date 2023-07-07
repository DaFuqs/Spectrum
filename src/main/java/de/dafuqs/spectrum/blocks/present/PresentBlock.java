package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PresentBlock extends BlockWithEntity {
	
	public enum WrappingPaper implements StringIdentifiable {
		RED,
		BLUE,
		CYAN,
		GREEN,
		PURPLE,
		CAKE,
		STRIPED,
		STARRY,
		WINTER,
		PRIDE;
		
		@Override
		public String asString() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	public static final int TICKS_PER_OPENING_STEP = 20;
	public static final int OPENING_STEPS = 6;
	
	public static final BooleanProperty OPENING = BooleanProperty.of("opening");
	private static final EnumProperty<WrappingPaper> VARIANT = EnumProperty.of("variant", WrappingPaper.class);
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	
	public PresentBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(OPENING, false).with(VARIANT, WrappingPaper.RED));
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(OPENING, VARIANT);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		BlockState downState = world.getBlockState(pos.down());
		return downState.isSideSolidFullSquare(world, pos, Direction.UP);
	}
	
	@Override
	public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		world.setBlockState(pos, state.with(PresentBlock.VARIANT, PresentItem.getVariant(itemStack.getNbt())));
		if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
			presentBlockEntity.setDataFromPresentStack(itemStack);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			return ActionResult.PASS;
		} else {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
					if (player.isSneaking()) {
						presentBlockEntity.setOpenerUUID(player);
						state = state.with(OPENING, true);
						world.setBlockState(pos, state, 3);
						world.createAndScheduleBlockTick(pos, state.getBlock(), TICKS_PER_OPENING_STEP);
					} else {
						if (presentBlockEntity.getOwnerName() != null) {
							player.sendMessage(Text.translatable("block.spectrum.present.tooltip.wrapped_placed.giver", presentBlockEntity.getOwnerName()), true);
						} else {
							player.sendMessage(Text.translatable("block.spectrum.present.tooltip.wrapped_placed"), true);
						}
						
					}
				}
				return ActionResult.CONSUME;
			}
		}
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
			return List.of(presentBlockEntity.retrievePresent(state.get(VARIANT)));
		} else {
			return super.getDroppedStacks(state, builder);
		}
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(OPENING)) {
			if (!world.isClient) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PresentBlockEntity presentBlockEntity) {
					int openingTick = presentBlockEntity.openingTick();
					Vec3d posVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);
					if (openingTick == OPENING_STEPS) {
						spawnParticles(world, pos, presentBlockEntity.colors);
						presentBlockEntity.triggerAdvancement();
						if (presentBlockEntity.isEmpty()) {
							world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 0.8F);
							SpectrumS2CPacketSender.playParticleWithExactVelocity(world, posVec, ParticleTypes.SMOKE, 5, Vec3d.ZERO);
						} else {
							world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, 4.0F);
							SpectrumS2CPacketSender.playParticleWithExactVelocity(world, posVec, ParticleTypes.EXPLOSION, 1, Vec3d.ZERO);
							ItemScatterer.spawn(world, pos, presentBlockEntity.stacks);
						}
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					} else {
						world.playSound(null, posVec.x, posVec.y, posVec.z, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 0.8F + openingTick * 0.1F, 1.0F);
						spawnParticles(world, pos, presentBlockEntity.colors);
					}
				}
				world.createAndScheduleBlockTick(pos, state.getBlock(), TICKS_PER_OPENING_STEP);
			}
		}
	}
	
	public static void spawnParticles(ServerWorld world, BlockPos pos, Map<DyeColor, Integer> colors) {
		SpectrumS2CPacketSender.playPresentOpeningParticles(world, pos, colors);
	}
	
	public static void spawnParticles(ClientWorld world, BlockPos pos, Map<DyeColor, Integer> colors) {
		if (colors.isEmpty()) {
			DyeColor randomColor = DyeColor.byId(world.random.nextInt(DyeColor.values().length));
			spawnParticles(world, pos, randomColor, 15);
		} else {
			for (Map.Entry<DyeColor, Integer> color : colors.entrySet()) {
				spawnParticles(world, pos, color.getKey(), color.getValue() * 10);
			}
		}
	}
	
	private static void spawnParticles(ClientWorld world, BlockPos pos, DyeColor color, int amount) {
		double posX = pos.getX() + 0.5;
		double posY = pos.getY() + 0.25;
		double posZ = pos.getZ() + 0.5;
		Random random = world.random;
		Vec3f colorVec = ColorHelper.getRGBVec(color);
		for (int i = 0; i < amount; i++) {
			double randX = 0.35 - random.nextFloat() * 0.7;
			double randY = random.nextFloat() * 0.7;
			double randZ = 0.35 - random.nextFloat() * 0.7;
			float randomScale = 0.5F + random.nextFloat();
			int randomLifetime = 20 + random.nextInt(20);
			
			ParticleEffect particleEffect = new DynamicParticleEffect(0.98F, colorVec, randomScale, randomLifetime, true, false);
			world.addParticle(particleEffect, posX, posY, posZ, randX, randY, randZ);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PresentBlockEntity(pos, state);
	}
	
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	
}
