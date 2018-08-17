package com.git.gdsbuilder.validator.fileReader.shp.parser;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.Filter;

import com.git.gdsbuilder.type.dt.layer.DTLayer;

public class SHPFileLayerParser {

	public DTLayer parseDTLayer(String epsg, String filePath, String shpName) throws Exception {

		SimpleFeatureCollection collection = getShpObject(epsg, filePath, shpName);

		if (collection != null) {
			DTLayer layer = new DTLayer();
			SimpleFeatureType featureType = collection.getSchema();
			GeometryType geometryType = featureType.getGeometryDescriptor().getType();
			String geomType = geometryType.getBinding().getSimpleName().toString();
			String layerName = shpName;
			layer.setLayerID(layerName);
			layer.setLayerType(geomType);
			layer.setSimpleFeatureCollection(collection);
			return layer;
		} else {
			return null;
		}
	}

	public SimpleFeatureCollection getShpObject(String epsg, String filePath, String shpName) {

		ShapefileDataStore beforeStore;
		try {
			// before
			File beforeFile = new File(filePath);
			if (beforeFile.isDirectory()) {
				beforeFile = new File(filePath, shpName + ".shp");
			}
			Map<String, Object> beforeMap = new HashMap<String, Object>();
//			System.out.println(beforeFile.toURI().toURL().toString());
			beforeMap.put("url", beforeFile.toURI().toURL().toString());
			beforeStore = (ShapefileDataStore) DataStoreFinder.getDataStore(beforeMap);
			Charset euckr = Charset.forName("EUC-KR");
			beforeStore.setCharset(euckr);
			String typeName = beforeStore.getTypeNames()[0];
			SimpleFeatureSource source = beforeStore.getFeatureSource(typeName);
			Filter filter = Filter.INCLUDE;
			
			
			
			SimpleFeatureCollection collection = source.getFeatures(filter);

			// CoordinateReferenceSystem dataCRS = CRS.decode(epsg);
			// CoordinateReferenceSystem worldCRS = CRS.decode("EPSG:32652");
			// MathTransform transform = CRS.findMathTransform(dataCRS,
			// worldCRS);
			//
			// DefaultFeatureCollection dfc = new DefaultFeatureCollection();
			// SimpleFeatureIterator sfi = collection.features();
			// while (sfi.hasNext()) {
			// SimpleFeature sf = sfi.next();
			// Geometry beforeGeom = (Geometry) sf.getDefaultGeometry();
			// Geometry afterGeom = JTS.transform(beforeGeom, transform);
			// sf.setDefaultGeometry(afterGeom);
			// dfc.add(sf);
			// }
			// sfi.close();
			beforeStore.dispose();
			return collection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
