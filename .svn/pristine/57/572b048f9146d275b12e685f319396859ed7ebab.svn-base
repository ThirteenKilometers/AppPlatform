package com.yw.platform.service;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxb on 2018/5/21.
 */

public class StateManger {
    private static StateManger instance=new StateManger();
    private List<StateChangeListener> listens=new ArrayList<>();
    private StateManger(){}
    public static StateManger getInstance() {
        return instance;
    }
    public void addSateChangeListener(StateChangeListener l){
        listens.add(l);
    }
    public void remove(StateChangeListener l){
        listens.remove(l);
    }
    public boolean setChangeCode(int code){
        for (StateChangeListener l:listens) {
            if(l.onCode(code)) return true;
        }
        return false;
    }
    /**
     *  10002		未登录	登录后重新请求
     *  10007		当前时间禁止访问
     *  20000		账号不存在	输入正确账号
     *  20001		账号已冻结
     *  20002		账号未登录
     *  20003		账号绑定设备数达到上限
     *  20004		账号已在其他设备上登录	重新登录
     *  30000		设备不存在
     *  30001		设备已冻结
     *  30002		设备已被其他账号绑定	登录设备绑定账号’
     *  30003	 	设备未绑定账号或设备已解绑	重新登录
     *  30004		设备不唯一
     * */
    public interface StateChangeListener{
        //对应后台返回的code
        boolean onCode(int code);

    }
}
