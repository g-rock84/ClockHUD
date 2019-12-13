package com.qkninja.clockhud;

import com.qkninja.clockhud.handler.ConfigurationHandler;
import com.qkninja.clockhud.proxy.ClientProxy;
import com.qkninja.clockhud.proxy.CommonProxy;
import com.qkninja.clockhud.reference.Reference;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple clock GUI to make telling the time easier.
 *
 * @author QKninja
 */
@Mod(value = Reference.MOD_ID)
public class ClockHUD {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static ClockHUD instance;

    public ClockHUD() {
        ConfigurationHandler.init();
        //ConfigurationHandler.load();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        instance = this;
    }

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ClientProxy::new);


    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("ClockHUD PREINIT");
        proxy.preInit();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        proxy.init();
    }

}