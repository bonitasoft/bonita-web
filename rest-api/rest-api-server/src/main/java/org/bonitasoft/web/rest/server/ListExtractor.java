package org.bonitasoft.web.rest.server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Elcrin
 */
public class ListExtractor<T, E> {

    public interface Extractor<T, E> {
        T extract(E entry);
    }

    public List<T> extract(List<E> original, Extractor<T, E> extractor) {
        List<T> extracted = new ArrayList<T>(original.size());
        for (E entry : original) {
            extracted.add(extractor.extract(entry));
        }
        return extracted;
    }
}
