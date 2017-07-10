package com.pub.http;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by zbx on 16/1/11.
 */
public interface IResourceOA {

    /**
     * action 拼接的url地址   @Url动态的url地址请求;半静态的url地址请求 @GET("users/{user}/repos") @Path("id") String id
     * map 传入的参数
     */
    @GET
    Call<JsonObject> GetRequest(@Url String action, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("baseUser/doLogin.do")
    Call<JsonObject> GetLogin(@Field("loginName") String loginName, @Field("password") String password);


    /**
     * 找回密码
     *
     * @param tokenid
     * @param phoneNumber
     * @return
     */
    @FormUrlEncoded
    @POST("baseUser/findPassword.do")
    Call<JsonObject> GetFindPassword(@Field("tokenid") String tokenid, @Field("phoneNumber") String phoneNumber);

    /**
     * 找回重置密码
     *
     * @param tokenid
     * @param phoneNumber
     * @param randomNumber
     * @param passWord
     * @return
     */
    @FormUrlEncoded
    @POST("baseUser/validMessagePwd.do")
    Call<JsonObject> GetFindMessagePwd(@Field("tokenid") String tokenid, @Field("phoneNumber") String phoneNumber, @Field("randomNumber") String randomNumber, @Field("passWord") String passWord);

    /**
     * OKR排序界面获取排序列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param orderByType
     * @param mOrderCustom
     * @param mDepartmentId
     * @param mYear
     * @param mMonth
     * @param mKeyword
     * @param page
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("okr/getOKRList")
    Call<JsonObject> GetRanking(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                @Field("Order") String orderByType, @Field("OrderCustom") String mOrderCustom,
                                @Field("DepartmentId") String mDepartmentId, @Field("Year") String mYear,
                                @Field("Month") String mMonth, @Field("Keyword") String mKeyword,
                                @Field("PageIndex") String page, @Field("PageSize") String pageSize);

    /**
     * 获取部门列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @return
     */
    @FormUrlEncoded
    @POST("Base/GetDepartmentList")
    Call<JsonObject> GetDepartmentList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId);


