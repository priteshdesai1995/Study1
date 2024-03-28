package com.base.api.security;

import static com.base.api.constants.CommonApiConstants.ACCEPT;
import static com.base.api.constants.CommonApiConstants.ALL;
import static com.base.api.constants.CommonApiConstants.AUTHORIZATION;
import static com.base.api.constants.CommonApiConstants.CONTENT_TYPE;
import static com.base.api.constants.CommonApiConstants.DELETE;
import static com.base.api.constants.CommonApiConstants.GET;
import static com.base.api.constants.CommonApiConstants.OPTION;
import static com.base.api.constants.CommonApiConstants.POST;
import static com.base.api.constants.CommonApiConstants.PUT;
import static com.base.api.constants.CommonApiConstants.X_FRAME_OPTIONS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.base.api.service.impl.UserServiceImpl;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] publicPaths = { "/user/forgotpassword", "/user/password/validate-reset-token",
            "/user/reset-password" };

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    CommonBasicAuthenticationEntryPoint commonBasicAuthenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(publicPaths);
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                "/configuration/security", "/swagger-ui.html", "/webjars/**", "/eventRecording/**");
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().and().cors().and().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/actuator/**").permitAll().antMatchers("/routes/**").permitAll()
                .antMatchers("/oauth/token").permitAll().antMatchers("/user/**").permitAll().antMatchers("/**")
                .authenticated().anyRequest().authenticated().and().httpBasic()
                .authenticationEntryPoint(commonBasicAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenStore tokenStore() {
//		return new InMemoryTokenStore();
        return new RedisTokenStore(jedisConnectionFactory);
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        /*
         * KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new
         * ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
         * JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
         * converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
         */
        // -- for the simple demo purpose, used the secret for signing.
        // -- for production, it is recommended to use public/private key pair
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("Demo-Key-1");

        return converter;
    }

    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> platformCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configAutenticacao = new CorsConfiguration();
        configAutenticacao.setAllowCredentials(true);
        configAutenticacao.addAllowedOriginPattern(ALL);
        configAutenticacao.addAllowedHeader(X_FRAME_OPTIONS);
        configAutenticacao.addAllowedHeader(AUTHORIZATION);
        configAutenticacao.addAllowedHeader(CONTENT_TYPE);
        configAutenticacao.addAllowedHeader(ACCEPT);
        configAutenticacao.addAllowedHeader(ALL);
        configAutenticacao.addAllowedMethod(POST);
        configAutenticacao.addAllowedMethod(GET);
        configAutenticacao.addAllowedMethod(DELETE);
        configAutenticacao.addAllowedMethod(PUT);
        configAutenticacao.addAllowedMethod(OPTION);
        configAutenticacao.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configAutenticacao);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(-110);
        return bean;
    }

    @Bean
    public JedisConnectionFactory jediConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMinIdle(10);
        poolConfig.setMaxIdle(30);

        return new JedisConnectionFactory(poolConfig);
    }

}
