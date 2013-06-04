package org.bonitasoft.report.scriptlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.bonitasoft.report.scriptlet.comparator.AggragateScriptletKeyCategoryNSerie;
import org.bonitasoft.report.scriptlet.comparator.AggragateScriptletKeySerieNCategory;

public class AggragateScriptlet extends JRDefaultScriptlet {

    List<AggragateScriptletMeasure> ds;

    ArrayList<HashMap<String, String>> aggregatedInfo;

    Boolean isInit;

    public static String KEY_CATEGORIE_FIELD_NAME = "categorie";

    public static String KEY_SERIE_FIELD_NAME = "serie";

    public static String KEY_MEASURE_FIELD_NAME = "measure";

    public static String KEY_MAP_VALUE_FIELD_NAME = "mapValue";

    public static String KEY_MAP_VALUES_FIELD_NAME = "mapValues";

    public static String KEY_DS_FIELD_NAME = "ds";

    public static String KEY_AGG_STAT_FIELD_NAME = "stat";

    public static String KEY_ITEM_MIN_NAME = "itemMin"; // to know the minimum of the dataset and have graph with the same scale

    public static String KEY_ITEM_MAX_NAME = "itemMax"; // to know the maximum of the dataset and have graph with the same scale

    public static String AGG_STAT_GEOM_AVG = "geomAvg"; // geometricMean(double[] values) // Returns the geometric mean of the entries in the input array, or
                                                        // Double.NaN if the array is empty.

    public static String AGG_STAT_MAX = "max"; // max(double[] values) //Returns the maximum of the entries in the input array, or Double.NaN if the array is
                                               // empty.

    public static String AGG_STAT_AVG = "avg"; // mean(double[] values) // Returns the arithmetic mean of the entries in the input array, or Double.NaN if the
                                               // array is empty.

    public static String AGG_STAT_MIN = "min"; // min(double[] values) // Returns the minimum of the entries in the input array, or Double.NaN if the array is
                                               // empty.

    public static String AGG_STAT_STD_VAR = "standarddeviation"; // normalize(double[] sample) //Normalize (standardize) the sample, so it is has a mean of 0
                                                                 // and a standard deviation of 1.

    public static String AGG_STAT_MEDIAN = "median"; // percentile(double[] values, 50) // Returns an estimate of the pth percentile of the values in the values
                                                     // array.

    // populationVariance(double[] values) //Returns the population variance of the entries in the input array, or Double.NaN if the array is empty.
    // product(double[] values) // Returns the product of the entries in the input array, or Double.NaN if the array is empty.
    public static String AGG_STAT_SUM = "sum"; // sum(double[] values)// Returns the sum of the values in the input array, or Double.NaN if the array is empty.

    // public static String KEY_AGG_STAT_SUM_LOG = "sumlog"; // sumLog(double[] values) // Returns the sum of the natural logs of the entries in the input
    // array, or Double.NaN if the array is empty.
    public static String AGG_STAT_SUM_SQ = "sumsq"; // sumSq(double[] values) //Returns the sum of the squares of the entries in the input array, or Double.NaN
                                                    // if the array is empty.

    public static String AGG_STAT_VAR = "variance"; // variance(double[] values) // Returns the variance of the entries in the input array, or Double.NaN if the
                                                    // array is empty.

    public boolean init(
            final ArrayList aggregatedInfo
            ) {

        this.aggregatedInfo = aggregatedInfo;
        isInit = new Boolean(true);

        return false;
    }

