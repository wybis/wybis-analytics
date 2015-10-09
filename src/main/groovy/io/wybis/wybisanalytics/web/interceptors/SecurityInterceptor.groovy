package io.wybis.wybisanalytics.web.interceptors

import groovy.util.logging.Slf4j
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    Map<String, Boolean> publicPaths = []

    @PostConstruct
    public void init() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (request.getRequestURI().contains(".")) {
            return super.preHandle(request, response, handler)
        }

        String ctxPath = request.getContextPath()
        String path = request.getRequestURI().substring(ctxPath.length())
        if (this.publicPaths.containsKey(path)) {
            return super.preHandle(request, response, handler)
        }

        // HttpSession session = request.getSession(true)
        // User user = (User) session
        // .getAttribute(SessionService.SESSION_USER_KEY)
        // if (user == null) {
        // response.sendError(401)
        // return false
        // }

        return super.preHandle(request, response, handler)
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
