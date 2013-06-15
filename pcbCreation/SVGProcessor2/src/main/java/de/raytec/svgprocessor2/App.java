package de.raytec.svgprocessor2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import com.vividsolutions.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import com.vividsolutions.jts.triangulate.DelaunayTriangulationBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 * Hello world!
 *
 */
public class App {
    
   public static void main(String[] args) throws Exception {
        try {
            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File("D:\\I2C_LevelShift_brd.svg");


            Document document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();

            for (Element group : rootNode.getChildren("g", Namespace.getNamespace("http://www.w3.org/2000/svg"))) {
                handleGroup(group);
            }

            File xmlOutFile = new File("D:\\I2C_LevelShift_brd-adapted.svg");
            XMLOutputter out = new XMLOutputter();
            FileOutputStream fos = new FileOutputStream(xmlOutFile);
            try {
                out.output(document, fos);
            } finally {
                fos.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private static void handleGroup(Element parent) {
        for (Element child : parent.getChildren("g", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handleGroup(child);
        }

        List<Element> toRemove = new ArrayList<Element>();
        List<Element> toAdd = new ArrayList<Element>();
        for (Element child : parent.getChildren("line", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            toRemove.add(child);
            toAdd.add(handleLine(child));
        }
        for (Element child : parent.getChildren("path", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            toRemove.add(child);
            toAdd.addAll(handlePath(child));
        }

        for (Element toprocess : toRemove) {
            toprocess.detach();
        }
        for (Element toprocess : toAdd) {
            parent.addContent(toprocess);
        }
    }

    private static Element handleLine(Element child) {
//        System.out.println("found line with id " + child.getAttributeValue("id"));

        String style = child.getAttributeValue("style");
//        System.out.println(style);
        Map<String, String> styleValues = parseStyle(style);

        String x1 = child.getAttributeValue("x1");
        String x2 = child.getAttributeValue("x2");
        String y1 = child.getAttributeValue("y1");
        String y2 = child.getAttributeValue("y2");

        double strikeWidth = Double.parseDouble(styleValues.get("stroke-width")) / 2.0;

        LineString linestring = new LineString(buildCoordinateSequence(x1, y1, x2, y2), new GeometryFactory());
        Geometry geometry = linestring.buffer(strikeWidth);
        return buildPolygonElement(geometry);
    }

    private static Map<String, String> parseStyle(String style) {
        Map<String, String> retValue = new HashMap<String, String>();

        for (String piece : style.split("; ")) {
            String[] parts = piece.split(":");

            retValue.put(parts[0], parts[1]);
        }

        return retValue;
    }

    private static CoordinateSequence buildCoordinateSequence(String x1, String y1, String x2, String y2) {
        return new CoordinateArraySequence(new Coordinate[]{buildCoordinate(x1, y1), buildCoordinate(x2, y2)});
    }

    private static Coordinate buildCoordinate(String x1, String y1) {
        return new Coordinate(Double.valueOf(x1), Double.valueOf(y1));
    }

    private static String buildPoints(Geometry geometry) {
        StringBuffer buffer = new StringBuffer();

        for (Coordinate coordinate : geometry.getCoordinates()) {
            buffer.append(coordinate.x);
            buffer.append(",");
            buffer.append(coordinate.y);
            buffer.append(" ");
        }

        return buffer.toString();
    }

    private static List<Element> handlePath(Element child) {

        String style = child.getAttributeValue("style");
        Map<String, String> styleValues = parseStyle(style);
        double strikeWidth = Double.parseDouble(styleValues.get("stroke-width")) / 1.8;

        List<Geometry> geometries = parsePath(child.getAttributeValue("d"), strikeWidth);
        System.out.println("path resulted in " + geometries.size() + " polygon(s)");
        List<Geometry> consideredGeometries = new ArrayList<Geometry>();
        Geometry masterGeometry = buildCollection(geometries);
        DouglasPeuckerSimplifier simplifier = new DouglasPeuckerSimplifier(masterGeometry);
        simplifier.setDistanceTolerance(strikeWidth / 5.0);
        masterGeometry = simplifier.getResultGeometry();

        ConformingDelaunayTriangulationBuilder triangulator = new ConformingDelaunayTriangulationBuilder();
        triangulator.setSites(masterGeometry);
        triangulator.setTolerance(strikeWidth / 5.0);
        triangulator.setConstraints(masterGeometry);
        GeometryCollection triangulationResult = (GeometryCollection) triangulator.getTriangles(new GeometryFactory());
        System.out.println("triangulation resulted in " + triangulationResult.getNumGeometries() + " triangles");

        for (int i = 0; i < triangulationResult.getNumGeometries(); i++) {
            Geometry piece = triangulationResult.getGeometryN(i);

            if (masterGeometry.contains(piece.getCentroid())) {
                consideredGeometries.add(piece);
            }
        }

        System.out.println("path resulted in " + consideredGeometries.size() + " polygon(s)");

        List<Element> toReturn = new ArrayList<Element>();
        for (Geometry geometry : consideredGeometries) {
            toReturn.add(buildPolygonElement(geometry));
        }

        return toReturn;
    }
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    private static List<Geometry> parsePath(String path, double strikeWidth) {
        List<Geometry> geometries = new ArrayList<Geometry>();

        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        for (String token : tokenize(path)) {
            token = token.trim();
            if (token.length() > 0) {
                if (token.startsWith("M")) {
                    if (!coordinateList.isEmpty()) {
                        geometries.add(buildLineString(coordinateList, strikeWidth));
                    }
                    coordinateList.clear();
                    coordinateList.add(buildCoordinateFromToken(token));
                }
                if (token.startsWith("L")) {
                    coordinateList.add(buildCoordinateFromToken(token));
                }
            }
        }
        if (!coordinateList.isEmpty()) {
            geometries.add(buildLineString(coordinateList, strikeWidth));
        }

        return geometries;
    }

    private static String[] tokenize(String path) {
        path = path.replace("M", "#M");
        path = path.replace("H", "#H");
        path = path.replace("V", "#V");
        path = path.replace("L", "#L");

        return path.split("#");
    }

    private static Geometry buildLineString(List<Coordinate> coordinateList, double strikeWidth) {

        CoordinateArraySequence sequence = new CoordinateArraySequence(coordinateList.size());
        for (int i = 0; i < coordinateList.size(); i++) {
            sequence.setOrdinate(i, 0, coordinateList.get(i).x);
            sequence.setOrdinate(i, 1, coordinateList.get(i).y);
        }

        LineString linestring = new LineString(sequence, new GeometryFactory());
        return linestring.buffer(strikeWidth);

    }

    private static Coordinate buildCoordinateFromToken(String token) {
        String[] pieces = token.substring(1).split(",");

        return new Coordinate(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]));
    }

    private static Element buildPolygonElement(Geometry geometry) {
        Element toReturn = new Element("polygon", Namespace.getNamespace("http://www.w3.org/2000/svg"));
        toReturn.setAttribute("points", buildPoints(geometry));
        toReturn.setAttribute("style", "fill:black;stroke:purple;stroke-width:0");
        return toReturn;
    }

    private static Geometry buildCollection(List<Geometry> geometries) {
        GeometryFactory factory = new GeometryFactory();

        // note the following geometry collection may be invalid (say with overlapping polygons)
        GeometryCollection geometryCollection = (GeometryCollection) factory.buildGeometry(geometries);

        return geometryCollection.union();
    }
}
