package net.steveson.createtrimmable.client;

import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.steveson.createtrimmable.client.model.TrimmedItemModel;

public class ClientEventHandler {
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::registerModelLoaders);
    }



    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event)
    {
//        event.register("contained_fluid", new ContainedFluidModel.Loader());
        event.register("trimmed_item", new TrimmedItemModel.Loader());
//        event.register("ingot_pile", IngotPileBlockModel.INSTANCE);
//        event.register("double_ingot_pile", DoubleIngotPileBlockModel.INSTANCE);
//        event.register("sheet_pile", SheetPileBlockModel.INSTANCE);
//        event.register("scraping", ScrapingBlockModel.INSTANCE);
    }

}
