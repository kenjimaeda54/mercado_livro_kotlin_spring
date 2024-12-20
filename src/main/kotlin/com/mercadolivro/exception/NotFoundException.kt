package com.mercadolivro.exception

//a exception qeu estamos herdadno ja possui message por isso override
class NotFoundException(override  val message: String,val errorCode: String): Exception() {
}