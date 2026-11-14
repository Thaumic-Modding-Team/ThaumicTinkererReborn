package mod.emt.thaumictinkerer.client.renderer.entity;

import mod.emt.thaumictinkerer.entity.EntityGasPhial;
import mod.emt.thaumictinkerer.registry.ModItemsTT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderGasPhial extends RenderSnowball<EntityGasPhial> {
    public RenderGasPhial(RenderManager renderManagerIn) {
        super(renderManagerIn, ModItemsTT.GAS_PHIAL_LIGHT, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public @NotNull ItemStack getStackToRender(EntityGasPhial entityIn) {
        return entityIn.getItem();
    }
}
