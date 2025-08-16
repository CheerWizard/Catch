package com.cws.nativeksp

//@JvmInline
//value class EntityArray(private val indices : IntArray) {
//
//    operator fun set(i: Int, entity: EntityData) = indices.set(i, entity.index)
//    operator fun get(i: Int) = EntityData(indices[i])
//
//    fun forEach(block : (EntityData) -> Unit) = indices.forEach { block(EntityData(it)) }
//
//}