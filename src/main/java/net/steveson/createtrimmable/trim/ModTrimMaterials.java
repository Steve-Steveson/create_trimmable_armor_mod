package net.steveson.createtrimmable.trim;

import com.simibubi.create.AllItems;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.steveson.createtrimmable.CreateTrimmableMod;

import java.util.Map;

public class ModTrimMaterials {
    public static final RegistryKey<ArmorTrimMaterial> ROSE_QUARTZ =
            RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(CreateTrimmableMod.MOD_ID, "rose_quartz"));
    public static final RegistryKey<ArmorTrimMaterial> BRASS =
            RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(CreateTrimmableMod.MOD_ID, "brass"));
    public static final RegistryKey<ArmorTrimMaterial> ZINC =
            RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(CreateTrimmableMod.MOD_ID, "zinc"));
    public static final RegistryKey<ArmorTrimMaterial> EXP =
            RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(CreateTrimmableMod.MOD_ID, "exp"));

    public static final RegistryKey<ArmorTrimMaterial> ANDESITE =
            RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(CreateTrimmableMod.MOD_ID, "andesite"));

    public static void bootstrap(Registerable<ArmorTrimMaterial> registerable) {
        register(registerable, ROSE_QUARTZ, Registries.ITEM.getEntry(AllItems.ROSE_QUARTZ.get()),
                Style.EMPTY.withColor(TextColor.parse("#f44471")), 0.01f);
        register(registerable, BRASS, Registries.ITEM.getEntry(AllItems.BRASS_INGOT.get()),
                Style.EMPTY.withColor(TextColor.parse("#d7a35d")), 0.02f);
        register(registerable, ZINC, Registries.ITEM.getEntry(AllItems.ZINC_INGOT.get()),
                Style.EMPTY.withColor(TextColor.parse("#a4bfa1")), 0.03f);
        register(registerable, EXP, Registries.ITEM.getEntry(AllItems.EXP_NUGGET.get()),
                Style.EMPTY.withColor(TextColor.parse("#64af3c")), 0.04f);

        register(registerable, ANDESITE, Registries.ITEM.getEntry(AllItems.ANDESITE_ALLOY.get()),
                Style.EMPTY.withColor(TextColor.parse("#909993")), 0.07f);
    }

    private static void register(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> armorTrimKey,
                                 RegistryEntry<Item> item, Style style, float itemModelIndex) {
        ArmorTrimMaterial trimMaterial = new ArmorTrimMaterial(armorTrimKey.getValue().getPath(), item, itemModelIndex, Map.of(),
                Text.translatable(Util.createTranslationKey("trim_material", armorTrimKey.getValue())).fillStyle(style));

        registerable.register(armorTrimKey, trimMaterial);
    }
}
