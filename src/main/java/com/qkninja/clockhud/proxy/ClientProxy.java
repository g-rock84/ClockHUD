package com.qkninja.clockhud.proxy;

import com.qkninja.clockhud.client.gui.GuiClock;
import com.qkninja.clockhud.client.gui.GuiDayCount;
import com.qkninja.clockhud.client.gui.ModGuiConfig;
import com.qkninja.clockhud.client.handler.KeyInputEventHandler;
import com.qkninja.clockhud.client.settings.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Handles the client side of the proxy.
 */
public class ClientProxy extends CommonProxy {

    private Minecraft mc = Minecraft.getInstance();

    private void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(new GuiClock(mc));
        MinecraftForge.EVENT_BUS.register(new GuiDayCount(mc));
    }

    private void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(KeyBindings.TOGGLE);
    }

    @Override
    public void preInit() {
        registerKeyBindings();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ModGuiConfig::new);
    }

    @Override
    public void init() {
        registerRenderers();

        MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());
    }

    @Override
    public void postInit() {
        /* NOOP */
    }
}
