package com.qkninja.clockhud.client.handler;

import com.qkninja.clockhud.client.settings.KeyBindings;
import com.qkninja.clockhud.reference.ConfigValues;
import com.qkninja.clockhud.reference.Key;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles all results for keyboard events.
 */
public class KeyInputEventHandler {

    private static Key getPressedKeyBinding() {
        if (KeyBindings.TOGGLE.isPressed())
            return Key.TOGGLE;
        else return Key.UNKNOWN;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        if (getPressedKeyBinding() == Key.TOGGLE)
            ConfigValues.INS.guiActive.set(!ConfigValues.INS.guiActive.get());
    }
}
