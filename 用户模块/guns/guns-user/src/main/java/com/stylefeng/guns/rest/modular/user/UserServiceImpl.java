package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingfilmUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MeetingfilmUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component
@Service(interfaceClass = UserAPI.class, loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MeetingfilmUserTMapper meetingfilmUserTMapper;

    @Override
    public boolean register(UserModel userModel) {

        MeetingfilmUserT meetingfilmUserT = new MeetingfilmUserT();
        meetingfilmUserT.setUserName(userModel.getUsername());
        String md5Password = MD5Util.encrypt(userModel.getPassword());
        meetingfilmUserT.setUserPwd(md5Password);
        meetingfilmUserT.setEmail(userModel.getEmail());
        meetingfilmUserT.setAddress(userModel.getAddress());
        meetingfilmUserT.setUserPhone(userModel.getPhone());

        Integer insert = meetingfilmUserTMapper.insert(meetingfilmUserT);
        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int login(String username, String password) {

        MeetingfilmUserT meetingfilmUserT = new MeetingfilmUserT();
        meetingfilmUserT.setUserName(username);
        MeetingfilmUserT result = meetingfilmUserTMapper.selectOne(meetingfilmUserT);
        if (result != null && result.getUuid() > 0) {
            String md5Password = MD5Util.encrypt(password);
            if (result.getUserPwd().equals(md5Password)) {
                return result.getUuid();
            }
        }

        return 0;
    }

    @Override
    public boolean checkUsername(String username) {

        EntityWrapper<MeetingfilmUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name", username);
        Integer result = meetingfilmUserTMapper.selectCount(entityWrapper);
        if (result != null && result > 0) {
            return false;
        } else {
            return true;
        }
    }

    private UserInfoModel do2UserInfo(MeetingfilmUserT meetingfilmUserT) {

        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setUuid(meetingfilmUserT.getUuid());
        userInfoModel.setHeadAddress(meetingfilmUserT.getHeadUrl());
        userInfoModel.setUsername(meetingfilmUserT.getUserName());
        userInfoModel.setUpdateTiem(meetingfilmUserT.getUpdateTime().getTime());
        userInfoModel.setSex(meetingfilmUserT.getUserSex());
        userInfoModel.setPhone(meetingfilmUserT.getUserPhone());
        userInfoModel.setNickname(meetingfilmUserT.getNickName());
        userInfoModel.setLifeState("" + meetingfilmUserT.getLifeState());
        userInfoModel.setEmail(meetingfilmUserT.getEmail());
        userInfoModel.setBirthday(meetingfilmUserT.getBirthday());
        userInfoModel.setBiography(meetingfilmUserT.getBiography());
        userInfoModel.setBeginTime(meetingfilmUserT.getBeginTime().getTime());
        userInfoModel.setAddress(meetingfilmUserT.getAddress());

        return userInfoModel;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {

        MeetingfilmUserT meetingfilmUserT = meetingfilmUserTMapper.selectById(uuid);
        UserInfoModel userInfoModel = do2UserInfo(meetingfilmUserT);
        return userInfoModel;
    }

    private Date time2Date(long time) {
        Date date = new Date(time);
        return date;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {

        MeetingfilmUserT meetingfilmUserT = new MeetingfilmUserT();

        meetingfilmUserT.setUuid(userInfoModel.getUuid());
        meetingfilmUserT.setUserSex(userInfoModel.getSex());
        meetingfilmUserT.setUpdateTime(time2Date(System.currentTimeMillis()));
        meetingfilmUserT.setNickName(userInfoModel.getNickname());
        meetingfilmUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        meetingfilmUserT.setHeadUrl(userInfoModel.getHeadAddress());
        meetingfilmUserT.setBirthday(userInfoModel.getBirthday());
        meetingfilmUserT.setBiography(userInfoModel.getBiography());
        meetingfilmUserT.setBeginTime(time2Date(userInfoModel.getBeginTime()));
        meetingfilmUserT.setEmail(userInfoModel.getEmail());
        meetingfilmUserT.setAddress(userInfoModel.getAddress());
        meetingfilmUserT.setUserPhone(userInfoModel.getPhone());

        Integer isSuccess = meetingfilmUserTMapper.updateById(meetingfilmUserT);
        if (isSuccess > 0) {
            UserInfoModel userInfo = getUserInfo(meetingfilmUserT.getUuid());
            return userInfo;
        } else {
            return userInfoModel;
        }
    }
}
