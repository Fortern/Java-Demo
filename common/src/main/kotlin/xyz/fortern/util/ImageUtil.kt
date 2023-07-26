package xyz.fortern.util

import java.awt.*
import java.io.File
import java.io.IOException
import java.io.OutputStream
import javax.imageio.ImageIO

/**
 * 给图片添加文字水印，结果输出到流中，避免生成中间文件
 *
 * @param srcImgFile   原始图片文件
 * @param outputStream 输出流
 * @param texts        多行水印内容
 * @param lineSpacing  行间距
 * @param strokeWidth  描边宽度
 * @param x            x坐标
 * @param yToBottom    最后一行距离底部的距离
 * @param formatName   图片的文件格式
 * @param textColor    文本颜色
 * @param outlineColor 描边颜色
 * @param font         字体
 * @return 操作是否成功
 * @throws IOException 图片读写出错时
 */
@Throws(IOException::class)
fun drawString(
	srcImgFile: File,
	outputStream: OutputStream,
	texts: Array<String?>,
	lineSpacing: Int,
	strokeWidth: Float,
	x: Int,
	yToBottom: Int,
	formatName: String,
	textColor: Color = Color.WHITE,
	outlineColor: Color = Color.BLACK,
	font: Font = Font(null, Font.PLAIN, 32)
): Boolean {
	val image = ImageIO.read(srcImgFile)
	val graphics = image.createGraphics()
	graphics.font = font
	val stroke: Stroke = BasicStroke(strokeWidth)
	graphics.stroke = stroke
	val y = image.height - (font.size + lineSpacing) * texts.size - yToBottom
	for (i in texts.indices) {
		//描边颜色
		graphics.color = outlineColor
		val text = texts[i] ?: ""
		val shape = graphics.font.createGlyphVector(graphics.fontRenderContext, text)
			.getOutline(x.toFloat(), (y + i * (font.size + 5)).toFloat())
		//描边效果
		graphics.draw(shape)
		//文字颜色
		graphics.color = textColor
		graphics.drawString(text, x, y + i * (font.size + 5))
	}
	graphics.dispose()
	return ImageIO.write(image, formatName, outputStream)
}

/**
 * 根据图片内容判断文件格式
 *
 * @param headerBytes 文图片文件前8个字节数据
 * @return 图片文件的格式
 */
fun detectImageFormat(headerBytes: ByteArray): PicFormat? {
	if (headerBytes.size != 8)
		throw RuntimeException("headerBytes size must be 8 bytes.")
	// 判断图片格式
	return if (isJPEG(headerBytes)) {
		PicFormat.JPEG
	} else if (isPNG(headerBytes)) {
		PicFormat.PNG
	} else if (isGIF(headerBytes)) {
		PicFormat.GIF
	} else if (isBMP(headerBytes)) {
		PicFormat.BMP
	} else if (isWEBP(headerBytes)) {
		PicFormat.WEBP
	} else {
		null
	}
}

private fun isJPEG(headerBytes: ByteArray): Boolean {
	return headerBytes.size >= 2 &&
			headerBytes[0] == 0xFF.toByte() &&
			headerBytes[1] == 0xD8.toByte()
}

private fun isPNG(headerBytes: ByteArray): Boolean {
	return headerBytes.size >= 8 &&
			headerBytes[0] == 0x89.toByte() && headerBytes[1] == 0x50.toByte() &&
			headerBytes[2] == 0x4E.toByte() && headerBytes[3] == 0x47.toByte() &&
			headerBytes[4] == 0x0D.toByte() && headerBytes[5] == 0x0A.toByte() &&
			headerBytes[6] == 0x1A.toByte() && headerBytes[7] == 0x0A.toByte()
}

private fun isGIF(headerBytes: ByteArray): Boolean {
	return headerBytes.size >= 6 &&
			headerBytes[0] == 'G'.code.toByte() && headerBytes[1] == 'I'.code.toByte() &&
			headerBytes[2] == 'F'.code.toByte() && headerBytes[3] == '8'.code.toByte() &&
			(headerBytes[4] == '7'.code.toByte() || headerBytes[4] == '9'.code.toByte()) &&
			headerBytes[5] == 'a'.code.toByte()
}

private fun isBMP(headerBytes: ByteArray): Boolean {
	return headerBytes.size >= 2 && headerBytes[0] == 'B'.code.toByte() && headerBytes[1] == 'M'.code.toByte()
}

private fun isWEBP(headerBytes: ByteArray): Boolean {
	return headerBytes.size >= 12 &&
			headerBytes[0] == 0x52.toByte() && headerBytes[1] == 0x49.toByte() &&
			headerBytes[2] == 0x46.toByte() && headerBytes[3] == 0x46.toByte() &&
			headerBytes[8] == 0x57.toByte() && headerBytes[9] == 0x45.toByte() &&
			headerBytes[10] == 0x42.toByte() && headerBytes[11] == 0x50.toByte()
}

/**
 * 图片格式枚举
 */
enum class PicFormat(
	/**
	 * 扩展名
	 */
	var extension: String
) {
	JPEG("jpg"),
	PNG("png"),
	BMP("bmp"),
	GIF("gif"),
	WEBP("webp")
}
