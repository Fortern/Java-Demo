package xyz.fortern.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 测试[001] 什么情况下会生成Session并将Session信息返回前端？
 * <p>
 * 结论：尝试向Session中存储数据的时候，会将Session的信息存进Cookie返回前端
 */
@RestController
@RequestMapping("/test")
public class TestController {
	@RequestMapping("/str")
	public ResponseEntity<String> test(HttpServletRequest request, @CookieValue("SESSION") String sessionStr) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("user");
		System.out.println(sessionStr);
		return ResponseEntity.ok((userId == null ? "未登录" : "ok") + "\n" + session.getId());
	}
	
	@RequestMapping("login/{id}")
	public ResponseEntity<String> login(HttpServletRequest request, @PathVariable int id) {
		HttpSession session = request.getSession();
		session.setAttribute("user", id);
		return ResponseEntity.ok("登录成功");
	}
}
