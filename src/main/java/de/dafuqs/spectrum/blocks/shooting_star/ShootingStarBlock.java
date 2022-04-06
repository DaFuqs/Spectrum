package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ShootingStarBlock extends BlockWithEntity {
	
	public enum Type {
		GLISTERING("glistering"),
		FIERY("fiery"),
		COLORFUL("colorful"),
		PRISTINE("pristine"),
		GEMSTONE("gemstone");
		
		public static final Identifier BOUNCE_LOOT_TABLE = new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_bounce");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public Block getBlock() {
			switch (this) {
				case PRISTINE -> {
					return SpectrumBlocks.PRISTINE_SHOOTING_STAR;
				}
				case GEMSTONE -> {
					return SpectrumBlocks.GEMSTONE_SHOOTING_STAR;
				}
				case FIERY -> {
					return SpectrumBlocks.FIERY_SHOOTING_STAR;
				}
				case COLORFUL -> {
					return SpectrumBlocks.COLORFUL_SHOOTING_STAR;
				}
				default -> {
					return SpectrumBlocks.GLISTERING_SHOOTING_STAR;
				}
			}
		}
		
		public static ShootingStarBlock.Type getWeightedRandomType(@NotNull Random random) {
			int r = random.nextInt(11);
			if(r == 0) {
				return FIERY;
			} else if(r < 2) {
				return PRISTINE;
			} else if(r < 4) {
				return GLISTERING;
			} else if(r < 7) {
				return COLORFUL;
			} else {
				return GEMSTONE;
			}
		}
		
		public static ShootingStarBlock.Type getType(int type) {
			ShootingStarBlock.Type[] types = values();
			if (type < 0 || type >= types.length) {
				type = 0;
			}
			
			return types[type];
		}
		
		public static ShootingStarBlock.Type getType(String name) {
			ShootingStarBlock.Type[] types = values();
			
			for (Type type : types) {
				if (type.getName().equals(name)) {
					return type;
				}
			}
			
			return types[0];
		}
		
		@Contract("_ -> new")
		public static @NotNull Identifier getLootTableIdentifier(int index) {
			return getLootTableIdentifier(values()[index]);
		}
		
		@Contract("_ -> new")
		public static @NotNull Identifier getLootTableIdentifier(@NotNull Type type) {
			switch (type) {
				case FIERY -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_fiery");
				}
				case COLORFUL -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_colorful");
				}
				case GEMSTONE -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_gemstone");
				}
				case PRISTINE -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_pristine");
				}
				default -> {
					return new Identifier(SpectrumCommon.MOD_ID, "entity/shooting_star/shooting_star_glistering");
				}
			}
		}
		
		public @NotNull Vec3f getRandomParticleColor(Random random) {
			switch (this) {
				case GLISTERING -> {
					int r = random.nextInt(5);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.YELLOW);
					} else if (r == 1) {
						return ColorHelper.getColor(DyeColor.WHITE);
					} else if (r == 2) {
						return ColorHelper.getColor(DyeColor.ORANGE);
					} else if (r == 3) {
						return ColorHelper.getColor(DyeColor.LIME);
					} else {
						return ColorHelper.getColor(DyeColor.BLUE);
					}
				}
				case COLORFUL -> {
					return ColorHelper.getColor(DyeColor.values()[random.nextInt(DyeColor.values().length)]);
				}
				case FIERY -> {
					int r = random.nextInt(2);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.ORANGE);
					} else {
						return ColorHelper.getColor(DyeColor.RED);
					}
				}
				case PRISTINE -> {
					int r = random.nextInt(3);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.BLUE);
					} else if(r == 1) {
						return ColorHelper.getColor(DyeColor.LIGHT_BLUE);
					} else {
						return ColorHelper.getColor(DyeColor.CYAN);
					}
				}
				default -> {
					int r = random.nextInt(4);
					if(r == 0) {
						return ColorHelper.getColor(DyeColor.CYAN);
					} else if(r == 1) {
						return ColorHelper.getColor(DyeColor.MAGENTA);
					} else if(r == 2) {
						return ColorHelper.getColor(DyeColor.WHITE);
					} else {
						return ColorHelper.getColor(DyeColor.YELLOW);
					}
				}
			}
		}
	}
	
	public final Type shootingStarType;
	
	public ShootingStarBlock(Settings settings, ShootingStarBlock.Type shootingStarType) {
		super(settings);
		this.shootingStarType = shootingStarType;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ShootingStarBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack itemStack = super.getPickStack(world, pos, state);
		world.getBlockEntity(pos, SpectrumBlockEntityRegistry.SHOOTING_STAR).ifPresent((blockEntity) -> {
			blockEntity.setStackNbt(itemStack);
		});
		return itemStack;
	}
	
	@Override
	public void onBreak(@NotNull World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient && !player.isCreative()) {
			ItemStack itemStack = this.shootingStarType.getBlock().asItem().getDefaultStack();
			world.getBlockEntity(pos, SpectrumBlockEntityRegistry.SHOOTING_STAR).ifPresent((blockEntity) -> {
				blockEntity.setStackNbt(itemStack);
			});
			
			ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
		}
		
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if(!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ShootingStarBlockEntity shootingStarBlockEntity) {
				shootingStarBlockEntity.setRemainingHits(ShootingStarItem.getRemainingHits(itemStack));
			}
		}
	}
	
	public static class ShootingStarBlockDispenserBehavior extends ItemDispenserBehavior {
		
		public ItemStack dispenseSilently(@NotNull BlockPointer pointer, @NotNull ItemStack stack) {
			Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
			World world = pointer.getWorld();
			double d = pointer.getX() + direction.getOffsetX() * 1.125F;
			double e = pointer.getY() + direction.getOffsetY() * 1.125F;
			double f = pointer.getZ() + direction.getOffsetZ() * 1.125F;
			
			ShootingStarEntity shootingStarEntity = new ShootingStarEntity(world, d, e + 0.05, f);
			ShootingStarBlock.Type type = ((ShootingStarItem) stack.getItem()).getType();
			shootingStarEntity.setShootingStarType(type, true);
			shootingStarEntity.setAvailableHits(ShootingStarItem.getRemainingHits(stack));
			shootingStarEntity.setYaw(direction.asRotation());
			shootingStarEntity.addVelocity(direction.getOffsetX() * 0.4, direction.getOffsetY() * 0.4, direction.getOffsetZ() * 0.4);
			world.spawnEntity(shootingStarEntity);
			
			stack.decrement(1);
			return stack;
		}
	}
	
}
