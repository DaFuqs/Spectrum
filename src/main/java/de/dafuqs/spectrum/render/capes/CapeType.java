package de.dafuqs.spectrum.render.capes;

import net.minecraft.util.Identifier;

import static de.dafuqs.spectrum.SpectrumCommon.locate;

public enum CapeType {
    IMMORTAL(locate("textures/capes/immortal.png"), true),
    LUNAR(locate("textures/capes/inaba_of_the_moon.png"), true),
    V1(locate("textures/capes/v1.png"), true),
    UNDERGROUND_ASTRONOMY(locate("textures/capes/underground_astronomy.png"), true),
    LUCKY_STARS(locate("textures/capes/lucky_stars.png"), true),
    PALE_ASTRONOMY(locate("textures/capes/pale_astronomy.png"), true),
    GUDY(locate("textures/capes/gudy.png"), true),
    CHROMED(locate("textures/capes/chromed.png"), true),
    NONE(null, false);

    public final Identifier capePath;
    public final boolean render;

    CapeType(Identifier capePath, boolean render) {
        this.capePath = capePath;
        this.render = render;
    }
}
