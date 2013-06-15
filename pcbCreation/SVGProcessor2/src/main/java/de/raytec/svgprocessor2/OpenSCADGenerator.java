/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.svgprocessor2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Ray
 */
public class OpenSCADGenerator {

    private final File outFile;
    private final Geometry geometry;
    private FileOutputStream fos;
    private int id = 0;

    public OpenSCADGenerator(File outFile, Geometry geometry) {
        this.outFile = outFile;
        this.geometry = geometry;
    }

    public void generateOutput() throws Exception {
        fos = new FileOutputStream(outFile);

        try {

            if (geometry instanceof MultiPolygon) {
                MultiPolygon multi = (MultiPolygon) geometry;

                for (int i = 0; i < multi.getNumGeometries(); i++) {
                    Polygon poly = (Polygon) multi.getGeometryN(i);

                    write(poly);

//                    System.out.println(poly);
                }
            } else if (geometry instanceof Polygon) {
                write((Polygon) geometry);
            } else {
                throw new Exception("geometry type not supported");
            }

            writeModuleInvocations();

        } finally {
            fos.close();
        }
    }

    private void write(Polygon poly) throws IOException {

        int id = getNextID();
        writeLN("module poly_" + id + "(){");

        if (requiresDifference(poly)) {
            writeLN("difference() {");
        }

        write(poly.getExteriorRing());

        if (requiresDifference(poly)) {
            for (int i = 0; i < poly.getNumInteriorRing(); i++) {
                writeLN("");
                write(poly.getInteriorRingN(i));
            }

            writeLN("}");
        }
        writeLN("}");
    }

    private void write(LineString boundary) throws IOException {
        write("polygon([");

        for (int i = 0; i < boundary.getNumPoints() - 2; i++) {
            write(boundary.getCoordinateN(i));
            write(",");
        }
        write(boundary.getCoordinateN(boundary.getNumPoints() - 2));

        writeLN("]);");
    }

    private void write(Coordinate coordinate) throws IOException {
        write("[");

        write(coordinate.x + "");
        write(",");
        write((-coordinate.y) + "");

        write("]");
    }

    private void writeLN(String string) throws IOException {
        fos.write((string + System.getProperty("line.separator")).getBytes());
    }

    private void write(String string) throws IOException {
        fos.write(string.getBytes());
    }

    private boolean requiresDifference(Polygon poly) {
        return poly.getNumInteriorRing() > 0;
    }

    private int getNextID() {
        return id++;
    }

    private void writeModuleInvocations() throws IOException {
        for (int i = 0; i < id; i++) {
            writeLN("linear_extrude(height=0.5){");
            writeLN("poly_" + i + "();");
            writeLN("}");
        }
    }
}
