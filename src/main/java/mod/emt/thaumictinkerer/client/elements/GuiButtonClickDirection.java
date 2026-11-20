package mod.emt.thaumictinkerer.client.elements;

import mod.emt.thaumictinkerer.tile.TileDynamismTablet.ClickDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class GuiButtonClickDirection extends GuiButton {
    private final ResourceLocation texture;
    private final int textureX;
    private final int textureY;
    private ClickDirection direction;

    public GuiButtonClickDirection(ResourceLocation texture, int buttonId, int x, int y, int textureX, int textureY, int width, int height, ClickDirection direction) {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.direction = direction;
    }

    public void nextDirection() {
        this.direction = this.direction.getNext();
    }

    @Override
    public void drawButton(@NotNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if(this.visible) {
            FontRenderer fontRenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(texture);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, textureX, textureY + state * this.height, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 0xe0e0e0;

            if (packedFGColour != 0) {
                color = packedFGColour;
            } else if (!this.enabled) {
                color = 0xa0a0a0;
            } else if (this.hovered) {
                color = 0xffffa0;
            }

            String buttonText = I18n.format(this.direction.getTranslationKey());
            this.drawCenteredString(fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
        }
    }
}
