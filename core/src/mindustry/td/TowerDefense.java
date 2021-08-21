package mindustry.td;

import arc.Events;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Strings;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.units.AIController;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.*;

import java.util.HashMap;

import static mindustry.Vars.state;

public class TowerDefense {

    public static HashMap<Item, String> itemIcons = new HashMap<>();

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
                boolean b = (action.tile.floor() != Blocks.darkPanel4 && action.tile.floor() != Blocks.darkPanel5) || action.block == Blocks.shockMine;
                if(!b && player.con != null) Call.infoToast(player.con, "[accent]You can not build on the enemy path.", 8f);
                return b;
            }

            if ((action.type == Administration.ActionType.depositItem || action.type == Administration.ActionType.withdrawItem) && action.tile != null && action.tile.block() != null && action.tile.block() instanceof CoreBlock) {
                if (player.con != null)
                    Call.infoToast(player.con, "[accent]You can not interact with the core.", 8f);

                return false;
            }
            return true;
        });

        // Disable enemy attacks, navanax have an exception to allow for their EMP to be fired
        // TODO: Maybe instead of the navanax check below, we can use Call.createBullet to spawn the EMP instead?
        // TODO: make it work like spores in flood ^^
        Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (e.unit.team() == state.rules.waveTeam) e.unit.apply(StatusEffects.disarmed, Float.MAX_VALUE);
        });


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
                    if(p.td_hud_toggle) {
                        Call.label(p.con, msg, Strings.stripColors(msg.replaceAll(" ", "")).length() / 14f, unit.x + Mathf.range(-4f, 4f), unit.y + Mathf.range(-4f, 4f));
                    }
                }
            }
        });

        Log.info("TowerDefense inited");
    }

}
