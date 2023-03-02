package xyz.fortern.exception

class OnvifResponseTimeoutException: RuntimeException {
	constructor() : super()
	
	constructor(message: String) : super(message)
}