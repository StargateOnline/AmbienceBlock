package com.sekai.ambienceblock.client.gui.ambience.tabs;

import com.sekai.ambienceblock.client.gui.widgets.button.CheckboxButton;
import com.sekai.ambienceblock.client.gui.widgets.TextFieldWidget;
import com.sekai.ambienceblock.client.gui.widgets.TextInstance;
import com.sekai.ambienceblock.client.gui.ambience.AmbienceGUI;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntityData;
import com.sekai.ambienceblock.util.ParsingUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class DelayTab extends AbstractTab
{
    private static final String MIN_DELAY = I18n.format("gui.ambienceblock.min") + " " + I18n.format("gui.ambienceblock.delay");
    private static final String MAX_DELAY = I18n.format("gui.ambienceblocks.max") + " " + I18n.format("gui.ambienceblock.delay");

    private static final String MIN_VOLUME = I18n.format("gui.ambienceblock.min") + " " + I18n.format("gui.ambienceblock.volume");
    private static final String MAX_VOLUME = I18n.format("gui.ambienceblock.max") + " " + I18n.format("gui.ambienceblock.volume");

    private static final String MIN_PITCH = I18n.format("gui.ambienceblock.min") + " " + I18n.format("gui.ambienceblock.pitch");
    private static final String MAX_PITCH = I18n.format("gui.ambienceblock.max") + " " + I18n.format("gui.ambienceblock.pitch");

    public TextInstance textMinDelay = new TextInstance(getBaseX(), getRowY(0) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, MIN_DELAY + " :", font);
    public TextFieldWidget minDelay = new TextFieldWidget(font, getNeighbourX(textMinDelay), getRowY(0), 50, 20, "name");

    public TextInstance textMaxDelay = new TextInstance(getNeighbourX(minDelay), getRowY(0) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, MAX_DELAY + " :", font);
    public TextFieldWidget maxDelay = new TextFieldWidget(font, getNeighbourX(textMaxDelay), getRowY(0), 50, 20, "name");

    public TextInstance textMinVolume = new TextInstance(0, 0, 0xFFFFFF, MIN_VOLUME + " :", font);
    public TextFieldWidget minVolume = new TextFieldWidget(font, 0, 0, 50, 20, "name");

    public TextInstance textMaxVolume = new TextInstance(0, 0, 0xFFFFFF, MAX_VOLUME + " :", font);
    public TextFieldWidget maxVolume = new TextFieldWidget(font, 0, 0, 50, 20, "name");

    public TextInstance textMinPitch = new TextInstance(0, 0, 0xFFFFFF, MIN_PITCH + " :", font);
    public TextFieldWidget minPitch = new TextFieldWidget(font, 0, 0, 50, 20, "name");

    public TextInstance textMaxPitch = new TextInstance(0, 0, 0xFFFFFF, MAX_PITCH + " :", font);
    public TextFieldWidget maxPitch = new TextFieldWidget(font, 0, 0, 50, 20, "name");

    public CheckboxButton canPlayOverSelf = new CheckboxButton(getBaseX(), getRowY(1), 20 + font.getStringWidth("Can play over itself"), 20, "Can play over itself", false);
    public CheckboxButton shouldStopPrevious = new CheckboxButton(getBaseX(), getRowY(2), 20 + font.getStringWidth("Should stop previous instance"), 20, "Should stop previous instance", false);

    public DelayTab(AmbienceGUI guiRef) {
        super(guiRef);
    }

    @Override
    public String getName() {
        return "Delay";
    }

    @Override
    public void initialInit() {
        minDelay.setValidator(ParsingUtil.numberFilter);
        minDelay.setMaxStringLength(8);
        minDelay.setText(String.valueOf(0));
        maxDelay.setValidator(ParsingUtil.numberFilter);
        maxDelay.setMaxStringLength(8);
        maxDelay.setText(String.valueOf(0));

        minVolume.setValidator(ParsingUtil.decimalNumberFilter);
        minVolume.setMaxStringLength(8);
        minVolume.setText(String.valueOf(0));
        maxVolume.setValidator(ParsingUtil.decimalNumberFilter);
        maxVolume.setMaxStringLength(8);
        maxVolume.setText(String.valueOf(0));

        minPitch.setValidator(ParsingUtil.decimalNumberFilter);
        minPitch.setMaxStringLength(8);
        minPitch.setText(String.valueOf(0));
        maxPitch.setValidator(ParsingUtil.decimalNumberFilter);
        maxPitch.setMaxStringLength(8);
        maxPitch.setText(String.valueOf(0));

        shouldStopPrevious.active = false;
        shouldStopPrevious.visible = false;

        addWidget(minDelay);
        addWidget(maxDelay);

        addWidget(minVolume);
        addWidget(maxVolume);

        addWidget(minPitch);
        addWidget(maxPitch);

        addWidget(canPlayOverSelf);
        addWidget(shouldStopPrevious);
    }

    @Override
    public void updateWidgetPosition() {
        textMinDelay.x = getBaseX(); textMinDelay.y = getRowY(0) + getOffsetY(font.FONT_HEIGHT);
        minDelay.x = getNeighbourX(textMinDelay); minDelay.y = getRowY(0);
        textMaxDelay.x = getNeighbourX(minDelay); textMaxDelay.y = getRowY(0) + getOffsetY(font.FONT_HEIGHT);
        maxDelay.x = getNeighbourX(textMaxDelay); maxDelay.y = getRowY(0);

        textMinVolume.x = getBaseX(); textMinVolume.y = getRowY(1) + getOffsetY(font.FONT_HEIGHT);
        minVolume.x = getNeighbourX(textMinVolume); minVolume.y = getRowY(1);
        textMaxVolume.x = getNeighbourX(minVolume); textMaxVolume.y = getRowY(1) + getOffsetY(font.FONT_HEIGHT);
        maxVolume.x = getNeighbourX(textMaxVolume); maxVolume.y = getRowY(1);

        textMinPitch.x = getBaseX(); textMinPitch.y = getRowY(2) + getOffsetY(font.FONT_HEIGHT);
        minPitch.x = getNeighbourX(textMinPitch); minPitch.y = getRowY(2);
        textMaxPitch.x = getNeighbourX(minPitch); textMaxPitch.y = getRowY(2) + getOffsetY(font.FONT_HEIGHT);
        maxPitch.x = getNeighbourX(textMaxPitch); maxPitch.y = getRowY(2);

        canPlayOverSelf.x = getBaseX(); canPlayOverSelf.y = getRowY(3);
        shouldStopPrevious.x = getBaseX(); shouldStopPrevious.y = getRowY(4);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        textMinDelay.render(mouseX, mouseY);
        minDelay.render(mouseX, mouseY, partialTicks);

        textMaxDelay.render(mouseX, mouseY);
        maxDelay.render(mouseX, mouseY, partialTicks);

        textMinVolume.render(mouseX, mouseY);
        minVolume.render(mouseX, mouseY, partialTicks);
        textMaxVolume.render(mouseX, mouseY);
        maxVolume.render(mouseX, mouseY, partialTicks);
        textMinPitch.render(mouseX, mouseY);
        minPitch.render(mouseX, mouseY, partialTicks);
        textMaxPitch.render(mouseX, mouseY);
        maxPitch.render(mouseX, mouseY, partialTicks);

        canPlayOverSelf.render(mouseX, mouseY, partialTicks);
        shouldStopPrevious.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY)
    {
        List<String> list = new ArrayList<String>();

        if(textMinDelay.isHovered()) {
            list.add(TextFormatting.RED + "Minimum Delay");
            list.add("Should be smaller than max.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
        if(textMaxDelay.isHovered()) {
            list.add(TextFormatting.RED + "Maximum Delay");
            list.add("Ticking for the delay will keep going as long as the block is loaded, so don't hesitate making this long.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if(textMinVolume.isHovered()) {
            list.add(TextFormatting.RED + "Minimum Volume");
            list.add("If the volume is random between two bounds then the lower bound is <tile's volume> - <value>");
            list.add(TextFormatting.GRAY + "0 on both min and max means that the sound won't have a random volume.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
        if(textMaxVolume.isHovered()) {
            list.add(TextFormatting.RED + "Maximum Volume");
            list.add("If the volume is random between two bounds then the upper bound is <tile's volume> + <value>");
            list.add(TextFormatting.GRAY + "0 on both min and max means that the sound won't have a random volume.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if(textMinPitch.isHovered()) {
            list.add(TextFormatting.RED + "Minimum Pitch");
            list.add("If the pitch is random between two bounds then the lower bound is <tile's pitch> - <value>");
            list.add(TextFormatting.GRAY + "0 on both min and max means that the sound won't have a random pitch.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
        if(textMaxPitch.isHovered()) {
            list.add(TextFormatting.RED + "Maximum Pitch");
            list.add("If the pitch is random between two bounds then the upper bound is <tile's pitch> + <value>");
            list.add(TextFormatting.GRAY + "0 on both min and max means that the sound won't have a random pitch.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if(canPlayOverSelf.isHovered()) {
            list.add("If this block's delay reaches the end before a previous sound finishes playing, it won't care and play anyway when this is checked.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
        if(shouldStopPrevious.isHovered()) {
            list.add("If this block plays over a previous sound it will stop the previous instance and play the new one.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
    }

    @Override
    public void tick() {
        minDelay.tick();
        maxDelay.tick();

        minVolume.tick();
        maxVolume.tick();

        minPitch.tick();
        maxPitch.tick();

        shouldStopPrevious.active = canPlayOverSelf.isChecked();
        shouldStopPrevious.visible = canPlayOverSelf.isChecked();
    }

    @Override
    public void setFieldFromData(AmbienceTileEntityData data) {
        minDelay.setText(String.valueOf(data.getMinDelay()));
        maxDelay.setText(String.valueOf(data.getMaxDelay()));

        minVolume.setText(String.valueOf(data.getMinRandVolume()));
        maxVolume.setText(String.valueOf(data.getMaxRandVolume()));

        minPitch.setText(String.valueOf(data.getMinRandPitch()));
        maxPitch.setText(String.valueOf(data.getMaxRandPitch()));

        canPlayOverSelf.setChecked(data.canPlayOverSelf());
        shouldStopPrevious.setChecked(data.shouldStopPrevious());
    }

    @Override
    public void setDataFromField(AmbienceTileEntityData data) {
        data.setMinDelay(ParsingUtil.tryParseInt(minDelay.getText()));
        data.setMaxDelay(ParsingUtil.tryParseInt(maxDelay.getText()));

        data.setMinRandVolume(ParsingUtil.tryParseFloat(minVolume.getText()));
        data.setMaxRandVolume(ParsingUtil.tryParseFloat(maxVolume.getText()));

        data.setMinRandPitch(ParsingUtil.tryParseFloat(minPitch.getText()));
        data.setMaxRandPitch(ParsingUtil.tryParseFloat(maxPitch.getText()));

        data.setCanPlayOverSelf(canPlayOverSelf.isChecked());
        if(data.canPlayOverSelf()) data.setShouldStopPrevious(shouldStopPrevious.isChecked());
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {

    }
}
