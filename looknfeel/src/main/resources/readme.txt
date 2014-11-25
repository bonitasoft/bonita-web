This is a look & feel archive for Bonita BPM Portal.

There are two types of page in the Portal: pages developed with GWT, as in earlier versions, and new pages that have been developed using Angular. The two types of page have different mechanisms for defining Look & Feel. This means that in some cases, you might need to specify a Look & Feel element in two places to get a consistent look for your Portal and custom pages.

For GWT pages, the most critical file in the Look & Feel definition is skin/skin.config.less file. For pages developed in Angular, the Look & Feel is defined using Bootstrap. To modify the Look & Feel of these pages, update the skin definition in skin\bootstrap\portal for Portal pages or in skin\bootstrap\applications for applications.

Key files and directories:
BonitaConsole.html:                     HTML entry page for Bonita BPM Portal. If you want to add a custom JavaScript or custom CSS, modify this file and add it to the header.
BonitaForm.html:                        HTML entry page for forms in Bonita BPM Portal. If you want to add a custom JavaScript or custom CSS, modify this file and add it to the header.
bonita_ie8.css:                         Specific CSS content for Internet Explorer 8 support.
main.less:                              Used to compile the LESS files into CSS. You must not rename this file, and we recommend that you do not change it.
css:                                    Directory containing default CSS files used by the process forms.
css/bonita_forms.css:                   Default CSS for process forms.
css/footer.css:                         CSS for footers in process forms.
images:                                 Directory of images. These images are the size needed for the standard layout. If you change the layout, you might need to adjust the images sizes. If you want to use an image that is a different size, you might need to modify the layout.
init/reset.less:                        For browser compatibility, do not change.
PIE.htc:                                Functions required for some CSS features to work in Internet Explorer.
scripts:                                Directory of scripts, including JQuery scripts.
skin:                                   Contains LESS files and directories for fonts and images used in the skin.
skin/bootstrap/applications:            Customizations to Bootstrap for applications. Change these files to modify the appearance of application pages.
skin/bootstrap/applications/main.less:  The entry point that identifies the files to be compiled to create the Bootstrap Look & Feel for applications.
skin/bootstrap/portal:                  Customizations to Bootstrap for the Portal. Change these files to modify the appearance of Portal pages.
skin/bootstrap/portal/main.less:        The entry point that identifies the files to be compiled to create the Bootstrap Look & Feel for Portal pages developed with Angular.
skin/skin.config.less:                  The main LESS file that defines the appearance. Change this file to modify the appearance of Bonita BPM Portal web (not mobile). You only need to modify the other LESS files if you want to change the behaviour of the pages.
tools:                                  Contains special files required for compatibility with Internet Explorer.
VERSION:                                Contains the version flag. Do not update or delete this file.