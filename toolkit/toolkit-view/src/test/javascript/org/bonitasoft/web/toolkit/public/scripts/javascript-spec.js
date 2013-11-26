/* global   jasmine:false,
            beforeEach,
            describe:false,
            it:false,
            expect:false */

describe("Javascript", function () {

    describe("arrays", function () {

        it("can be merged", function () {
            var foo = ["a"],
                bar = ["b", "c"];

            foo.push(bar);

            expect(foo.join()).toBe("a,b,c");
        });

        it("are not automatically sorted after merge", function () {
            var foo = ["b"],
                bar = ["a", "c"],
                baz;

            baz = foo.concat(bar);

            expect(baz[0]).toBe("b");
            expect(baz[1]).toBe("a");
            expect(baz[2]).toBe("c");
        });

        it("are sorted by alphabetical order by default", function () {
            var foo = ["b", "a", "c"];

            foo.sort();

            expect(foo.join()).toBe("a,b,c");
        });

        it("can be sorted by numerical order by setting a order function", function () {
            var foo = [2, 4, 2, 1, 3, 5];

            foo.sort(function (a, b) {
                return a - b;
            });

            expect(foo.join()).toBe("1,2,2,3,4,5");
        });

        it("default sort can order numbers", function () {
            var foo = [2, 4, "2", 1, "3", 5];

            foo.sort();

            expect(foo.join()).toBe("1,2,2,3,4,5");
        });
    });

    describe("can use private field", function () {

        var factory = {};

        (function (factory) {

            var _key = {};
            
            var Foo = function (bar) {
                var hidden = {
                    bar: bar
                };

                this.private = function (key) {
                    return key === _key && hidden;
                };
            };
            Foo.prototype = {
                get public() {
                    return this.private(_key).bar;
                },
                set public(bar) {
                    this.private(_key).bar = bar;
                }
            };

            Foo.create = function (bar) {
                return new Foo(bar);
            };

            // export
            factory.Foo = Foo;
        })(factory);
        
        it("which can't be accessed", function () {
            var foo = factory.Foo.create('baz');
            
            expect(foo.private({}).bar).toBeUndefined();
        });

        it("which are handled per object instance", function () {
            var foo1 = factory.Foo.create('baz'),
                foo2 = factory.Foo.create('qux');
            
            expect(foo1.public).toBe('baz');
            expect(foo2.public).toBe('qux');
        });
        
        it("which works with prototype methods", function () {
            var foo1 = factory.Foo.create('baz'),
                foo2 = factory.Foo.create('qux');
            foo1.public = 'qux';
            foo2.public = 'baz';

            expect(foo1.public).toBe('qux');
            expect(foo2.public).toBe('baz');

        });
        
        it("but which can be hijacked", function () {
            var foo = factory.Foo.create('baz');
            
            var _key,
                override = foo.private;
            foo.private = function (key) {
                _key = key;
                return override(key);
            };
            expect(foo.public).toBe('baz'); // need to be called once

            expect(foo.private(_key).bar).toBe('baz');
        });
    });
});