    public void aggregate() throws JRException {

        if (isInit == null || !isInit.booleanValue()) {
            return;
        }
        if (aggregatedInfo == null) {
            return;
        }

        AggragateScriptletKey key;
        String cat;
        String ser;
        Double newValue;

        String categorieFieldName;
        String serieFieldName;
        String measureFieldName;
        String mapValueVariableName;
        String mapValuesVariableName;

        final String aggFieldName;
        String aggMethod;

        HashMap<AggragateScriptletKey, Double> catsNSeriesValue = null;
        HashMap<AggragateScriptletKey, Double[]> catsNSeriesValues = null;

        for (final HashMap<String, String> tmpAggregatedInfo : aggregatedInfo) {

            categorieFieldName = tmpAggregatedInfo.get(KEY_CATEGORIE_FIELD_NAME);
            serieFieldName = tmpAggregatedInfo.get(KEY_SERIE_FIELD_NAME);
            measureFieldName = tmpAggregatedInfo.get(KEY_MEASURE_FIELD_NAME);
            mapValueVariableName = tmpAggregatedInfo.get(KEY_MAP_VALUE_FIELD_NAME);
            mapValuesVariableName = tmpAggregatedInfo.get(KEY_MAP_VALUES_FIELD_NAME);

            aggMethod = tmpAggregatedInfo.get(KEY_AGG_STAT_FIELD_NAME);

            cat = "unknown";
            ser = "unknown";
            newValue = new Double(0.0);

            // Try to detemine the categorie for the chart
            // in Field or in Varialbe or in Parameter
            try {
                // look for the categorie in Field
                if (getFieldValue(categorieFieldName) != null) {
                    cat = (String) getFieldValue(categorieFieldName);
                }
            } catch (final JRScriptletException e) {
            }
            try {
                // look for the categorie in Varialbe
                if (getVariableValue(categorieFieldName) != null) {
                    cat = (String) getVariableValue(categorieFieldName);
                }

            } catch (final JRScriptletException e) {
            }
            try {
                // look for the categorie in Parameter
                if (this.getParameterValue(categorieFieldName) != null) {
                    cat = (String) this.getParameterValue(categorieFieldName);
                }

            } catch (final JRScriptletException e) {
            }

            // System.out.println("\n\n***********");
            // System.out.println("cat : " + cat);

            // Try to detemine the series for the chart
            // in Field or in Varialbe or in Parameter
            try {
                // look for the series in Field
                if (getFieldValue(serieFieldName) != null) {
                    ser = (String) getFieldValue(serieFieldName);
                }
            } catch (final JRScriptletException e) {
            }
            try {
                // look for the series in Varialbe
                if (getVariableValue(serieFieldName) != null) {
                    ser = (String) getVariableValue(serieFieldName);
                }

            } catch (final JRScriptletException e) {
            }
            try {
                // look for the series in Parameter
                if (this.getParameterValue(serieFieldName) != null) {
                    ser = (String) this.getParameterValue(serieFieldName);
                }

            } catch (final JRScriptletException e) {
            }

            // System.out.println("ser : " + ser);

            // Try to detemine the measure for the chart
            try {
                // look for the categorie in Field or in Varialbe
                try {
                    if (getFieldValue(measureFieldName) != null) {
                        newValue = (Double) getFieldValue(measureFieldName);
                    }
                } catch (final JRScriptletException e) {
                }
                try {
                    if (getVariableValue(measureFieldName) != null) {
                        newValue = (Double) getVariableValue(measureFieldName);
                    }
                } catch (final JRScriptletException e) {
                }

                if (newValue == null) {
                    newValue = new Double(0.0);
                }

            } catch (final Exception e) {
                throw new JRException("Field " + measureFieldName + " not found. A Serie must be defined.");
            }

            // get Map Value
            try {

                if (getVariableValue(mapValueVariableName) != null) {
                    catsNSeriesValue = (HashMap<AggragateScriptletKey, Double>) getVariableValue(mapValueVariableName);
                }
            } catch (final JRScriptletException e) {
                throw new JRException("Variable " + mapValueVariableName + "not found. A Serie must be defined.");
            }

            // get Map Values
            try {

                if (getVariableValue(mapValuesVariableName) != null) {
                    catsNSeriesValues = (HashMap<AggragateScriptletKey, Double[]>) getVariableValue(mapValuesVariableName);
                }
            } catch (final JRScriptletException e) {
                throw new JRException("Variable " + mapValueVariableName + "not found. A Serie must be defined.");
            }

            if (catsNSeriesValue == null) {
                catsNSeriesValue = new HashMap<AggragateScriptletKey, Double>();
            }
            if (catsNSeriesValues == null) {
                catsNSeriesValues = new HashMap<AggragateScriptletKey, Double[]>();
            }

            // compute
            key = new AggragateScriptletKey(cat, ser);

            if (catsNSeriesValue.containsKey(key)) {

                final DescriptiveStatistics stats = new DescriptiveStatistics();

                final Double[] olDvalues = catsNSeriesValues.get(key);
                final Double[] newValues = new Double[olDvalues.length + 1];

                System.arraycopy(olDvalues, 0, newValues, 0, olDvalues.length);
                // add new value to the array
                newValues[olDvalues.length] = newValue;

                // add new value to the array for the computation

                for (int i = 0; i < newValues.length; i++) {
                    stats.addValue(newValues[i].doubleValue());
                }
                /*
                 * System.out.println("\nValues : ");
                 * for (int i = 0; i < newValues.length; i++) {
                 * System.out.print(newValues[i].doubleValue() + " - ");
                 * }
                 */

                if (AggragateScriptlet.AGG_STAT_GEOM_AVG.equals(aggMethod)) {
                    newValue = stats.getGeometricMean();
                } else if (AggragateScriptlet.AGG_STAT_MAX.equals(aggMethod)) {
                    newValue = stats.getMax();
                } else if (AggragateScriptlet.AGG_STAT_AVG.equals(aggMethod)) {
                    newValue = stats.getMean();
                } else if (AggragateScriptlet.AGG_STAT_MIN.equals(aggMethod)) {
                    newValue = stats.getMin();
                } else if (AggragateScriptlet.AGG_STAT_STD_VAR.equals(aggMethod)) {
                    newValue = stats.getStandardDeviation();
                } else if (AggragateScriptlet.AGG_STAT_MEDIAN.equals(aggMethod)) {
                    newValue = stats.getPercentile(50.0);
                } else if (AggragateScriptlet.AGG_STAT_SUM.equals(aggMethod)) {
                    newValue = stats.getSum();
                } else if (AggragateScriptlet.AGG_STAT_SUM_SQ.equals(aggMethod)) {
                    newValue = stats.getSumsq();
                } else if (AggragateScriptlet.AGG_STAT_VAR.equals(aggMethod)) {
                    newValue = stats.getVariance();
                }

                // System.out.println("\ncompute result " + aggMethod + " : " + newValue);

                catsNSeriesValue.put(key, newValue);
                catsNSeriesValues.put(key, newValues);

            } else {

                // initialisation of all
                final Double[] values = new Double[1];
                values[0] = newValue;

                catsNSeriesValue.put(key, newValue);
                catsNSeriesValues.put(key, values);
            }

            // set ((categorie, serie), values)
            setVariableValue(mapValueVariableName, catsNSeriesValue);
            setVariableValue(mapValuesVariableName, catsNSeriesValues);
        }

    }

