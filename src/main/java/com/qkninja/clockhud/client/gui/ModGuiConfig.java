package com.qkninja.clockhud.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.qkninja.clockhud.reference.ConfigValues;
import com.qkninja.clockhud.reference.Names;
import com.qkninja.clockhud.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Optional;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class ModGuiConfig extends Screen {

    private final Screen parent;
    private final Minecraft mc;

    public CheckboxButton guiActiveControl;// = true;
    public CheckboxButton showDayCountControl;// = true;
    public CheckboxButton centerClockControl;// = false;
    public TextFieldWidget xCoordControl;// = 2;
    public TextFieldWidget yCoordControl;// = 2;
    public TextFieldWidget scaleControl;//= .7F;

    public ModGuiConfig(Minecraft mc, Screen parent) {
        super(new StringTextComponent(""));
        this.parent = parent;
        this.mc = mc;
    }

    //render
    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //renderBackground
        super.func_230446_a_(matrixStack);
        //render
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);

        FontRenderer fontRenderer = this.field_230712_o_;
        //drawString
        fontRenderer.func_238421_b_(matrixStack, I18n.format(Names.Config.X_COORD), xCoordControl.field_230690_l_ - 50, xCoordControl.field_230691_m_, 0xFFFFFF);
        //render
        xCoordControl.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        //drawString
        fontRenderer.func_238421_b_(matrixStack, I18n.format(Names.Config.Y_COORD), yCoordControl.field_230690_l_ - 50, yCoordControl.field_230691_m_, 0xFFFFFF);
        //render
        yCoordControl.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        //drawString
        fontRenderer.func_238421_b_(matrixStack, I18n.format(Names.Config.SCALE), scaleControl.field_230690_l_ - 50, scaleControl.field_230691_m_, 0xFFFFFF);
        //render
        scaleControl.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    //init
    public void func_231160_c_() {
        int y = 2;
        int margin = 22;
        int width = this.field_230708_k_;//width
        FontRenderer fontRenderer = this.field_230712_o_;

        guiActiveControl = new CheckboxButton(width / 2 - 100, y, 200, 20,
                new StringTextComponent(I18n.format(Names.Config.GUI_ACTIVE)), ConfigValues.INS.guiActive.get());
        this.addWidget(guiActiveControl);
        y += margin;
        showDayCountControl = new CheckboxButton(width / 2 - 100, y, 200, 20,
                new StringTextComponent(I18n.format(Names.Config.SHOW_DAY_COUNT)), ConfigValues.INS.showDayCount.get());
        this.addWidget(showDayCountControl);
        y += margin;
        centerClockControl = new CheckboxButton(width / 2 - 100, y, 200, 20,
                new StringTextComponent(I18n.format(Names.Config.CENTER_CLOCK)), ConfigValues.INS.centerClock.get());

        this.addWidget(centerClockControl);

        y += margin;
        xCoordControl = new TextFieldWidget(fontRenderer, width / 2 - 50, y, 140, 14, new StringTextComponent(I18n.format(Names.Config.X_COORD)));
        xCoordControl.setText(ConfigValues.INS.xCoord.get() + "");
        this.addChild(xCoordControl);
        y += margin;
        yCoordControl = new TextFieldWidget(fontRenderer, width / 2 - 50, y, 140, 14, new StringTextComponent(I18n.format(Names.Config.Y_COORD)));
        yCoordControl.setText(ConfigValues.INS.yCoord.get() + "");
        this.addChild(yCoordControl);
        y += margin;
        scaleControl = new TextFieldWidget(fontRenderer, width / 2 - 50, y, 140, 14, new StringTextComponent(I18n.format(Names.Config.SCALE)));
        scaleControl.setText(ConfigValues.INS.scale.get() + "");
        this.addChild(scaleControl);

        y += margin;
        this.addWidget(new Button(width / 2 - 100, y, 200, 20,
                new StringTextComponent(I18n.format(Names.Config.SAVE)), this::save));
        y += margin;
        this.addWidget(new Button(width / 2 - 100, y, 200, 20,
                new StringTextComponent(I18n.format("gui.done")), b -> mc.displayGuiScreen(parent)));
        y += margin;
        this.addWidget(new Button(width / 2 - 100, y, 200, 20,
                new StringTextComponent(I18n.format(Names.Config.OPEN_CONFIG_FILE)), this::openConfigFile));

    }

    private void openConfigFile(Button btn) {
        Util.getOSType().openFile(FMLPaths.CONFIGDIR.get().resolve(Reference.MOD_ID + "-client.toml").toFile());
    }

    private void save(Button button) {
        ConfigValues.INS.guiActive.set(guiActiveControl.isChecked());
        ConfigValues.INS.showDayCount.set(showDayCountControl.isChecked());
        ConfigValues.INS.centerClock.set(centerClockControl.isChecked());

        safeParseInt(xCoordControl.getText(), s -> {
            xCoordControl.setText(ConfigValues.INS.xCoord.get() + "");
        }).ifPresent(ConfigValues.INS.xCoord::set);

        safeParseInt(yCoordControl.getText(), s -> {
            yCoordControl.setText(ConfigValues.INS.yCoord.get() + "");
        }).ifPresent(ConfigValues.INS.yCoord::set);

        safeParseDouble(scaleControl.getText(), s -> {
            scaleControl.setText(ConfigValues.INS.scale.get() + "");
        }).ifPresent(ConfigValues.INS.scale::set);
    }

    private Optional<Integer> safeParseInt(String str, Consumer<String> onError) {
        Integer value = null;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            onError.accept(e.getMessage());
        }
        return Optional.ofNullable(value);
    }

    private Optional<Double> safeParseDouble(String str, Consumer<String> onError) {
        Double value = null;
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            onError.accept(e.getMessage());
        }
        return Optional.ofNullable(value);
    }

    private <T extends Widget> T addWidget(T widget) {
        return super.func_230480_a_(widget);
    }

    private <T extends IGuiEventListener> T addChild(T item) {
        return super.func_230481_d_(item);
    }

}
