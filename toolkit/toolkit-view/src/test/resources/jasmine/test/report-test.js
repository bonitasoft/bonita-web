/* jshint   devel:true */
/* global   bonitasoft:false, 
            jasmine:false,
            beforeEach:false,
            describe:false,
            it:false,
            expect:false */

describe("Bonita report utils", function () {

    var report = bonitasoft.utils.report,
        render = function (item) {
            return {
                text: item.text,
                val: item.val
            };
        },
        element, select;

    beforeEach(function () {
        element = jasmine.createSpyObj('select', ['html', 'append', 'data']);
        select = new report.form.Select(element);
    });

    describe("should provide a method to clear a select", function () {

        it('to leave it empty', function () {

            select.clear();

            expect(element.html).toHaveBeenCalledWith('');
            expect(element.append).not.toHaveBeenCalled();
        });

        it("and add a default value", function () {
            var option = createOption(1);

            select.clear(option);

            expect(element.html).toHaveBeenCalledWith('');
            expectAppendCall(element, 0).toBe(option.outerHTML);
        });
    });

    describe("should be able to populate a select", function () {
        it("which is empty", function () {
            var option1 = createOption(1),
                option2 = createOption(2);

            select.add([option1, option2], render);

            expectAppendCall(element, 0).toBe(option1.outerHTML);
            expectAppendCall(element, 1).toBe(option2.outerHTML);
        });

        it("which is already contains options", function () {
            var option1 = createOption(1),
                option2 = createOption(2),
                option3 = createOption(3);
            select.add([option1, option2], render);

            select.add([option3], render);

            expectAppendCall(element, 0).toBe(option1.outerHTML);
            expectAppendCall(element, 1).toBe(option2.outerHTML);
            expectAppendCall(element, 2).toBe(option3.outerHTML);
        });
    });

    function createOption(value) {
        var label = 'label' + value;
        return {
            text: label,
            val: value,
            outerHTML: "<option value=\"" + value + "\">" + label + "</option>"
        };
    }

    function expectAppendCall(element, number) {
        return expect(element.append.calls[number].args[0].outerHTML);
    }
});