package org.bonitasoft.web.toolkit.client.ui.html;

import static com.google.gwt.query.client.GQuery.$;

import java.util.LinkedList;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public abstract class XML {

    private static String lastUniqueId = null;

    public static Element makeElement(final String html) {
        return (Element) $(html).get(0);
    }

    public static LinkedList<Element> makeElements(final String html) {
        final GQuery elements = $(html);
        final LinkedList<Element> result = new LinkedList<Element>();
        for (int i = 0; i < elements.length(); i++) {
            result.add((Element) elements.get(i));
        }
        return result;
    }

    public static String getUniqueId() {
        lastUniqueId = DOM.createUniqueId();
        return lastUniqueId;
    }

    public static String getLastUniqueId() {
        return lastUniqueId;
    }

    public static String openTag(final String tagName) {
        return "<" + tagName.toLowerCase() + ">";
    }

    public static String openTag(final String tagName, final XMLAttributes attributes) {
        return "<" + tagName.toLowerCase() + " " + (attributes != null ? attributes.toString() : "") + ">";
    }

    public static String openSingleTag(final String tagName) {
        return "<" + tagName.toLowerCase() + " />";
    }

    public static String openSingleTag(final String tagName, final XMLAttributes attributes) {
        return "<" + tagName.toLowerCase() + " " + (attributes != null ? attributes.toString() : "") + " />";
    }

    public static String closeTag(final String tagName) {
        return "</" + tagName.toLowerCase() + ">";
    }

}
