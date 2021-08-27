package mindustry.world.blocks.payloads;

import arc.audio.*;
import arc.graphics.g2d.*;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.td.TowerDefense;
import mindustry.type.UnitType;
import mindustry.world.Tile;

import static mindustry.content.Blocks.payloadPropulsionTower;

public class PayloadVoid extends PayloadBlock{
    public Effect incinerateEffect = Fx.blastExplosion;
    public Sound incinerateSound = Sounds.bang;

    public PayloadVoid(String name){
        super(name);

        outputsPayload = false;
        acceptsPayload = true;
        update = true;
        rotate = false;
        size = 3;
        payloadSpeed = 1.2f;
        //make sure to display large units.
        clipSize = 120;
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region, topRegion};
    }

    public class PayloadVoidBuild extends PayloadBlockBuild<Payload>{

        @Override
        public void draw(){
            Draw.rect(region, x, y);

            //draw input
            for(int i = 0; i < 4; i++){
                if(blends(i)){
                    Draw.rect(inRegion, x, y, (i * 90) - 180);
                }
            }

            Draw.rect(topRegion, x, y);

            Draw.z(Layer.blockOver);
            drawPayload();
        }

        @Override
        public boolean acceptUnitPayload(Unit unit){
            return true;
        }

        @Override
        public void updateTile(){
            if(moveInPayload(false) && cons.valid()){
                if(payload instanceof UnitPayload) {
                    // send to closest spawn
                    Tile sendTo = TowerDefense.payloadPaths.get(this);
                    if(sendTo != null) {
                        UnitType ut = ((UnitPayload) payload).unit.type;

                        Call.soundAt(Sounds.shootBig, x, y, 1, 1);

                        float lifetimeScl = dst(sendTo) / Bullets.artilleryPlasticFrag.lifetime / Bullets.artilleryPlasticFrag.speed;
                        Call.createBullet(Bullets.artilleryPlasticFrag, team(), x, y, angleTo(sendTo), 0f, 1f, lifetimeScl); // ? ? ?

                        Timer.schedule(() -> {
                            if(sendTo != null) {
                                Unit waveUnit = ut.create(Vars.state.rules.waveTeam);
                                waveUnit.set(sendTo);
                                waveUnit.add();
                            }
                        }, lifetimeScl * Bullets.artilleryPlasticFrag.lifetime / 60f);
                    }
                }
                payload = null;
                incinerateEffect.at(this);
                incinerateSound.at(this);
            }
        }
    }
}
