package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class ShootingStarBlock extends Block {
	
	public enum Type {
		GLISTERING(SpectrumBlocks.GLISTERING_SHOOTING_STAR, "glistering"),
		FIERY(SpectrumBlocks.FIERY_SHOOTING_STAR, "fiery"),
		COLORFUL(SpectrumBlocks.COLORFUL_SHOOTING_STAR, "colorful"),
		PRISTINE(SpectrumBlocks.PRISTINE_SHOOTING_STAR, "pristine"),
		GEMSTONE(SpectrumBlocks.GEMSTONE_SHOOTING_STAR, "gemstone");
		
		private final Block block;
		private final String name;
		
		private Type(Block block, String name) {
			this.name = name;
			this.block = block;
		}
		
		public String getName() {
			return this.name;
		}
		
		public Block getBlock() {
			return this.block;
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
		
		public static Identifier getLootTableIdentifier(int index) {
			return getLootTableIdentifier(values()[index]);
		}
		
		public static Identifier getLootTableIdentifier(Type type) {
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
	}
	
	public final Type shootingStarType;
	
	public ShootingStarBlock(Settings settings, ShootingStarBlock.Type shootingStarType) {
		super(settings);
		this.shootingStarType = shootingStarType;
	}
	
}
