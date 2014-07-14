package org.bonitasoft.web.toolkit.client.ui.html;

import static com.google.gwt.query.client.GQuery.$;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Element;

public class HTML extends XML {

    private static Map<String, String> getEntitiesMap() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put(">", "gt");
        result.put("<", "lt");
        result.put("&", "amp");
        result.put("\"", "quot");
        result.put("'", "#039");
        result.put("\\", "#092");
        result.put("\u00a9", "copy");
        result.put("\u00ae", "reg");

        return result;
    }

    /**
     * Encode a string
     */
    public final static String encode(final String text) {
        if (text == null) {
            return "";
        }

        final Map<String, String> entityTableEncode = getEntitiesMap();

        final StringBuffer sb = new StringBuffer(text.length() * 2);
        Character currentCharacter = null;
        for (int i = 0; i < text.length(); ++i) {
            currentCharacter = text.charAt(i);

            // Named entities
            if (entityTableEncode.containsKey(String.valueOf(currentCharacter))) {
                sb.append("&" + entityTableEncode.get(String.valueOf(currentCharacter)) + ";");
            }
            // Allowed characters
            else if (currentCharacter >= 33 && currentCharacter <= 90 || currentCharacter >= 97 && currentCharacter <= 122 || currentCharacter == ' '
                    || currentCharacter == '\t' || currentCharacter == '\r' || currentCharacter == '\n') {
                sb.append(currentCharacter);
            }
            // Unicode characters
            else if (currentCharacter != null) {
                sb.append("&#" + String.valueOf(Integer.valueOf(currentCharacter)) + ";");
            }
        }
        return sb.toString();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LISTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String ul() {
        return XML.openTag("ul");
    }

    public static String ul(final XMLAttributes attributes) {
        return XML.openTag("ul", attributes);
    }

    public static String _ul() {
        return XML.closeTag("ul");
    }

    public static String ol() {
        return XML.openTag("ol");
    }

    public static String ol(final XMLAttributes attributes) {
        return XML.openTag("ol", attributes);
    }

    public static String _ol() {
        return XML.closeTag("ol");
    }

    public static String li() {
        return XML.openTag("li");
    }

    public static String li(final XMLAttributes attributes) {
        return XML.openTag("li", attributes);
    }

    public static String _li() {
        return XML.closeTag("li");
    }

    public static String dl() {
        return XML.openTag("dl");
    }

    public static String dl(final XMLAttributes attributes) {
        return XML.openTag("dl", attributes);
    }

    public static String _dl() {
        return XML.closeTag("dl");
    }

    public static String dd() {
        return XML.openTag("dd");
    }

    public static String dd(final XMLAttributes attributes) {
        return XML.openTag("dd", attributes);
    }

    public static String _dd() {
        return XML.closeTag("dd");
    }

    public static String dt() {
        return XML.openTag("dt");
    }

    public static String dt(final XMLAttributes attributes) {
        return XML.openTag("dt", attributes);
    }

    public static String _dt() {
        return XML.closeTag("dt");
    }

    // TITRES //

    public static String h1() {
        return XML.openTag("h1");
    }

    public static String h1(final XMLAttributes attributes) {
        return XML.openTag("h1", attributes);
    }

    public static String _h1() {
        return XML.closeTag("h1");
    }

    public static String h2() {
        return XML.openTag("h2");
    }

    public static String h2(final XMLAttributes attributes) {
        return XML.openTag("h2", attributes);
    }

    public static String _h2() {
        return XML.closeTag("h2");
    }

    public static String h3() {
        return XML.openTag("h3");
    }

    public static String h3(final XMLAttributes attributes) {
        return XML.openTag("h3", attributes);
    }

    public static String _h3() {
        return XML.closeTag("h3");
    }

    public static String h4() {
        return XML.openTag("h4");
    }

    public static String h4(final XMLAttributes attributes) {
        return XML.openTag("h4", attributes);
    }

    public static String _h4() {
        return XML.closeTag("h4");
    }

    public static String h5() {
        return XML.openTag("h5");
    }

    public static String h5(final XMLAttributes attributes) {
        return XML.openTag("h5", attributes);
    }

    public static String _h5() {
        return XML.closeTag("h5");
    }

    public static String h6() {
        return XML.openTag("h6");
    }

    public static String h6(final XMLAttributes attributes) {
        return XML.openTag("h6", attributes);
    }

    public static String _h6() {
        return XML.closeTag("h6");
    }

    // LIENS //
    public static String a(final String url, final String title) {
        return a(url, title, null);
    }

    public static String a(final String url, final String title, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }

        attributes.add("title", title);
        attributes.add("href", url);

        return XML.openTag("a", attributes);
    }

    public static String _a() {
        return XML.closeTag("a");
    }

    public String anchor(final String name) {
        return XML.openSingleTag("a", new XMLAttributes().add("name", name));
    }

    public static final void append(final Element rootElement, final List<Element> elements) {
        if (elements != null) {
            for (final Element e : elements) {
                rootElement.appendChild(e);
            }
        }
    }

    public static final void append(final Element rootElement, final Element element) {
        rootElement.appendChild(element);
    }

    public static final void prepend(final Element rootElement, final List<Element> elements) {
        if (elements != null) {
            for (int i = elements.size() - 1; i >= 0; i--) {
                $(rootElement).prepend(elements.get(i));
            }
        }
    }

    public static final void after(final Element rootElement, final Element element) {
        $(rootElement).after(element);
    }

    public static final void after(final Element rootElement, final List<Element> elements) {
        $(rootElement).after($(elements));
    }

    public static final void prepend(final Element rootElement, final Element element) {
        $(rootElement).prepend(element);
    }

    public static final void before(final Element rootElement, final Element element) {
        $(rootElement).before(element);
    }

    public static final void before(final Element rootElement, final List<Element> elements) {
        $(rootElement).before($(elements));
    }

    // MEDIAS //
    public static String img(final String url, final String title) {
        return img(url, title, null);
    }

    public static String img(final String url, final String title, XMLAttributes attributes) {
        if (url == "") {
            return "";
        }

        if (attributes == null) {
            attributes = new XMLAttributes();
        }

        attributes
                .add("src", url)
                .add("title", title)
                .add("alt", title, false);

        return XML.openSingleTag("img", attributes);
    }

    // MISE EN PAGE //
    public static String div() {
        return div(null);
    }

    public static String div(final XMLAttributes attributes) {
        return XML.openTag("div", attributes);
    }

    public static String _div() {
        return XML.closeTag("div");
    }

    public static String span() {
        return span(null, null);
    }

    public static String span(final String content) {
        return span(content, null);
    }

    public static String span(final XMLAttributes attributes) {
        return span(null, attributes);
    }

    public static String span(final String content, final XMLAttributes attributes) {
        return XML.openTag("span", attributes) + (content != null ? content + _span() : "");
    }

    public static String _span() {
        return XML.closeTag("span");
    }

    public static String br() {
        return XML.openSingleTag("br");
    }

    public static String space(final int count) {
        String result = "";
        for (int i = 0; i < count; i++) {
            result += "&nbsp;";
        }
        return result;
    }

    public static String space() {
        return "&nbsp;";
    }

    public static String hr() {
        return hr(null);
    }

    public static String hr(final XMLAttributes attributes) {
        return XML.openSingleTag("hr", attributes);
    }

    public static String pre() {
        return pre(null);
    }

    public static String pre(final XMLAttributes attributes) {
        return XML.openTag("pre", attributes);
    }

    public static String _pre() {
        return XML.closeTag("pre");
    }

    public static String p() {
        return p(null);
    }

    public static String p(XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return XML.openTag("p", attributes);
    }

    public static String _p() {
        return XML.closeTag("p");
    }

    public static String blockquote() {
        return blockquote(null);
    }

    public static String blockquote(XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return XML.openTag("blockquote", attributes);
    }

    public static String _blockquote() {
        return XML.closeTag("blockquote");
    }

    public static String strong() {
        return strong(null, null);
    }

    public static String strong(final String content) {
        return strong(content, null);
    }

    public static String strong(final XMLAttributes attributes) {
        return strong(null, attributes);
    }

    public static String strong(final String content, final XMLAttributes attributes) {
        return XML.openTag("strong", attributes) + (content != null ? content + _strong() : "");
    }

    public static String _strong() {
        return XML.closeTag("strong");
    }

    public static String em() {
        return em(null, null);
    }

    public static String em(final String content) {
        return em(content, null);
    }

    public static String em(final XMLAttributes attributes) {
        return em(null, attributes);
    }

    public static String em(final String content, final XMLAttributes attributes) {
        return XML.openTag("em", attributes) + (content != null ? content + _em() : "");
    }

    public static String _em() {
        return XML.closeTag("em");
    }

    // TABLEAUX
    public static String table() {
        return table(null);
    }

    public static String table(final XMLAttributes attributes) {
        return XML.openTag("table", attributes);
    }

    public static String _table() {
        return XML.closeTag("table");
    }

    public static String tr() {
        return tr(null);
    }

    public static String tr(final XMLAttributes attributes) {
        return XML.openTag("tr", attributes);
    }

    public static String _tr() {
        return XML.closeTag("tr");
    }

    public static String thead() {
        return thead(null);
    }

    public static String thead(final XMLAttributes attributes) {
        return XML.openTag("thead", attributes);
    }

    public static String _thead() {
        return XML.closeTag("thead");
    }

    public static String tbody() {
        return tbody(null);
    }

    public static String tbody(final XMLAttributes attributes) {
        return XML.openTag("tbody", attributes);
    }

    public static String _tbody() {
        return XML.closeTag("tbody");
    }

    public static String tfoot() {
        return tfoot(null);
    }

    public static String tfoot(XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return XML.openTag("tfoot", attributes);
    }

    public static String _tfoot() {
        return XML.closeTag("tfoot");
    }

    public static String td() {
        return td(null);
    }

    public static String td(XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return XML.openTag("td", attributes);
    }

    public static String _td() {
        return XML.closeTag("td");
    }

    public static String th() {
        return th(null);
    }

    public static String th(XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return XML.openTag("th", attributes);
    }

    public static String _th() {
        return XML.closeTag("th");
    }

    // FORMULAIRES //
    public static String form(final String action, final String method) {
        return form(action, method, null);
    }

    public static String form(final String action, final String method, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }

        attributes.add("action", action);
        attributes.add("method", method);

        return XML.openTag("form", attributes);
    }

    public static String _form() {
        return XML.closeTag("form");
    }

    public static String textarea(final String name, final String value) {
        return textarea(name, value, null);
    }

    public static String textarea(final String name, final String value, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("name", name);

        return XML.openTag("textarea", attributes)
                + text(value)
                + XML.closeTag("textarea");
    }

    public static String text(final String text) {
        return HTML.encode(text).replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;").replaceAll("\r\n", "<br />").replaceAll("[\r\n]", "<br />")
                .replaceAll("^-+$", "<hr />");
    }

    public static String input(final String type, final String name) {
        return input(type, name, null, null);
    }

    public static String input(final String type, final String name, final String value) {
        return input(type, name, value, null);
    }

    public static String input(final String type, final String name, final String value, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("name", name);
        attributes.add("value", value);
        attributes.add("type", type);

        return XML.openSingleTag("input", attributes);
    }

    public static String inputImage(final String name, final String value, final String src, final String title) {
        return inputImage(name, value, src, title);
    }

    public static String inputImage(final String name, final String value, final String src, final String title, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }

        attributes.add("src", src);
        attributes.add("title", title);
        attributes.add("alt", title, false);

        return input("image", name, value, attributes);
    }

    public static String inputHidden(final String name, final String value) {
        return inputHidden(name, value, null);
    }

    public static String inputHidden(final String name, final List<String> value) {
        return inputHidden(name, value, null);
    }

    public static String inputHidden(final String name, final String value, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return input("hidden", name, value, attributes);
    }

    public static String inputHidden(final String name, final List<String> value, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        String result = "";
        for (final String v : value) {
            result += inputHidden(name + "[]", v, attributes);
        }
        return result;
    }

    public static String inputText(final String name, final String value, final int maxLength) {
        return inputText(name, value, maxLength, null);

    }

    public static String inputText(final String name, final String value, final XMLAttributes attributes) {
        return inputText(name, value, 255, attributes);

    }

    public static String inputText(final String name, final String value) {
        return inputText(name, value, 255, null);

    }

    public static String inputText(final String name, final XMLAttributes attributes) {
        return inputText(name, null, 255, attributes);
    }

    public static String inputText(final String name) {
        return inputText(name, null, 255, null);
    }

    public static String inputText(final String name, final String value, final int maxLength, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        if (maxLength > 0) {
            attributes.add("maxlength", String.valueOf(maxLength));
        }
        return input("text", name, value, attributes);
    }

    public static String inputFile(final String name) {
        return inputFile(name, null);
    }

    public static String inputFile(final String name, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("id", name, false);

        return input("file", name, "", attributes);
    }

    public static String submit(final String label) {
        return submit(label, null);
    }

    public static String submit(final String label, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return input("submit", null, label, attributes);
    }

    public static String select(final String name) {
        return select(name, null);
    }

    public static String select(final String name, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("name", name);
        return XML.openTag("select", attributes);
    }

    public static String _select() {
        return XML.closeTag("select");
    }

    public static String optionGroup(final String label) {
        return optionGroup(label, null);
    }

    public static String optionGroup(final String label, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("label", label);
        return XML.openTag("optgroup", attributes);
    }

    public static String _optionGroup() {
        return XML.closeTag("optgroup");
    }

    public static String option(final String label, final String value) {
        return option(label, value, false, null);
    }

    public static String option(final String label, final String value, final XMLAttributes attributes) {
        return option(label, value, false, null);
    }

    public static String option(final String label, final String value, final boolean selected) {
        return option(label, value, selected, null);
    }

    public static String option(final String label, final String value, final boolean selected, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        if (selected) {
            attributes.add("selected", "selected");
        }
        attributes.add("value", value);
        return XML.openTag("option", attributes) + label + XML.closeTag("option");

    }

    public static String radio(final String name, final String value, final boolean checked) {
        return radio(name, value, checked, null);
    }

    public static String radio(final String name, final String value, final XMLAttributes attributes) {
        return radio(name, value, false, null);
    }

    public static String radio(final String name, final String value) {
        return radio(name, value, false, null);
    }

    public static String radio(final String name, final String value, final boolean checked, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        if (checked) {
            attributes.add("checked", "checked");
        }

        return input("radio", name, value, attributes);
    }

    public static String checkbox(final String name, final String value, final boolean checked) {
        return checkbox(name, value, checked, null);
    }

    public static String checkbox(final String name, final String value, final XMLAttributes attributes) {
        return checkbox(name, value, false, attributes);
    }

    public static String checkbox(final String name, final String value) {
        return checkbox(name, value, false, null);
    }

    public static String checkbox(final String name, final String value, final boolean checked, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        if (checked) {
            attributes.add("checked", "checked");
        }

        return input("checkbox", name, value, attributes);
    }

    public static String fieldset() {
        return fieldset(null);
    }

    public static String fieldset(XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        return XML.openTag("fieldset", attributes);
    }

    public static String _fieldset() {
        return XML.closeTag("fieldset");
    }

    public static String legend() {
        return legend(null, null);
    }

    public static String legend(final String legend) {
        return legend(legend, null);
    }

    public static String legend(final XMLAttributes attributes) {
        return legend(null, attributes);
    }

    public static String legend(final String legend, final XMLAttributes attributes) {
        return XML.openTag("legend", attributes) + (legend != null ? legend + _legend() : "");
    }

    public static String _legend() {
        return XML.closeTag("legend");
    }

    public static String label(final String text) {
        return label(text, null, null);
    }

    public static String label(final String text, final XMLAttributes attributes) {
        return label(text, null, attributes);

    }

    public static String label(final String text, final String forId) {
        return label(text, forId, null);

    }

    public static String label(final String text, final String forId, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("for", forId);
        return XML.openTag("label", attributes) + text + XML.closeTag("label");
    }

    // Iframe
    public static String iFrame(final String url) {
        return iFrame(url, null);
    }

    public static String iFrame(final String url, XMLAttributes attributes) {
        if (attributes == null) {
            attributes = new XMLAttributes();
        }
        attributes.add("src", url);
        return XML.openSingleTag("iframe", attributes);
    }

}
