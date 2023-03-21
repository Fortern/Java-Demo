package xyz.fortern.util

import be.teletask.onvif.models.OnvifDevice
import be.teletask.onvif.models.OnvifPreset
import be.teletask.onvif.models.OnvifStatus
import xyz.fortern.pojo.Camera
import xyz.fortern.pojo.Preset
import xyz.fortern.pojo.PtzInfo

fun Camera.toDevice(): OnvifDevice {
	return OnvifDevice(this.ip + ":" + this.port, this.username, this.password)
}

fun OnvifPreset.toPreset(manufacturer: String): Preset {
	val name: String = this.name
	val token: String = this.token
	val pan: Float
	val tilt: Float
	val zoom: Float
	if (manufacturer == "HIKVISION") {
		pan = this.pan.toFloat() * 180 + 180
		tilt = this.tilt.toFloat() * 90
		zoom = (this.zoom.toFloat() + 1) * 10
	} else {
		pan = this.pan.toFloat()
		tilt = this.tilt.toFloat()
		zoom = this.zoom.toFloat()
	}
	return Preset(name, token, pan, tilt, zoom)
}

fun OnvifStatus.toPtzInfo(): PtzInfo {
	return PtzInfo(this.pan.toFloat(), this.tilt.toFloat(), this.zoom.toFloat())
}
