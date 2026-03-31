package mod.emt.thaumictinkerer.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import com.cleanroommc.groovyscript.documentation.linkgenerator.LinkGeneratorHooks;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GSPlugin implements GroovyPlugin {
    public static GSContainer instance;

    @Override
    public @NotNull String getModId() {
        return ThaumicTinkerer.MOD_ID;
    }

    @Override
    public @NotNull String getContainerName() {
        return ThaumicTinkerer.MOD_NAME;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        LinkGeneratorHooks.registerLinkGenerator(new GSLinkGenerator());
    }

    @Override
    public @Nullable GroovyPropertyContainer createGroovyPropertyContainer() {
        return instance == null ? instance = new GSContainer() : instance;
    }
}
