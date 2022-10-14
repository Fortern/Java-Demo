package xyz.fortern.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "pay.ali")
data class AlipayProperties(
    /*
    一个大坑，默认情况下，下面的每个属性都会被当做Bean进行注入
    然而并不存在String类型的Bean，导致报错
    导入依赖kotlin-reflect后问题解决
     */
    
    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    val appId: String,
    
    /**
     * 商户应用私钥，您的PKCS8格式RSA2私钥
     */
    val merchantPrivateKey: String,
    
    /**
     * 对应APPID下的支付宝公钥。
     */
    val alipayPublicKey: String,
    
    /**
     * 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    val notifyUrl: String,
    
    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    val returnUrl: String,

    /**
     * 签名方式
     */
    val signType: String,

    /**
     * 字符编码格式
     */
    val charset: String,

    /**
     * 支付宝网关
     */
    val gatewayUrl: String
)