package com.sekai.ambienceblock.client.gui.widgets.button;

import com.sekai.ambienceblock.AmbienceMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL14;

@SideOnly(Side.CLIENT)
public class CheckboxButton extends AbstractButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(AmbienceMod.MOD_ID,"textures/gui/checkbox.png");
    private boolean checked;

    public CheckboxButton(int x, int y, int width, int height, String text, boolean isChecked)
    {
        super(x, y, width, height, text);
        this.checked = isChecked;
    }

    @Override
    public void onPress()
    {
        this.checked = !checked;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean b)
    {
        if(b && !isChecked())
            onPress();
        if(!b && isChecked())
            onPress();
    }

    @Override
    public void renderButton(int x, int y, float partialTicks) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableDepth();
        FontRenderer font = minecraft.fontRenderer;

        GlStateManager.color(1.0F, 1.0F, 1.0F, this.alpha);
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.factor, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.factor, GlStateManager.SourceFactor.ONE.factor, GlStateManager.DestFactor.ZERO.factor);
        CheckboxButton.drawModalRectWithCustomSizedTexture(this.x, this.y, 0.0f, this.checked ? 20.0f : 0.0f, 20, this.height, 32, 64);
        this.renderBg(minecraft, x, y);
        int color = 14737632;
        this.drawString(font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }
}
