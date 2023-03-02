package xyz.fortern.util

import be.teletask.onvif.models.OnvifDevice
import xyz.fortern.pojo.OnvifCamera

fun OnvifCamera.toDevice(): OnvifDevice {
	return OnvifDevice(this.ip + ":" + this.port, this.username, this.password)
}