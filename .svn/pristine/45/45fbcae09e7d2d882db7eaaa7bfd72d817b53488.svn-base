package com.yw.platform.yhtext.netty.client;

import android.content.Context;

import com.yw.platform.global.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;


public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private NettyListener listener;

    private int WRITE_WAIT_SECONDS = 10;

    private int READ_WAIT_SECONDS = 13;

    public NettyClientInitializer(NettyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        SslContext sslCtx = SslContextBuilder.forClient()
//                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();


        ChannelPipeline pipeline = ch.pipeline();

        //添加sslHandler
     SSLEngine engine = getClientSSLContext().createSSLEngine();
      engine.setUseClientMode(true);
       pipeline.addFirst("ssl", new SslHandler(engine));

        pipeline.addLast(new LoggingHandler(LogLevel.INFO));    // 开启日志，可以设置日志等级
        pipeline.addLast(new NettyClientHandler(listener));

    }
    private SSLContext getClientSSLContext() throws KeyStoreException
            , IOException
            , NoSuchAlgorithmException
            , KeyManagementException, UnrecoverableKeyException, CertificateException {
        Context mContext= MyApplication.getInstance();
        //单向认证
        KeyStore trustKeyStore = KeyStore.getInstance("BKS"); // 访问Java密钥库，JKS是keytool创建的Java密钥库
        InputStream keyStream = mContext.getAssets().open("server.bks"); //打开证书文件（.jks格式）
        char keyStorePass[] = "yw123456".toCharArray(); //证书密码
        trustKeyStore.load(keyStream, keyStorePass);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustKeyStore);//保存服务端的授权证书
        SSLContext clientContext = SSLContext.getInstance("SSL");
        clientContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        return clientContext;
      /*  //双向认证
        SSLContext ctx = SSLContext.getInstance("SSL");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        KeyStore ks = KeyStore.getInstance("BKS");
        KeyStore tks = KeyStore.getInstance("BKS");// kclient：客户端私钥 tclient:服务端证书


        ks.load(mContext.getAssets().open("android.bks"), "yw123456".toCharArray());
        tks.load(mContext.getAssets().open("server.bks"), "yw123456".toCharArray());

        kmf.init(ks, "yw123456".toCharArray());
        tmf.init(tks);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return  ctx;*/

    }
}