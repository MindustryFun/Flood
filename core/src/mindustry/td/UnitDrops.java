package mindustry.td;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;

public class UnitDrops {
    public static ObjectMap<UnitType, ItemStack[]> drops = ObjectMap.of(
        // Spiders
        UnitTypes.crawler, ItemStack.with(Items.copper, 20, Items.lead, 10, Items.graphite, 3)
        UnitTypes.atrax, ItemStack.with(Items.copper, 30, Items.lead, 40, Items.graphite, 10, Items.titanium, 5)
        UnitTypes.spiroct, ItemStack.with(Items.lead, 100, Items.silicon, 40, Items.graphite, 40, Items.thorium, 10)
        UnitTypes.arkyid, ItemStack.with(Items.copper, 300, Items.graphite, 80, Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)
        UnitTypes.toxopid, ItemStack.with(Items.copper, 400, Items.lead, 400, Items.silicon, 120, Items.graphite, 120, Items.thorium, 40, Items.plastanium, 40, Items.phaseFabric, 5, Items.surgeAlloy, 15)

        // Shooters
        UnitTypes.dagger, ItemStack.with(Items.copper, 20, Items.lead, 10, Items.silicon, 3)
        UnitTypes.mace, ItemStack.with(Items.copper, 30, Items.lead, 40, Items.silicon, 10, Items.titanium, 5)
        UnitTypes.fortress, ItemStack.with(Items.lead, 100, Items.silicon, 40, Items.graphite, 40, Items.thorium, 10)
        UnitTypes.scepter, ItemStack.with(Items.copper, 300, Items.silicon, 80, Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)
        UnitTypes.reign, ItemStack.with(Items.copper, 400, Items.lead, 400, Items.silicon, 120, Items.graphite, 120, Items.thorium, 40, Items.plastanium, 40, Items.phaseFabric, 5, Items.surgeAlloy, 15)

        // Lazers
        UnitTypes.nova, ItemStack.with(Items.copper, 20, Items.lead, 10, Items.metaglass, 3)
        UnitTypes.pulsar, ItemStack.with(Items.copper, 30, Items.lead, 40, Items.metaglass, 10)
        UnitTypes.quasar, ItemStack.with(Items.lead, 100, Items.metaglass, 40, Items.silicon, 40, Items.titanium, 80, Items.thorium, 10)
        UnitTypes.vela, ItemStack.with(Items.copper, 300, Items.metaglass, 80, Items.graphite, 80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5)
        UnitTypes.corvus, ItemStack.with(Items.copper, 400, Items.lead, 400, Items.silicon, 100, Items.metaglass, 120, Items.graphite, 100, Items.titanium, 120, Items.thorium, 60, Items.phaseFabric, 10, Items.surgeAlloy, 10)

        // Flyers
        UnitTypes.flare, ItemStack.with(Items.copper, 20, Items.lead, 10, Items.graphite, 3, Items.scrap, 1)
        UnitTypes.horizon, ItemStack.with(Items.copper, 30, Items.lead, 40, Items.graphite, 10, Items.scrap, 2)
        UnitTypes.zenith, ItemStack.with(Items.lead, 100, Items.silicon, 40, Items.graphite, 40, Items.titanium, 30, Items.plastanium, 10, Items.scrap, 3)
        UnitTypes.antumbra, ItemStack.with(Items.copper, 300, Items.graphite, 80, Items.metaglass, 80, Items.titanium, 60, Items.surgeAlloy, 15, Items.scrap, 4)
        UnitTypes.eclipse, ItemStack.with(Items.copper, 400, Items.lead, 400, Items.silicon, 120, Items.graphite, 120, Items.titanium, 120, Items.thorium, 40, Items.plastanium, 40, Items.phaseFabric, 10, Items.surgeAlloy, 5, Items.scrap, 5)

        // Support
        UnitTypes.mono, ItemStack.with(Items.copper, 20, Items.lead, 10, Items.silicon, 3)
        UnitTypes.poly, ItemStack.with(Items.copper, 30, Items.lead, 40, Items.silicon, 10, Items.titanium, 5)
        UnitTypes.mega, ItemStack.with(Items.lead, 100, Items.silicon, 40, Items.graphite, 40, Items.thorium, 10)
        UnitTypes.quad, ItemStack.with(Items.copper, 300, Items.silicon, 80, Items.metaglass, 80, Items.titanium, 80, Items.thorium, 20, Items.phaseFabric, 10)
        UnitTypes.oct, ItemStack.with(Items.copper, 400, Items.lead, 400, Items.silicon, 120, Items.graphite, 120, Items.thorium, 40, Items.plastanium, 40, Items.phaseFabric, 5, Items.surgeAlloy, 15)

        // Ships
        UnitTypes.risso, ItemStack.with(Items.copper, 20, Items.lead, 10, Items.metaglass, 3)
        UnitTypes.minke, ItemStack.with(Items.copper, 30, Items.lead, 40, Items.metaglass, 10)
        UnitTypes.bryde, ItemStack.with(Items.lead, 100, Items.metaglass, 40, Items.silicon, 40, Items.titanium, 80, Items.thorium, 10)
        UnitTypes.sei, ItemStack.with(Items.copper, 300, Items.metaglass, 80, Items.graphite, 80, Items.titanium, 60, Items.plastanium, 20, Items.surgeAlloy, 5)
        UnitTypes.omura, ItemStack.with(Items.copper, 400, Items.lead, 400, Items.silicon, 100, Items.metaglass, 120, Items.graphite, 100, Items.titanium, 120, Items.thorium, 60, Items.phaseFabric, 10, Items.surgeAlloy, 10)


        // New ships retusa, oxynoe, cyerce, aegires, navanax
        UnitTypes.aegires, ItemStack.with(Items.copper, 8, Items.lead, 2, Items.scrap, 8)
        UnitTypes.oxynoe, ItemStack.with(Items.copper, 12, Items.lead, 4, Items.scrap, 16, Items.silicon, 8, Items.plastanium, 2)
        UnitTypes.cyerce, ItemStack.with(Items.lead, 23, Items.metaglass, 27, Items.scrap, 86, Items.phaseFabric, 2, Items.thorium, 4)
        UnitTypes.aegires, ItemStack.with(Items.silicon, 47, Items.phaseFabric, 8, Items.surgeAlloy, 4, Items.plastanium, 18, Items.thorium, 18)
        UnitTypes.navanax, ItemStack.with(Items.surgeAlloy, 50, Items.phaseFabric, 50)

        // Basic
        UnitTypes.alpha, ItemStack.with(Items.copper, 30, Items.lead, 30, Items.silicon, 20, Items.graphite, 20, Items.metaglass, 20)
        UnitTypes.beta, ItemStack.with(Items.titanium, 40, Items.thorium, 20)
        UnitTypes.gamma, ItemStack.with(Items.plastanium, 20, Items.phaseFabric, 10, Items.surgeAlloy, 10)
    );
}
