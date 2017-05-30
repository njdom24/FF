package com.dommie.mariobros.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dommie.mariobros.sprites.InteractiveTileObject;
import com.dommie.mariobros.sprites.Player;
import com.dommie.mariobros.sprites.NPC;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by njdom24 on 5/18/2017.
 */

public class WorldContactListener implements ContactListener
{
    public static Set<String> currentCollisions = new TreeSet<String>();
    public static Player player;
    public static ArrayList<NPC> npcs;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "bottom" || fixB.getUserData() == "bottom")
        {
            //bottom = player, object = external force
            Fixture bottom = fixA.getUserData() == "bottom" ? fixA : fixB;
            Fixture object = bottom == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject)
            {
                ((InteractiveTileObject) object.getUserData()).onDownCollision();

            }
        }
        else
            if(fixA.getUserData() == "top" || fixB.getUserData() == "top")
            {
                Fixture bottom = fixA.getUserData() == "top" ? fixA : fixB;
                Fixture object = bottom == fixA ? fixB : fixA;

                if(object.getUserData() instanceof InteractiveTileObject)
                {
                    ((InteractiveTileObject) object.getUserData()).onUpCollision();
                }
            }
            else
                if(fixA.getUserData() == "left" || fixB.getUserData() == "left")
                {
                    Fixture bottom = fixA.getUserData() == "left" ? fixA : fixB;
                    Fixture object = bottom == fixA ? fixB : fixA;

                    if(object.getUserData() instanceof InteractiveTileObject)
                    {
                        ((InteractiveTileObject) object.getUserData()).onLeftCollision();
                    }
                }
                else
                    if(fixA.getUserData() == "right" || fixB.getUserData() == "right")
                    {
                        Fixture bottom = fixA.getUserData() == "right" ? fixA : fixB;
                        Fixture object = bottom == fixA ? fixB : fixA;

                        if(object.getUserData() instanceof InteractiveTileObject)
                        {
                            ((InteractiveTileObject) object.getUserData()).onRightCollision();
                        }

                    }
    }

    @Override
    public void endContact(Contact contact) {
       // Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    //necessary because body positions are slightly off
    //returns true if a and b are 1/1000 of a pixel off or closer
    public static boolean smallDifference(float a, float b)
    {
        return (Math.abs(a-b) <= 0.001);
    }
}
