/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui.utils;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * @author Séverin Moussel
 * 
 */
public class Color {

    public static final Color aliceblue = new Color(240, 248, 255);

    public static final Color antiquewhite = new Color(248, 236, 216);

    public static final Color aqua = new Color(0, 255, 255);

    public static final Color aquamarine = new Color(128, 255, 216);

    public static final Color azure = new Color(240, 255, 255);

    public static final Color beige = new Color(248, 244, 224);

    public static final Color bisque = new Color(255, 228, 200);

    public static final Color black = new Color(0, 0, 0);

    public static final Color blanchedalmond = new Color(255, 236, 208);

    public static final Color blue = new Color(0, 0, 255);

    public static final Color blueviolet = new Color(176, 0, 224);

    public static final Color brown = new Color(168, 44, 40);

    public static final Color burlywood = new Color(224, 184, 136);

    public static final Color cadetblue = new Color(96, 160, 160);

    public static final Color chartreuse = new Color(128, 255, 0);

    public static final Color chocolate = new Color(208, 104, 32);

    public static final Color coral = new Color(255, 128, 80);

    public static final Color cornflowerblue = new Color(104, 148, 240);

    public static final Color cornsilk = new Color(255, 248, 224);

    public static final Color crimson = new Color(224, 20, 64);

    public static final Color cyan = new Color(0, 255, 255);

    public static final Color darkblue = new Color(0, 0, 136);

    public static final Color darkcyan = new Color(0, 140, 136);

    public static final Color darkgoldenrod = new Color(184, 136, 0);

    public static final Color darkgray = new Color(168, 168, 168);

    public static final Color darkgreen = new Color(0, 100, 0);

    public static final Color darkkhaki = new Color(184, 136, 0);

    public static final Color darkmagenta = new Color(136, 0, 136);

    public static final Color darkolivegreen = new Color(88, 108, 48);

    public static final Color darkorange = new Color(255, 140, 0);

    public static final Color darkorchid = new Color(152, 52, 208);

    public static final Color darkred = new Color(136, 0, 0);

    public static final Color darksalmon = new Color(232, 152, 120);

    public static final Color darkseagreen = new Color(144, 188, 144);

    public static final Color darkslateblue = new Color(72, 60, 136);

    public static final Color darkslategray = new Color(48, 80, 80);

    public static final Color darkturquoise = new Color(0, 208, 208);

    public static final Color darkviolet = new Color(152, 0, 208);

    public static final Color deeppink = new Color(255, 20, 144);

    public static final Color deepskyblue = new Color(0, 192, 255);

    public static final Color dimgray = new Color(104, 104, 104);

    public static final Color dodgerblue = new Color(32, 144, 255);

    public static final Color firebrick = new Color(176, 36, 32);

    public static final Color floralwhite = new Color(255, 252, 240);

    public static final Color forestgreen = new Color(32, 140, 32);

    public static final Color fuchsia = new Color(255, 0, 255);

    public static final Color gainsboro = new Color(224, 220, 224);

    public static final Color ghostwhite = new Color(248, 248, 255);

    public static final Color gold = new Color(255, 216, 0);

    public static final Color goldenrod = new Color(216, 164, 32);

    public static final Color gray = new Color(128, 128, 128);

    public static final Color green = new Color(0, 128, 0);

    public static final Color greenyellow = new Color(176, 255, 48);

    public static final Color honeydew = new Color(240, 255, 240);

    public static final Color hotpink = new Color(255, 104, 184);

    public static final Color indianred = new Color(208, 92, 96);

    public static final Color indigo = new Color(72, 0, 128);

    public static final Color ivory = new Color(255, 255, 240);

    public static final Color khaki = new Color(240, 232, 144);

    public static final Color lavender = new Color(232, 232, 248);

    public static final Color lavenderblush = new Color(255, 240, 248);

    public static final Color lawngreen = new Color(128, 252, 0);

    public static final Color lemonchiffon = new Color(255, 252, 208);

    public static final Color lightblue = new Color(176, 216, 232);

