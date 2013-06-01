/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketScissorSpagegoo.obo;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
public class GameChoice {
    
    private final List<GameChoice> subChoicesTaken = new LinkedList<GameChoice>();
    
    public void addSubChoice(GameChoice choice){
        subChoicesTaken.add(choice);
    }
}
