fix(v6 forms) date picker / suggest box display with cherome >= 61

- duplicate gwt-user 2.5.1 classes and apply the [same fix](https://github.com/gwtproject/gwt/commit/88a028f6b74ecc529a4b878301448d057d2c6e89) as the one that was made in GWT 2.8.2
- use classpath shadowing to patch GWT 2.5.1

Covers [BS-17207](https://bonitasoft.atlassian.net/browse/BS-17207)