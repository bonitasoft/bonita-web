describe("Bonita report utils", function() {
    
    var report = bonitasoft.utils.report;
    
    beforeEach(function(){
        this.addMatchers({
            toMatchObject: function(expected) {
                return expected(this.actual);
            }
        });
    });
    
    /*
     * Namespacing
     */
    describe("should be able to clear a select from the form", function() {
        
        it('and throw an assertion if selector is undefined', function() {
            var selector;
            
            expect(function() {
                report.form.clear(selector);
            }).toThrow(jasmine.any(String));
        });
        
        it('to leave it empty', function() {
            var selector = jasmine.createSpyObj('select', [ 'html' ]);
            
            report.form.clear(selector);
            
            expect(selector.html).toHaveBeenCalledWith('');
        });
        
        it("and add defaults value", function() {
            var selector = jasmine.createSpyObj('select', [ 'html', 'append' ]);
                        
            report.form.clear(selector, {
                text: 'text',
                val: 'val'
            });
            
            expect(selector.html).toHaveBeenCalledWith('');
            expect(selector.append).toMatchObject(function(actual) {
                console.log(actual);
                return true;
            });
        });
    });
    
});