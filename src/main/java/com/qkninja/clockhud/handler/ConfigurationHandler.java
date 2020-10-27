package com.qkninja.clockhud.handler;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.qkninja.clockhud.reference.ConfigValues;
import com.qkninja.clockhud.reference.Reference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

/**
 * Sets up the config file for ClockHUD.
 */
public class ConfigurationHandler {
    public static final ForgeConfigSpec clientConfigSpec;

    static {
        final Pair<ConfigValues, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
                .configure(ConfigValues::new);
        clientConfigSpec = specPair.getRight();
        ConfigValues.INS = specPair.getLeft();
    }

    public static void init() {
        ModLoadingContext ctx = ModLoadingContext.get();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ctx.registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);
        });
    }

    public static void load() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            loadConfig(clientConfigSpec, FMLPaths.CONFIGDIR.get()
                    .resolve(Reference.MOD_ID + "-client.toml")); // needs to be modid
        });
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }

}
