/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface GameAI {
    public Action computeAction(List<Player> players, List<Planet> planets, List<Fleet> fleets);
}
