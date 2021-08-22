package mindustry.ai.types;

import arc.Core;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class MinerAI extends AIController{
    public boolean mining = true;
    public Item targetItem;
    public Tile ore;

    @Override
    public void updateMovement(){
        Building core = unit.closestCore();


        if(!(unit.canMine()) || core == null) return;

        circle(core, unit.type.range / 1.8f);
        if(core != null && core.health < core.maxHealth && core.dst(unit) < tilesize * 2 + unit.type.range / 1.8f) {
            Call.soundAt(Sounds.splash, core.x, core.y, 1f, 1.2f);
            Call.effect(Fx.healBlockFull, core.x, core.y, core.block.size, core.team.color);
            core.heal(unit.health);
            unit.kill();
        }
        return;

        /*
        if(unit.mineTile != null && !unit.mineTile.within(unit, unit.type.miningRange)){
            unit.mineTile(null);
        }

        if(mining){
            if(timer.get(timerTarget2, 60 * 4) || targetItem == null){
                targetItem = unit.team.data().mineItems.min(i -> indexer.hasOre(i) && unit.canMine(i), i -> core.items.get(i));
            }

            //core full of the target item, do nothing
            if(targetItem != null && core.acceptStack(targetItem, 1, unit) == 0){
                unit.clearItem();
                unit.mineTile = null;
                return;
            }

            //if inventory is full, drop it off.
            if(unit.stack.amount >= unit.type.itemCapacity || (targetItem != null && !unit.acceptsItem(targetItem))){
                mining = false;
            }else{
                if(timer.get(timerTarget3, 60) && targetItem != null){
                    ore = indexer.findClosestOre(unit, targetItem);
                }

                if(ore != null){
                    moveTo(ore, unit.type.miningRange / 2f, 20f);

                    if(ore.block() == Blocks.air && unit.within(ore, unit.type.miningRange)){
                        unit.mineTile = ore;
                    }

                    if(ore.block() != Blocks.air){
                        mining = false;
                    }
                }
            }
        }else{
            unit.mineTile = null;

            if(unit.stack.amount == 0){
                mining = true;
                return;
            }

            no no no
            if(unit.within(core, unit.type.range)){
                if(core.acceptStack(unit.stack.item, unit.stack.amount, unit) > 0){
                    Call.transferItemTo(unit, unit.stack.item, unit.stack.amount, unit.x, unit.y, core);
                }

                unit.clearItem();
                mining = true;
            }

        }
        */
    }
}
