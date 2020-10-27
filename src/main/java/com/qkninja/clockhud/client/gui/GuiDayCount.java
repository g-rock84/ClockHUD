package com.qkninja.clockhud.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.qkninja.clockhud.reference.ConfigValues;
import com.qkninja.clockhud.reference.Names;
import com.qkninja.clockhud.reference.Reference;
import com.qkninja.clockhud.utility.Algorithms;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;


/**
 * Renders the day count on the screen after each new day.
 *
 * @author Sam Beckmann
 */
public class GuiDayCount extends AbstractGui {
    private static final int ANIMATION_TIME = 3000; // 3 second animation
    private Minecraft mc;
    private long endAnimationTime;
    private boolean isRunning;

    public GuiDayCount(Minecraft mc) {
        super();
        isRunning = false;
        this.mc = mc;
    }

    /**
     * Renders the Day Count on the screen.
     *
     * @param event variables associated with the event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderExperienceBar(RenderGameOverlayEvent.Post event) {
        if (ConfigValues.INS.showDayCount.get() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE &&
            (isRunning || isNewDay())) {
            long currentTime = System.nanoTime() / 1000000;

            if (isRunning && currentTime >= endAnimationTime) {
                isRunning = false;
                return;
            }

            if (!isRunning) {
                isRunning = true;
                endAnimationTime = currentTime + ANIMATION_TIME;
            }

            float scaleFactor = getScaleFactor((endAnimationTime - currentTime) / (float) ANIMATION_TIME);
            String dayString = formDayString();

            RenderSystem.scalef(scaleFactor, scaleFactor, scaleFactor);
            MainWindow mainWindow = event.getWindow();
            int alpha = Math.max(getOpacityFactor((endAnimationTime - currentTime) / (float) ANIMATION_TIME), 5);
            int color = (alpha << 24) | 0xffffff;
            float xPos = (mainWindow.getScaledWidth() - this.mc.fontRenderer.getStringWidth(dayString) * scaleFactor) / (2 * scaleFactor);
            float yPos = mainWindow.getScaledHeight() / 7F / scaleFactor;
            this.mc.fontRenderer.drawString(dayString, xPos, yPos, color);
            RenderSystem.scalef(1 / scaleFactor, 1 / scaleFactor, 1 / scaleFactor);
        }
    }

    /**
     * Tests if it's a new day.
     *
     * @return if the dayTime is the specified time of a new day.
     */
    private boolean isNewDay() {
        return Minecraft.getInstance().world.getDayTime() % Reference.DAY_TICKS == Reference.NEW_DAY_TICK;
    }

    /**
     * Creates the day string based on total world time
     *
     * @return String of "Day: " + day number
     */
    private String formDayString() {
        return I18n.format(Names.Text.DAYCOUNT, Minecraft.getInstance().world.getGameTime() / Reference.DAY_TICKS);
    }

    /**
     * Gets the factor that the text should be scaled by.
     * Ensures even scaling throughout the time of the animation.
     *
     * @param percentRemaining scaled value between 0-1 indicating percent of the animation remaining.
     * @return Value evenly scaled between 2 and 2.5 based on input.
     */
    private float getScaleFactor(float percentRemaining) {
        return 2.5F - percentRemaining / 2;
    }

    /**
     * Gets the opacity at which the text should be displayed.
     * Handles fade in/out of text.
     *
     * @param percentRemaining scaled value between 0-1 indicating percent of the animation remaining.
     * @return value between 0 and 255, indicating alpha value.
     */
    private int getOpacityFactor(float percentRemaining) {
        if (percentRemaining > .8)
            return (int) (255 * (0.8 - Algorithms.scale(percentRemaining, 0.8, 1, 0, 0.8)));
        else if (percentRemaining < .2)
            return (int) (255 * Algorithms.scale(percentRemaining, 0, 0.2, 0, 0.8));
        else
            return (int) (255 * 0.8F);
    }
}
