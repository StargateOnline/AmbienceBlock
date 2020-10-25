package com.sekai.ambienceblock.client.gui.widgets;

import org.lwjgl.opengl.GL14;
import com.sekai.ambienceblock.client.gui.IGuiEventHandler;
import com.sekai.ambienceblock.client.gui.IRenderable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@SideOnly(Side.CLIENT)
public class Widget extends Gui implements IGuiEventHandler, IRenderable
{
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    private static final int NARRATE_DELAY_MOUSE = 750;
    private static final int NARRATE_DELAY_FOCUS = 200;

    public int width;
    public int height;

    public int x;
    public int y;

    public String message;

    private boolean wasHovered;
    protected boolean isHovered;
    public boolean active = true;
    public boolean visible = true;
    protected float alpha = 1.0f;
    private boolean focused;

    public Widget(int x, int y, String text)
    {
        this(x, y, 200, 20, text);
    }

    public Widget(int x, int y, int width, int height, String text)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = text;
    }

    protected int getYImage(boolean bl) {
        int n = 1;
        if (!this.active) {
            n = 0;
        } else if (bl) {
            n = 2;
        }
        return n;
    }

    @Override
    public void render(int x, int y, float partialTicks) {
        if (!this.visible) {
            return;
        }
        boolean bl = this.isHovered = x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
        if (this.visible) {
            this.renderButton(x, y, partialTicks);
        }
        this.wasHovered = this.isHovered();
    }

    public void renderButton(int x, int y, float partialTicks) {
        Minecraft minecraft = Minecraft.getMinecraft();
        FontRenderer font = minecraft.fontRenderer;

        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color(1.0F, 1.0F, 1.0F, this.alpha);

        int imageId = this.getYImage(this.isHovered());

        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.factor, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.factor, GlStateManager.SourceFactor.ONE.factor, GlStateManager.DestFactor.ZERO.factor);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.drawTexturedModalRect(this.x, this.y, 0, 46 + imageId * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + imageId * 20, this.width / 2, this.height);

        this.renderBg(minecraft, x, y);
        int color = this.active ? 16777215 : 10526880;
        this.drawCenteredString(font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }

    protected void renderBg(Minecraft minecraft, int x, int y) {
    }

    public void onClick(double x, double y) {
    }

    public void onRelease(double x, double y) {
    }

    protected void onDrag(double x1, double y1, double x2, double y2) {
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (this.isValidClickButton(mouseButton) && this.clicked(x, y)) {
            this.playDownSound(Minecraft.getMinecraft().getSoundHandler());
            this.onClick(x, y);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int mouseButton) {
        if (this.isValidClickButton(mouseButton)) {
            this.onRelease(x, y);
            return true;
        }
        return false;
    }

    protected boolean isValidClickButton(int n) {
        return n == 0;
    }

    @Override
    public boolean mouseDragged(double x, double y, int mouseButton, double x2, double y2) {
        if (this.isValidClickButton(mouseButton)) {
            this.onDrag(x, y, x2, y2);
            return true;
        }
        return false;
    }

    protected boolean clicked(double d, double d2) {
        return this.active && this.visible && d >= (double)this.x && d2 >= (double)this.y && d < (double)(this.x + this.width) && d2 < (double)(this.y + this.height);
    }

    public boolean isHovered() {
        return this.isHovered || this.focused;
    }

    @Override
    public boolean changeFocus(boolean bl) {
        if (!this.active || !this.visible) {
            return false;
        }
        this.focused = !this.focused;
        this.onFocusedChanged(this.focused);
        return this.focused;
    }

    protected void onFocusedChanged(boolean bl) {
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        return this.active && this.visible && x >= (double)this.x && y >= (double)this.y && x < (double)(this.x + this.width) && y < (double)(this.y + this.height);
    }

    public void renderToolTip(int n, int n2) {
    }

    public void playDownSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setMessage(String string) {
        this.message = string;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isFocused() {
        return this.focused;
    }

    protected void setFocused(boolean bl) {
        this.focused = bl;
    }
}
