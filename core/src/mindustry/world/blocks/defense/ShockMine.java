package mindustry.world.blocks.defense;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.effect.Lightning;
import mindustry.entities.type.Unit;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.*;

public class ShockMine extends Block{
    public final int timerDamage = timers++;

    public float cooldown = 80f;
    public float tileDamage = 5f;
    public float damage = 13;
    public int length = 10;
    public int tendrils = 6;

    public ShockMine(String name){
        super(name);
        update = true;
        destructible = true;
        solid = false;
        targetable = false;
        layer = Layer.overlay;
        rebuildable = false;
    }

    @Override
    public void drawLayer(Tile tile){
        super.draw(tile);
        Draw.color(tile.getTeam().color);
        Draw.alpha(0.22f);
        Fill.rect(tile.drawx(), tile.drawy(), 2f, 2f);
        Draw.color();
    }

    @Override
    public void drawTeam(Tile tile){
        //no
    }

    @Override
    public void draw(Tile tile){
        //nope
    }

    @Override
    public void unitOn(Tile tile, Unit unit){
        if(unit.getTeam() != tile.getTeam() && tile.entity.timer.get(timerDamage, cooldown)){
            for(int i = 0; i < tendrils; i++){
                Lightning.create(tile.getTeam(), Pal.lancerLaser, damage, tile.drawx(), tile.drawy(), Mathf.random(360f), length);
            }
            tile.entity.damage(tileDamage);
        }
    }

    @Override
    public void update(Tile tile){
        if(tile.entity.health != tile.entity.maxHealth()) return;
        Tile infect = tile.getNearby(Mathf.random(0, 3));
        if(infect == null || infect.block() != Blocks.air) return;
        if(!Build.validPlace(tile.getTeam(), infect.x, infect.y, this, tile.rotation())) return;
        Call.onConstructFinish(infect, this, -1, tile.rotation(), tile.getTeam(), true);
        tile.entity.damage(tile.entity.maxHealth() / 2);
        infect.entity.damage(tile.entity.maxHealth() / 2);
    }
}
