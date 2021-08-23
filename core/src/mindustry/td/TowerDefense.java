package mindustry.td;

import arc.Events;
import arc.math.Mathf;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.units.AIController;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Menus;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;

import java.util.HashMap;

import static mindustry.Vars.*;

public class TowerDefense {

    public static HashMap<Item, String> itemIcons = new HashMap<>();

    static String getTrafficlightColor(double value){
        return "#"+Integer.toHexString(java.awt.Color.HSBtoRGB((float)value/3f, 1f, 1f)).substring(2);
    }

    public static void init() {

        // this has to be done ...
        itemIcons.put(Items.copper, "\uF838");
        itemIcons.put(Items.lead, "\uF837");
        itemIcons.put(Items.metaglass, "\uF836");
        itemIcons.put(Items.graphite, "\uF835");
        itemIcons.put(Items.sand, "\uF834");
        itemIcons.put(Items.coal, "\uF833");
        itemIcons.put(Items.titanium, "\uF832");
        itemIcons.put(Items.thorium, "\uF831");
        itemIcons.put(Items.scrap, "\uF830");
        itemIcons.put(Items.silicon, "\uF82F");
        itemIcons.put(Items.plastanium, "\uF82E");
        itemIcons.put(Items.phaseFabric, "\uF82D");
        itemIcons.put(Items.surgeAlloy, "\uF82C");
        itemIcons.put(Items.sporePod, "\uF82B");
        itemIcons.put(Items.blastCompound, "\uF82A");
        itemIcons.put(Items.pyratite, "\uF829");

        Vars.netServer.admins.addActionFilter(action -> {
            Player player = action.player;
            if (player == null) return true;

            if(action.type == Administration.ActionType.placeBlock){
                boolean b = action.block == Blocks.shockMine || action.block instanceof CoreBlock || checkPlacement(action.tile, action.block);
                if(!b && player.con != null) Call.label(player.con, "[scarlet]\uE868", 4f, action.tile.worldx(), action.tile.worldy());
                return b;
            }

            if ((action.type == Administration.ActionType.depositItem || action.type == Administration.ActionType.withdrawItem) && action.tile != null && action.tile.block() != null && action.tile.block() instanceof CoreBlock) {
                if (player.con != null)
                    Call.label(player.con, "[scarlet]\uE868", 4f, action.tile.worldx(), action.tile.worldy());

                return false;
            }
            return true;
        });

        // Disable enemy attacks, navanax have an exception to allow for their EMP to be fired
        // TODO: Maybe instead of the navanax check below, we can use Call.createBullet to spawn the EMP instead?
        Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (e.unit.team() == state.rules.waveTeam && e.unit.type() != UnitTypes.navanax) e.unit.apply(StatusEffects.disarmed, Float.MAX_VALUE);
            e.unit.damageMultiplier = 0f;
        });

        Events.on(EventType.UnitCreateEvent.class, e -> {
            if(e.unit.team() != state.rules.defaultTeam || e.unit.isPlayer() || !(e.spawner.block instanceof Reconstructor || e.spawner.block instanceof UnitFactory)) return;
            float deathTime = state.rules.unitBuildSpeed(e.unit.team) * (e.spawner.block instanceof Reconstructor ? ((Reconstructor)e.spawner.block).constructTime : 60 * 5f);
            Timer.schedule (e.unit::kill, deathTime);
        });

        Events.on(EventType.WorldLoadEvent.class, e -> {
            state.multiplier = 1f;
        });

        Events.on(EventType.WaveEvent.class, e -> {
            int wave = state.wave;
            state.multiplier = Mathf.clamp(((wave * wave / 1000f) + 0.5f), state.multiplier, 42069f); // if u reach this number u are god
        });

        Events.on(EventType.PlayerJoin.class, e -> {
            if(e.player.getInfo().timesJoined <= 1) {
                String[][] options1 = {
                        {"[#49e87c]\uE875 Take the tutorial[]"},
                        {"[#e85e49]⚠ Skip (not recommended)[]"}
                };
                Call.menu(e.player.con, 1, "[accent]Welcome![]", "Looks like it's your first time playing..", options1);
            }
        });

        String[][] tutOptions = {{"[#49e87c]\uE829 Continue[]"}};
        String[][] tutFinal = {{"[#49e87c]\uE829 Finish[]"}};
        String[] tutEntries = {
        "[accent]\uE875[] Tutorial 1/2", "In [accent]\uE86D tower defense[] units [scarlet]cannot[] shoot, they can only explode when they approach the core. Most units follow a fixed path so it is easy to setup defenses against them.",
        "[accent]\uE875[] Tutorial 2/2", "You have to kill [accent]\uE86D enemy units[] before they reach your core. You [scarlet]cannot[] deliver any resources to your core, as they will drop as [accent]loot[] from killed foes.",
        "[white]\uF848 & \uF790[]", "[accent]Overclock Turrets[]\nInstead of healing, [accent]repair points[] actively boost friendly units' damage & reload speed by 2x.",
        "[white]\uF89B & \uF89A[]", "[accent]Elecro Fields[]\nInstead of healing, [accent]menders[] damage & slow down enemies hit by the wave. (good combo with arcs & lancers)",
        "[white]\uF898[]", "[accent]Sap Projector[]\n[accent]Force Projectors[] slow down enemies caught in the field & make them take more damage.",
        "[white]\uF780[]", "[accent]EMP[]\n[accent]The navanax unit[] shoots deadly EMP missiles that take out your turrets & assembly lines for a short period of time.",
        "[white]\uF7F1[]", "[accent]Mono Sacrifice[]\n[accent]Monos[] circle the core and sacrifice into it to heal it when it's low HP. This is the only way to heal the core.",
        "[white]\uE88F[]", "[accent]Chat Commands[]\nYou can hide the right side HUD with \n    [accent] - /hud[]\nYou can hide unit drops with \n    [accent] - /drops[]"
        };

        for(int i = 0; i < tutEntries.length / 2; i++){
            int j = i;
            Menus.registerMenu(i + 1, (player, selection) -> {
                if(selection == 0)
                    Call.menu(player.con, j + 2, tutEntries[2 * j], tutEntries[2 * j + 1], j == tutEntries.length / 2 - 1 ? tutFinal : tutOptions);
            });
        }

        Timer.schedule(() -> {
            for (Player p : Groups.player) {
                if (p.td_hud_toggle)
                    Call.infoPopup(p.con, "\uE86D [accent]Unit health multiplier:[" + getTrafficlightColor(Mathf.clamp(1f - (state.multiplier / 10f), 0f, 1f)) + "] " + (String.valueOf(state.multiplier).length() > 3 ? String.valueOf(state.multiplier).substring(0, 4) : state.multiplier) + "x", 10f, 20, 50, 20, 450, 0);
            }
        }, 0, 10);


        Events.on(EventType.UnitDestroyEvent.class, e -> {
            if (e.unit.team() == state.rules.waveTeam) {
                Unit unit = e.unit;

                AIController controller = unit.controller() instanceof AIController ? (AIController) unit.controller() : null;
                if(controller == null) return;

                CoreBlock.CoreBuild core = controller.target != null ? controller.target.team().core() : unit.closestEnemyCore();
                if(core == null) return;

                ItemStack[] is = UnitDrops.drops.get(unit.type);
                if(is ==  null || state.gameOver) return;

                StringBuilder message = new StringBuilder();

                for(ItemStack stack : is){
                    Item item = stack.item;
                    int amount = stack.amount;


                    if(item != null) {
                        // Call.sendMessage(item.name + " -> " + item.emoji()); // debug
                        int calc = Mathf.random(amount - amount / 2, amount + amount / 2); // todo: remove RNG maybe? - not good with pvp
                        message.append("[accent]+").append(calc).append("[] ").append(itemIcons.getOrDefault(item, "[scarlet]?[]")).append("  ");
                        amount = core.tile.build.acceptStack(item, calc, core);
                        if (amount > 0) {
                            Call.transferItemTo(unit, item, amount, unit.x + Mathf.range(8f), unit.y + Mathf.range(8f), core);
                        }
                    }
                }

                String msg = message.toString();
                for(Player p : Groups.player) {
                    if(p.td_drops_hud_toggle) {
                        Call.label(p.con, msg, Strings.stripColors(msg.replaceAll(" ", "")).length() / 14f, unit.x + Mathf.range(-4f, 4f), unit.y + Mathf.range(-4f, 4f));
                    }
                }
            }
        });

        Log.info("TowerDefense inited");
    }

    public static boolean checkPlacement(Tile tile, Block block){
        int offsetx = -(block.size - 1) / 2;
        int offsety = -(block.size - 1) / 2;

        for(int dx = 0; dx < block.size; dx++){
            for(int dy = 0; dy < block.size; dy++){
                int wx = dx + offsetx + tile.x, wy = dy + offsety + tile.y;

                Tile check = world.tile(wx, wy);

                if (check.floor() == Blocks.darkPanel4 || check.floor() == Blocks.darkPanel5) return false;
            }
        }
        return true;
    }
}
