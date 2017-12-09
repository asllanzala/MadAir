package com.honeywell.hch.airtouch.plateform.http.model.multicommtask;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 10/14/15.
 * get group by group id
 */
public class MultiCommTaskListResponse implements  Serializable {
    public final static String MUTLICOMMTASK = "multiCommTask";

    @SerializedName("commTaskResponselist")
    private List<MultiCommTaskResponse> mMultiCommTaskResponses;

    public List<MultiCommTaskResponse> getMultiCommTaskResponses() {
        return mMultiCommTaskResponses;
    }

    public void putMultiCommTaskResponses(MultiCommTaskResponse commTaskResponse) {
        mMultiCommTaskResponses.add(commTaskResponse);
    }

    public void setmMultiCommTaskResponses(List<MultiCommTaskResponse> mMultiCommTaskResponses) {
        this.mMultiCommTaskResponses = mMultiCommTaskResponses;
    }

    public MultiCommTaskListResponse (List<MultiCommTaskListResponse> multiCommTaskResponses) {
        this.mMultiCommTaskResponses = mMultiCommTaskResponses;
    }
    public MultiCommTaskListResponse () {}
}
