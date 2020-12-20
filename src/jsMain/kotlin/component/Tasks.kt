package component

import com.github.kyamada.sample.model.TaskData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.ApiClient
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.li
import react.dom.nav
import reactJSPagination
import styled.*

interface TasksState : RState {
    var items: List<TaskData>
    var page: Int
    var totalCount: Long
    var text: String
}

class Tasks : RComponent<RProps, TasksState>() {
    override fun TasksState.init() {
        items = listOf()
        text = ""
        totalCount = 0
        MainScope().launch {
            setState {
                text = ""
            }
            fetchTasks(1)
        }
    }

    private suspend fun fetchTasks(page: Int) {
        val resp = ApiClient.getTasks(page, PER_PAGE)
        setState {
            this.page = page
            items = resp.tasks
            totalCount = resp.totalCount
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                margin(10.pt)
                width = 400.pt
            }

            styledDiv {
                css {
                    classes = mutableListOf("input-group", "mb-3")
                    marginBottom = 10.pt
                }
                styledInput(type = InputType.text, name = "itemText") {
                    css {
                        classes = mutableListOf("form-control")
                    }
                    key = "itemText"
                    attrs {
                        value = state.text
                        placeholder = "Add a to-do item"
                        onChangeFunction = {
                            val target = it.target as HTMLInputElement
                            setState {
                                text = target.value
                            }
                        }
                    }
                }

                button(classes = "btn btn-primary") {
                    +"Add"
                    attrs {
                        onClickFunction = {
                            if (state.text.isNotEmpty()) {
                                val newTask = TaskData(-1, state.text, 0, 0)
                                MainScope().launch {
                                    val createdTask = ApiClient.addTask(newTask)
                                    setState {
                                        items += createdTask
                                        text = ""
                                    }
                                }
                            }
                        }
                    }
                }
            }

            styledUl {
                css {
                    classes = mutableListOf("list-group")
                    marginBottom = 10.pt
                }
                for (item in state.items) {
                    li("list-group-item") {
                        key = item.id.toString()
                        +item.title
                        styledButton(type = ButtonType.button) {
                            css {
                                classes = mutableListOf("btn", "btn-outline-danger", "btn-sm")
                                marginLeft = 10.pt
                            }
                            +"×"
                            attrs {
                                onClickFunction = {
                                    MainScope().launch {
                                        ApiClient.deleteTask(item.id)
                                        setState {
                                            items = items.filterNot { it.id == item.id }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            nav {
                reactJSPagination {
                    attrs.activePage = state.page
                    attrs.itemsCountPerPage = PER_PAGE
                    attrs.totalItemsCount = state.totalCount.toInt()
                    attrs.pageRangeDisplayed = 5
                    attrs.itemClass = "page-item"
                    attrs.linkClass = "page-link"
                    attrs.onChange = ::handlePageChange
                }
            }
        }
    }

    private fun handlePageChange(page: Int) {
        MainScope().launch {
            fetchTasks(page)
        }
    }

    companion object {
        private const val PER_PAGE = 5
    }
}
