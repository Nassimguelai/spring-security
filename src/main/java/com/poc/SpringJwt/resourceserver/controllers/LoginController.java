package com.poc.SpringJwt.resourceserver.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class LoginController {

    @GetMapping("/home")
    public String welcomeHome(){
        return "Welcome Home";
    }

    @GetMapping("/admin")
    public String welcomeAdmin(){
        return "Welcome Admin";
    }

    @GetMapping("/editor")
    public String welcomeEditor(){
        return "Welcome Editor";
    }

    @GetMapping("/user")
    public String welcomeUser(){
        return "Welcome User";
    }

    public StringBuffer getJwtLoginInfo(Principal user){
        StringBuffer userInfo = new StringBuffer();
        JwtAuthenticationToken token = (JwtAuthenticationToken) user;
        if(token.isAuthenticated()){
            userInfo.append(token.getTokenAttributes().toString());
        }else{
            userInfo.append("na");
        }

        return userInfo;
    }

    @GetMapping("/default")
    public String getUserInfo(Principal user, @AuthenticationPrincipal OidcUser oidcUser){

        StringBuffer userInfo = new StringBuffer();
        if(user instanceof UsernamePasswordAuthenticationToken){
            userInfo.append(getUsernamePasswordLoginInfo(user));
        }else if(user instanceof OAuth2AuthenticationToken){
            userInfo.append(getOauth2LoginInfo(user, oidcUser));
        }else if(user instanceof JwtAuthenticationToken){
            userInfo.append(getJwtLoginInfo(user));
        }
        return userInfo.toString();
    }


    private StringBuffer getUsernamePasswordLoginInfo(Principal user){
        StringBuffer usernameInfo = new StringBuffer();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;

        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome ").append(u.getUsername());
        }else{
            usernameInfo.append("N/A");
        }
        return usernameInfo;
    }

    private StringBuffer getOauth2LoginInfo(Principal user, OidcUser oidcUser){
        StringBuffer protectedInfo = new StringBuffer();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) user;

        if(token.isAuthenticated()){
            Map<String, Object> userAttributes = token.getPrincipal().getAttributes();
            protectedInfo.append("Welcome ").append(userAttributes.get("name")).append("<br><br>");
            protectedInfo.append("email ").append(userAttributes.get("email")).append("<br><br>");
            if(oidcUser!=null){
                OidcIdToken idToken = oidcUser.getIdToken();
                if(idToken != null){
                    protectedInfo.append("Token mapped value <br><br>");
                    Map<String, Object> claims = idToken.getClaims();
                    for(String key : claims.keySet()){
                        protectedInfo.append(" ").append(key).append(" : ").append(claims.get(key)).append("<br>");
                    }
                }
            }
        }else{
            protectedInfo.append("NA");
        }

        return protectedInfo;
    }
    @GetMapping("/read")
    public String read(){
        return "read";
    }

    @GetMapping("/github")
    public String getGithub(Principal user){
        return "Welcome" + user.toString();
    }
}