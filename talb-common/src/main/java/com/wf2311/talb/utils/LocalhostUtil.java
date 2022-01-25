package com.wf2311.talb.utils;

import cn.hutool.core.net.NetUtil;
import com.wf2311.talb.base.TalbConstants;

/**
 * 主要用来获取本地host和ip的工具类，带缓存
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 1.3.0
 */
public class LocalhostUtil {

    private static String hostIp = TalbConstants.UNKNOWN;

    private static String hostName = TalbConstants.UNKNOWN;

    public static String getHostIp(){
        try{
            if (hostIp.equals(TalbConstants.UNKNOWN)){
                hostIp = NetUtil.getLocalhostStr();
            }
        }catch (Exception e){}
        return hostIp;
    }

    public static String getHostName(){
        try{
            if (hostName.equals(TalbConstants.UNKNOWN)){
                hostName = NetUtil.getLocalHostName();
            }
        }catch (Exception e){}
        return hostName;
    }

}
