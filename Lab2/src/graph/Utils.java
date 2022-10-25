package graph;

import graph.command.Command;
import graph.command.errors.IllegalCommandFormatException;
import graph.parts.Edge;
import graph.parts.Entity;

import java.util.*;

public class Utils {
    public static Command parseFromString(String str) throws IllegalCommandFormatException {
        if (str.isEmpty()) throw new IllegalCommandFormatException("Command string is empty");
        String from = getFirstState(str);
        Character c = getCharacterFromCommandString(str);
        String to = getSecondState(str);
        if (isCorrectStateFormat(from) && isCorrectStateFormat(to)) {
            return new Command(from, c, to);
        } else {
            throw new IllegalCommandFormatException("Not correct format of state in edge : " + from + ", " + to);
        }
    }

    private static String getFirstState(String str) throws IllegalCommandFormatException {
        try {
            return str.substring(0, str.indexOf(","));
        } catch (Exception ex) {
            throw new IllegalCommandFormatException("Can't get first state from command string");
        }
    }

    private static Character getCharacterFromCommandString(String str) throws IllegalCommandFormatException {
        try {
            String subString = str.substring(str.indexOf(",") + 1, str.lastIndexOf("="));
            if (subString.length() != 1) throw new Exception();
            return subString.charAt(0);
        } catch (Exception ex) {
            throw new IllegalCommandFormatException("Can't get character from command string");
        }
    }

    private static String getSecondState(String str) throws IllegalCommandFormatException {
        try {
            return str.substring(str.lastIndexOf("=") + 1, str.length());
        } catch (Exception ex) {
            throw new IllegalCommandFormatException("Can't get first state from command string");
        }
    }

    private static boolean isCorrectStateFormat(String str) {
        boolean answer = true;
        if (Character.isAlphabetic(str.charAt(0))) {
            for (Character c : str.substring(1).toCharArray()) {
                if (!Character.isDigit(c)) {
                    answer = false;
                    break;
                }
            }
        } else {
            answer = false;
        }
        return answer;
    }

    public static boolean normalizeForOneEntity(Graph gp, Entity entityWithSomeName) {
        Entity entity = gp.getEntities().get(entityWithSomeName.getName());
        Character ch = getIssueCharacterFrom(entity);
        if (ch == null) return false;
        Set<Entity> issueEntities = new HashSet<>();
        for (Edge e : entity.getEdges()) {
            if (e.getCharacter().equals(ch))
                issueEntities.add(e.getTo());
        }
        String name = getRightNameForEntity(gp, issueEntities);
        Entity newRightEntity = makeNewRightEntity(name, issueEntities);
        Map<Entity, Set<Entity>> map = mapGraph(gp);
        for (Entity e : map.keySet()) {
            if (map.get(e).containsAll(issueEntities)) {
                Object[] edges = e.getEdges().toArray();
                for (int i = 0; i < edges.length; i++) {
                    Character characterFrom = ((Edge) (edges[i])).getCharacter();
                    if (isReplaceableEdge(e, characterFrom, issueEntities, newRightEntity)) {
                        if (issueEntities.contains(e) && ((Edge) edges[i]).getTo().equals(e)) {
                            replaceWithCycle(gp, e, (Edge) edges[i], new Edge(characterFrom, newRightEntity));
                        } else {
                            replaceEdge(gp, e, ((Edge) edges[i]), new Edge(characterFrom, newRightEntity));
                        }
                    }
               }
//                                for (Edge edge : e.getEdges()) {
//                    if (issueEntities.contains(edge.getTo()))
//                        replaceEdge(gp, e, edge, new Edge(edge.getCharacter(), newRightEntity));
//                  }
           }
        }
        return true;
    }

    private static boolean isReplaceableEdge(Entity entity, Character character, Collection<Entity> issueEntities, Entity newEntity) {
        Collection<Entity> forCharacter = new ArrayList<>();
        for (Edge edge : entity.getEdges()) {
            if (edge.getCharacter().equals(character)) forCharacter.add(edge.getTo());
        }
        forCharacter.add(entity);
        if (forCharacter.contains(newEntity)) return true;
        return forCharacter.containsAll(issueEntities);
    }

