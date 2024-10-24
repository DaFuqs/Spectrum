package de.dafuqs.spectrum.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;

import java.util.Map;
import java.util.function.Supplier;

import static de.dafuqs.spectrum.datafixer.SpectrumDataFixers.INK_STORAGE_BLOCKS;

public class Schema1 extends IdentifierNormalizingSchema {
    public Schema1(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
        for (String block : INK_STORAGE_BLOCKS) {
            schema.register(map, block, DSL::remainder);
        }
        return map;
    }
}
