package xyz.fortern.controller

import com.alipay.api.AlipayApiException
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.request.AlipayTradePagePayRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import xyz.fortern.config.AlipayProperties
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 支付宝支付
 */
@Controller
@RequestMapping("/alipay")
class AlipayController(
	@Autowired
	val alipayProperties: AlipayProperties,
) {
	/**
	 * 生成订单直接跳转支付宝付款
	 */
	@RequestMapping("/to_alipay.do")
	fun toAlipay(request: HttpServletRequest, response: HttpServletResponse) {
		val alipayClient = DefaultAlipayClient(
			alipayProperties.gatewayUrl, alipayProperties.appId,
			alipayProperties.merchantPrivateKey, "json", alipayProperties.charset,
			alipayProperties.alipayPublicKey, alipayProperties.signType
		)
		// 取购买人名称
		val inName = request.getParameter("in_name")
		// 取手机号
		val inPhone = request.getParameter("in_phone")
		// 创建唯一订单号
		val random = (Math.random() * 10000).toInt()
		val dateStr = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
		
		// 订单号拼接规则：手机号后四位+当前时间后四位+随机数四位数
		val outTradeNo = inPhone.substring(7) + dateStr.substring(10) + random
		
		// 拼接订单名称
		val subject = inName + "的订单"
		
		// 取付款金额
		val totalAmount = request.getParameter("in_money")
		
		// 设置请求参数
		val alipayRequest = AlipayTradePagePayRequest()
		alipayRequest.returnUrl = alipayProperties.returnUrl //支付成功响应后跳转地址
		alipayRequest.notifyUrl = alipayProperties.notifyUrl //异步请求地址
		
		alipayRequest.bizContent = "{\"out_trade_no\":\"$outTradeNo\"," +
				"\"total_amount\":\"$totalAmount\",\"subject\":\"$subject\"," +
				"\"body\":\"\",\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}"
		
		
		// 请求
		try {
			//通过阿里客户端，发送支付页面请求
			val result = alipayClient.pageExecute(alipayRequest).body
			response.contentType = "text/html;charset=UTF-8"
			response.characterEncoding = "UTF-8"
			response.writer.println(result)
			response.writer.flush()
		} catch (e: AlipayApiException) {
			e.printStackTrace()
		} finally {
			response.writer.close()
		}
	}
}