    public JRBeanCollectionDataSource mapToDataSource(
            final int sortField,
            final int sortType,
            final int limit,
            final boolean debug
            ) throws JRScriptletException {

        final List<AggragateScriptletMeasure> mergedDs = new ArrayList<AggragateScriptletMeasure>();

        for (final HashMap<String, String> tmpAggregatedInfo : aggregatedInfo) {

            map2ds(tmpAggregatedInfo.get(KEY_DS_FIELD_NAME), tmpAggregatedInfo.get(KEY_MAP_VALUE_FIELD_NAME));

            mergedDs.addAll(ds);
        }

        ds = mergedDs;

        return this.toJRBeanCollectionDataSource(sortField, sortType, limit, debug);
    }

    @Override
    public void afterReportInit() throws JRScriptletException
    {
        super.afterReportInit();

        try {
            // look for the categorie in Field
            if (getVariableValue("aggInfo") != null) {
                final ArrayList aggInfo = (ArrayList) getVariableValue("aggInfo");

                init(aggInfo);
            }

        } catch (final JRScriptletException e) {
        }

    }

    @Override
    public void afterDetailEval() throws JRScriptletException {

        try {
            // aggregate the current line
            aggregate();

        } catch (final JRException e) {
            throw new JRScriptletException(e);
        }
    }

    public ArrayList buildArrayAggInfo(ArrayList aggInfo, final HashMap<String, String> map) {

        if (aggInfo == null) {
            aggInfo = new ArrayList<HashMap<String, String>>();
        }

        aggInfo.add(map);

        return aggInfo;
    }

    public HashMap buildMapAggInfo(HashMap mapInfo, final String key, final String value) {

        if (mapInfo == null) {
            mapInfo = new HashMap<String, String>();
        }

        mapInfo.put(key, value);

        return mapInfo;
    }

    /**** private methods ****/

    private void map2ds(
            final String dsVariableName,
            final String mapVariableName
            ) throws JRScriptletException {

        HashMap<AggragateScriptletKey, Double> catsNSeriesValue = null;

        ds = (List<AggragateScriptletMeasure>) getVariableValue(dsVariableName);
        if (ds == null) {
            ds = new ArrayList<AggragateScriptletMeasure>();
        }

        if (getVariableValue(mapVariableName) != null) {
            catsNSeriesValue = (HashMap<AggragateScriptletKey, Double>) getVariableValue(mapVariableName);
        }

        if (catsNSeriesValue == null) {
            return;
        }

        final Iterator<AggragateScriptletKey> keysIt = catsNSeriesValue.keySet().iterator();
        AggragateScriptletKey key;

        // populate ds

        while (keysIt.hasNext()) {
            key = keysIt.next();
            final Double d = catsNSeriesValue.get(key);
            ds.add(new AggragateScriptletMeasure(key, d));
        }

        setVariableValue(dsVariableName, ds);
    }

    private void sortDsAsc(final int sortField) throws JRScriptletException {

        if (ds == null) {
            throw new JRScriptletException("No data source defined. can not sort data source");
        }

        try {

            switch (sortField) {
                case 1: {
                    Collections.sort(ds, new AggragateScriptletKeyCategoryNSerie());
                    break;
                }
                case 2: {
                    Collections.sort(ds, new AggragateScriptletKeySerieNCategory());
                    break;
                }
                case 3: {
                    Collections.sort(ds);
                    break;
                }

            }

        } catch (final java.lang.UnsupportedOperationException e) {
            throw new JRScriptletException("unsortable data source");
        }

    }