    /**
     * 获取考勤规则时间
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("Attendance/getRuleTimeList")
    Call<JsonObject> GetRuleTimeDataList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId);

    /**
     * 添加考勤
     *
     * @param tokenid
     * @param ip
     * @param address
     * @param longitude
     * @param latitude
     */
    @FormUrlEncoded
    @POST("Attendance/posAttendancetAdd")
    Call<JsonObject> SaveRuleTimeDataList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                          @Field("IP") String ip, @Field("address") String address,
                                          @Field("Checktime") String Checktime, @Field("Longitude") String longitude,
                                          @Field("Latitude") String latitude);

    /**
     * 查看考勤记录
     *
     * @param tokenid
     * @param date
     */
    @FormUrlEncoded
    @POST("Attendance/getAttendanceList")
    Call<JsonObject> SearchDataList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                    @Field("UserID") String UserID, @Field("AttendanceDate") String AttendanceDate);


    /**
     * 考勤申诉
     *
     * @param tokenid
     * @param attendanceid
     * @param appealsource
     * @param appealType
     * @param appealtime
     * @param appealreason
     */
    @FormUrlEncoded
    @POST("Attendance/postAppeal")
    Call<JsonObject> SaveDataList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                  @Field("AttendanceID") String AttendanceID, @Field("AppealSource") String AppealSource,
                                  @Field("AppealType") String AppealType, @Field("AppealTime") String AppealTime,
                                  @Field("AppealReason") String AppealReason);

    /**
     * 获取考勤申诉类型
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("Attendance/getAppealType")
    Call<JsonObject> GetAppealType(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId);

    /**
     * 查看考勤记录
     *
     * @param tokenid
     * @param date
     */
    @FormUrlEncoded
    @POST("oaAttendance/dataList.do")
    Call<JsonObject> SearchDataListRecord(@Field("tokenid") String tokenid, @Field("date") String date);


    /**
     * 查找公司
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("baseZMSCompany/findCompany.do")
    Call<JsonObject> FindCompany(@Field("tokenid") String tokenid);


    /**
     * 切换公司
     *
     * @param tokenid
     * @param zmsCompanyIdOld
     * @param zmsCompanyIdNow
     */
    @FormUrlEncoded
    @POST("baseZMSCompany/updateDefaultCompany.do")
    Call<JsonObject> SwitchCompany(@Field("tokenid") String tokenid, @Field("zmsCompanyIdOld") String zmsCompanyIdOld, @Field("zmsCompanyIdNow") String zmsCompanyIdNow);


    /**
     * 消息设置，获取消息列表信息
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("mobilePush/dataList.do")
    Call<JsonObject> MessageSettingInfo(@Field("tokenid") String tokenid);


    /**
     * 消息设置，设置消息推送状态
     *
     * @param tokenid
     * @param pushState
     * @param isPush
     */
    @FormUrlEncoded
    @POST("mobilePush/updatePushState.do")
    Call<JsonObject> MessageSettingSet(@Field("tokenid") String tokenid, @Field("pushState") String pushState, @Field("isPush") String isPush);


    //下面几个是个人中心的几个请求方法

    /**
     * 取消收藏
     *
     * @param tokenid
     * @param bulletinId
     * @param isCollect
     */
    @FormUrlEncoded
    @POST("oaAlertUser/collect.do")
    Call<JsonObject> CancelCollect(@Field("tokenid") String tokenid, @Field("bulletinId") String bulletinId, @Field("isCollect") String isCollect);


    /**
     * 获取收藏消息列表
     *
     * @param tokenid
     * @param page
     * @param rows
     */
    @FormUrlEncoded
    @POST("oaBulletin/getCollectList.do")
    Call<JsonObject> GetCollectionList(@Field("tokenid") String tokenid, @Field("page") String page, @Field("rows") String rows);


    /**
     * N币明细  这个带有UserId
     *
     * @param tokenid
     * @param page
     * @param panding
     * @param userid
     */
    @FormUrlEncoded
    @POST("baseUserScoreController/selectMyScorels.do")
    Call<JsonObject> GetIntegralDetail(@Field("tokenid") String tokenid, @Field("page") String page, @Field("panding") String panding, @Field("userid") String userid);


    /**
     * N币总排名
     *
     * @param tokenid
     * @param page
     * @param scoreType
     */
    @FormUrlEncoded
    @POST("baseUserScoreController/baseUserScorephb.do")
    Call<JsonObject> IntegralTotalRanking(@Field("tokenid") String tokenid, @Field("page") String page, @Field("scoreType") String scoreType);


    /**
     * N币明细  这个没有UserId
     *
     * @param tokenid
     * @param page
     * @param panding
     */
    @FormUrlEncoded
    @POST("baseUserScoreController/selectMyScorels.do")
    Call<JsonObject> GetIntegralDetailNoUserId(@Field("tokenid") String tokenid, @Field("page") String page, @Field("panding") String panding);

    /**
     * 获取消息未读列表
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("oaAlert/findBaseUserOaAlertUnRead.do")
    Call<JsonObject> FindBaseUserOaAlertUnRead(@Field("tokenid") String tokenid);


    /**
     * 将通知公告标记为已读或者未读    state 标志位 0：未读   1：已读
     *
     * @param tokenid
     * @param id
     * @param state
     */
    @FormUrlEncoded
    @POST("oaBulletin/updateReadState.do")
    Call<JsonObject> MessageCenterSetRead(@Field("tokenid") String tokenid, @Field("id") String id, @Field("state") String state, @Field("label") String label);

    /**
     * 获取消息列表每一个item的点击跳转事件所获取的条目
     *
     * @param tokenid
     * @param page
     * @param rows
     * @param label
     * @param keyword
     */
    @FormUrlEncoded
    @POST("oaAlert/findBaseUserOaAlertType.do")
    Call<JsonObject> MessageCenterGetCount(@Field("tokenid") String tokenid, @Field("page") String page, @Field("rows") String rows, @Field("label") String label, @Field("keyword") String keyword);


    /**
     * 获取日志列表
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaWorkBlog/dataList.do")
    Call<JsonObject> getWorkBlogCenter(@Field("tokenid") String tokenid, @Field("userId") String userId, @Field("page") int page, @Field("rows") int rows);

    /**
     * 获取日志详情
     *
     * @param tokenid
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("oaWorkBlog/getId.do")
    Call<JsonObject> getWorkBlogDetail(@Field("tokenid") String tokenid, @Field("id") int id);

    /**
     * 获取评论列表
     *
     * @param tokenid
     * @param type    类型:1--公告 2--日志
     * @param dataId
     * @param page
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("baseComment/dataList.do")
    Call<JsonObject> getCommentList(@Field("tokenid") String tokenid, @Field("type") int type, @Field("dataId") int dataId, @Field("page") int page, @Field("rows") int rows);


    /**
     * 评论某条日志或公告
     *
     * @param tokenid
     * @param type    类型
     * @param dataId  公告或日志对应的ID
     * @param content 评论内容
     * @return
     */
    @FormUrlEncoded
    @POST("baseComment/save.do")
    Call<JsonObject> postCommentList(@Field("tokenid") String tokenid, @Field("type") int type, @Field("dataId") int dataId, @Field("content") String content);


    /**
     * 回复某人评论
     *
     * @param tokenid
     * @param commentId 对应评论的id
     * @param replyToId 被回复人的id
     * @param content   评论内容
     * @return
     */
    @FormUrlEncoded
    @POST("baseReply/save.do")
    Call<JsonObject> postReplyTo(@Field("tokenid") String tokenid, @Field("commentId") int commentId, @Field("replyToId") int replyToId, @Field("content") String content);

    /**
     * 创建日志
     *
     * @param tokenid
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("oaWorkBlog/save.do")
    Call<JsonObject> postCreatWorkBlog(@Field("tokenid") String tokenid, @Field("content") String content);

    /**
     * 查询用户当天是否写了日志
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaWorkBlog/getBlogToday.do")
    Call<JsonObject> getIsBlogToday(@Field("tokenid") String tokenid);

    /**
     * 发布公告
     *
     * @param tokenid
     * @param title        Don't Null
     * @param content      Don't Null
     * @param persons      Don't Null
     * @param istop
     * @param date
     * @param allowComment
     * @return
     */
    @FormUrlEncoded
    @POST("oaBulletin/publishBulletin.do")
    Call<JsonObject> postCreatBulletin(@Field("tokenid") String tokenid, @Field("title") String title, @Field("content") String content, @Field("persons") String[] persons, @Field("istop") int istop, @Field("time") String date, @Field("allowComment") int allowComment);

    /**
     * 获取申请列表
     *
     * @param tokenid
     * @param page
     * @param State
     * @param Type
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/selectApprove.do")
    Call<JsonObject> getApplyForList(@Field("tokenid") String tokenid, @Field("page") int page, @Field("State") String State, @Field("Type") String Type);

    /**
     * 获取我的审批列表
     *
     * @param tokenid
     * @param page
     * @param State
     * @param Type
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/selectMyApprove.do")
    Call<JsonObject> getMyApprovalList(@Field("tokenid") String tokenid, @Field("page") int page, @Field("State") String State, @Field("Type") String Type);

    /**
     * 获取查询列表
     *
     * @param tokenid
     * @param page
     * @param State
     * @param Type
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/selectAllApprove.do")
    Call<JsonObject> getInquireList(@Field("tokenid") String tokenid, @Field("page") int page, @Field("State") String State, @Field("Type") String Type);

    /**
     * 获取申请类型列表
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/selecttype.do")
    Call<JsonObject> getSelectType(@Field("tokenid") String tokenid);

    /**
     * 获取审批状态列表
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/selectFrame.do")
    Call<JsonObject> getSelectFrame(@Field("tokenid") String tokenid);

    /**
     * 修改申请状态
     *
     * @param tokenid
     * @param id          申请表ID
     * @param state       想要修改的状态
     * @param comment     审批意见
     * @param approveUser 转审人ID(转审时再传)
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/UpdateFlow.do")
    Call<JsonObject> postApprovalOpinion(@Field("tokenid") String tokenid, @Field("id") String id, @Field("state") int state, @Field("comment") String comment, @Field("approveUser") String approveUser);

    /**
     * 下载日志H5压缩包
     *
     * @return
     */
    @GET("upload/set.zip")
    Call<ResponseBody> downloadFileWorkBlogZip();

    /**
     * 获取当前用户身份（申请人或审批人）
     *
     * @param tokenid
     * @param infoid
     * @return
     */
    @FormUrlEncoded
    @POST("oaApproveInfoController/selectinfoType.do")
    Call<JsonObject> getInfoController(@Field("tokenid") String tokenid, @Field("infoid") String infoid);

    /**
     * 获取任务列表
     *
     * @param tokenid
     * @param parentID
     * @param KeyWord
     * @param TaskType
     * @param taskState
     * @param SecondaryTaskStatus
     * @param pageIndex
     * @param pageSize
     * @param currentCompanyId
     * @return
     */
    @FormUrlEncoded
    @POST("task/GetTaskList")
    Call<JsonObject> getMissionList(@Field("tokenid") String tokenid, @Field("ParentID") String parentID,
                                    @Field("KeyWord") String KeyWord, @Field("TaskType") String TaskType,
                                    @Field("TaskState") String taskState, @Field("SecondaryTaskStatus") String SecondaryTaskStatus, @Field("PageIndex") String pageIndex,
                                    @Field("PageSize") String pageSize, @Field("CurrentCompanyId") String currentCompanyId);

    /**
     * 修改任务状态
     *
     * @param tokenid
     * @param currentCompanyId
     * @param taskId
     * @param content
     * @param ActionType
     * @return
     */
    @FormUrlEncoded
    @POST("task/SetStatus")
    Call<JsonObject> postTaskState(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String currentCompanyId, @Field("TaskId") String taskId, @Field("Content") String content, @Field("ActionType") String ActionType);

    /**
     * 修改任务进度
     *
     * @param tokenid
     * @param currentCompanyId
     * @param taskId
     * @param taskProgress
     * @return
     */
    @FormUrlEncoded
    @POST("task/SetProgress")
    Call<JsonObject> postProgress(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String currentCompanyId, @Field("taskId") String taskId, @Field("progress") String taskProgress);

    /**
     * 获取任务详情
     *
     * @param tokenid
     * @param currentCompanyId
     * @param TaskId
     * @return
     */
    @FormUrlEncoded
    @POST("task/Show")
    Call<JsonObject> getMissionDetails(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String currentCompanyId, @Field("TaskId") String TaskId);

    /**
     * 获取获取okr分数
     *
     * @param tokenid
     * @param currentCompanyId
     * @return
     */
    @FormUrlEncoded
    @POST("task/GetPrewOKR")
    Call<JsonObject> getPrewOKR(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String currentCompanyId,
                                @Field("DiffcultyLevel") String DiffcultyLevel, @Field("WorkHours") String WorkHours,
                                @Field("WorkMins") String WorkMins);

    /**
     * 获取任务评论列表
     *
     * @param tokenid
     * @param currentCompanyId
     * @param taskID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("getTaskCommentList")
    Call<JsonObject> getMissionCommentList(@Field("TokenId") String tokenid, @Field("CurrentCompanyId") String currentCompanyId, @Field("TaskID") String taskID, @Field("PageIndex") String pageIndex, @Field("PageSize") String pageSize);


    /**
     * 发表任务评论
     *
     * @param tokenid
     * @param currentCompanyId
     * @param taskId
     * @param taskContent
     * @return
     */
    @FormUrlEncoded
    @POST("task/InsertComment")
    Call<JsonObject> postComment(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String currentCompanyId, @Field("TaskId") String taskId, @Field("Content") String taskContent);

    /**
     * 创建任务
     *
     * @param tokenid
     * @param parentID
     * @param Title
     * @param endTime
     * @param WorkHours
     * @param ChargerUserId
     * @param TranscutorUserId
     * @param ReviewerUserId
     * @param DiffcultyLevel
     * @param TaskLevel
     * @param Info
     * @param AttachmentGUID
     * @param currentCompanyId
     * @param WorkMins
     * @param IsLock
     * @param ActionType
     * @return
     */
    @FormUrlEncoded
    @POST("task/Create")
    Call<JsonObject> postCreatMission(@Field("tokenid") String tokenid, @Field("ParentID") String parentID,
                                      @Field("Title") String Title, @Field("EndTime") String endTime,
                                      @Field("WorkHours") String WorkHours, @Field("WorkMins") String WorkMins, @Field("ChargerUserId") String ChargerUserId,
                                      @Field("TranscutorUserId") String TranscutorUserId, @Field("ReviewerUserId") String ReviewerUserId,
                                      @Field("DiffcultyLevel") String DiffcultyLevel, @Field("TaskLevel") String TaskLevel, @Field("Info") String Info,
                                      @Field("AttachmentGUID") String AttachmentGUID, @Field("CurrentCompanyId") String currentCompanyId,
                                      @Field("IsLock") String IsLock, @Field("ActionType") String ActionType);

    /**
     * 修改任务
     *
     * @return
     */
    @FormUrlEncoded
    @POST("task/Update")
    Call<JsonObject> postUpdateMission(@Field("tokenid") String tokenid, @Field("ID") String ID, @Field("ParentID") String parentID,
                                       @Field("Title") String Title, @Field("EndTime") String endTime,
                                       @Field("WorkHours") String WorkHours, @Field("WorkMins") String WorkMins, @Field("ChargerUserId") String ChargerUserId,
                                       @Field("TranscutorUserId") String TranscutorUserId, @Field("ReviewerUserId") String ReviewerUserId,
                                       @Field("DiffcultyLevel") String DiffcultyLevel, @Field("TaskLevel") String TaskLevel, @Field("Info") String Info,
                                       @Field("AttachmentGUID") String AttachmentGUID, @Field("CurrentCompanyId") String currentCompanyId,
                                       @Field("IsLock") String IsLock, @Field("ActionType") String ActionType);
