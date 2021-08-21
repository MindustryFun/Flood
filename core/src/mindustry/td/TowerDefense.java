package mindustry.td;

import arc.Events;
import arc.math.Mathf;
import arc.util.Log;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.state;

public class TowerDefense {

    public static void init() {

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

                ItemStack[] is = UnitDrops.drops.get(unit.type);
                if(is ==  null || state.gameOver) return;

                StringBuilder message = new StringBuilder();

                for(ItemStack stack : is){
                    Item item = stack.item;
                    int amount = stack.amount;

                    // TODO: fair drops (pvp compatible)
                    /*
                    if(item != null && unit) { // unit.target == core
                        int calc = Mathf.random(amount - amount / 2, amount + amount / 2);
                        message.append("[accent]+").append(calc).append("[] ").append(item.emoji()).append("  ");
                        amount = depositCore.tile.build.acceptStack(item, calc, Team.sharded.core());
                        if (amount > 0) {
                            Call.transferItemTo(unit, item, amount, unit.x + Mathf.range(2f), unit.y + Mathf.range(2f), state.core);
                        }
                    }

                     */
                }
                /*
                String msg = message.toString();
                for(Player p : Groups.player) {
                    if(state.huds.containsKey(p.uuid()) && state.huds.get(p.uuid())) {
                        Call.label(p.con, msg, Strings.stripColors(msg.replaceAll(" ", "")).length() / 14f, unit.x + Mathf.range(-2f, 2f), unit.y + Mathf.range(-2f, 2f));
                    }
                }
                 */
            }
        });

        Log.info("TowerDefense inited");
    }

}
