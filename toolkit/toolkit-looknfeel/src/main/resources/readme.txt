This is a look & feel archive for Bonita BPM Portal.

BonitaConsole.html is the entry point for the portal and BonitaForm.html is the entry point for process forms.
You can customize this look & feel by modifying the existing resources or by adding or removing some. If you add a resource, you must declare it in the header of the HTML pages).
The easiest way to configure the portal look & feel is to modify the skin/skin.config.less file.

Content detail:
css						Directory containing default CSS files used by the process forms.
css/bonita_forms.css	Default CSS for process forms.
css/footer.css			CSS for footers in process forms.
images 					Directory of images. These images are the size needed for the standard layout. If you change the layout, you might need to adjust the images sizes. If you want to use an image that is a different size, you might need to modify the layout.
init/reset.less			For browser compatibility, do not change.
scripts					Directory of scripts, including JQuery scripts.
skin					Contains LESS files and directories for fonts and images used in the skin.
skin/skin.config.less	The main LESS file that defines the appearance. Change this file to modify the appearance of Bonita BPM Portal web <not mobile>. You only need to modify the other LESS files if you want to change the behaviour of the pages.
tools					Contains special files required for compatibility with Internet Explorer.
bonita_ie8.css			Specific CSS content for Internet Explorer 8 support.
BonitaConsole.html		HTML entry page for Bonita BPM Portal. If you want to add a custom JavaScript or custom CSS, modify this file and add it to the header.
BonitaForm.html			HTML entry page for forms in Bonita BPM Portal. If you want to add a custom JavaScript or custom CSS, modify this file and add it to the header.
main.less				Main LESS file Used to compile the LESS files into CSS. You must not rename this file, and we recommend that you do not change it.
PIE.htc					Functions required for some CSS features to work in Internet Explorer.