//    Call<JsonObject> postUpdateMission(@Field("tokenid") String tokenid, @Field("ID") String ID, @Field("ParentID") String parentID,
//                                       @Field("TaskTitle") String taskTitle, @Field("EndTime") String endTime, @Field("WorkHours") String workHours,
//                                       @Field("PersonLeadingId") String personLeadingId, @Field("PersonExecuteIds") String personExecuteIds,
//                                       @Field("PersonCheckId") String personCheckId, @Field("PersonSendIds") String personSendIds,
//                                       @Field("DifficultyLevel") String difficultyLevel, @Field("UrgentLevel") String urgentLevel, @Field("TaskState") String taskState,
//                                       @Field("DoneProgress") String doneProgress, @Field("PersonLeadingName") String personLeadingName, @Field("TaskContent") String taskContent);


    /**
     * 提醒类型
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaScheduleRule/findPrecedeType.do")
    Call<JsonObject> getPrecedeType(@Field("tokenid") String tokenid);

    /**
     * 获取应用界面的轮播图
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("mobilePicture/dataList.do")
    Call<JsonObject> getCarouselPicture(@Field("tokenid") String tokenid);

    /**
     * 消息详情界面展示接口
     * 通知公告详情
     *
     * @param tokenid
     * @param id
     */
    @FormUrlEncoded
    @POST("oaBulletin/getDetailById.do")
    Call<JsonObject> GetDetailById(@Field("tokenid") String tokenid, @Field("id") String id);

    /**
     * 消息详情界面展示接口
     * 系统消息详情
     *
     * @param tokenid
     * @param id
     */
    @FormUrlEncoded
    @POST("oaBulletin/getSystemMsgDetailById.do")
    Call<JsonObject> GetSystemMsgDetailById(@Field("tokenid") String tokenid, @Field("id") String id);

    /**
     * 消息详情界面展示接口
     * 是否取消收藏
     *
     * @param tokenid
     * @param bulletinId
     * @param isCollect
     */
    @FormUrlEncoded
    @POST("oaAlertUser/collect.do")
    Call<JsonObject> MessageCollectOrNot(@Field("tokenid") String tokenid, @Field("bulletinId") String bulletinId, @Field("isCollect") String isCollect);


    /**
     * 消息详情界面展示接口
     * 是否取消置顶公告
     *
     * @param tokenid
     * @param id
     * @param istop
     */
    @FormUrlEncoded
    @POST("oaBulletin/updateIsTop.do")
    Call<JsonObject> MessageIsStopOrNot(@Field("tokenid") String tokenid, @Field("id") String id, @Field("istop") String istop);

    /**
     * 消息详情界面展示接口
     * 是否删除公告
     *
     * @param tokenid
     * @param id
     */
    @FormUrlEncoded
    @POST("oaBulletin/delete.do")
    Call<JsonObject> MessageIsDeleteOrNot(@Field("tokenid") String tokenid, @Field("id") String id);


    /**
     * 更新个人信息
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("baseUser/updateUserInfo.do")
    Call<JsonObject> MemberCenterUpdateUserInfo(@Field("tokenid") String tokenid, @FieldMap Map<String, String> map);


    /**
     * 个人中心中进行密码修改的设置
     *
     * @param tokenid
     * @param password
     * @param newPassword
     */
    @FormUrlEncoded
    @POST("baseUser/updatePassword.do")
    Call<JsonObject> MemberCenterModifyPassword(@Field("tokenid") String tokenid, @Field("password") String password, @Field("newPassword") String newPassword);


    /**
     * 个人中心中意见反馈中的意见反馈
     *
     * @param tokenid
     * @param isanonymity
     * @param content
     */
    @FormUrlEncoded
    @POST("baseSuggest/suggest.do")
    Call<JsonObject> MemberCenterFeedBackMessage(@Field("tokenid") String tokenid, @Field("isanonymity") String isanonymity, @Field("content") String content);

    /**
     * 个人中心中自己的排名
     *
     * @param tokenid
     */
    @FormUrlEncoded
    @POST("baseUserScoreController/selectMyph.do")
    Call<JsonObject> MemberCenterPaiMing(@Field("tokenid") String tokenid);


    /**
     * 获取通讯录
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("baseUser/findDepartmentContacts.do")
    Call<JsonObject> getFindDepartmentContacts(@Field("tokenid") String tokenid);


    /**
     * 新增员工
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param UserName
     * @param PassWord
     * @param Gender
     * @param DepartmentPosition
     */
    @FormUrlEncoded
    @POST("Orgnization/createEmployee")
    Call<JsonObject> getAddStaff(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("UserName") String UserName, @Field("PassWord") String PassWord,
                                 @Field("Gender") int Gender, @Field("DepartmentPosition") String DepartmentPosition);

    /**
     * 新增部门
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ParentID
     * @param DepartmentName
     * @param DepartmentDesc
     */
    @FormUrlEncoded
    @POST("base/createDepartment")
    Call<JsonObject> getAddDepartment(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("ParentID") String ParentID, @Field("DepartmentName") String DepartmentName, @Field("DepartmentDesc") String DepartmentDesc);

    /**
     * 删除部门
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param DepartmentID
     */
    @FormUrlEncoded
    @POST("base/deleteDepartment")
    Call<JsonObject> getDeleteDepartment(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("DepartmentID") int DepartmentID);

    /**
     * 编辑部门
     *
     * @param ParentID
     * @param DepartmentID
     * @param DepartmentName
     * @param DepartmentDesc
     */
    @FormUrlEncoded
    @POST("base/updateDepartment")
    Call<JsonObject> getUpdateDepartment(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("ParentID") int ParentID, @Field("DepartmentID") int DepartmentID, @Field("DepartmentName") String DepartmentName, @Field("DepartmentDesc") String DepartmentDesc);

    /**
     * 选择岗位
     *
     * @param tokenid
     * @param CurrentCompanyId
     */
    @FormUrlEncoded
    @POST("Position/getPositionList")
    Call<JsonObject> getChooseJob(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId);

    /**
     * 编辑员工
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param UserID
     * @param Gender
     * @param UserName
     * @param UserDeptPosition
     */
    @FormUrlEncoded
    @POST("Orgnization/UpdateEmployeeInfo")
    Call<JsonObject> getCompileStaff(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("UserID") String UserID,
                                     @Field("Gender") String Gender, @Field("UserName") String UserName, @Field("UserDeptPosition") String UserDeptPosition);


    /**
     * 停用员工
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param UserID
     * @param isActive
     */
    @FormUrlEncoded
    @POST("Orgnization/stopemployee")
    Call<JsonObject> getStopStaff(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("UserID") String UserID,
                                  @Field("isActive") String isActive);

    /**
     * 获取user的部门职位
     *
     * @param tokenid
     * @param UserID
     */
    @FormUrlEncoded
    @POST("baseUserDeptPosition/getIdList.do")
    Call<JsonObject> getUserPosition(@Field("tokenid") String tokenid, @Field("userid") String UserID);

    /**
     * 邀请员工
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param JsoninviteEmployee
     */
    @FormUrlEncoded
    @POST("Orgnization/inviteEmployee")
    Call<JsonObject> getInviteNewEmployee(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("JsoninviteEmployee") String JsoninviteEmployee);

    /**
     * 自动升级
     *
     * @param flag
     * @return
     */
    @FormUrlEncoded
    @POST("mobileVersion/newVersion.do")
    Call<JsonObject> getNewVersion(@Field("flag") String flag);


    /**
     * 获取职位下人员列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param PositionID
     */
    @FormUrlEncoded
    @POST("Position/GetPositionInfo")
    Call<JsonObject> getUserList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("PositionID") int PositionID);

    /**
     * 编辑职位
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param PostionID
     * @param Name
     * @param Desc
     * @param Perlist
     */
    @FormUrlEncoded
    @POST("Position/updatePosition")
    Call<JsonObject> getUpdatePosition(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                       @Field("PositionID") int PostionID, @Field("Name") String Name,
                                       @Field("Desc") String Desc, @Field("Perlist") String Perlist);

    /**
     * 新增职位
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param Name
     * @param Desc
     * @param PerList
     */
    @FormUrlEncoded
    @POST("Position/addPosition")
    Call<JsonObject> getaddPosition(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                    @Field("Name") String Name,
                                    @Field("Desc") String Desc,
                                    @Field("PerList") String PerList);

    /**
     * 删除职位
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param PositionID
     */
    @FormUrlEncoded
    @POST("Position/DeletePosition")
    Call<JsonObject> getdeletePosition(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                       @Field("PositionID") int PositionID);

    /**
     * 获取职位下权限列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param PositionID
     */
    @FormUrlEncoded
    @POST("Position/getPositionPermissionList")
    Call<JsonObject> getPositionPermissionList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                               @Field("PositionID") int PositionID);

    /**
     * 添加员工and修改员工职位
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param PositionID
     * @param UserList
     */
    @FormUrlEncoded
    @POST("Position/addEmployee")
    Call<JsonObject> getaddEmployee(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                    @Field("PositionID") int PositionID, @Field("UserList") String UserList);

    /**
     * 获取职位下权限ID列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param PositionID
     */
    @FormUrlEncoded
    @POST("Position/GetPositionPermissionIDList")
    Call<JsonObject> getPermissionIDList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                         @Field("PositionID") int PositionID);

    /**
     * CRM获取客户列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param Type
     * @param FieldName
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/getCustomerList")
    Call<JsonObject> getCustomList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                   @Field("Type") int Type, @Field("FieldName") String FieldName, @Field("FieldValue") String FieldValue, @Field("Condition") String Condition,
                                   @Field("PageIndex") int pageIndex, @Field("PageSize") int pageSize);

    /**
     * CRM获取筛选下拉框
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param name
     * @return CustPublicPool客户公共池, CustSource来源, CustState客户状态, RecordTypeList跟进记录类型
     */
    @FormUrlEncoded
    @POST("CustomerManager/getStaticByNameList")
    Call<JsonObject> getStaticList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                   @Field("Name") String name);

    /**
     * CRM行业
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/getIndustryCatalog")
    Call<JsonObject> getIndustryCatalog(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId);

    /**
     * CRM获取客户详情-跟进记录列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/getFollowList")
    Call<JsonObject> getFollowList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId, @Field("CustID") String id,
                                   @Field("PageIndex") int page, @Field("PageSize") int pageSize);

    /**
     * CRM新建或者编辑客户客户
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID
     * @param CustName
     * @param Name
     * @param Mobile
     * @param CustState
     * @param Province
     * @param City
     * @param Area
     * @param Address
     * @param Telephone
     * @param Fax
     * @param WebSite
     * @param Email
     * @param CustPublicPool
     * @param Source
     * @param Industry
     * @param FollowUser
     * @param Remark
     * @param Contacts         以json字符串形式上传
     */
    @FormUrlEncoded
    @POST("CustomerManager/createCustomer")
    Call<JsonObject> createCustomer(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                    @Field("ID") String ID, @Field("CustName") String CustName,
                                    @Field("Name") String Name, @Field("Mobile") String Mobile,
                                    @Field("CustState") String CustState, @Field("Province") String Province,
                                    @Field("City") String City, @Field("Area") String Area,
                                    @Field("Address") String Address, @Field("Telephone") String Telephone,
                                    @Field("Fax") String Fax, @Field("WebSite") String WebSite,
                                    @Field("Email") String Email, @Field("CustPublicPool") String CustPublicPool,
                                    @Field("Source") String Source, @Field("Industry") String Industry,
                                    @Field("FollowUser") String FollowUser, @Field("Remark") String Remark,
                                    @Field("Contacts") String Contacts);

    /**
     * CRM新建或者修改花费费用
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID               修改的时候必须传
     * @param FeeProject
     * @param Fee
     * @param FeeTime
     * @param Remark
     * @param CustID
     */
    @FormUrlEncoded
    @POST("CustomerManager/createPay")
    Call<JsonObject> createPay(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                               @Field("ID") String ID, @Field("FeeProject") String FeeProject,
                               @Field("Fee") String Fee, @Field("FeeTime") String FeeTime,
                               @Field("Remark") String Remark, @Field("CustID") String CustID
    );

    /**
     * CRM新建或者修改成交费用
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID               修改的时候必须传
     * @param FeeProject
     * @param Fee
     * @param FeeTime
     * @param Remark
     * @param CustID
     */
    @FormUrlEncoded
    @POST("CustomerManager/createDeal")
    Call<JsonObject> createDeal(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                @Field("ID") String ID, @Field("FeeProject") String FeeProject,
                                @Field("Fee") String Fee, @Field("FeeTime") String FeeTime,
                                @Field("Remark") String Remark, @Field("CustID") String CustID
    );


    /**
     * CRM获取花费费用明细列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID
     * @param pageIndex
     * @param pageSize
     */
    @FormUrlEncoded
    @POST("CustomerManager/getPayList")
    Call<JsonObject> getPayList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                @Field("ID") String ID,
                                @Field("PageIndex") int pageIndex, @Field("PageSize") int pageSize
    );


    /**
     * CRM获取成交费用明细列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID
     * @param pageIndex
     * @param pageSize
     */
    @FormUrlEncoded
    @POST("CustomerManager/getDealList")
    Call<JsonObject> getDealList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                 @Field("ID") String ID,
                                 @Field("PageIndex") int pageIndex, @Field("PageSize") int pageSize);

    /**
     * CRM获取客户概述
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID
     */
    @FormUrlEncoded
    @POST("CustomerManager/getCustomerSummary")
    Call<JsonObject> getCustomerSummary(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                        @Field("ID") String ID);

    /**
     * CRM获取客户详情
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID
     */
    @FormUrlEncoded
    @POST("CustomerManager/getCustomerDetail")
    Call<JsonObject> getCustomerDetail(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                       @Field("ID") String ID);

    /**
     * CRM获取操作记录列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param CustID
     * @param page
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/getLogList")
    Call<JsonObject> getLogList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                @Field("CustID") String CustID, @Field("PageIndex") int page, @Field("PageSize") int pageSize);

    /**
     * CRM新建修改共同跟进人
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param CustID
     * @param userIds
     */
    @FormUrlEncoded
    @POST("CustomerManager/createCommonFollow")
    Call<JsonObject> createCommonFollow(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                        @Field("CustID") String CustID, @Field("userIds") String userIds);

    /**
     * CRM标记客户为无效
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param CustID
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/postCustomerInvalid")
    Call<JsonObject> postCustomerInvalid(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                         @Field("CustID") String CustID);

    /**
     * CRM转移他人
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param CustID
     * @param FollowUserID
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/transferCustomer")
    Call<JsonObject> postTransferCustomer(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                          @Field("CustID") String CustID, @Field("FollowUserID") String FollowUserID);

    /**
     * 新增跟进记录
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param CustID           客户ID
     * @param ID               跟进记录id
     * @param RecordContent    内容
     * @param RecordType       跟进记录类型
     * @param AttachmentGUID   附件地址， json格式字符串  扩展名称
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/createFollow")
    Call<JsonObject> postFollowUp(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                  @Field("CustID") String CustID, @Field("ID") String ID,
                                  @Field("RecordContent") String RecordContent, @Field("RecordType") String RecordType,
                                  @Field("AttachmentGUID") String AttachmentGUID);

    /**
     * 获取跟进记录评论列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param FollowRecordID   跟进记录id
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/getCommentList")
    Call<JsonObject> getFollowUpCommentList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                            @Field("FollowRecordID") String FollowRecordID);

    /**
     * 提交跟进记录评论
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param FollowRecordID   跟进记录id
     * @param ReplyContent     评论内容
     * @param ReplyToID        被回复评论id(回复时传，非回复时传跟进记录ID)
     * @return
     */
    @FormUrlEncoded
    @POST("CustomerManager/postComment")
    Call<JsonObject> postFollowUpComment(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                         @Field("FollowRecordID") String FollowRecordID, @Field("ReplyContent") String ReplyContent,
                                         @Field("ReplyToID") String ReplyToID);

    /**
     * 按月份查询用户当月全部的日程
     *
     * @param startTime
     * @return
     */
    @FormUrlEncoded
    @POST("oaScheduleRule/findSchedule.do")
    Call<JsonObject> getMonthSchedule(@Field("tokenid") String tokenid, @Field("starttime") String startTime);

    /**
     * 获取日程默认分享人
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaScheduleFavor/findOaScheduleFavorUserName.do")
    Call<JsonObject> getScheduleShare(@Field("tokenid") String tokenid);

    /**
     * 新建和修改日程
     * 	http://192.168.1.110/WebApi_action/schedule/postCreateSchedule
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID                 日程ID，进行修改的时候上传
     * @param UserID             日程拥有人，创建人
     * @param ForUserID          日程拥有人
     * @param Title              标题
     * @param IsAllday           是否全天
     * @param StartTime          开始时间
     * @param EndTime            结束时间
     * @param IsRepeat           是否重复
     * @param RepeatType         重复类型   0：天  1：周   2：月   3：年
     * @param Frequency          重复频率
     * @param WeekStr            重复周字符串
     * @param FinishType         结束方式 0：永不   1：次数   2：日期
     * @param FinishTimes        结束次数
     * @param FinishTime         重复结束时间
     * @param IsAlarm            是否提醒
     * @param AlarmType          提醒类型  0:开始时提醒   1:10分钟前   2:15分钟前   3:30分钟前   4:1小时前    5:2小时前   6：一天前
     * @param ScheduleDetail     日程详情
     * @param Location           日程地点
     * @param SelectMemberIDlist 参与人IDs
     * @return
     */
    @FormUrlEncoded
    @POST("schedule/postCreateSchedule")
    Call<JsonObject> postUpdateSchedule(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                        @Field("ID") String ID, @Field("UserID") String UserID,
                                        @Field("ForUserID") String ForUserID, @Field("Title") String Title,
                                        @Field("IsAllday") String IsAllday, @Field("StartTime") String StartTime,
                                        @Field("EndTime") String EndTime, @Field("IsRepeat") String IsRepeat,
                                        @Field("RepeatType") String RepeatType, @Field("Frequency") String Frequency,
                                        @Field("WeekStr") String WeekStr, @Field("FinishType") String FinishType,
                                        @Field("FinishTimes") String FinishTimes, @Field("FinishTime") String FinishTime,
                                        @Field("IsAlarm") String IsAlarm, @Field("AlarmType") String AlarmType,
                                        @Field("ScheduleDetail") String ScheduleDetail, @Field("Location") String Location,
                                        @Field("SelectMemberIDlist") String SelectMemberIDlist);

    /**
     * 删除日程
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ScheduleID
     * @return
     */
    @FormUrlEncoded
    @POST("schedule/DeleteSchedulel")
    Call<JsonObject> postDeleteSchedule(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                        @Field("ID") String id);


    /**
     * 日程同事列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param UserID
     * @return
     */
    @FormUrlEncoded
    @POST("schedule/getScheduleUserList")
    Call<JsonObject> getScheduleUserList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                         @Field("UserID") String UserID);


    /**
     * 日程列表
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param UserID           日程用户ID
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("schedule/getSchedulelList")
    Call<JsonObject> getScheduleList(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                     @Field("UserID") String UserID, @Field("CurUserID") String CurUserID,
                                     @Field("StartTime") String StartTime);

    /**
     * 日程详情
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ID
     * @param UserID
     * @return
     */
    @FormUrlEncoded
    @POST("schedule/getScheduleDetail")
    Call<JsonObject> getScheduleDetails(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                        @Field("ID") String ID, @Field("UserID") String UserID);

    /**
     * 转发日程
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param ScheduleID
     * @param ToUserID
     * @return
     */
    @FormUrlEncoded
    @POST("schedule/ForwardSchedule")
    Call<JsonObject> ForwardSchedule(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                     @Field("ScheduleID") String ScheduleID, @Field("ToUserID") String ToUserID);

    /**
     * 获取日志同事列表
     *
     * @param tokenid
     * @return
     */
    @FormUrlEncoded
    @POST("oaWorkBlog/getOtherUserList.do")
    Call<JsonObject> getOtherUserList(@Field("tokenid") String tokenid);

    /**
     * 获取考勤当月统计数量
     *
     * @param tokenid
     * @param CurrentCompanyId
     * @param UserID
     * @param AttendanceDate
     * @return
     */
    @FormUrlEncoded
    @POST("Attendance/getDetailCount")
    Call<JsonObject> getDetailCount(@Field("tokenid") String tokenid, @Field("CurrentCompanyId") String CurrentCompanyId,
                                    @Field("UserID") String UserID, @Field("AttendanceDate") String AttendanceDate);

}
