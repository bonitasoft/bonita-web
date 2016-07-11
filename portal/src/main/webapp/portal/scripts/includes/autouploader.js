$(function () {

    var PLUGIN_UPLOADER = 'plugin.uploader';

    /**
     * Override val function
     */
    var super_val = $.fn.val;
    $.fn.val = function (value) {
        var inputUploader = $(this).data(PLUGIN_UPLOADER);
        if ($.isDefined(inputUploader)) {
            if (value === null) {
                inputUploader.reset();
            } else {
                return inputUploader.val();
            }
        } else {
            if ($.isNull(value)) {
                // get super value
                return super_val.call(this);
            } else {
                // set super value
                super_val.call(this, value);
            }
            return this;
        }
    };

    /**
     * Check if all inputs have finished to upload
     */
    function areAllUploadOver(inputs) {
        for (var i = 0; i < inputs.length; i++) {
            var input = $(inputs.get(i)).data(PLUGIN_UPLOADER);
            if ($.isDefined(input) && input.isUploading()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Apply uploader plugin to all inputs
     */
    function applyPluginOnInputs(inputs) {
        inputs.each(function () {
            // for each input in the form
            var self = $(this);
            self.data(PLUGIN_UPLOADER, new AutoUploader(self));
        })
    }

    /**
     * Create auto upload plugin
     */
    $.fn.autoUploader = function (action) {
        switch (action) {
            case 'finished':
                return areAllUploadOver($(':file', this));
            default:
                return applyPluginOnInputs(this);
        }
    };

    /**
     * Add plugin to makers
     */
    $.uiManager.addMaker(function (context) {
        $(':file[rel]', context).autoUploader();
    });

    // /////////////////////////////////////////////////////////
    // Auto uploader object definition
    // /////////////////////////////////////////////////////////
    function AutoUploader(fileInput) {
        var __set__ = {};

        var input = fileInput;
        var inputName = input.attr("name");
        var uploaderId= 'uploader_' + inputName;
        var uploaderEl = $('<div class="uploader" id="'+uploaderId+'"><a id="' + inputName + '"></a></div>');
        input.after(uploaderEl).hide();
        
        var pluploader = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: inputName,
            container: uploaderId,
            url: input.getOption('url'),
            flash_swf_url: 'scripts/ext/Moxie.swf',
            silverlight_xap_url: 'scripts/ext/Moxie.xap',
            filters: input.getOption('filters')
        });
        // /////////////////////////////////////////////////////////
        // private
        // /////////////////////////////////////////////////////////

        var UPLOADER_VALUE = 'uploader_value',
            UPLOADING_STATUS = 'uploading_status',
            CSS_IS_READY = 'ready',
            CSS_IS_UPLOADING = 'uploading',
            CSS_IS_FAILED = 'failed',
            CSS_IS_DONE = 'done';

        // object initialization
        var init = function () {



            pluploader.bind('QueueChanged', onQueueChanged);
            pluploader.bind('FileUploaded', onFileUploaded);
            pluploader.bind('Error', onError);
            pluploader.init();

            updateUploaderState(false, "", CSS_IS_READY, input.getOption('text.filepicker'));
        };

        /**
         * Fired when the file queue is changed.
         */
        var onQueueChanged = function (pluploader) {
            if (pluploader.files.length > 0) {
            	$('div.alert_message.ERROR').remove();
            	updateUploaderState(true, pluploader.files[0].name,
            			CSS_IS_UPLOADING, pluploader.files[0].name);
            
              /* WTF... */
            	var cache = document.createElement("div");
            	cache.id="uploadCache_"+input.attr("name");
            	cache.style.position="absolute";
            	cache.style.background="#000000";
            	cache.style.opacity="0.3";
            	cache.style.width="100%";
            	cache.style.height="100%";
            	cache.style.top="0";
            	cache.style.left="0";
            	$('div.page_processupload div.fileupload div.input div#uploader'+input.attr("name")).append(cache);
            	/* ...is that?! */
            
            	var linkInstallUpload = $("a.installUpload");
              if (!linkInstallUpload.hasClass("disabled")) {
            		linkInstallUpload.addClass("disabled");
            	}
            	pluploader.start();
            }
        };

        /**
         * Fired when a file is successfully uploaded.
         */
        var onFileUploaded = function (pluploader, file, response) {
            $('div.alert_message.ERROR').remove();
            pluploader.removeFile(file);
            $("a.installUpload").removeClass("disabled");
            $("#uploadCache_"+input.attr("name")).remove();
            //fix IE9 bug : pre tag is added to the response
            //may be fixed in the latest version but autouploader.js does not works with the latest version
            var resp = response.response;
            if(resp) resp = resp.replace(/<\/?pre>/gi,'');
            updateUploaderState(false, resp, CSS_IS_DONE);
        };

        var addError = function (message) {
            $('div.alert_message.ERROR').remove();
            $('#filepicker_' + input.attr("name") + ', #uploader_' + input.attr("name")).after(
            		'<div class="alert_message ERROR">' + message + '</div>');
        };

        /**
         * Fires when a error occurs.
         */
        var onError = function (pluploader, file, response) {
            if(file.file) pluploader.removeFile(file.file);
            if (file.code == -601) {
            	addError(input.getOption('text.extensionerror'));
            } else if (file.code == -500) {
            	addError('The specified file cannot be uploaded. Add plugin (like flashplayer or silverlight) to your browser to handle this type of file, then try the upload again.');
            } else {
            	addError(file.message);
            }
            updateUploaderState(false, file.message, CSS_IS_FAILED)
        };

        /**
         * Update object status, values, style as well as message
         */
        var updateUploaderState = function (isUploading, value, cssClass, message) {
            cleanAllCssClasses();
            uploaderEl.addClass(cssClass);

            if ($.isDefined(message)) {
                $(uploaderEl.children()[0], uploaderEl).text(message)
            }

            input.data(UPLOADING_STATUS, isUploading)
                .data(UPLOADER_VALUE, value);
        };

        var cleanAllCssClasses = function () {
            uploaderEl.removeClass(CSS_IS_READY)
                .removeClass(CSS_IS_UPLOADING)
                .removeClass(CSS_IS_DONE)
                .removeClass(CSS_IS_FAILED);
        };

        // /////////////////////////////////////////////////////////
        // public
        // /////////////////////////////////////////////////////////

        __set__.isUploading = function () {
            return input.data(UPLOADING_STATUS);
        };

        __set__.val = function () {
            return input.data(UPLOADER_VALUE);
        };

        __set__.reset = function () {
            updateUploaderState(false, "", CSS_IS_READY, input.getOption('text.filepicker'));

        };

        // /////////////////////////////////////////////////////////
        // constructor
        // /////////////////////////////////////////////////////////

        init();

        return __set__;
    }

});