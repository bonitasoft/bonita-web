package org.bonitasoft.console.common.server.servlet;

class IconContent {
    private byte[] content;
    private String mimeType;

    public IconContent(byte[] content, String mimeType) {
        this.content = content;
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public String getMimeType() {
        return mimeType;
    }
}
