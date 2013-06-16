/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.svgprocessor2;

import com.vividsolutions.jts.awt.FontGlyphReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;
import static de.raytec.svgprocessor2.Appv2.geometries;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author raymoon
 */
public class SVGParser {

    private final File inFile;
    private Geometry geometry;

    public SVGParser(File inFile) {
        this.inFile = inFile;
    }

    private void parse() throws Exception {
        SAXBuilder builder = new SAXBuilder();

        Document document = (Document) builder.build(inFile);
        Element rootNode = document.getRootElement();

        for (Element group : rootNode.getChildren("g", Namespace.getNamespace("http://www.w3.org/2000/svg"))) {
            handleGroup(group);
        }

        System.out.println("SVG resulted in " + geometries.size() + " polygon(s)");
        Geometry masterGeometry = buildCollection(geometries);

        geometry = TopologyPreservingSimplifier.simplify(masterGeometry, 0.02);
    }

    public Geometry getGeometry() throws Exception {
        if (geometry == null) {
            parse();
        }

        return geometry;
    }

    private static void handleGroup(Element parent) throws Exception {
        for (Element child : parent.getChildren("g", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handleGroup(child);
        }

        List<Element> toRemove = new ArrayList<Element>();
        List<Element> toAdd = new ArrayList<Element>();
        for (Element child : parent.getChildren("line", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handleLine(child);
        }
        for (Element child : parent.getChildren("path", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handlePath(child);
        }
        for (Element child : parent.getChildren("rect", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handleRectangle(child);
        }
        for (Element child : parent.getChildren("circle", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handleCircle(child);
        }
        for (Element child : parent.getChildren("polygon", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handlePolygon(child);
//            System.out.println("ignored polygon as polygon interpretation is not yet stable");
        }
        for (Element child : parent.getChildren("text", Namespace.getNamespace("http://www.w3.org/2000/svg"))) { // recursion
            handleText(child);
//            System.out.println("ignored polygon as polygon interpretation is not yet stable");
        }
    }

    private static void handleText(Element child) {
//        System.out.println("found line with id " + child.getAttributeValue("id"));

        String style = child.getAttributeValue("style");
//        System.out.println(style);
        Map<String, String> styleValues = parseStyle(style);

        String x = child.getAttributeValue("x");
        String y = child.getAttributeValue("y");
        String fontSize = styleValues.get("font-size").replace("pt;", "");

// FontGlyphReader.read(java.lang.String text, java.awt.Font font, double flatness, GeometryFactory geomFact) 
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 1);//Font.getFont(Font.SANS_SERIF);
        font = font.deriveFont((float) Double.parseDouble(fontSize));

        Geometry geom = FontGlyphReader.read(child.getText(), font, 0.1, new GeometryFactory());

        AffineTransformation translation = AffineTransformation.translationInstance(Double.parseDouble(x), Double.parseDouble(y));
        AffineTransformation mirror = AffineTransformation.reflectionInstance(10,0);
        geom.apply(mirror.compose(translation));

        declareGeometry("TEXT", geom);
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
        declareGeometry("LINE", geometry);

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
        return buildCoord(Double.valueOf(x1), Double.valueOf(y1));
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

    private static void handlePath(Element child) {

        String style = child.getAttributeValue("style");
        Map<String, String> styleValues = parseStyle(style);
        double strikeWidth = Double.parseDouble(styleValues.get("stroke-width")) / 2.0 + 0.1;

        Map<String, Geometry> geometries = parsePath(child.getAttributeValue("d"), strikeWidth);
//        System.out.println("path resulted in " + geometries.size() + " polygon(s)");
//        List<Geometry> consideredGeometries = new ArrayList<Geometry>();
        Geometry masterGeometry = buildCollection(geometries);
        masterGeometry = masterGeometry.buffer(-0.1);
//        DouglasPeuckerSimplifier simplifier = new DouglasPeuckerSimplifier(masterGeometry);
//        simplifier.setDistanceTolerance(strikeWidth / 5.0);
//        masterGeometry = simplifier.getResultGeometry();
//
//        ConformingDelaunayTriangulationBuilder triangulator = new ConformingDelaunayTriangulationBuilder();
//        triangulator.setSites(masterGeometry);
//        triangulator.setTolerance(strikeWidth / 5.0);
//        triangulator.setConstraints(masterGeometry);
//        GeometryCollection triangulationResult = (GeometryCollection) triangulator.getTriangles(new GeometryFactory());
//        System.out.println("triangulation resulted in " + triangulationResult.getNumGeometries() + " triangles");
//
//        for (int i = 0; i < triangulationResult.getNumGeometries(); i++) {
//            Geometry piece = triangulationResult.getGeometryN(i);
//
//            if (masterGeometry.contains(piece.getCentroid())) {
//                consideredGeometries.add(piece);
//            }
//        }
//
//        System.out.println("path resulted in " + consideredGeometries.size() + " polygon(s)");

        declareGeometry("PATH", masterGeometry);
//
//        List<Element> toReturn = new ArrayList<Element>();
//        for (Geometry geometry : consideredGeometries) {
//            toReturn.add(buildPolygonElement(geometry));
//        }
//
//        return toReturn;
    }
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    private static Map<String, Geometry> parsePath(String path, double strikeWidth) {
        Map<String, Geometry> geometries = new HashMap<String, Geometry>();

        int id = 0;

        List<Coordinate> coordinateList = new ArrayList<Coordinate>();
        for (String token : tokenize(path)) {
            token = token.trim();
            if (token.length() > 0) {
                if (token.startsWith("M")) {
                    if (!coordinateList.isEmpty()) {
                        geometries.put("" + id++, buildLineString(coordinateList, strikeWidth));
                    }
                    coordinateList.clear();
                    coordinateList.add(buildCoordinateFromToken(token));
                }
                if (token.startsWith("L")) {
                    coordinateList.add(buildCoordinateFromToken(token));
                }
                if (token.startsWith("A")) {
                    // ignore ...
                    coordinateList.clear();
                    System.out.println("");
                }
            }
        }
        if (!coordinateList.isEmpty()) {
            geometries.put("" + id++, buildLineString(coordinateList, strikeWidth));
        }

        return geometries;
    }

    private static String[] tokenize(String path) {
        path = path.replace("M", "#M");
        path = path.replace("H", "#H");
        path = path.replace("V", "#V");
        path = path.replace("L", "#L");
        path = path.replace("A", "#A");

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

        return buildCoord(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]));
    }

    private static Element buildPolygonElement(Geometry geometry) {
        Element toReturn = new Element("polygon", Namespace.getNamespace("http://www.w3.org/2000/svg"));
        toReturn.setAttribute("points", buildPoints(geometry));
        toReturn.setAttribute("style", "fill:black;stroke:purple;stroke-width:0");
        return toReturn;
    }

    private static Geometry buildCollection(Map<String, Geometry> geometries) {
        GeometryFactory factory = new GeometryFactory();

        for (Map.Entry<String, Geometry> geom : geometries.entrySet()) {
            if (!geom.getValue().isValid()) {
                System.out.println(geom);
                System.out.println("INVALID POLYGON encountered: " + geom.getKey());
            }
        }

        // note the following geometry collection may be invalid (say with overlapping polygons)
        GeometryCollection geometryCollection = (GeometryCollection) factory.buildGeometry(geometries.values());

        return geometryCollection.union();
    }

    private static void handleRectangle(Element child) throws Exception {
        String style = child.getAttributeValue("style");
//        System.out.println(style);
//        Map<String, String> styleValues = parseStyle(style);

        // <rect class='l1' x='-1.524000' y='-0.762000' width='3.048000' height='1.524000' rx='0.762000' transform='translate(8.890000,-10.160000) rotate(-180.000000)'/>
        String x = child.getAttributeValue("x");
        String y = child.getAttributeValue("y");
        String width = child.getAttributeValue("width");
        String height = child.getAttributeValue("height");
        String rx = child.getAttributeValue("rx");
        String ry = child.getAttributeValue("ry");
        String transform = child.getAttributeValue("transform");

        if (rx != null && ry == null) {
            ry = rx;
        } else if (ry != null && rx == null) {
            rx = ry;
        } else if (ry == null && rx == null) {
            rx = "0.0";
            ry = "0.0";
        }

        // translate(38.100000,-17.780000) rotate(0.000000)
        double alpha = 0.0;
        double alphaX = 0.0;
        double alphaY = 0.0;
        double dx = 0.0;
        double dy = 0.0;
        if (transform != null) {
            String[] tokens = transform.split(" ");
            for (String token : tokens) {
                if (token.startsWith("translate(")) {
                    token = token.replace("translate(", "").replace(")", "");
                    dx = Double.parseDouble(token.split(",")[0]);
                    dy = Double.parseDouble(token.split(",")[1]);
                }
                if (token.startsWith("rotate(")) {
                    token = token.replace("rotate(", "").replace(")", "");
                    String[] pieces = token.split(",");
                    alpha = Double.parseDouble(pieces[0]);
                    if (pieces.length > 1) {
                        alphaX = Double.parseDouble(pieces[1]);
                        alphaY = Double.parseDouble(pieces[2]);
                    }
                }
            }
        }

        declareGeometry("RECTANGLE", buildRectangle(Double.parseDouble(x), Double.parseDouble(y), dx, dy, alpha, alphaX, alphaY, Double.parseDouble(width), Double.parseDouble(height), Double.parseDouble(rx), Double.parseDouble(ry)));
    }

    private static Geometry buildRectangle(double x, double y, double dx, double dy, double alpha, double alphaX, double alphaY, double width, double height, double rx, double ry) throws Exception {
        AffineTransformation transform = AffineTransformation.rotationInstance(Math.toRadians(alpha), alphaX, alphaY).compose(AffineTransformation.translationInstance(dx, dy));

        if (rx == ry) {
            LinearRing ring = new LinearRing(buildSequence(x, y, width - rx * 2.0, height - ry * 2.0), new GeometryFactory());
            Polygon poly = new Polygon(ring, new LinearRing[]{}, new GeometryFactory());
            poly.apply(AffineTransformation.translationInstance(rx, ry));
            Geometry retValue = poly.buffer(rx);
            retValue.apply(transform);

            return retValue;
        } else {
            throw new Exception("rectangles with rx != ry are not yet implemented");
        }
    }

    private static CoordinateSequence buildSequence(double x, double y, double width, double height) {
        return new CoordinateArraySequence(new Coordinate[]{buildCoord(x, y),
            buildCoord(x + width, y),
            buildCoord(x + width, y + height),
            buildCoord(x, y + height),
            buildCoord(x, y)});

//                return new CoordinateArraySequence(new Coordinate[]{new Coordinate(x - width / 2.0, y - height / 2.0),
//                    new Coordinate(x + width / 2.0, y - height / 2.0),
//                    new Coordinate(x + width / 2.0, y + height / 2.0),
//                    new Coordinate(x - width / 2.0, y + height / 2.0),
//                    new Coordinate(x - width / 2.0, y - height / 2.0)});
    }

    private static void handleCircle(Element child) {
//        System.out.println("found line with id " + child.getAttributeValue("id"));
        String style = child.getAttributeValue("style");
        Map<String, String> styleValues = parseStyle(style);
        double strikeWidth = Double.parseDouble(styleValues.get("stroke-width")) / 2.0 + 0.1;


        String cx = child.getAttributeValue("cx");
        String cy = child.getAttributeValue("cy");
        String r = child.getAttributeValue("r");

        Point point = new Point(new CoordinateArraySequence(new Coordinate[]{buildCoord(Double.parseDouble(cx), Double.parseDouble(cy))}), new GeometryFactory());
        Geometry geometry = point.buffer(Double.parseDouble(r));

        if (!geometry.isValid()) {
            System.out.println(geometry);
            System.out.println("INVALID POLYGON encountered");
        }

        declareGeometry("CIRCLE", geometry);

    }

    private static void handlePolygon(Element child) {
//        System.out.println("found line with id " + child.getAttributeValue("id"));


        String points = child.getAttributeValue("points");
        String transform = child.getAttributeValue("transform");
        String modPoints = points;


        double alpha = 0.0;
        double dx = 0.0;
        double dy = 0.0;
        if (transform != null) {
            String[] tokens = transform.split(" ");
            for (String token : tokens) {
                if (token.startsWith("translate(")) {
                    token = token.replace("translate(", "").replace(")", "");
                    dx = Double.parseDouble(token.split(",")[0]);
                    dy = Double.parseDouble(token.split(",")[1]);
                }
                if (token.startsWith("rotate(")) {
                    token = token.replace("rotate(", "").replace(")", "");
                    alpha = Double.parseDouble(token);
                }
            }
        }
        AffineTransformation affineTransform = AffineTransformation.rotationInstance(Math.toRadians(alpha)).compose(AffineTransformation.translationInstance(dx, dy));

        modPoints = modPoints.replace(",", " ");
        modPoints = modPoints.replace("-", " -");
        modPoints = modPoints.replaceAll("[ ]+", " ");


        List<String> tokens = new ArrayList();
        for (String piece : modPoints.split(" ")) {
            if (piece.trim().length() > 0) {
                tokens.add(piece);
            }
        }

        List<Coordinate> coordinates = new ArrayList<Coordinate>();

        for (int i = 0; i < tokens.size() / 2; i++) {
            String x = tokens.get(i * 2 + 0);
            String y = tokens.get(i * 2 + 1);

            coordinates.add(buildCoord(Double.parseDouble(x), Double.parseDouble(y)));
        }

        Polygon poly = new Polygon(new LinearRing(buildSequence(coordinates), new GeometryFactory()), new LinearRing[]{}, new GeometryFactory());

        String oldWKT = poly.toString();
        poly.apply(affineTransform);
        if (!poly.isValid()) {
            System.out.println(poly);
            System.out.println(oldWKT);
            System.out.println("INVALID POLYGON encountered");
        }
        declareGeometry("POLY", poly);

    }

    private static CoordinateSequence buildSequence(List<Coordinate> coordinates) {

        Coordinate[] coords = new Coordinate[coordinates.size() + 1];
        for (int i = 0; i < coords.length; i++) {
            Coordinate source = coordinates.get(i % coordinates.size());
            coords[i] = new Coordinate(source.x, source.y);
        }
        return new CoordinateArraySequence(coords);
    }

    private static Coordinate buildCoord(double d, double y) {
//        d *= 10000.0;
//        y *= 10000.0;
//        
//        d = Math.round(d);
//        y = Math.round(y);
//        
        return new Coordinate(d, y);
    }
    static int idCount = 0;

    private static void declareGeometry(String id, Geometry geometry) {
        geometries.put(id + idCount++, geometry);
    }
}
