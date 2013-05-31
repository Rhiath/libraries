/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.actions.AttackAction;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.actions.NullAction;
import org.json.JSONObject;

/**
 *
 * @author raymoon
 */
public class GameParticipator implements Runnable {

    private final Account account;
    private final GameAI ai;
    private final String host;
    private final int port;

    public GameParticipator(GameAI ai, Account account, String host, int port) {
        this.account = account;
        this.ai = ai;
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket(host, port);

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            boolean endOfGamereached = false;
            login(br, os);

            String state = br.readLine();
            while (!endOfGamereached) {
                if (isGameState(state)) {
                    JSONObject object = new JSONObject(state);
                    endOfGamereached = getGameHasReachedEnd(object);
                    if (!endOfGamereached) {
                        Action action = determineResultingAction(object);
                        performAction(action, os);
                    }
                }
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(GameParticipator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameParticipator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void login(BufferedReader br, OutputStream os) throws IOException {
        send(os, "login " + account.getUsername() + " " + account.getPassword());
    }

    private boolean isGameState(String state) {
        return state.startsWith("{");
    }

    private List<Player> parsePlayers(JSONObject object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Planet> parsePlanets(JSONObject object, List<Player> players) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Fleet> parseFleets(JSONObject object, List<Player> players, List<Planet> planets) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void performAction(Action action, OutputStream os) throws IOException {
        if ( action instanceof NullAction ){
            send(os, "nop");
        } else if ( action instanceof AttackAction ){
            AttackAction attackAction = (AttackAction) action;
            send(os, "send "+attackAction.getSource().getId()+" "+attackAction.getDestination().getId()+" "+attackAction.getDefenseStrength().get(ShipType.ROCKET)+" "+attackAction.getDefenseStrength().get(ShipType.SCISSORS)+" "+attackAction.getDefenseStrength().get(ShipType.SPACEGOO));
        }
    }

    private Action determineResultingAction(JSONObject object) {
        List<Player> players = parsePlayers(object);
        List<Planet> planets = parsePlanets(object, players);
        List<Fleet> fleets = parseFleets(object, players, planets);
        Action action = ai.computeAction(players, planets, fleets);
        return action;
    }

    private boolean getGameHasReachedEnd(JSONObject object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void send(OutputStream os, String nop) throws IOException {
        os.write((nop+"\n\r").getBytes());
    }
}
