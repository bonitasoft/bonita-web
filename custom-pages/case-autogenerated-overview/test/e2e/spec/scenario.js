'use strict';
describe('custom page', function() {
  beforeEach(function(){
    browser.get('http://localhost:3000/bonita/bonita-custom-page-seed/index.html');
  });

  it('should have a title', function() {
    expect(browser.getTitle()).toEqual('App');
  });
});
