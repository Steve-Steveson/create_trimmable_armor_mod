package net.steveson.createtrimmable.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.steveson.createtrimmable.CreateTrimmableMod;
import net.steveson.createtrimmable.trim.ModTrimMaterials;


import java.util.LinkedHashMap;

public class VanillaTrimmedItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(ModTrimMaterials.ROSE_QUARTZ, 0.01F);
        trimMaterials.put(ModTrimMaterials.BRASS, 0.02F);
        trimMaterials.put(ModTrimMaterials.ZINC, 0.03F);
        trimMaterials.put(ModTrimMaterials.EXP, 0.04F);

        trimMaterials.put(ModTrimMaterials.ANDESITE, 0.07F);

        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }



    public VanillaTrimmedItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "minecraft", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        trimmedVanillaArmorItem(Items.TURTLE_HELMET);

        trimmedVanillaArmorItem(Items.LEATHER_HELMET);
        trimmedVanillaArmorItem(Items.LEATHER_CHESTPLATE);
        trimmedVanillaArmorItem(Items.LEATHER_LEGGINGS);
        trimmedVanillaArmorItem(Items.LEATHER_BOOTS);
        trimmedVanillaArmorItem(Items.CHAINMAIL_HELMET);
        trimmedVanillaArmorItem(Items.CHAINMAIL_CHESTPLATE);
        trimmedVanillaArmorItem(Items.CHAINMAIL_LEGGINGS);
        trimmedVanillaArmorItem(Items.CHAINMAIL_BOOTS);
        trimmedVanillaArmorItem(Items.IRON_HELMET);
        trimmedVanillaArmorItem(Items.IRON_CHESTPLATE);
        trimmedVanillaArmorItem(Items.IRON_LEGGINGS);
        trimmedVanillaArmorItem(Items.IRON_BOOTS);
        trimmedVanillaArmorItem(Items.GOLDEN_HELMET);
        trimmedVanillaArmorItem(Items.GOLDEN_CHESTPLATE);
        trimmedVanillaArmorItem(Items.GOLDEN_LEGGINGS);
        trimmedVanillaArmorItem(Items.GOLDEN_BOOTS);
        trimmedVanillaArmorItem(Items.DIAMOND_HELMET);
        trimmedVanillaArmorItem(Items.DIAMOND_CHESTPLATE);
        trimmedVanillaArmorItem(Items.DIAMOND_LEGGINGS);
        trimmedVanillaArmorItem(Items.DIAMOND_BOOTS);
        trimmedVanillaArmorItem(Items.NETHERITE_HELMET);
        trimmedVanillaArmorItem(Items.NETHERITE_CHESTPLATE);
        trimmedVanillaArmorItem(Items.NETHERITE_LEGGINGS);
        trimmedVanillaArmorItem(Items.NETHERITE_BOOTS);
    }



    // Shoutout to El_Redstoniano for making what this used to be before I mangled it
    private void trimmedVanillaArmorItem(Item itemRegistryObject) {
        final String MOD_ID = CreateTrimmableMod.MOD_ID; // Change this to your mod id

        if(itemRegistryObject instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                boolean isVanilla = (trimValue * 1000) % 100 == 0;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";


//                String[] armorMatNameSplit = armorItem.getMaterial().getName().split(":");
//                boolean overrideDarker = armorMatNameSplit.length == 2 && armorMatNameSplit[1].equalsIgnoreCase(trimMaterial.location().getPath());
                boolean overrideDarker = armorItem.getMaterial().getName().equalsIgnoreCase(trimMaterial.location().getPath());



                if (overrideDarker){
                    trimPath = trimPath + "_darker";

                    currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_darker_trim";
//                    currentTrimName = currentTrimName + "_darker";
                }


//                ResourceLocation armorItemResLoc = new ResourceLocation(armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName.replaceFirst("item/", "item/minecraft/"));

                ResourceLocation itemRL = ForgeRegistries.ITEMS.getKey(itemRegistryObject);
                if(isVanilla) {
                    trimNameResLoc = new ResourceLocation(currentTrimName);
                }

//                System.out.println(currentTrimName);
//                System.out.println(trimResLoc);


                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

//                // Trimmed armorItem files
//                if(!isVanilla) {
//                    getBuilder(currentTrimName)
//                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
//                            .texture("layer0", armorItemResLoc)
//                            .texture("layer1", trimResLoc);
//                }

                // Non-trimmed armorItem file (normal variant)

                if(armorItem.getMaterial() == ArmorMaterials.LEATHER) {
                    this.withExistingParent(itemRL.getPath(),
                                    mcLoc("item/generated"))
                            .override()
                            .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                            .predicate(mcLoc("trim_type"), trimValue).end()
                            .texture("layer0",
                                    new ResourceLocation("item/" + itemRL.getPath()))
                            .texture("layer1",
                                    new ResourceLocation("item/" + itemRL.getPath() + "_overlay"));
                }
                else {
                    this.withExistingParent(itemRL.getPath(),
                                    mcLoc("item/generated"))
                            .override()
                            .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                            .predicate(mcLoc("trim_type"), trimValue).end()
                            .texture("layer0",
                                    new ResourceLocation("item/" + itemRL.getPath()));
                }
            });
        }
    }
}
