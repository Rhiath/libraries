/*
 * Copyright 2013 raymoon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author raymoon
 */
public class GameStateParser {

    private List<Player> players = null;
    private List<Planet> planets = null;
    private List<Fleet> fleets = null;
    private final JSONObject object;

    public GameStateParser(JSONObject object) {
        this.object = object;
    }

    public List<Player> parsePlayers() {
        if (players == null) {
            players = new LinkedList<Player>();

            JSONArray array = object.getJSONArray("players");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                players.add(new Player(obj.getString("name"), obj.getInt("id")));
            }
        }

        return players;
    }

    public List<Planet> parsePlanets() {
        if (planets == null) {
            planets = new LinkedList<Planet>();

            JSONArray array = object.getJSONArray("planets");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                int id = obj.getInt("id");
                int x = obj.getInt("x");
                int y = obj.getInt("y");
                int ownerID = obj.getInt("owner_id");
                Map<ShipType, Integer> typeCount = parseTypeCount(obj.getJSONArray("ships"));
                Map<ShipType, Integer> production = parseTypeCount(obj.getJSONArray("production"));

                planets.add(new Planet(x, y, getPlayer(ownerID), typeCount, production, id));
            }
        }

        return planets;

    }

    // "fleets": [{"id": 0, "owner_id": 1, "ships": [10, 15, 10], "origin": 1, "target": 7, "eta": 8}
    public List<Fleet> parseFleets() {
        if (fleets == null) {
            fleets = new LinkedList<Fleet>();

            JSONArray array = object.getJSONArray("fleets");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                int id = obj.getInt("id");
                int originID = obj.getInt("origin");
                int targetID = obj.getInt("target");
                int eta = obj.getInt("eta");
                int ownerID = obj.getInt("owner_id");
                Map<ShipType, Integer> ships = parseTypeCount(obj.getJSONArray("ships"));
          
                fleets.add(new Fleet(getPlayer(ownerID), getPlanet(originID), getPlanet(targetID), eta, ships));
            }
        }

        return fleets;
    }

    private Map<ShipType, Integer> parseTypeCount(JSONArray jsonArray) {
        Map<ShipType, Integer> retValue = new HashMap<ShipType, Integer>();

        retValue.put(ShipType.ROCKET, jsonArray.getInt(0));
        retValue.put(ShipType.SCISSORS, jsonArray.getInt(1));
        retValue.put(ShipType.SPACEGOO, jsonArray.getInt(2));

        return retValue;
    }

    private Player getPlayer(int ownerID) {
        for (Player player : this.parsePlayers()) {
            if (player.getId() == ownerID) {
                return player;
            }
        }

        return null;
    }
    private Planet getPlanet(int playerID) {
        for (Planet player : this.parsePlanets()) {
            if (player.getId() == playerID) {
                return player;
            }
        }

        return null;
    }
}