    private static void replaceEdge(Graph gp, Entity e, Edge oldEdge, Edge newEdge) {
        System.out.println("aaaaa");
        gp.removeEdge(e,oldEdge);
        gp.addNewEntity(e,newEdge);
        gp.addNewEdgeFromCommand(new Command(e.getName(), newEdge.getCharacter(), newEdge.getTo().getName()));
    }

    private static void replaceWithCycle(Graph gp, Entity e, Edge oldEdge, Edge newEdge) {
        gp.removeEdge(e, oldEdge);
        gp.addNewEntity(e,newEdge);
        gp.addNewEdgeFromCommand(new Command(newEdge.getTo().getName(), newEdge.getCharacter(), newEdge.getTo().getName()));
    }

    private static Map<Entity, Set<Entity>> mapGraph(Graph gp) {
        Map<Entity, Set<Entity>> map = new HashMap<>();
        for (Entity entity : gp.getEntities().values()) {
            HashSet<Entity> set = new HashSet<>();
            for (Edge e : entity.getEdges()) {
                set.add(e.getTo());
            }
            map.put(entity, set);
        }
        return map;
    }

    private static Character getIssueCharacterFrom(Entity entity) {
        Map<Character, Integer> chToNum = new HashMap<>();
        Character charBuff;
        for (Edge e : entity.getEdges()) {
            charBuff = e.getCharacter();
            if (chToNum.containsKey(charBuff)) {
                return charBuff;
            } else {
                chToNum.put(charBuff, 1);
            }
        }
        return null;
    }


    private static Entity makeNewRightEntity(String name, Set<Entity> entities) {
        Entity entity = new Entity(name);
        for (Entity e : entities) {
            for (Edge edge : e.getEdges()) {
                if (entities.contains(edge.getTo())) {
                    entity.addEdge(new Edge(edge.getCharacter(), entity));
                } else {
                    entity.addEdge(edge);
                }
            }
        }
        return entity;
    }

    private static boolean containsFinalEntity(Set<Entity> entities) {
        for (Entity e : entities) {
            if (e.getName().startsWith("f")) return true;
        }
        return false;
    }

    private static String getRightNameForEntity(Graph gp, Set<Entity> entities) {
        if (containsFinalEntity(entities)) return "f" + gp.getNumOfFinalStates();
        else return "g" + gp.getNumOfCommonStates();
    }

    private static boolean isDeterminedEntity(Entity entity) {
        Set<Character> characters = new HashSet<>();
        for (Edge e : entity.getEdges()) {
            if (characters.contains(e.getCharacter())) return false;
            characters.add(e.getCharacter());
        }
        return true;
    }

    public static Entity getIssueEntity(Graph gp) {
        for (Entity entity : gp.getEntities().values()) {
            if (!isDeterminedEntity(entity)) return entity;
        }
        return null;
    }

    public static void removeUselessEntity(Graph gp) {
        for(Entity entity:gp.getEntities().values()){
            if(isUselessEntity(gp,entity)){
                gp.removeEntity(entity);
                break;
            }
        }
    }

    public static boolean haveUselessEntities(Graph gp){
        for (Entity entity :gp.getEntities().values()){
            if(isUselessEntity(gp,entity)) return true;
        }
        return false;
    }

    private static boolean isUselessEntity(Graph gp, Entity entity) {
        int numOfInputEdges = 0;
        int numOfOutputEdges = 0;
        for (Entity e : gp.getEntities().values()) {
            if (numOfInputEdges != 0) break;
            for (Edge edge : e.getEdges()) {
                if (edge.getTo().equals(entity)) {
                    numOfInputEdges++;
                    break;
                }
            }
        }
        numOfOutputEdges = entity.getEdges().size();
        if (numOfInputEdges == 0 && numOfOutputEdges == 0) {
            return true;
        } else {
            return false;
        }
    }
}