    public static final Color lightcoral = new Color(240, 128, 128);

    public static final Color lightcyan = new Color(224, 255, 255);

    public static final Color lightgoldenrodyellow = new Color(248, 252, 208);

    public static final Color lightgreen = new Color(144, 240, 144);

    public static final Color lightgrey = new Color(208, 212, 208);

    public static final Color lightpink = new Color(255, 184, 192);

    public static final Color lightsalmon = new Color(255, 160, 120);

    public static final Color lightseagreen = new Color(32, 180, 168);

    public static final Color lightskyblue = new Color(136, 208, 248);

    public static final Color lightslategray = new Color(120, 136, 152);

    public static final Color lightsteelblue = new Color(176, 196, 224);

    public static final Color lightyellow = new Color(255, 255, 224);

    public static final Color lime = new Color(0, 255, 0);

    public static final Color limegreen = new Color(48, 204, 48);

    public static final Color linen = new Color(248, 240, 232);

    public static final Color magenta = new Color(255, 0, 255);

    public static final Color maroon = new Color(128, 0, 0);

    public static final Color mediumauqamarine = new Color(104, 204, 168);

    public static final Color mediumblue = new Color(0, 0, 208);

    public static final Color mediumorchid = new Color(184, 84, 208);

    public static final Color mediumpurple = new Color(144, 112, 216);

    public static final Color mediumseagreen = new Color(64, 180, 112);

    public static final Color mediumslateblue = new Color(120, 104, 240);

    public static final Color mediumspringgreen = new Color(0, 252, 152);

    public static final Color mediumturquoise = new Color(72, 208, 208);

    public static final Color mediumvioletred = new Color(200, 20, 136);

    public static final Color midnightblue = new Color(24, 24, 112);

    public static final Color mintcream = new Color(248, 255, 248);

    public static final Color Mistyrose = new Color(255, 228, 224);

    public static final Color moccasin = new Color(255, 228, 184);

    public static final Color navajowhite = new Color(255, 224, 176);

    public static final Color navy = new Color(0, 0, 128);

    public static final Color oldlace = new Color(255, 244, 232);

    public static final Color olive = new Color(128, 128, 0);

    public static final Color olivedrab = new Color(104, 144, 32);

    public static final Color orange = new Color(255, 164, 0);

    public static final Color orangered = new Color(255, 68, 0);

    public static final Color orchid = new Color(216, 112, 216);

    public static final Color palegoldenrod = new Color(240, 232, 168);

    public static final Color palegreen = new Color(152, 252, 152);

    public static final Color paleturquoise = new Color(176, 240, 240);

    public static final Color palevioletred = new Color(216, 112, 144);

    public static final Color papayawhip = new Color(255, 240, 216);

    public static final Color peachpuff = new Color(255, 220, 184);

    public static final Color peru = new Color(208, 132, 64);

    public static final Color pink = new Color(255, 192, 200);

    public static final Color plum = new Color(224, 160, 224);

    public static final Color powderblue = new Color(176, 224, 232);

    public static final Color purple = new Color(128, 0, 128);

    public static final Color red = new Color(255, 0, 0);

    public static final Color rosybrown = new Color(192, 144, 144);

    public static final Color royalblue = new Color(64, 104, 224);

    public static final Color saddlebrown = new Color(136, 68, 16);

    public static final Color salmon = new Color(248, 128, 112);

    public static final Color sandybrown = new Color(248, 164, 96);

    public static final Color seagreen = new Color(48, 140, 88);

    public static final Color seashell = new Color(255, 244, 240);

    public static final Color sienna = new Color(160, 84, 48);

    public static final Color silver = new Color(192, 192, 192);

    public static final Color skyblue = new Color(136, 208, 232);

    public static final Color slateblue = new Color(104, 92, 208);

    public static final Color slategray = new Color(112, 128, 144);

    public static final Color snow = new Color(255, 252, 248);

    public static final Color springgreen = new Color(0, 255, 128);

    public static final Color steelblue = new Color(72, 132, 184);

    public static final Color tan = new Color(208, 180, 144);