    private void sortDsDesc(final int sortField) throws JRScriptletException {

        if (ds == null) {
            throw new JRScriptletException("No data source defined. can not sort data source");
        }

        try {
            switch (sortField) {
                case 1: {
                    Collections.sort(ds, new AggragateScriptletKeyCategoryNSerie());
                    break;
                }
                case 2: {
                    Collections.sort(ds, new AggragateScriptletKeySerieNCategory());
                    break;
                }
                case 3: {
                    Collections.sort(ds, Collections.reverseOrder());
                    break;
                }

            }

        } catch (final java.lang.UnsupportedOperationException e) {
            throw new JRScriptletException("unsortable data source");
        }

    }

    private void limitDs(final int limit) throws JRScriptletException {

        if (ds == null) {
            throw new JRScriptletException("No data source defined. can not limit data source");
        }

        final ArrayList<AggragateScriptletMeasure> tempDs = new ArrayList<AggragateScriptletMeasure>();

        for (int i = 0; i < ds.size() && i < limit; i++) {
            tempDs.add(ds.get(i));
        }

        ds = tempDs;

    }

    private void showDs() throws JRScriptletException {

        if (ds == null) {
            throw new JRScriptletException("No data source defined. can not limit data source");
        }

        final StringBuilder s = new StringBuilder();
        AggragateScriptletMeasure tmp;

        s.append("{\n");
        for (int i = 0; i < ds.size(); i++) {
            tmp = ds.get(i);
            s.append("{{category:").append(tmp.getKey().getCategory()).append(", serie:").append(tmp.getKey().getSerie()).append("}, measure:")
                    .append(tmp.getMeasure().doubleValue()).append("}");
            if (i < ds.size() - 1) {
                s.append(",");
            }
            s.append("\n");
        }
        s.append("}");

        System.out.println(s.toString());
    }

    private JRBeanCollectionDataSource toJRBeanCollectionDataSource(
            final int sortField,
            final int sortType,
            final int limit,
            final boolean debug)
            throws JRScriptletException {

        if (ds == null) {
            throw new JRScriptletException("No data source defined. can not limit data source");
        }

        // sort the collection
        if (sortType == 1) {
            sortDsAsc(sortField);
        }
        if (sortType == 2) {
            sortDsDesc(sortField);
        }

        if (limit > 0) {
            limitDs(limit);
        }

        if (debug) {
            showDs();
        }

        return new JRBeanCollectionDataSource(ds);
    }

    public JRBeanCollectionDataSource toJRBeanCollectionDataSource(
            final String categorieFieldName,
            final String dsFieldName,
            final int sortField,
            final int sortType,
            final int limit,
            final boolean debug)
            throws JRScriptletException {

        map2ds(dsFieldName, categorieFieldName);

        // sort the collection
        if (sortType == 1) {
            sortDsAsc(sortField);
        }
        if (sortType == 2) {
            sortDsDesc(sortField);
        }

        if (limit > 0) {
            limitDs(limit);
        }

        if (debug) {
            showDs();
        }

        return new JRBeanCollectionDataSource(ds);
    }

    public Double rangeMinValue(final String categorieFieldName)
            throws JRScriptletException
    {
        HashMap<AggragateScriptletKey, Double> catsNSeriesValue = null;

        if (getVariableValue(categorieFieldName) != null) {
            catsNSeriesValue = (HashMap<AggragateScriptletKey, Double>) getVariableValue(categorieFieldName);
        }

        if (catsNSeriesValue == null) {
            return null;
        }

        // min
        Double min = null;

        final Iterator<AggragateScriptletKey> keysIt = catsNSeriesValue.keySet().iterator();
        AggragateScriptletKey key;

        // populate ds

        while (keysIt.hasNext()) {
            key = keysIt.next();
            final Double d = catsNSeriesValue.get(key);

            if (min == null) {
                min = d;
            }

            min = min.doubleValue() < d.doubleValue() ? min : d;

        }

        return min;
    }

    public Double rangeMaxValue(final String categorieFieldName)
            throws JRScriptletException
    {
        HashMap<AggragateScriptletKey, Double> catsNSeriesValue = null;

        if (getVariableValue(categorieFieldName) != null) {
            catsNSeriesValue = (HashMap<AggragateScriptletKey, Double>) getVariableValue(categorieFieldName);
        }

        if (catsNSeriesValue == null) {
            return null;
        }

        // max
        Double max = null;

        final Iterator<AggragateScriptletKey> keysIt = catsNSeriesValue.keySet().iterator();
        AggragateScriptletKey key;

        // populate ds

        while (keysIt.hasNext()) {
            key = keysIt.next();
            final Double d = catsNSeriesValue.get(key);

            if (max == null) {
                max = d;
            }

            max = max.doubleValue() > d.doubleValue() ? max : d;

        }

        return max;
    }
    // statistic

}
