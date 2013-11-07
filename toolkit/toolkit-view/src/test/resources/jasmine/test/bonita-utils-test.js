describe("Bonita utils", function() {
    
    var utils = bonitasoft.utils;
    
    /*
     * Namespacing
     */
    describe("should be able to extend a namespace", function() {
        
        it('from a non existing path', function() {
            var extension = 'extension', root = {};
            
            utils.namespace.extend(root, 'path.which.doesnt.exist', function(exist) {
                exist.extension = extension;
            });
            
            expect(root.path.which.doesnt.exist.extension).toEqual(extension);
        });
        
        it("from an existing path", function() {
            var extension = 'extension',
                existing = 'existing',
                root = {
                    to: {
                        extend: {
                            existing: existing
                        }
                    }
                };
            
                utils.namespace.extend(root, 'to.extend', function(extend) {
                    extend.extension = extension;
                });
            
                expect(root.to.extend.existing).toEqual(existing);
                expect(root.to.extend.extension).toEqual(extension);
        });
    });
    
    /*
     * Assertion
     */
    describe("should provide assertion", function() {
        
        it("which can success", function() {
            var foo = '';
            
            utils.assertion.assert(foo !== undefined);
        });
        
        it("which can fail", function() {
            var foo,
                message = 'foo must be initialized';
            
            expect(function() {
                utils.assertion.assert(foo !== undefined, message)
            }).toThrow(message);
        });
    });
    
    /*
     * Variable
     */
    describe("should provide variable replacement in html context", function() {
        
        it("which assert if context is undefined", function() {
            var context;
            
            expect(function() {
                utils.variable.inject(context, "name", "value");
            }).toThrow(jasmine.any(String));
            
        });
        
        it("which replace a variable if context contains it", function() {
            var context = jasmine.createSpyObj('html', [ 'html' ]);
            context.html.andCallFake(function() {
                return 'variable = %variable%';
            });
            
            utils.html.variable.inject(context, 'variable', 'value');
            
            expect(context.html).toHaveBeenCalledWith('variable = value');
        });
        
        it("which replace multiple variables if context contains them", function() {
            var context = jasmine.createSpyObj('html', [ 'html' ]);
            context.html.andCallFake(function() {
                return 'variable 1 = %variable%, variable 2 = %variable%';
            });
            
            utils.html.variable.inject(context, 'variable', 'value');
            
            expect(context.html).toHaveBeenCalledWith('variable 1 = value, variable 2 = value');
        });
    });
    
});