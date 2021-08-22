package mindustry.ai.types;

import arc.math.*;
import mindustry.ai.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class GroundAI extends AIController{
    private Pathfinder.Flowfield path = null;

    @Override
    public void init() {
        path = find_path(Pathfinder.fieldCore);
    }

    @Override
    public void updateMovement(){

        Building core = unit.closestEnemyCore();

        if(core != null && unit.within(core, unit.range() / 2f + core.block.size * tilesize / 2f)){
            target = core;
            for(var mount : unit.mounts){
                if(mount.weapon.controllable && mount.weapon.bullet.collidesGround){
                    mount.target = core;
                }
            }
        }

        if((core == null || !unit.within(core, unit.type.range * 0.1f)) && command() == UnitCommand.attack){
            target = target(unit.x, unit.y, unit.range() * 100f, unit.type.targetAir, unit.type.targetGround);

            boolean move = true;

            if(state.rules.waves && unit.team == state.rules.defaultTeam){
                Tile spawner = getClosestSpawner();
                if(spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 120f)) move = false;
            }

            if(move){
                if(unit.team == state.rules.waveTeam) {
                    if (path != null) {
                        move_with_field(path);
                    }else{
                        path = find_path(Pathfinder.fieldCore);
                    }
                }else{
                    if(target != null && target.within(unit, unit.type.range * 5f)) {
                        moveTo(target, unit.type.range * 0.2f);
                        unit.lookAt(target);
                    }else{
                        pathfind(Pathfinder.fieldCore);
                    }
                }
            };
        }

        if(command() == UnitCommand.rally){
            Teamc target = targetFlag(unit.x, unit.y, BlockFlag.rally, false);

            if(target != null && !unit.within(target, 70f)){
                pathfind(Pathfinder.fieldRally);
            }
        }

        if(unit.type.canBoost && unit.elevation > 0.001f && !unit.onSolid()){
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, unit.type.riseSpeed);
        }

        faceTarget();
    }
}
