package com.qkninja.clockhud.client.gui;

import com.qkninja.clockhud.reference.ConfigValues;
import com.qkninja.clockhud.reference.Names;
import com.qkninja.clockhud.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
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

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        font.drawString(I18n.format(Names.Config.X_COORD), xCoordControl.x - 50, xCoordControl.y, 0xFFFFFF);
        xCoordControl.render(mouseX, mouseY, partialTicks);
        font.drawString(I18n.format(Names.Config.Y_COORD), yCoordControl.x - 50, yCoordControl.y, 0xFFFFFF);
        yCoordControl.render(mouseX, mouseY, partialTicks);
        font.drawString(I18n.format(Names.Config.SCALE), scaleControl.x - 50, scaleControl.y, 0xFFFFFF);
        scaleControl.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        int y = 2;
        int margin = 22;
        guiActiveControl = new CheckboxButton(this.width / 2 - 100, y, 200, 20,
                I18n.format(Names.Config.GUI_ACTIVE), ConfigValues.INS.guiActive.get());
        this.addButton(guiActiveControl);
        y += margin;
        showDayCountControl = new CheckboxButton(this.width / 2 - 100, y, 200, 20,
                I18n.format(Names.Config.SHOW_DAY_COUNT), ConfigValues.INS.showDayCount.get());
        this.addButton(showDayCountControl);
        y += margin;
        centerClockControl = new CheckboxButton(this.width / 2 - 100, y, 200, 20,
                I18n.format(Names.Config.CENTER_CLOCK), ConfigValues.INS.centerClock.get());

        this.addButton(centerClockControl);

        y += margin;
        xCoordControl = new TextFieldWidget(font, this.width / 2 - 50, y, 140, 14, "xCoord");
        xCoordControl.setText(ConfigValues.INS.xCoord.get() + "");
        this.children.add(xCoordControl);
        y += margin;
        yCoordControl = new TextFieldWidget(font, this.width / 2 - 50, y, 140, 14, "yCoord");
        yCoordControl.setText(ConfigValues.INS.yCoord.get() + "");
        this.children.add(yCoordControl);
        y += margin;
        scaleControl = new TextFieldWidget(font, this.width / 2 - 50, y, 140, 14, "scale");
        scaleControl.setText(ConfigValues.INS.scale.get() + "");
        this.children.add(scaleControl);

        y += margin;
        this.addButton(new Button(this.width / 2 - 100, y, 200, 20,
                I18n.format(Names.Config.SAVE), this::save));
        y += margin;
        this.addButton(new Button(this.width / 2 - 100, y, 200, 20,
                I18n.format("gui.done"), b -> mc.displayGuiScreen(parent)));
        y += margin;
        this.addButton(new Button(this.width / 2 - 100, y, 200, 20,
                I18n.format(Names.Config.OPEN_CONFIG_FILE), this::openConfigFile));

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

}
