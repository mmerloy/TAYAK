package graph.parts;

import java.util.*;

public class Entity {
    private String name;
    private Collection<Edge> edges;
    private boolean isFinal;

    public Entity(String name) {
        this.name = name;
        if (name.startsWith("f")) this.isFinal = true;
        else this.isFinal = false;
        edges = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge){
        edges.remove(edge);
    }

    public Collection<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("Entity name: ").append(name).append("\n");
        for (Edge e : edges) {
            stringBuilder
                    .append("\t")
                    .append("character: ")
                    .append(e.getCharacter())
                    .append(", to : ")
                    .append(e.getTo().getName())
                    .append(".\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            return ((Entity)obj).getName().equals(name);
        } else {
            return super.equals(obj);
        }
    }
}