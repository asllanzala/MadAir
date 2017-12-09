package com.honeywell.hch.airtouch.ui.enroll.constant;

/**
 * Created by h127856 on 16/10/11.
 */
public class EnrollConstants {

    /**
     * 已经完成的步骤
     */
    public static final int ENROLL_DONE_STEP = 0;

    /**
     * 正在操作的步骤
     */
    public static final int ENROLL_DOING_STEP = 1;

    /**
     * 未完成的步骤
     */
    public static final int ENROLL_UNDO_STEP = 2;


    /**
     * enroll设备的四个步骤
     */
    public static final int TOTAL_FOUR_STEP = 4;
    public static final int TOTAL_THREE_STEP = 3;

    /**
     * 更新wifi 两个步骤
     */
    public static final int TOTAL_TWO_STEP = 2;


    /**
     * enroll的四个步骤
     */
    public static final int STEP_ONE = 1;
    public static final int STEP_TWO = 2;
    public static final int STEP_THREE = 3;
    public static final int STEP_FOUR = 4;

    public static final String ENROLL_RESULT = "enroll_result";

    public static final  int ENROLL_RESULT_UPDATE_WIFI_FAILED = -1;
    public static final  int ENROLL_RESULT_UPDATE_WIFI_SUCCESS = 0;
    public static final  int ENROLL_RESULT_ADDDEVICE_TIMEOUT = 1;
    public static final  int ENROLL_RESULT_REGISTER_BY_OTHER = 2;
    public static final  int ENROLL_RESULT_NOT_GET_COMMTASK_RESULT_FAILED = 3;
    public static final  int ENROLL_RESULT_OTHER_FAILED = 4;
    public static final  int ENROLL_RESULT_ADDDEVICE_SUCCESS = 5;
}
