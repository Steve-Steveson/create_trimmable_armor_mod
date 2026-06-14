package net.steveson.createtrimmable;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.steveson.createtrimmable.client.ClientEventHandler;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateTrimmableMod.MOD_ID)
public class CreateTrimmableMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "trimmable_create_armor";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public CreateTrimmableMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init();
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        RegistryAccess access = event.getServer().registryAccess();

        Registry<TrimMaterial> registry =
                access.registryOrThrow(Registries.TRIM_MATERIAL);

        System.out.println("zzz THE PRINT CODE IS WORKING  I AM PRINTING THE CODE");
//        System.out.println("THE SIZE IS " + registry.size());


        HashMap<TrimMaterial, ResourceLocation> trimPaletteMap = new HashMap<>();

        for (Holder.Reference<TrimMaterial> holder : registry.holders().toList()) {


            ResourceLocation materialId = holder.key().location();
            TrimMaterial material = holder.value();
            Item materialItem = holder.value().ingredient().get();
            String modId = materialId.getNamespace();
            String materialPath = materialId.getPath();
            ResourceLocation texturePath = new ResourceLocation(modId, "textures/trims/color_palettes/" + materialPath + ".png");

            // this will output from  resources/data/mod_id/trim_material/json files
            System.out.println(materialId);
            // this prints out the ingredient item without the modid
//            System.out.println(materialItem);
//            System.out.println(texturePath);

            System.out.println(
                Minecraft.getInstance().getResourceManager().getResource(texturePath).isPresent()
            );

            trimPaletteMap.put(material, texturePath);
        }


    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
