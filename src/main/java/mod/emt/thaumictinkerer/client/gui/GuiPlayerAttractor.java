package mod.emt.thaumictinkerer.client.gui;

import mod.emt.thaumictinkerer.client.elements.GuiButtonToggleable;
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
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerPlayerAttractor.BUTTON_WHITELIST, this.getGuiLeft() + 84, this.getGuiTop() + 31, 176, 0, 11, 11, attractor.isWhitelist()));
        this.addButton(new GuiButtonToggleable(TEXTURE, ContainerPlayerAttractor.BUTTON_IGNORE_SNEAK, this.getGuiLeft() + 84, this.getGuiTop() + 45, 176, 0, 11, 11, attractor.ignoreSneak()));
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
            if(button instanceof GuiButtonToggleable) {
                ((GuiButtonToggleable) button).setToggled(!((GuiButtonToggleable) button).isToggled());
                this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, button.id);
            }
        }
    }
}
