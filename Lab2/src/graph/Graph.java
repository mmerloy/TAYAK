package graph;

import graph.command.Command;
import graph.parts.Edge;
import graph.parts.Entity;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    private static final String FIRST_STATE_NAME = "q0";

    private HashMap<String, Entity> entities;
    private int numOfCommonStates;
    private int numOfFinalStates;

    public Graph() {
        entities = new HashMap<>();
        numOfCommonStates = 0;
        numOfFinalStates = 0;
    }


    public Entity getFirstEntity() {
        return entities.get(FIRST_STATE_NAME);
    }

    public void removeEdge(Entity from, Edge edge) {
        entities.get(from.getName()).removeEdge(edge);
        String name = edge.getTo().getName();
        //if(entities.get(name)!=null) {
        //entities.remove(name);
        if (name.startsWith("f")) numOfFinalStates--;
        if (name.startsWith("q")) numOfCommonStates--;
        //}
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getName());
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public int getNumOfCommonStates() {
        return numOfCommonStates;
    }

    public int getNumOfFinalStates() {
        return numOfFinalStates;
    }

    public void addNewEdgeFromCommand(Command command) {
        Entity entityFrom = makeOrGetExistingEntityFromString(command.getFrom());
        Entity entityTo = makeOrGetExistingEntityFromString(command.getTo());
        entityFrom.addEdge(new Edge(command.getCharacter(), entityTo));
    }

    public void addNewEntity(Entity e,Edge newEdge) {
        makeOrGetExistingEntity(newEdge.getTo());
        e.addEdge(newEdge);
    }

    public Entity makeOrGetExistingEntity(Entity entity) {
        if (entities.containsKey(entity.getName())) return entity;
        entities.put(entity.getName(),entity);
        if (entity.getName().startsWith("q")) numOfCommonStates++;
        else if (entity.getName().startsWith("f")) numOfFinalStates++;
        return entity;
    }

    private Entity makeOrGetExistingEntityFromString(String name) {
        if (entities.containsKey(name)) {
            return entities.get(name);
        } else {
            Entity entity = new Entity(name);
            entities.put(name, entity);
            if (name.startsWith("q")) numOfCommonStates++;
            else if (name.startsWith("f")) numOfFinalStates++;
            return entity;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("Graph :\n");
        for (Entity e : entities.values()) {
            stringBuilder.append(e.toString());
        }
        return stringBuilder.toString();
    }
}