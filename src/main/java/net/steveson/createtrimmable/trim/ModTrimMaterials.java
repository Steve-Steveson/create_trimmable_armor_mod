package net.steveson.createtrimmable.trim;

import com.simibubi.create.AllItems;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.steveson.createtrimmable.CreateTrimmableMod;

import java.util.Map;

public class ModTrimMaterials {
    public static final ResourceKey<TrimMaterial> ROSE_QUARTZ =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(CreateTrimmableMod.MOD_ID, "rose_quartz"));
    public static final ResourceKey<TrimMaterial> BRASS =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(CreateTrimmableMod.MOD_ID, "brass"));
    public static final ResourceKey<TrimMaterial> ZINC =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(CreateTrimmableMod.MOD_ID, "zinc"));
    public static final ResourceKey<TrimMaterial> EXP =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(CreateTrimmableMod.MOD_ID, "exp"));

    public static final ResourceKey<TrimMaterial> ANDESITE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(CreateTrimmableMod.MOD_ID, "andesite"));


    public static void bootstrap(BootstapContext<TrimMaterial> pContext) {
        register(pContext, ROSE_QUARTZ, AllItems.ROSE_QUARTZ.get(), Style.EMPTY.withColor(16008305), 0.01F);
        register(pContext, BRASS, AllItems.BRASS_INGOT.get(), Style.EMPTY.withColor(14132061), 0.02F);
        register(pContext, ZINC, AllItems.BRASS_INGOT.get(), Style.EMPTY.withColor(10796961), 0.03F);
        register(pContext, EXP, AllItems.EXP_NUGGET.get(), Style.EMPTY.withColor(6598460), 0.04F);

        register(pContext, ANDESITE, AllItems.ANDESITE_ALLOY.get(), Style.EMPTY.withColor(9476499), 0.07F);
    }

    private static void register(BootstapContext<TrimMaterial> pContext, ResourceKey<TrimMaterial> pMaterialKey, Item pIngredient,
                                 Style pStyle, float pItemModelIndex) {
        TrimMaterial trimmaterial = TrimMaterial.create(pMaterialKey.location().getPath(), pIngredient, pItemModelIndex,
                Component.translatable(Util.makeDescriptionId("trim_material", pMaterialKey.location())).withStyle(pStyle), Map.of());
        pContext.register(pMaterialKey, trimmaterial);
    }
}
