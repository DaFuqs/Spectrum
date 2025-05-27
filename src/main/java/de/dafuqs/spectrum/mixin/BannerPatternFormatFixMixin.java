package de.dafuqs.spectrum.mixin;

import com.mojang.datafixers.schemas.*;
import net.minecraft.datafixer.fix.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(BannerPatternFormatFix.class)
public class BannerPatternFormatFixMixin {
	
	@Shadow
	@Final
	@Mutable
	private static Map<String, String> OLD_TO_NEW_PATTERNS;
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void changePatternFixes(Schema outputSchema, CallbackInfo ci){
		OLD_TO_NEW_PATTERNS =
				Map.ofEntries(Map.entry("b", "minecraft:base"), Map.entry("bl", "minecraft:square_bottom_left"), Map.entry("br", "minecraft:square_bottom_right"), Map.entry("tl", "minecraft:square_top_left"), Map.entry("tr", "minecraft:square_top_right"), Map.entry("bs", "minecraft:stripe_bottom"), Map.entry("ts", "minecraft:stripe_top"), Map.entry("ls", "minecraft:stripe_left"), Map.entry("rs", "minecraft:stripe_right"), Map.entry("cs", "minecraft:stripe_center"), Map.entry("ms", "minecraft:stripe_middle"), Map.entry("drs", "minecraft:stripe_downright"), Map.entry("dls", "minecraft:stripe_downleft"), Map.entry("ss", "minecraft:small_stripes"), Map.entry("cr", "minecraft:cross"), Map.entry("sc", "minecraft:straight_cross"), Map.entry("bt", "minecraft:triangle_bottom"), Map.entry("tt", "minecraft:triangle_top"), Map.entry("bts", "minecraft:triangles_bottom"), Map.entry("tts", "minecraft:triangles_top"), Map.entry("ld", "minecraft:diagonal_left"), Map.entry("rd", "minecraft:diagonal_up_right"), Map.entry("lud", "minecraft:diagonal_up_left"), Map.entry("rud", "minecraft:diagonal_right"), Map.entry("mc", "minecraft:circle"), Map.entry("mr", "minecraft:rhombus"), Map.entry("vh", "minecraft:half_vertical"), Map.entry("hh", "minecraft:half_horizontal"), Map.entry("vhr", "minecraft:half_vertical_right"), Map.entry("hhb", "minecraft:half_horizontal_bottom"), Map.entry("bo", "minecraft:border"), Map.entry("cbo", "minecraft:curly_border"), Map.entry("gra", "minecraft:gradient"), Map.entry("gru", "minecraft:gradient_up"), Map.entry("bri", "minecraft:bricks"), Map.entry("glb", "minecraft:globe"), Map.entry("cre", "minecraft:creeper"), Map.entry("sku", "minecraft:skull"), Map.entry("flo", "minecraft:flower"), Map.entry("moj", "minecraft:mojang"), Map.entry("pig", "minecraft:piglin"),
				// Our fixes
						Map.entry("spectrum_l", "spectrum:spectrum_logo"),
						Map.entry("spectrum_acl", "spectrum:amethyst_cluster"),
						Map.entry("spectrum_as", "spectrum:amethyst_shard"),
						Map.entry("spectrum_ct", "spectrum:crafting_tablet"),
						Map.entry("spectrum_flc", "spectrum:four_leaf_clover"),
						Map.entry("spectrum_if", "spectrum:ink_flask"),
						Map.entry("spectrum_kg", "spectrum:knowledge_gem"),
						Map.entry("spectrum_gui", "spectrum:guidebook"),
						// Ok I give up there will be like 2 people for who this is useful outside of the manor ones
						Map.entry("spectrum_ast", "spectrum:astrologer"),
						Map.entry("spectrum_vast", "spectrum:velvet_astrologer"),
						Map.entry("spectrum_psn", "spectrum:poisonbloom"),
						Map.entry("spectrum_dl", "spectrum:deep_light")
						
				);
	}
}
