package com.yaojiafeng.oauth2.server.web;

import com.yaojiafeng.oauth2.server.entity.User;
import com.yaojiafeng.oauth2.server.service.UserService;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yaojiafeng
 * @create 2018-03-22 下午5:10
 */
@Controller

public class UserInfoController {

    @Autowired
    private UserService userService;

    @RequestMapping("/oauthserver/userInfo")
    public HttpEntity userInfo(HttpServletRequest request) throws OAuthSystemException {
        try {
            //获取客户端传来的OAuth资源请求
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);

            //获取Access Token
            String accessToken = oauthRequest.getAccessToken();

            //验证Access Token
            if (StringUtils.isEmpty(accessToken)) {
                // 如果不存在/过期了，返回未验证错误，需重新验证
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();

                HttpHeaders headers = new HttpHeaders();

                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }

            //返回用户名
            User user = userService.selectByPrimaryKey(1);

            return new ResponseEntity(user.getUname(), HttpStatus.OK);
        } catch (OAuthProblemException e) {
            e.printStackTrace();

            //检查是否设置了错误码
            String errorCode = e.getError();

            if (OAuthUtils.isEmpty(errorCode)) {
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .buildHeaderMessage();

                HttpHeaders headers = new HttpHeaders();

                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }

            OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();

            HttpHeaders headers = new HttpHeaders();

            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                    oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}
