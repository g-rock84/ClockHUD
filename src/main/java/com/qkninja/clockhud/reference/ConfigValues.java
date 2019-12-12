package com.qkninja.clockhud.reference;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

/**
 * Stores config values for ClockHUD.
 * Default values are visible here.
 */
public class ConfigValues {

    public static ConfigValues INS;

    public BooleanValue guiActive;// = true;
    public BooleanValue showDayCount;// = true;
    public BooleanValue centerClock;// = false;

    public IntValue xCoord;// = 2;
    public IntValue yCoord;// = 2;

    public DoubleValue scale;//= .7F;

    public ConfigValues(ForgeConfigSpec.Builder builder) {
        builder.comment("Client Settings").push(Reference.MOD_ID);

        guiActive = builder.define("guiActive", true);
        showDayCount = builder.define("showDayCount", true);
        centerClock = builder.define("centerClock", false);
        xCoord = builder.defineInRange("xCoord", 2, 0, Integer.MAX_VALUE);
        yCoord = builder.defineInRange("yCoord", 2, 0, Integer.MAX_VALUE);
        scale = builder.defineInRange("scale", .7D, 0D, Double.MAX_VALUE);

        builder.pop();
    }

}
