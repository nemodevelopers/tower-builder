package ru.nemodev.towerbuilder.entity.game.player;

import com.badlogic.gdx.physics.box2d.World;

import ru.nemodev.towerbuilder.core.model.Box2dActor;
import ru.nemodev.towerbuilder.core.model.GameObject;
import ru.nemodev.towerbuilder.entity.game.tower.TowerBlockGenerator;
import ru.nemodev.towerbuilder.entity.game.tower.TowerBlockMove;


public class PlayerActor extends Box2dActor
{
    public interface PlayerEventListener
    {
        void playerDropBlock();
    }

    private final TowerBlockGenerator towerBlockGenerator;
    private PlayerEventListener playerEventListener;

    private TowerBlockMove towerBlockMove;

    private volatile boolean isTouch;

    public PlayerActor(World world, TowerBlockGenerator towerBlockGenerator)
    {
        super(world);

        this.towerBlockGenerator = towerBlockGenerator;
        this.isTouch = false;
    }

    public void setPlayerEventListener(PlayerEventListener playerEventListener)
    {
        this.playerEventListener = playerEventListener;
    }

    @Override
    public GameObject isTouch(float x, float y)
    {
        if (isTouch || towerBlockMove == null)
            return null;

        isTouch = true;

        return this;
    }

    @Override
    public boolean touchDown(float x, float y)
    {
        return true;
    }

    @Override
    protected void doAct(float delta)
    {
        if (towerBlockMove == null)
        {
            if (towerBlockGenerator.isReadyForDropBlock())
            {
                towerBlockMove = towerBlockGenerator.generate();
                addGameObject(towerBlockMove);
            }
        }

        if (isTouch && towerBlockMove != null)
        {
            towerBlockMove.dropBlock();
            playerEventListener.playerDropBlock();
            towerBlockMove = null;

            isTouch = false;
        }
    }

}
