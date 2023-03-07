package edu.eci.proyect.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static edu.eci.proyect.controller.auth.AuthController.getSECRET;


@Component
public class JwtRequestFilter extends OncePerRequestFilter
{

    public JwtRequestFilter()
    {
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException
    {
        String authHeader = request.getHeader( HttpHeaders.AUTHORIZATION );
        if ( HttpMethod.OPTIONS.name().equals( request.getMethod() ) )
        {
            response.setStatus( HttpServletResponse.SC_OK );
            filterChain.doFilter( request, response );
        }
        else
        {
            try
            {
                Optional<Cookie> optionalCookie =
                        request.getCookies() != null ? Arrays.stream( request.getCookies() ).filter(
                                cookie -> Objects.equals( cookie.getName(), "AUTHENTICATION" ) ).findFirst() : Optional.empty();
                String headerJwt = null;
                if ( authHeader != null && authHeader.startsWith( "Bearer " ) )
                {
                    headerJwt = authHeader.substring( 7 );
                }
                String token = optionalCookie.isPresent() ? optionalCookie.get().getValue() : headerJwt;
                if ( token != null )
                {
                    Jws<Claims> claims = Jwts.parserBuilder().setSigningKey( getSECRET() ).build().parseClaimsJws( token );
                    Claims claimsBody = claims.getBody();
                    String subject = claimsBody.getSubject();
                    List<String> roles  = claims.getBody().get( new ArrayList<>( Collections.singleton( Roles.WORKER )).toString() , ArrayList.class);
                    if (roles == null) {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token roles");
                    } else {
                        SecurityContextHolder.getContext().setAuthentication( new TokenAuthentication( token, subject, roles));
                    }

                    request.setAttribute( "claims", claimsBody );
                    request.setAttribute( "jwtUserId", subject );
                    request.setAttribute("jwtUserRoles", roles);

                }
                filterChain.doFilter( request, response );
            }
            catch ( MalformedJwtException e )
            {
                response.sendError( HttpStatus.BAD_REQUEST.value(), "Missing or wrong token" );
            }
            catch ( ExpiredJwtException e )
            {
                response.sendError( HttpStatus.UNAUTHORIZED.value(), "Token expired or malformed" );
            }
        }
    }

}