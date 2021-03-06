package com.yw.platform.net.netty;

import android.util.Log;

import com.yw.platform.global.MyApplication;
import com.yw.platform.net.netty.inter.NettyListener;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;

/**
 *
 * Created by LiuSaibao on 11/23/2016.
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private NettyListener listener;
    public NettyClientInitializer(NettyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//      SslContext sslCtx = SslContextBuilder.forClient()
//                .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                .build();
//      pipeline.addLast(sslCtx.newHandler(ch.alloc()));    // 开启SSL
        try {
            SSLEngine engine = getClientContext().createSSLEngine();
            engine.setUseClientMode(true);
            pipeline.addFirst("ssl", new SslHandler(engine));
        }catch (Exception e){
            Log.e("异常",e.getMessage());
        }
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));    // 开启日志，可以设置日志等级
        pipeline.addLast(new NettyClientHandler(listener));
    }

    public  SSLContext getClientContext(){
        InputStream in = null;
        InputStream tIN = null;
        try{
            KeyManagerFactory kmf = null;
            KeyStore ks = KeyStore.getInstance("bks");
            in =  MyApplication.getInstance().getAssets().open("keyStore30.bks");
            ks.load(in, "yw123456".toCharArray());//秘钥库密码
            String alg= KeyManagerFactory.getDefaultAlgorithm();
            Log.e("加密算法: ","-"+alg );
            kmf = KeyManagerFactory.getInstance(alg);
            kmf.init(ks, "yw123456".toCharArray());//秘钥密码

            //对等信任认证
            KeyStore tks = KeyStore.getInstance("bks");
            tIN = MyApplication.getInstance().getAssets().open("server.bks");
            tks.load(tIN, "yw123456".toCharArray());

            Log.e("加密算法: ","-"+alg );
            TrustManagerFactory tf = TrustManagerFactory.getInstance(alg);
            tf.init(tks);
            SSLContext ssl = SSLContext.getInstance("SSL");
            //初始化此上下文
            //参数一：认证的密钥      参数二：对等信任认证  参数三：伪随机数生成器
            ssl.init(kmf.getKeyManagers(),tf.getTrustManagers(), new SecureRandom());
            return ssl;
        }catch(Exception e){
            throw new Error("Failed to initialize the client-side SSLContext");
        }finally{
            if(in !=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (tIN != null){
                try {
                    tIN.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tIN = null;
            }
        }
    }


}
