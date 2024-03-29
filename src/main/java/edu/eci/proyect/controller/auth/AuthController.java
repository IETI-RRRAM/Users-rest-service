package edu.eci.proyect.controller.auth;


import edu.eci.proyect.exception.InvalidCredentialsException;
import edu.eci.proyect.model.user.ErrorResponse;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.security.LoginDto;
import edu.eci.proyect.security.Roles;
import edu.eci.proyect.security.TokenDto;
import edu.eci.proyect.service.user.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.*;


@RestController
@RequestMapping( "v1/auth" )
public class AuthController
{

    private final Integer TOKEN_DURATION_MINUTES = 60;

    private static final SecretKey SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);;

    private final UsersService usersService;

    public AuthController( @Autowired UsersService usersService )
    {
        this.usersService = usersService;
    }

    public static SecretKey getSECRET() {
        return SECRET;
    }

    @PostMapping
    public TokenDto login(@RequestBody LoginDto loginDto )
    {
        User user = usersService.findByEmail( loginDto.getEmail() ).orElseThrow(InvalidCredentialsException::new);
        if ( BCrypt.checkpw( loginDto.getPassword(), user.getPasswordHash() ) )
        {
            return generateTokenDto( user );
        }
        else
        {
            throw new InvalidCredentialsException();
        }

    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ErrorResponse(e.getReason(), HttpStatus.UNAUTHORIZED.value());
    }

    private String generateToken( User user, Date expirationDate )
    {
        return Jwts.builder()
                .setSubject( user.getId() )
                .claim(new ArrayList<>( Collections.singleton( Roles.WORKER )).toString(), user.getRoles() )
                .setIssuedAt(new Date() )
                .setExpiration( expirationDate )
                .signWith(SECRET)
                .compact();
    }

    private TokenDto generateTokenDto( User user )
    {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add( Calendar.MINUTE, TOKEN_DURATION_MINUTES );
        String token = generateToken( user, expirationDate.getTime() );
        return new TokenDto( token, expirationDate.getTime() );
    }
}
