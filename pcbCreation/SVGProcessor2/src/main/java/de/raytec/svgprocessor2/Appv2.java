package de.raytec.svgprocessor2;

import com.vividsolutions.jts.geom.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Appv2 {

    static Map<String, Geometry> geometries = new HashMap<String, Geometry>();

    public static void main(String[] args) throws Exception {
        File xmlFile;
        File outFile;
        if (args.length < 2 || args.length > 3) {
            System.out.println("program expects two or three parameters: <source SVG file> <target openSCAD file> <extrude height [mm]>");
            System.out.println("program encountered "+args.length+" parameters: ");
            for ( String argument : args ){
                System.out.println(argument);
            }
            throw new Exception("invalid parameter count encountered");
        }

        xmlFile = new File(args[0]);
        outFile = new File(args[1]);
        
        double extrusionHeight = 0.5;
        if ( args.length == 3 ){
            extrusionHeight = Double.parseDouble(args[2]);
        }

        Geometry masterGeometry = new SVGParser(xmlFile).getGeometry();

        OpenSCADGenerator generator = new OpenSCADGenerator(outFile, masterGeometry);
        generator.generateOutput(extrusionHeight);
    }
}
