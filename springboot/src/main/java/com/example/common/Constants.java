package com.example.common;

public interface Constants {

    String TOKEN = "token";

    String USER_DEFAULT_PASSWORD = "123";
    // ==============账号可用不可用 ,可不可用
    String  AVAILABLE = "1";

    String  UNAVAILABLE = "0";

    int DEPARTMENT_MANAGER = 3;

    int ADMIN = 1;
    int TEACHER = 2;
    int MANAGER = 3;
    int DEANSOFFICE = 4;

//    1 未审批 2 待教务处审批 3 通过 4 未通过
    String NO_APPROVAL = "1";
    String PENDING_APPROVAL_OFFICE = "2";
    String APPROVED = "3";
    String FAILDE = "4";

    // 未取消 1 取消2
    String UNCANCEL = "1";
    String CANCEL = "2";

}
