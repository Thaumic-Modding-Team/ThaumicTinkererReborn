package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.client.renderer.entity.RenderGasPhial;
import mod.emt.thaumictinkerer.entity.EntityGasPhial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntitiesTT {
    private static int ENTITY_START_ID = 100;

    public static void registerEntities() {
        EntityRegistry.registerModEntity(new ResourceLocation(ThaumicTinkerer.MOD_ID, "gas_phial"), EntityGasPhial.class,
                "gas_phial", ENTITY_START_ID++, ThaumicTinkerer.instance, 50, 1, true);
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGasPhial.class, RenderGasPhial::new);
    }
}
