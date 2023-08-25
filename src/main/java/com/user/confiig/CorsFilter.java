//package com.user.confiig;
//
//
//    import lombok.RequiredArgsConstructor;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.filter.OncePerRequestFilter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @RequiredArgsConstructor
//    public class CorsFilter extends OncePerRequestFilter{
//
//        @Override
//        protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
//            response.addHeader("Access-Control-Allow-Origin", "http://5.63.154.191:8098");
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
//            response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
//            response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
//            response.addHeader("Access-Control-Allow-Credentials", "true");
//            response.addIntHeader("Access-Control-Max-Age", 10);
//            filterChain.doFilter(request, response);
//        }
//    }
