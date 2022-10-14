package xyz.fortern.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/str")
    fun sessionTest(request: HttpServletRequest): ResponseEntity<String> {
        /**
         * 在使用SpringSecurity时，哪些情况会生成Session？
         * <p>
         * - 仅启用SpringSession，不会自动创建Session<p>
         * - 开启SpringSecurity(无配置),401,会自动创建Session<p>
         * - 开启SpringSecurity(SecurityFilterChain),200,不会自动创建Session<p>
         * - 继续配置authorizeRequests，放行的接口200不生成Session，拦截的接口403生成Session<p>
         * - 将sessionCreationPolicy设为NEVER，依旧会生成Session<p>
         * - 禁用requestCache后，访问需要身份认证的接口，不再产生Session
         */
        val create = request.getParameter("create").toBoolean()
        val session = request.getSession(create)
        println("session:${session?.id}")
        return ResponseEntity.ok(null)
    }

    @GetMapping("/common")
    fun login(request: HttpServletRequest?): ResponseEntity<String>? {
        return ResponseEntity.ok("ok")
    }

}