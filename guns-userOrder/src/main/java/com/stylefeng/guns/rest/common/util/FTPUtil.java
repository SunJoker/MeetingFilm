package com.stylefeng.guns.rest.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Auther gongfukang
 * @Date 11/14 10:03
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPUtil {

    // 地址 + 端口 + 用户名 + 密码
    private String hostName;
    private String userName;
    private String password;

    private FTPClient ftpClient = null;

    private void initFTPClient() {
        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostName);
            ftpClient.login(userName, password);
        } catch (Exception e) {
            log.error("初始化 FTP 失败", e);
        }
    }

    /**
     * 输入文件路径 —> 返回文件的字符串
     */
    public String getFileStrByAddress(String fileAddress){

        initFTPClient();

        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(ftpClient.retrieveFileStream(fileAddress))
            );
            while (true) {
                String lineStr = bufferedReader.readLine();
                if (lineStr == null) {
                    break;
                }
                stringBuffer.append(lineStr);
            }
            ftpClient.logout();
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        FTPUtil ftpUtil = new FTPUtil();
        String fileStrByAddress = ftpUtil.getFileStrByAddress("MeetingFilm/userOrder/cgs.json");
        System.out.println(fileStrByAddress);
    }
}