    public static final Color teal = new Color(0, 128, 128);

    public static final Color thistle = new Color(216, 192, 216);

    public static final Color tomato = new Color(255, 100, 72);

    public static final Color turquoise = new Color(64, 224, 208);

    public static final Color violet = new Color(240, 132, 240);

    public static final Color wheat = new Color(248, 224, 176);

    public static final Color white = new Color(255, 255, 255);

    public static final Color whitesmoke = new Color(248, 244, 248);

    public static final Color yellow = new Color(255, 255, 0);

    public static final Color yellowGreen = new Color(152, 204, 48);

    private int r = 0, g = 0, b = 0, a = 255;

    public Color(final String colorText) {
        this.setColor(colorText);
    }

    public Color(final int red, final int green, final int blue) {
        this.setColor(red, green, blue);
    }

    public Color(final int red, final int green, final int blue, final int alpha) {
        this.setColor(red, green, blue, alpha);
    }

    public Color(final String hexRed, final String hexGreen, final String hexBlue) {
        this.setColor(hexRed, hexGreen, hexBlue);
    }

    public Color(final String hexRed, final String hexGreen, final String hexBlue, final String hexAlpha) {
        this.setColor(hexRed, hexGreen, hexBlue, hexAlpha);
    }

    public int getRed()
    {
        return this.r;
    }

    public int getGreen()
    {
        return this.g;
    }

    public int getBlue() {
        return this.b;
    }

    public int getAlpha() {
        return this.a;
    }

    @Override
    public String toString() {
        return toRgbString();
    }

    private String hexPad(final String string) {
        switch (string.length()) {
            case 1:
                return "0" + string;
            case 0:
                return "00";
            default:
                return string;
        }
    }

    public String toHexString() {
        return "#" + hexPad(Integer.toHexString(this.r)) + hexPad(Integer.toHexString(this.g)) + hexPad(Integer.toHexString(this.b))
                + (this.a < 255 ? hexPad(Integer.toHexString(this.a)) : "");
    }

    public String toRgbString() {
        return "rgb" + (this.a < 255 ? "a" : "") + "("
                + Integer.toString(this.r) + ","
                + Integer.toString(this.g) + ","
                + Integer.toString(this.b)
                + (this.a < 255 ? "," + Integer.toString(this.a) : "")
                + ")";
    }

    private void setColor(final int red, final int green, final int blue) {
        this.setColor(red, green, blue, 255);
    }

    private void setColor(final int red, final int green, final int blue, final int alpha) {
        assert red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255 && this.a >= 0 && this.a <= 255;

        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = alpha;
    }

    private void setColor(final String hexRed, final String hexGreen, final String hexBlue) {
        this.setColor(hexRed, hexGreen, hexBlue, "255");
    }

    private void setColor(final String hexRed, final String hexGreen, final String hexBlue, final String hexAlpha) {
        this.setColor(
                Integer.parseInt(hexRed + (hexRed.length() == 1 ? hexRed : ""), 16),
                Integer.parseInt(hexGreen + (hexGreen.length() == 1 ? hexGreen : ""), 16),
                Integer.parseInt(hexBlue + (hexBlue.length() == 1 ? hexBlue : ""), 16),
                hexAlpha != null ? Integer.parseInt(hexAlpha + (hexAlpha.length() == 1 ? hexAlpha : ""), 16) : 255
                );
    }

    private void setColor(final Color color) {
        this.setColor(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha());
    }

    private void setColor(final String colorCode) {
        if (colorCode.charAt(0) == '#') {
            setHexColor(colorCode);
        } else if (colorCode.length() > 5 && "rgb(".equalsIgnoreCase(colorCode.substring(0, 4))) {
            setRgbColor(colorCode);
        } else {
            setHtmlColor(colorCode);
        }
    }

