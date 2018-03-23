package com.yaojiafeng.oauth2.server.web;

import com.yaojiafeng.oauth2.server.service.UserService;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yaojiafeng
 * @create 2018-03-22 下午3:28
 */
@Controller
public class AuthorizeController {

    @Autowired
    private UserService userService;

    //向客户端返回授权许可码 code
    @RequestMapping("/oauthserver/responseCode")
    public Object responseCode(Model model, HttpServletRequest request) {
        try {
            //构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            // 这里可以对客户端信息做验证
            oauthRequest.getClientId();

            if (!StringUtils.isEmpty(oauthRequest.getClientId())) {
                //设置授权码,可以存进缓存，关联客户端
                String authorizationCode = "authorizationCode";

                //利用oauth授权请求设置responseType，目前仅支持CODE，另外还有TOKEN
                String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

                //进行OAuth响应构建
                OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                        OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

                //设置授权码
                builder.setCode(authorizationCode);

                //得到到客户端重定向地址
                String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

                //构建响应
                final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

                String responceUri = response.getLocationUri();

                return "redirect:" + responceUri;
            }
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
        return null;
    }

}
