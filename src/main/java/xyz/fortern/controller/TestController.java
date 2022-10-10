package xyz.fortern.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/test")
public class TestController {
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
	@RequestMapping("/str")
	public ResponseEntity<String> sessionTest(HttpServletRequest request) {
		boolean create = Boolean.parseBoolean(request.getParameter("create"));
		HttpSession session = request.getSession(create);
		System.out.println("session:" + (session == null ? null : session.getId()));
		return ResponseEntity.ok(null);
		
	}
	
	@RequestMapping("/common")
	public ResponseEntity<String> login(HttpServletRequest request) {
		return ResponseEntity.ok("ok");
	}
}
