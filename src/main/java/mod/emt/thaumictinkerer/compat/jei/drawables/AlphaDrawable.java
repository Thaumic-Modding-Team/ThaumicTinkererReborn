package mod.emt.thaumictinkerer.compat.jei.drawables;

import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AlphaDrawable implements IDrawableStatic {
    private final ResourceLocation resourceLocation;
    private final int u;
    private final int v;
    private final int width;
    private final int height;
    private final int paddingTop;
    private final int paddingBottom;
    private final int paddingLeft;
    private final int paddingRight;

    public AlphaDrawable(ResourceLocation resourceLocation, int u, int v, int width, int height) {
        this(resourceLocation, u, v, width, height, 0, 0, 0, 0);
    }

    public AlphaDrawable(ResourceLocation resourceLocation, int u, int v, int width, int height, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        this.resourceLocation = resourceLocation;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
    }

    public int getWidth() {
        return this.width + this.paddingLeft + this.paddingRight;
    }

    public int getHeight() {
        return this.height + this.paddingTop + this.paddingBottom;
    }

    public void draw(@NotNull Minecraft minecraft) {
        this.draw(minecraft, 0, 0);
    }

    public void draw(@NotNull Minecraft minecraft, int xOffset, int yOffset) {
        this.draw(minecraft, xOffset, yOffset, 0, 0, 0, 0);
    }

    public void draw(Minecraft minecraft, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
        minecraft.getTextureManager().bindTexture(this.resourceLocation);
        GlStateManager.enableBlend();
        int x = xOffset + this.paddingLeft + maskLeft;
        int y = yOffset + this.paddingTop + maskTop;
        int u = this.u + maskLeft;
        int v = this.v + maskTop;
        int width = this.width - maskRight - maskLeft;
        int height = this.height - maskBottom - maskTop;
        Gui.drawModalRectWithCustomSizedTexture(x, y, (float)u, (float)v, width, height, 256.0f, 256.0f);
        GlStateManager.disableBlend();
    }
}
