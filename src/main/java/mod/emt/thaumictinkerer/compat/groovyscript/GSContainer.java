package mod.emt.thaumictinkerer.compat.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import mod.emt.thaumictinkerer.compat.groovyscript.handlers.NecromancyTablet;

public class GSContainer extends GroovyPropertyContainer {
    public final NecromancyTablet NecromancyTablet = new NecromancyTablet();

    public GSContainer() {
        this.addProperty(NecromancyTablet);
    }
}
