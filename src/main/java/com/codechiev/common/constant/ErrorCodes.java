package com.codechiev.common.constant;

public class ErrorCodes {

	//服务器繁忙，请稍后重试
	public static final int UNKNOWN_REQUEST_ERROR = 1;

	//请求无法处理
	public static final int HANDLE_NOT_FOUND = 2;

	//用户未登陆
	public static final int UNKNOWN_USER = 3;

	//没有操作权限
	public static final int NO_PERMISSION = 4;

	//包含非法关键字：{0}
	public static final int ILLEGAL_WORD = 5;

	//字符长度要大于等于{0}或者小于等于{1}
	public static final int ILLEGAL_WORK_LENGTH = 6;

	//包含非法字符
	public static final int ILLEGAL_CHAR = 7;

	//服务器繁忙，请稍后重试
	public static final int UNKNOWN_ERROR = 8;

	//参数错误
	public static final int ILLEGAL_ARGUMENT = 9;

	//身份证号码位数不对
	public static final int IDCARD_NUM_ERROR = 10;

	//身份证号码出生日期超出范围或含有非法字符
	public static final int IDCARD_ILLEGAL_WORK = 11;

	//身份证号码校验错误
	public static final int IDCARD_CHECK_ERROR = 12;

	//身份证地区非法
	public static final int IDCARD_ILLEGAL_AREA = 13;
	
	//没有此连接
	public static final int UNKOWN_CONNECTION = 14;
	
	//重复登入下线
	public static final int ERROR_MULTI_SIGNIN = 15;

	//服务器关闭中
	public static final int SERVER_CLOSING = 16;
	
	//请先完成登录
	public static final int LOGIN_PLEASE = 17;
	
	//棋子不存在
	public static final int CHESS_NOT_FOUND = 18;
	
	//不可报名比赛
	public static final int ENROLL_NOT_AVAIL = 19;
}