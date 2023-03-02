package xyz.fortern.advice

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch

@Service
@Aspect
class TimerAdvice {
	private final val logger: Logger = LoggerFactory.getLogger(this.javaClass)
	
	@Pointcut("execution(* xyz.fortern.service..*.*(..))")
	fun timerPointcut() {
	}
	
	@Around("timerPointcut()")
	fun permissionCheckFirst(joinPoint: ProceedingJoinPoint): Any? {
		val stopWatch = StopWatch()
		stopWatch.start()
		try {
			return joinPoint.proceed()
		} catch (e: Throwable) {
			throw e
		} finally {
			stopWatch.stop()
			val lastTaskTimeMillis = stopWatch.lastTaskTimeMillis
			if (lastTaskTimeMillis > 150) {
				logger.warn(
					"{} consumption: {}ms. Parameter list: {}",
					joinPoint.signature.name,
					lastTaskTimeMillis,
					joinPoint.args
				)
			} else {
				logger.info("{} consumption: {}ms", joinPoint.signature.name, lastTaskTimeMillis)
			}
		}
		
	}
}
