package graph.parts;

public class Edge {
    private Character character;
    private Entity to;

    public Edge(Character c, Entity to) {
        this.character = c;
        this.to = to;
    }

    public Character getCharacter() {
        return character;
    }

    public Entity getTo() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge edge = (Edge) obj;
            return edge.getCharacter().equals(character) && edge.getTo().getName().equals(to.getName());
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return character.hashCode()+to.getName().hashCode();
    }
}