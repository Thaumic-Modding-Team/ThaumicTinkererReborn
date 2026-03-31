package mod.emt.thaumictinkerer.compat.crafttweaker;

import crafttweaker.mc1120.events.ScriptRunEvent;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.registry.ModRecipesTT;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CTPlugin {
    @SubscribeEvent
    public void onScriptReloading(ScriptRunEvent.Pre event) {
        NecromancyRecipeRegistry.removeAllRecipes();
    }

    @SubscribeEvent
    public void onScriptReloadingPost(ScriptRunEvent.Post event) {
        ModRecipesTT.initNecromancyRecipes();
    }
}
