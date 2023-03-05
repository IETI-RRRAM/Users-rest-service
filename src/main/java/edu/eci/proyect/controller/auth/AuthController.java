package edu.eci.proyect.controller.auth;


import edu.eci.proyect.exception.InvalidCredentialsException;
import edu.eci.proyect.model.user.User;
import edu.eci.proyect.security.LoginDto;
import edu.eci.proyect.security.TokenDto;
import edu.eci.proyect.service.user.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;


@RestController
@RequestMapping( "v1/auth" )
public class AuthController
{

    @Value( "${app.secret}" )
    String secret;
    private final Integer TOKEN_DURATION_MINUTES = 60;

    private final UsersService usersService;

    public AuthController( @Autowired UsersService usersService )
    {
        this.usersService = usersService;
    }

    @PostMapping
    public TokenDto login(@RequestBody LoginDto loginDto )
    {
        User user = usersService.findByEmail( loginDto.getEmail() ).orElseThrow();
        if ( BCrypt.checkpw( loginDto.getPassword(), user.getPasswordHash() ) )
        {
            return generateTokenDto( user );
        }
        else
        {
            throw new InvalidCredentialsException();
        }

    }

    private String generateToken( User user, Date expirationDate )
    {
        return Jwts.builder()
                .setSubject( user.getId() )
                .claim( CLAIMS_ROLES_KEY, user.getRoles() )
                .setIssuedAt(new Date() )
                .setExpiration( expirationDate )
                .signWith( SignatureAlgorithm.HS256, secret )
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
