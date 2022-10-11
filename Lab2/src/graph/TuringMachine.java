package graph;

import graph.parts.Edge;
import graph.parts.Entity;

public class TuringMachine {
    private Graph gp;
    private Entity currentEntity;

    public TuringMachine(Graph gp) {
        this.gp = gp;
    }

    public boolean isCorrectString(String string) {
        char[] charMas = string.toCharArray();
        currentEntity = gp.getFirstEntity();
        for (char c : charMas) {
            currentEntity = getNextEntity(c);
            if(currentEntity==null) return false;
        }
        if(currentEntity.isFinal()) return true;
        else return false;
    }

    private Entity getNextEntity(Character c) {
        for (Edge edge : currentEntity.getEdges()) {
            if(edge.getCharacter().equals(c))
                return edge.getTo();
        }
        return null;
    }
}