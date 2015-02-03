/* global   bonitasoft:false,

            jasmine:false,
            beforeEach,
            describe:false,
            it:false,
            expect:false */

describe("Bonita utils", function () {

    var utils = bonitasoft.utils;

    /*
     * Namespacing
     */
    describe("should be able to extend a namespace", function () {

        it('from a non existing path', function () {
            var extension = 'extension',
                root = {};

            utils.namespace.extend(root, 'path.which.doesnt.exist', function (exist) {
                exist.extension = extension;
            });

            expect(root.path.which.doesnt.exist.extension).toEqual(extension);
        });

        it("from an existing path", function () {
            var extension = 'extension',
                existing = 'existing',
                root = {
                    to: {
                        extend: {
                            existing: existing
                        }
                    }
                };

            utils.namespace.extend(root, 'to.extend', function (extend) {
                extend.extension = extension;
            });

            expect(root.to.extend.existing).toEqual(existing);
            expect(root.to.extend.extension).toEqual(extension);
        });
    });

    /*
     * Assertion
     */
    describe("should provide assertion", function () {

        it("which can success", function () {
            var foo = '';

            utils.assertion.assert(foo !== undefined);
        });

        it("which can fail", function () {
            var foo,
                message = 'foo must be initialized';

            expect(function () {
                utils.assertion.assert(foo !== undefined, message);
            }).toThrow(message);
        });
        
        describe("for isArray", function() {
            it("which can success", function() {
                var foo = [];
                
                utils.assertion.isArray(foo);
            });
            
            it("which fail for undefined", function() {
                var foo,
                    message = "foo can't be undefined";
                
                expect(function () {
                    utils.assertion.isArray(foo, message);
                }).toThrow(message);
            });
            
            it("which fail for Object", function() {
                var foo = {},
                    message = "foo can't be an object";
                
                expect(function () {
                    utils.assertion.isArray(foo, message);
                }).toThrow(message);
                
            });
        });
        
        describe("for isDefined", function() {
            it("which can success", function() {
                var foo = {};
                
                utils.assertion.isDefined(foo);
            });
            
            it("which fail for undefined", function() {
                var foo,
                    message = "foo must be defined";
                
                expect(function () {
                    utils.assertion.isArray(foo, message);
                }).toThrow(message);
            });
        });
        
        
    });

    /*
     * Variable
     */
    describe("should provide variable replacement in html context", function () {

        it("which assert if context is undefined", function () {
            var context;

            expect(function () {
                utils.variable.inject(context, "name", "value");
            }).toThrow(jasmine.any(String));

        });

        it("which replace a variable if context contains it", function () {
            var context = jasmine.createSpyObj('html', ['html']);
            context.html.andCallFake(function () {
                return 'variable = %variable%';
            });

            utils.html.variable.inject(context, 'variable', 'value');

            expect(context.html).toHaveBeenCalledWith('variable = value');
        });

        it("which replace multiple variables if context contains them", function () {
            var context = jasmine.createSpyObj('html', ['html']);
            context.html.andCallFake(function () {
                return 'variable 1 = %variable%, variable 2 = %variable%';
            });

            utils.html.variable.inject(context, 'variable', 'value');

            expect(context.html).toHaveBeenCalledWith('variable 1 = value, variable 2 = value');
        });
    });

    /*
     * Form utils
     */
    describe("provide form utilities", function () {

        var renderer = function (item) {
            return {
                text: item.text,
                value: item.value
            };
        },
            element, select;

        beforeEach(function () {
            element = jasmine.createSpyObj('select', ['html', 'append', 'data']);
            select = utils.form.select.wrap(element);
        });

        it("that should provide a method to clear a select", function () {

            select.clear();

            expect(element.html).toHaveBeenCalledWith('');
        });

        it("that should be able to add options of a select", function () {

            var options = [createOption(1), createOption(2)];

            select.add(options, renderer);
            
            verifyAppendSequence(element, options);

        });
        
        function createOption(value) {
            var label = 'label' + value;
            return {
                text: label,
                value: value,
            };
        }

        /*
         * Verify options that have been append to the element's html.
         * The order matter.
         */
        function verifyAppendSequence(element, sequence) {
            for (var i = 0; i < sequence.length; i += 1) {
                expect(element.append.calls[i].args[0].innerHTML)
                    .toBe(sequence[i].text);
                expect(+element.append.calls[i].args[0].value)
                    .toBe(sequence[i].value);
            }
        }
    });

    /*
     * Arrays utils
     */
    describe("provide array utils", function () {
        describe("to merge two arrays", function () {
            it("unordered", function () {
                var foo = ["1", "2", "4"],
                    bar = ["3", "5"];

                var composite = utils.arrays.merge(foo, bar);
                
                expect(composite).toEqual(["1", "2", "4", "3", "5"]);
            });
            
            it("ordered", function () {
                var foo = ["1", "2", "4"],
                    bar = ["3", "5"];

                var composite = utils.arrays.merge(foo, bar, function(a, b) {
                    return b - a;
                });
                
                expect(composite).toEqual(["5", "4", "3", "2", "1"]);
            });

            it("which throw an exception if foo isn't an array", function () {
                var foo,
                    bar = ["1", "2"];
                
                expect(function(){
                    utils.arrays.merge(foo, bar);
                    
                }).toThrow("a must be an array");
            });

            it("which doesn't merge b if not an array", function () {});

        });
    });

});