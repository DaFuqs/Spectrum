package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class ShootingStarBlock extends Block {
	
	public enum Type {
		GLISTERING("glistering"),
		FIERY("fiery"),
		COLORFUL("colorful"),
		PRISTINE("pristine"),
		GEMSTONE("gemstone");
		
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
