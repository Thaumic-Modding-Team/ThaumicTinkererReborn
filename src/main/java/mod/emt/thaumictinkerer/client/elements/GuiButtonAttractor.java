package mod.emt.thaumictinkerer.client.elements;

import mod.emt.thaumictinkerer.client.gui.AbstractGuiAttractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class GuiButtonAttractor extends GuiButton {
    private final int textureX;
    private final int textureY;
    private boolean toggled;

    public GuiButtonAttractor(int buttonId, int x, int y, boolean isToggled) {
        super(buttonId, x, y, 11, 11, "");
        this.textureX = 176;
        this.textureY = 0;
        this.toggled = isToggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    @Override
    public void drawButton(@NotNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if(this.visible) {
            FontRenderer fontRenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(AbstractGuiAttractor.TEXTURE);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.isToggled() ? 1 : 0;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, textureX, textureY + state * this.height, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 0xe0e0e0;

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!this.enabled) {
                j = 0xa0a0a0;
            } else if (this.hovered) {
                j = 0xffffa0;
            }

            this.drawCenteredString(fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {

    }
}
