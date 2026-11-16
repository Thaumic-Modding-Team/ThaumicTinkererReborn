package mod.emt.thaumictinkerer.client.gui;

import mod.emt.thaumictinkerer.client.elements.GuiButtonAttractor;
import mod.emt.thaumictinkerer.inventory.containers.ContainerPlayerAttractor;
import mod.emt.thaumictinkerer.tile.TilePlayerAttractor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiPlayerAttractor extends AbstractGuiAttractor<ContainerPlayerAttractor> {
    public GuiPlayerAttractor(ContainerPlayerAttractor container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        TilePlayerAttractor attractor = this.getContainer().getAttractor();
        this.addButton(new GuiButtonAttractor(ContainerPlayerAttractor.BUTTON_WHITELIST, this.getGuiLeft() + 84, this.getGuiTop() + 31, attractor.isWhitelist()));
        this.addButton(new GuiButtonAttractor(ContainerPlayerAttractor.BUTTON_IGNORE_SNEAK, this.getGuiLeft() + 84, this.getGuiTop() + 45, attractor.ignoreSneak()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.whitelist"), 98, 33, 0x0e0e0e);
        this.fontRenderer.drawString(I18n.format("gui.thaumictinkerer:attractor.ignore_sneak"), 98, 47, 0x0e0e0e);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(!button.enabled || !button.visible)
            return;

        if(button.id == ContainerPlayerAttractor.BUTTON_WHITELIST || button.id == ContainerPlayerAttractor.BUTTON_IGNORE_SNEAK) {
            if(button instanceof GuiButtonAttractor) {
                ((GuiButtonAttractor) button).setToggled(!((GuiButtonAttractor) button).isToggled());
                this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
            }
        }
    }
}
