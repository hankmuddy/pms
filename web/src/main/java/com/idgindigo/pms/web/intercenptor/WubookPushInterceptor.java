package com.idgindigo.pms.web.intercenptor;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vomel
 * @since 30.01.14 17:53
 */
public class WubookPushInterceptor extends HandlerInterceptorAdapter {

    @Inject
    private HotelRepository hotelRepository;
    private static final Logger logger = LoggerFactory.getLogger(WubookPushInterceptor.class);

    /*@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        HotelRepository bean = context.getBean(HotelRepository.class);
        hotelRepository = bean;
    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (/*"POST".equalsIgnoreCase(request.getMethod()) &&*/ request.getParameter("lcode") == null /*&& (request.getRequestURI().contains("wubooking/"))*/) {
            logger.error("A request without lcode has been made by wubook url {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        String lcode = request.getParameter("lcode");
        Hotel hotel = hotelRepository.findByLcode(lcode);
        if (hotel == null) {
            logger.error("Could not find hotel for lcode: {}", lcode);
            throw new AccessDeniedException("!");
        }
        SecurityUtils.authenticateNonTenantUser(new WubookAuthentication(hotel));
        return true;
    }

/*
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest request1 = (HttpServletRequest) request;
            if ("POST".equalsIgnoreCase(request1.getMethod()) && request1.getParameter("lcode") != null && (request1.getRequestURI().contains("wubooking/"))) {
                String lcode = request1.getParameter("lcode");
                Hotel hotel = hotelRepository.findByLcode(lcode);
                if (hotel == null) {
                    logger.error("Could not find hotel for lcode: {}", lcode);
                    throw new AccessDeniedException("!");
                }
                SecurityUtils.authenticateNonTenantUser(new WubookAuthentication(hotel));
            } */
/*else if (request1.getParameter("hotelId") != null && (request1.getRequestURI().contains("bb/"))) {
                String hotelId = request1.getParameter("hotelId");
                Hotel hotel = hotelRepository.findByTenantId(hotelId);
                if (hotel == null) {
                    logger.error("Could not find hotel for id: {}", hotelId);
                    throw new AccessDeniedException("!");
                }
                SecurityUtils.authenticateNonTenantUser(new WubookAuthentication(hotel));
            }*//*

            chain.doFilter(request, response);
        } catch (AccessDeniedException ex) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                sb.append(entry.getKey()).append("=>").append(Arrays.deepToString(entry.getValue())).append("\n");
            }
            logger.error("Exception while processing request {} with method {} with parameters: {}", ((HttpServletRequest) request).getRequestURI(), ((HttpServletRequest) request).getMethod(), sb.toString(), e);
            throw e;
        }
        if (((HttpServletResponse) response).getStatus() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                sb.append(entry.getKey()).append("=>").append(Arrays.deepToString(entry.getValue())).append("\n");
            }
            logger.error("Exception while processing request {} with method {} with parameters: {}", ((HttpServletRequest) request).getRequestURI(), ((HttpServletRequest) request).getMethod(), sb.toString());
        }
    }

    @Override
    public void destroy() {

    }*/

    private static class WubookAuthentication extends Authentication {

        WubookAuthentication(Hotel hotel) {
            this.hotel = hotel;
        }

        @Override
        public String getUserType() {
            return "wubook";
        }
    }
}
