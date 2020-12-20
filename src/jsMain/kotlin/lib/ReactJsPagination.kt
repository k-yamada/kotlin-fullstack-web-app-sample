@file:JsModule("react-js-pagination")
@file:JsNonModule

import react.RClass
import react.RProps

@JsName("default")
external val reactJSPagination: RClass<ReactJSPaginationProps>

external interface ReactJSPaginationProps : RProps {
    var activePage: Int
    var itemsCountPerPage: Int
    var totalItemsCount: Int
    var pageRangeDisplayed: Int
    var itemClass: String
    var linkClass: String
    var onChange: (Int) -> Unit
}
