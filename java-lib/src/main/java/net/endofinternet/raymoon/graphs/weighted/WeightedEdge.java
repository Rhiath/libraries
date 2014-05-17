/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.weighted;

/**
 *
 * @author raymoon
 */
public class WeightedEdge implements Comparable<WeightedEdge> {

    private final int vertexA;
    private final int vertexB;
    private double weight;

    public WeightedEdge(int vertexA, int vertexB, double weight) {
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.weight = weight;
    }

    public int either() {
        return vertexA;
    }

    public int other() {
        return vertexB;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(WeightedEdge t) {
        return Double.valueOf(this.weight).compareTo(Double.valueOf(t.weight));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.vertexA;
        hash = 59 * hash + this.vertexB;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.weight) ^ (Double.doubleToLongBits(this.weight) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeightedEdge other = (WeightedEdge) obj;
        if (this.vertexA != other.vertexA) {
            return false;
        }
        if (this.vertexB != other.vertexB) {
            return false;
        }
        if (Double.doubleToLongBits(this.weight) != Double.doubleToLongBits(other.weight)) {
            return false;
        }
        return true;
    }
}
