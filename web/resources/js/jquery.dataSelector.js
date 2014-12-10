// Selector for data attributes
// Usage examples:
// | data selector     | jQuery selector        |
// |-------------------|------------------------|
// | $$('name')        | $('[data-name]')       |
// | $$('name', 10)    | $('[data-name=10]')    |
// | $$('name', false) | $('[data-name=false]') |
// | $$('name', null)  | $('[data-name]')       |
// | $$('name', {})    | Syntax error           |

window.$$ = function(dataName, dataVal) {
  return $(document).dataFind(dataName, dataVal);
};

$.fn.dataFind = function(dataName, dataVal) {
  if (dataVal != null) {
    return this.find("[data-" + dataName + "=" + dataVal + "]");
  } else {
    return this.find("[data-" + dataName + "]");
  }
};