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
		tilt = this.tilt.toFloat() * 50 - 40
		zoom = this.zoom.toFloat() * 10
	} else {
		pan = this.pan.toFloat() * 180 + 180
		tilt = this.tilt.toFloat() * 50 - 40
		zoom = this.zoom.toFloat() * 10
	}
	return Preset(name, token, pan, tilt, zoom)
}

fun presetToXyz(p: Double, t: Double, z: Double): Array<Double> {
	return arrayOf((p - 180) / 180, (t + 40) / 50, z / 10)
}

fun OnvifStatus.toPtzInfo(): PtzInfo {
	val pan: Float = this.pan.toFloat() * 180 + 180
	val tilt: Float = this.tilt.toFloat() * 50 - 40
	val zoom: Float = this.zoom.toFloat() / 10
	return PtzInfo(pan, tilt, zoom)
}