    private void setHtmlColor(final String htmlCode) {

        // Common colors
        if ("black".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.black);
        } else if ("white".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.white);
        } else if ("gray".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.gray);

        } else if ("red".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.red);
        } else if ("blue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.blue);
        } else if ("green".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.green);

        } else if ("orange".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.orange);
        } else if ("turquoise".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.turquoise);
        } else if ("pink".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.pink);
        } else if ("purple".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.purple);
        } else if ("violet".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.violet);
        } else if ("yellow".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.yellow);

            // Less used colors
        } else if ("aliceblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.aliceblue);
        } else if ("antiquewhite".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.antiquewhite);
        } else if ("aqua".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.aqua);
        } else if ("aquamarine".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.aquamarine);
        } else if ("azure".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.azure);
        } else if ("beige".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.beige);
        } else if ("bisque".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.bisque);
        } else if ("blanchedalmond".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.blanchedalmond);
        } else if ("blueviolet".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.blueviolet);
        } else if ("brown".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.brown);
        } else if ("burlywood".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.burlywood);
        } else if ("cadetblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.cadetblue);
        } else if ("chartreuse".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.chartreuse);
        } else if ("chocolate".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.chocolate);
        } else if ("coral".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.coral);
        } else if ("cornflowerblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.cornflowerblue);
        } else if ("cornsilk".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.cornsilk);
        } else if ("crimson".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.crimson);
        } else if ("cyan".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.cyan);
        } else if ("darkblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkblue);
        } else if ("darkcyan".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkcyan);
        } else if ("darkgoldenrod".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkgoldenrod);
        } else if ("darkgray".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkgray);
        } else if ("darkgreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkgreen);
        } else if ("darkkhaki".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkkhaki);
        } else if ("darkmagenta".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkmagenta);
        } else if ("darkolivegreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkolivegreen);
        } else if ("darkorange".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkorange);
        } else if ("darkorchid".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkorchid);
        } else if ("darkred".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkred);
        } else if ("darksalmon".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darksalmon);
        } else if ("darkseagreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkseagreen);
        } else if ("darkslateblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkslateblue);
        } else if ("darkslategray".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkslategray);
        } else if ("darkturquoise".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkturquoise);
        } else if ("darkviolet".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.darkviolet);
        } else if ("deeppink".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.deeppink);
        } else if ("deepskyblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.deepskyblue);
        } else if ("dimgray".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.dimgray);
        } else if ("dodgerblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.dodgerblue);
        } else if ("firebrick".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.firebrick);
        } else if ("floralwhite".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.floralwhite);
        } else if ("forestgreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.forestgreen);
        } else if ("fuchsia".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.fuchsia);
        } else if ("gainsboro".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.gainsboro);
        } else if ("ghostwhite".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.ghostwhite);
        } else if ("gold".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.gold);
        } else if ("goldenrod".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.goldenrod);
        } else if ("greenyellow".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.greenyellow);
        } else if ("honeydew".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.honeydew);
        } else if ("hotpink".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.hotpink);
        } else if ("indianred".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.indianred);
        } else if ("indigo".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.indigo);
        } else if ("ivory".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.ivory);
        } else if ("khaki".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.khaki);
        } else if ("lavender".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lavender);
        } else if ("lavenderblush".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lavenderblush);
        } else if ("lawngreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lawngreen);
        } else if ("lemonchiffon".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lemonchiffon);
        } else if ("lightblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightblue);
        } else if ("lightcoral".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightcoral);
        } else if ("lightcyan".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightcyan);
        } else if ("lightgoldenrodyellow".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightgoldenrodyellow);
        } else if ("lightgreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightgreen);
        } else if ("lightgrey".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightgrey);
        } else if ("lightpink".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightpink);
        } else if ("lightsalmon".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightsalmon);
        } else if ("lightseagreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightseagreen);
        } else if ("lightskyblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightskyblue);
        } else if ("lightslategray".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightslategray);
        } else if ("lightsteelblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightsteelblue);
        } else if ("lightyellow".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lightyellow);
        } else if ("lime".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.lime);
        } else if ("limegreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.limegreen);
        } else if ("linen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.linen);
        } else if ("magenta".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.magenta);
        } else if ("maroon".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.maroon);
        } else if ("mediumauqamarine".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumauqamarine);
        } else if ("mediumblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumblue);
        } else if ("mediumorchid".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumorchid);
        } else if ("mediumpurple".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumpurple);
        } else if ("mediumseagreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumseagreen);
        } else if ("mediumslateblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumslateblue);
        } else if ("mediumspringgreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumspringgreen);
        } else if ("mediumturquoise".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumturquoise);
        } else if ("mediumvioletred".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mediumvioletred);
        } else if ("midnightblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.midnightblue);
        } else if ("mintcream".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.mintcream);
        } else if ("Mistyrose".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.Mistyrose);
        } else if ("moccasin".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.moccasin);
        } else if ("navajowhite".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.navajowhite);
        } else if ("navy".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.navy);
        } else if ("oldlace".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.oldlace);
        } else if ("olive".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.olive);
        } else if ("olivedrab".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.olivedrab);
        } else if ("orangered".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.orangered);
        } else if ("orchid".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.orchid);
        } else if ("palegoldenrod".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.palegoldenrod);
        } else if ("palegreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.palegreen);
        } else if ("paleturquoise".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.paleturquoise);
        } else if ("palevioletred".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.palevioletred);
        } else if ("papayawhip".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.papayawhip);
        } else if ("peachpuff".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.peachpuff);
        } else if ("peru".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.peru);
        } else if ("plum".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.plum);
        } else if ("powderblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.powderblue);
        } else if ("rosybrown".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.rosybrown);
        } else if ("royalblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.royalblue);
        } else if ("saddlebrown".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.saddlebrown);
        } else if ("salmon".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.salmon);
        } else if ("sandybrown".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.sandybrown);
        } else if ("seagreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.seagreen);
        } else if ("seashell".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.seashell);
        } else if ("sienna".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.sienna);
        } else if ("silver".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.silver);
        } else if ("skyblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.skyblue);
        } else if ("slateblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.slateblue);
        } else if ("slategray".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.slategray);
        } else if ("snow".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.snow);
        } else if ("springgreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.springgreen);
        } else if ("steelblue".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.steelblue);
        } else if ("tan".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.tan);
        } else if ("teal".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.teal);
        } else if ("thistle".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.thistle);
        } else if ("tomato".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.tomato);
        } else if ("wheat".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.wheat);
        } else if ("whitesmoke".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.whitesmoke);
        } else if ("yellowGreen".equalsIgnoreCase(htmlCode)) {
            this.setColor(Color.yellowGreen);
        }
    }

    private void setRgbColor(final String rgbCode) {
        final RegExp c = RegExp.compile("rgb *\\( *([0-9]{1,3}), *([0-9]{1,3}), *([0-9]{1,3}) *([0-9]{1,3}) *\\)");
        final MatchResult m = c.exec(rgbCode);

        if (c.test(rgbCode)) {
            this.setColor(
                    Integer.parseInt(m.getGroup(1)),
                    Integer.parseInt(m.getGroup(2)),
                    Integer.parseInt(m.getGroup(3)),
                    m.getGroup(4) != null ? Integer.parseInt(m.getGroup(4)) : 255
                    );
        }
    }

    private void setHexColor(final String hexCode) {
        String code = hexCode;
        if (code.charAt(0) == '#') {
            code = code.substring(1);
        }

        switch (code.length()) {
            case 3:
                code = code.substring(0, 1) + code.substring(0, 1) + code.substring(1, 2) + code.substring(1, 2) + code.substring(2, 3) + code.substring(2, 3)
                        + "FF";
                break;
            case 4:
                code = code.substring(0, 1) + code.substring(0, 1) + code.substring(1, 2) + code.substring(1, 2) + code.substring(2, 3) + code.substring(2, 3)
                        + code.substring(3, 4) + code.substring(3, 4);
                break;
            case 6:
                code += "FF";
                break;
        }

        this.setColor(
                code.substring(0, 2),
                code.substring(2, 4),
                code.substring(4, 6),
                code.substring(6, 8)
                );
    }
}
