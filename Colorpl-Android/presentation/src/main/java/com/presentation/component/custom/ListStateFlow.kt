package com.presentation.component.custom

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class ListStateFlow<T> {


    private val _items = MutableStateFlow<MutableList<T>>(mutableListOf())
    val items: StateFlow<List<T>> get() = _items


    fun get(idx: Int): T {
        return items.value[idx]
    }

    fun add(item: T) {
        _items.update {
            it.toMutableList().apply { add(item) }
        }
    }


    fun remove(item: T) {
        _items.update {
            it.toMutableList().apply { remove(item) }
        }
    }

    fun addOrRemove(item: T) {

        if (items.value.contains(item)) {
            remove(item)
            Timber.d("아이템 뭐야 들어와? 삭제 $item")
        } else {
            add(item)
            Timber.d("아이템 뭐야 들어와? 추가 $item")
        }
    }

    // 형선
    fun getItemCount(): Int {
        return items.value.size
    }

    fun checkItemEmpty(): Boolean {
        return items.value.size == 0
    }


    fun clear() {
        _items.update {
            it.toMutableList().apply { clear() }
        }
    }
}