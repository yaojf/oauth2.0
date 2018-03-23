package com.yaojiafeng.oauth2.server.web;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yaojiafeng
 * @create 2018-03-22 下午5:09
 */
@Controller
public class AccessTokenController {

    //获取客户端的code码，向客户端返回access token
    @RequestMapping(value = "/oauthserver/responseAccessToken", method = RequestMethod.POST)
    public HttpEntity responseAccessToken(HttpServletRequest request) {
        OAuthIssuer oauthIssuerImpl = null;

        OAuthResponse response = null;

        //构建OAuth请求
        try {
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            // 获取授权码，根据ClientId从缓存获取验证
            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            String clientId = oauthRequest.getClientId();

            if (!StringUtils.isEmpty(clientId)) {
                //生成Access Token
                oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

                final String accessToken = oauthIssuerImpl.accessToken();

                //生成OAuth响应
                response = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .buildJSONMessage();
            }

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
        return null;
    }

}