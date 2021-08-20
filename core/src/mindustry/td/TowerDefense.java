package mindustry.td;

import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.world.blocks.storage.*;

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
        Events.on(EventType.UnitCreateEvent.class, e -> {
            if (e.unit.team == state.rules.waveTeam && e.unit.type != UnitTypes.navanax) e.unit.apply(StatusEffects.disarmed, Float.MAX_VALUE);
        });
        Events.on(EventType.WorldLoadEvent.class, e -> {
            state.rules.waveTeam.rules().unitDamageMultiplier = 0; // Disable unit damage so that navanax doesnt break stuff
        });
    }
}
